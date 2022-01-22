package com.aktimetrics.core.api;


import com.aktimetrics.core.transferobjects.Event;
import com.aktimetrics.core.transferobjects.EventType;

/**
 * @author arun kumar kandakatla
 */
public interface EventHandler {

    EventType getType();

    /**
     * @param event
     */
    void handle(Event<?, ?> event);
}
