package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.house.events.Event;

import java.util.List;

/**
 * Represents observable objects in observer pattern.
 * This objects notify their observers about any changes.
 */
public interface Observable {

    /**
     * Adds new observer to this observable object.
     *
     * @param observer Observer to be added.
     */
    void attachObserver(Observer observer);

    /**
     * Adds new observers to this observable object.
     *
     * @param observers Observers to be added.
     */
    void attachObservers(List<Observer> observers);

    /**
     * Removes existing observer from this observable object.
     *
     * @param observer Observer to be removed.
     */
    void detachObserver(Observer observer);

    /**
     * Notifies all observers of this observable object
     * about changes related to the required event.
     *
     * @param event Actual event.
     */
    void notifyAllObservers(Event event);
}
