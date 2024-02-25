package io.getarrays.BackIo.service.implementation;

import io.getarrays.BackIo.domain.UserEvent;
import io.getarrays.BackIo.enumeration.EventType;
import io.getarrays.BackIo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventServiceImplementation implements io.getarrays.BackIo.service.EventService {
    private final EventRepository eventRepository;
    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) { return eventRepository.getEventsByUserId(userId); }
    @Override
    public void addUserEvent(Long  userId, EventType eventType, String device, String ipAddress) { eventRepository.addUserEvent(userId, eventType, device, ipAddress); }
    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) { eventRepository.addUserEvent(email, eventType, device, ipAddress); }
}
