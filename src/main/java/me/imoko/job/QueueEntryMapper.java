package me.imoko.job;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sutao on 15/1/19.
 */
@Component
public class QueueEntryMapper implements RowMapper<QueueEntry> {
    @Override
    public QueueEntry mapRow(ResultSet rs, int i) throws SQLException {
        QueueEntry queueEntry = new QueueEntry();
        queueEntry.setId(rs.getInt("id"));
        queueEntry.setqName(rs.getString("q_name"));
        queueEntry.setJobEntry(rs.getString("job_entry"));
        queueEntry.setLockedAt(rs.getInt("locked_at"));
        queueEntry.setLockedBy(rs.getString("locked_by"));
        queueEntry.setCreateTime(rs.getInt("create_time"));
        queueEntry.setScheduledAt(rs.getInt("scheduled_at"));
        queueEntry.setRetryCnt(rs.getInt("retry_cnt"));
        queueEntry.setStatus(rs.getInt("status"));
        return queueEntry;
    }
}
