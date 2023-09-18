package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.house.events.Event;

/**
 * Represents observer in observer pattern.
 * Observers must be notified about observable objects' changes.
 */
public interface Observer {

    /**
     * Processes updates needed to produce the event.
     *
     * @param event The required event.
     */
    void update(Event event);

    /**
     * Processes the required event.
     *
     * @param event The required event.
     */
    void processEvent(Event event);
}
