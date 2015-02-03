package me.imoko.job;

import java.io.Serializable;

/**
 * Created by sutao on 15/1/19.
 */
public class QueueEntry implements Serializable {
    private int id;
    private String qName;
    private String jobEntry;
    private int lockedAt;
    private String lockedBy;
    private int createTime;
    private int scheduledAt;
    private int status;
    private int retryCnt;

    @Override
    public String toString() {
        return "QueueEntry{" +
                "id=" + id +
                ", qName='" + qName + '\'' +
                ", jobEntry='" + jobEntry + '\'' +
                ", lockedAt=" + lockedAt +
                ", lockedBy='" + lockedBy + '\'' +
                ", createTime=" + createTime +
                ", scheduledAt=" + scheduledAt +
                ", status=" + status +
                ", retryCnt=" + retryCnt +
                '}';
    }

    public int getRetryCnt() {
        return retryCnt;
    }

    public void setRetryCnt(int retryCnt) {
        this.retryCnt = retryCnt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getqName() {
        return qName;
    }

    public void setqName(String qName) {
        this.qName = qName;
    }

    public String getJobEntry() {
        return jobEntry;
    }

    public void setJobEntry(String jobEntry) {
        this.jobEntry = jobEntry;
    }

    public int getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(int lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public int getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(int scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

}
