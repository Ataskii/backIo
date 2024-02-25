package io.backQL.BackIo.listener;

import io.backQL.BackIo.event.NewUserEvent;
import io.backQL.BackIo.service.EventService;
import io.backQL.BackIo.utils.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewUserEventListener {
    private final EventService eventService;
    private final HttpServletRequest request;


    @EventListener
    public void onNewUserEvent(NewUserEvent event) {
        log.info("NewUserEvent is fired");
        eventService.addUserEvent(event.getEmail(), event.getEventType(), RequestUtils.getDevice(request), RequestUtils.getIpAddress(request));
    }
}
