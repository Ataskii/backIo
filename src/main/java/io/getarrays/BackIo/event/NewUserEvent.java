package io.getarrays.BackIo.event;

import io.getarrays.BackIo.enumeration.EventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class NewUserEvent extends ApplicationEvent {
    private EventType eventType;
    private String email;

    public NewUserEvent(String email, EventType eventType) {
        super(email);
        this.eventType = eventType;
        this.email = email;

    }
}
