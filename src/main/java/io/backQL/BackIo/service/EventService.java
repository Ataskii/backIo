package io.backQL.BackIo.service;

import io.backQL.BackIo.domain.UserEvent;
import io.backQL.BackIo.enumeration.EventType;

import java.util.Collection;

public interface EventService {
    Collection<UserEvent> getEventsByUserId(Long userId);
    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);

}
