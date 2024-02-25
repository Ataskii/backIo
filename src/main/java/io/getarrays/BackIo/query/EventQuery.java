package io.getarrays.BackIo.query;

public class EventQuery {
    public static final String SELECT_EVENTS_BY_USER_ID_QUERY = "SELECT ue.id, ue.device, ue.ip_address, e.type, e.description, ue.created_at FROM Events e JOIN UserEvents ue ON  ue.event_id = e.id  JOIN Users u ON u.id = ue.user_id WHERE u.id = :id ORDER BY ue.created_at DESC LIMIT 10";
    public static final String INSERT_EVENT_BY_EMAIL_QUERY = "INSERT INTO UserEvents (user_id, event_id, device, ip_address) VALUES ((SELECT id FROM Users WHERE email = :email), (SELECT id FROM Events WHERE type = :type), :device, :ipAddress)";
}
