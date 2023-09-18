package cz.cvut.omo.smartHome.house.events;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.house.Room;

/**
 * Represents events that are unlikely to happen accidentally
 * in the house with a small probability.
 */
public class Event {
    private Room location;
    private String eventName;
    private Creature eventSolver;
    private int eventID;
    private int currentIteration;

    public Event(String eventName, Room location, int eventID, int currentIteration) {
        this.location = location;
        this.eventName = eventName;
        this.eventID = eventID;
        this.currentIteration = currentIteration;
    }

    public void setEventSolver(Creature eventSolver) {
        this.eventSolver = eventSolver;
    }

    public Room getLocation() {
        return location;
    }

    public Creature getEventSolver() {
        return eventSolver;
    }

    public int getEventID() {
        return eventID;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }

    @Override
    public String toString() {
        return eventName;
    }
}
