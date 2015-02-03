package me.imoko.job;

import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by sutao on 15/1/19.
 */
@Repository
public class QueueDao {

    @Autowired
    NamedParameterJdbcTemplate template;

    @Autowired
    QueueEntryMapper queueEntryMapper;

    private static final String ENQUEUE = " INSERT INTO `JobQueue`(`q_name`, `job_entry`, " +
            " `create_time`, `scheduled_at`, `scheduled_cnt` `status`) VALUES(:qName, :jobEntry, :createTime, :scheduledAt, :scheduledCnt, :status)";

    public void enqueue(String queueName, String jobEntry, int scheduledAt) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("qName", queueName)
                .addValue("jobEntry", jobEntry)
                .addValue("createTime", (int) (DateTimeUtils.currentTimeMillis() / 1000))
                .addValue("scheduledAt", scheduledAt)
                .addValue("status", JobStatus.PENDING.getValue())
                .addValue("scheduledCnt", 0);
        this.template.update(ENQUEUE, parameters);
    }

    public void enqueue(QueueEntry entry) {
        BeanPropertySqlParameterSource parameters = new BeanPropertySqlParameterSource(entry);
        this.template.update(ENQUEUE, parameters);
    }


    private static final int FETCH_SIZE = 10;

    private static final String LOCK_QUEUE = " UPDATE `JobQueue` SET `locked_at` = :curTime, " +
            " `locked_by` = :workerId " +
            " WHERE `locked_by` is NULL AND `scheduled_at` <= :curTime AND `q_name` = :queueName AND `status` = :status" +
            " limit :limit ";

    public int lock(String queueName, String processorId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("queueName", queueName)
                .addValue("curTime", (int) (DateTimeUtils.currentTimeMillis() / 1000))
                .addValue("workerId", processorId)
                .addValue("limit", FETCH_SIZE)
                .addValue("status", JobStatus.PENDING.getValue());

        return this.template.update(LOCK_QUEUE, parameters);
    }

    private static final String FETCH_LOCKED = " SELECT * FROM `JobQueue` WHERE " +
            " `q_name` = :qName AND `locked_by` = :workerId  limit :limit";

    public List<QueueEntry> fetchLocked(String queueName, String processorId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("qName", queueName)
                .addValue("workerId", processorId)
                .addValue("limit", FETCH_SIZE);

        return this.template.query(FETCH_LOCKED, parameters, queueEntryMapper);
    }

    private static final String DELETE_JOB = " DELETE FROM `JobQueue` WHERE `id` = :id ";

    public void deleteJob(int id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id);
        this.template.update(DELETE_JOB, parameters);
    }

    private static final String UPDATE_STATUS = " UPDATE  `JobQueue` SET `status` = :status WHERE `id` = :id";

    public void changeStatus(int id, int status) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("id", id)
                .addValue("status", status);
        this.template.update(UPDATE_STATUS, parameters);
    }

}
