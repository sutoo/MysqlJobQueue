package me.imoko.job;

/**
 * Created by sutao on 15/1/21.
 */
public enum JobStatus {
    PENDING(1), PROCESSING(2), FAILED(3);

    public int getValue() {
        return value;
    }

    private int value;

    JobStatus(int value) {
        this.value = value;
    }
}
