package cz.cvut.omo.smartHome.entities.creatures;

import cz.cvut.omo.smartHome.entities.Entity;
import cz.cvut.omo.smartHome.house.events.Event;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.Action;
import cz.cvut.omo.smartHome.house.controller.Observer;
import cz.cvut.omo.smartHome.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents living beings.
 */
public abstract class Creature extends Entity implements Observer {
    protected CreatureInfo creatureInfo;
    protected boolean isAlive = true;
    protected boolean isBusy = false;
    protected boolean isHungry = false;
    protected List<Action> startedActions = new ArrayList<>();
    protected Action currentAction;
    protected Event currentEvent;

    public Creature(String name, Room location, int age, int IQ, int height, int weight, CreatureInfo.Gender gender) {
        super(name, location);
        this.creatureInfo = new CreatureInfo(age, IQ, height, weight, gender);
    }

    /**
     *  Decides whether to move to another room.
     *  Executes action with item in chosen room.
     *
     * @param rooms List of rooms in the house.
     * @param homeItems List of all items in the house.
     */
    public void update(List<Room> rooms, List<HomeItem> homeItems) {
        if (!isBusy) {
            moveToAnotherRoom(rooms);
            chooseAction(homeItems.stream()
                    .filter(homeItem -> homeItem.getLocation() == getLocation())
                    .filter(HomeItem::isUsable)
                    .collect(Collectors.toList()));
        }
        if (currentAction != null) currentAction.update();
    }

    /**
     * Decides whether to move to another room (in 30% - yes).
     * If decided to move, chooses a random room and changes location.
     *
     * @param rooms List of rooms in the house.
     */
    public void moveToAnotherRoom(List<Room> rooms) {
        Random rand = new Random();
        if (rand.nextDouble() <= 0.3) {
            Room room = rooms.get(rand.nextInt(rooms.size()));
            if (room != getLocation()){
                System.out.println(getName() + " walks from " + getLocation() + " to the " + room);
                setLocation(room);
            }
        }
    }

    /**
     * Chooses a random item and executes action using it.
     * If this item is broken and creature is a human, fixes it.
     *
     * @param homeItems List of all items in the house.
     */
    abstract void chooseAction(List<HomeItem> homeItems);

    /**
     * Stops executing current action.
     */
    public void reset() {
        this.isBusy = false;
        this.currentAction = null;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public List<Action> getStartedActions() {
        return startedActions;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    @Override
    public void update(Event event) {
        processEvent(event);
    }

    @Override
    public void processEvent(Event event) {
        System.out.println(getName() + ": Event " + event.toString() + " in " + event.getLocation() + "!");

        switch (event.getEventID()) {
            case Constants.ELECTRICITY_OUTAGE:
                if (isBusy) {
                    System.out.println("stop using " + currentAction.getItemToUse().getName());
                    reset();
                }
                currentEvent = event;
                isBusy = true;
                currentAction = new Action(this, getLocation(), Constants.ELECTRICITY_OUTAGE);
                break;
        }

    }

    @Override
    public String toString() {
        return getName();
    }
}
