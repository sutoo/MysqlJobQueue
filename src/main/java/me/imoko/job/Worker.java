package me.imoko.job;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by sutao on 15/1/20.
 */
public class Worker implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(Worker.class);

    final QueueDao queueDao;
    final String queueName;
    final String workerId;
    final IProcessor processor;

    private volatile boolean isRunning;

    public Worker(String queueName, String processorId, IProcessor processor, QueueDao queueDao) {
        this.queueDao = queueDao;
        this.isRunning = true;
        this.queueName = queueName;
        this.workerId = processorId;
        this.processor = processor;
    }

    private int doWork() {
        int locked = queueDao.lock(this.queueName, this.workerId);
        LOG.info("{}|{}", this.workerId, locked);
        List<QueueEntry> queueEntries = this.queueDao.fetchLocked(this.queueName, this.workerId);

        if (queueEntries.size() < locked) {
            LOG.warn("FetchLocked size < locked size.");
        }

        int processed = 0;

        for (QueueEntry entry : queueEntries) {
            LOG.info("processQueueEntry|{}|{}", this.workerId, entry.getId());
            String jobEntry = entry.getJobEntry();

            boolean isDone = false;
            try {
                isDone = this.processor.process(entry.getId(), jobEntry);
            } catch (Exception e) {
                LOG.warn("processQueueEntry|FAILED|{}|{}", this.workerId, entry.getId(), e);
                this.queueDao.changeStatus(entry.getId(), JobStatus.FAILED.getValue());
            }
            if (!isDone) {
                reSchedule(entry);
            }
            processed++;
        }

        return processed;
    }

    private void dropJob(QueueEntry entry) {
        this.queueDao.deleteJob(entry.getId());
    }


    private boolean rebuildJobEntry(QueueEntry entry) {
        int scheduleIntervalBySec = entry.getScheduledAt() - entry.getCreateTime();
        if (scheduleIntervalBySec <= 0) {
            return false;
        }
        int nowSecond = (int) (DateTime.now().getMillis() / 1000);
        entry.setCreateTime(nowSecond);
        entry.setScheduledAt(nowSecond + scheduleIntervalBySec);
        entry.setStatus(JobStatus.PENDING.getValue());
        entry.setRetryCnt(entry.getRetryCnt() - 1);
        return true;
    }

    private void reSchedule(QueueEntry entry) {
        int retryCnt = entry.getRetryCnt();
        if (retryCnt <= 0) {
            dropJob(entry);
            return;
        }

        if (rebuildJobEntry(entry)) {
            this.queueDao.enqueue(entry);
            this.queueDao.deleteJob(entry.getId());
        } else {
            this.queueDao.changeStatus(entry.getId(), JobStatus.FAILED.getValue());
        }
    }

    @Override
    public void run() {
        try {
            while (isRunning) {
                int doneCnt = doWork();
                if (doneCnt == 0) {
                    //punish
                    TimeUnit.SECONDS.sleep(10);
                }
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (Exception e) {
            LOG.error("Worker run exception|{}", this.workerId, e);
        }
    }
}
