package io.getarrays.BackIo.service;

import io.getarrays.BackIo.domain.UserEvent;
import io.getarrays.BackIo.enumeration.EventType;

import java.util.Collection;

public interface EventService {
    Collection<UserEvent> getEventsByUserId(Long userId);
    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);

}
