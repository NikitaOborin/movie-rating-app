package com.project.movieratingapp.repository.event;

import com.project.movieratingapp.model.Event;

import java.util.List;

public interface EventRepository {
    void addEventForUserByEntityId(Long userId, Long entityId, String eventType, String operationType);

    List<Event> getEventFeedByUserId(Long userId);
}
