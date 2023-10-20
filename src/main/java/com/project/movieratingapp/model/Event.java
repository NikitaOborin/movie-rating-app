package com.project.movieratingapp.model;

import lombok.Data;

@Data
public class Event {
    Long eventId;
    Long userId;
    Long entityId;
    String eventType;
    String operation;
    Long timestamp;
}
