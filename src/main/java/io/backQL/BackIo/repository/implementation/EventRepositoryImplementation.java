package io.backQL.BackIo.repository.implementation;

import io.backQL.BackIo.domain.UserEvent;
import io.backQL.BackIo.enumeration.EventType;
import io.backQL.BackIo.repository.EventRepository;
import io.backQL.BackIo.rowmapper.userEventRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

import static io.backQL.BackIo.query.EventQuery.INSERT_EVENT_BY_EMAIL_QUERY;
import static io.backQL.BackIo.query.EventQuery.SELECT_EVENTS_BY_USER_ID_QUERY;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EventRepositoryImplementation implements EventRepository {
    private final NamedParameterJdbcTemplate jdbc;


    @Override
    public Collection<UserEvent> getEventsByUserId(Long userId) {
        return jdbc.query(SELECT_EVENTS_BY_USER_ID_QUERY, Map.of("id", userId), new userEventRowMapper());
    }

    @Override
    public void addUserEvent(String email, EventType eventType, String device, String ipAddress) {
        jdbc.update(INSERT_EVENT_BY_EMAIL_QUERY, Map.of("email", email, "type", eventType.toString(), "device", device, "ipAddress", ipAddress));
    }

    @Override
    public void addUserEvent(Long userId, EventType eventType, String device, String ipAddress) {

    }
}
