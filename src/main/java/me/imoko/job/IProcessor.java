package me.imoko.job;

/**
 * Job processor for the queue.
 * <p/>
 * Created by sutao on 15/1/20.
 */
public interface IProcessor {
    /**
     * @param jobId
     * @param jobEntry
     * @return is process finished.
     */
    boolean process(int jobId, String jobEntry);
}
