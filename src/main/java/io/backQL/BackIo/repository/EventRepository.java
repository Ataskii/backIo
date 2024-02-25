package io.backQL.BackIo.repository;

import io.backQL.BackIo.domain.UserEvent;
import io.backQL.BackIo.enumeration.EventType;

import java.util.Collection;


public interface EventRepository{
    Collection<UserEvent> getEventsByUserId(Long userId);
    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);
}
