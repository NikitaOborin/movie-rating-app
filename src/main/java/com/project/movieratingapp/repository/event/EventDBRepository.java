package com.project.movieratingapp.repository.event;

import com.project.movieratingapp.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.Instant;
import java.util.List;

@Repository
public class EventDBRepository implements EventRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventDBRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addEventForUserByEntityId(Long userId, Long entityId, String eventType, String operationType) {
        String sqlQuery = "INSERT INTO event (user_id, entity_id, event_type, operation, timestamp) " +
                          "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, new String[]{"event_id"});
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, entityId);
            preparedStatement.setString(3, eventType);
            preparedStatement.setString(4, operationType);
            preparedStatement.setLong(5, (Instant.now().toEpochMilli()));

            return preparedStatement;
        }, keyHolder);
    }

    @Override
    public List<Event> getEventFeedByUserId(Long userId) {
        return jdbcTemplate.query("SELECT * FROM event WHERE user_id=?", eventRowMapper, userId);
    }

    private RowMapper<Event> eventRowMapper = (rs, rowNum) -> {
        Event event = new Event();
        event.setEventId(rs.getLong("event_id"));
        event.setUserId(rs.getLong("user_id"));
        event.setEntityId(rs.getLong("entity_id"));
        event.setEventType(rs.getString("event_type"));
        event.setOperation(rs.getString("operation"));
        event.setTimestamp(rs.getLong("timestamp"));

        return event;
    };
}
