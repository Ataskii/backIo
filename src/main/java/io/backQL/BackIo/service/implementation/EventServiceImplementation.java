package io.backQL.BackIo.service.implementation;

import io.backQL.BackIo.domain.UserEvent;
import io.backQL.BackIo.enumeration.EventType;
import io.backQL.BackIo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventServiceImplementation implements io.backQL.BackIo.service.EventService {
    private final EventRepository eventRepository;
    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) { return eventRepository.getEventsByUserId(userId); }
    @Override
    public void addUserEvent(Long  userId, EventType eventType, String device, String ipAddress) { eventRepository.addUserEvent(userId, eventType, device, ipAddress); }
    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) { eventRepository.addUserEvent(email, eventType, device, ipAddress); }
}
