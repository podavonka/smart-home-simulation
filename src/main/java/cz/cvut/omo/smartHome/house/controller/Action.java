package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.utils.Constants;

import java.util.Random;

/**
 * Represents all types of actions that creatures can perform.
 */
public class Action {
    protected Creature executor;
    protected HomeItem itemToUse;
    protected Room location;
    private int eventID;
    protected int actionID = -1;
    protected boolean isDone = false;
    protected int numOfCurrentPhase = 0;
    protected int numOfPhases;

    public Action(Creature executor, Room location, int eventID) {
        this.executor = executor;
        this.location = location;
        this.eventID = eventID;
        this.actionID = eventID;
        this.numOfPhases = Constants.PHASES_TO_FIND_ROOM_WITHOUT_ELECTRICITY;
    }

    public Action(Creature executor, HomeItem itemToUse, int actionID) {
        Random rand = new Random();
        this.executor = executor;
        this.itemToUse = itemToUse;
        this.actionID = actionID;
        this.numOfPhases = rand.nextInt(3) + 1;
        if (actionID == Constants.DO_SPORT_ACTION) this.numOfPhases = rand.nextInt(2) + 3;
    }

    /**
     * Updates the necessary parameters to perform the action.
     */
    public void update() {
        if (itemToUse != null) {
            switch (actionID) {
                case Constants.USUAL_USING_ACTION -> updateToUseItem();
                case Constants.DO_SPORT_ACTION -> updateToDoSport();
            }
            itemToUse.increaseTotalUsageTime();
        } else updateEventAction();
    }

    /**
     * Processes the event and solves problems related to it.
     */
    private void updateEventAction() {
        switch (eventID) {
            case Constants.ELECTRICITY_OUTAGE:
                numOfCurrentPhase++;
                System.out.println(executor.getName() + " is trying to solve problem with electricity in "
                        + executor.getLocation() + "    | " + numOfCurrentPhase + " / " + numOfPhases + " |");

                if (numOfCurrentPhase >= numOfPhases)
                    isDone = true;

                if (isDone) {
                    executor.getCurrentEvent().setEventSolver(executor);
                    executor.reset();
                    location.resetCurrentEvent();
                    location.setElectricity(true);
                }
                break;
        }
    }

    /**
     * Processes using of the item.
     */
    private void updateToUseItem() {
        if (itemToUse instanceof Device) {
            if (!getItemToUse().getLocation().hasElectricity()) {
                System.out.println(executor.getName() + " stop using " + getItemToUse().getName() + " in " + executor.getLocation()
                        + ", because there is no electricity!");
                numOfCurrentPhase = 0;
                ((Device) itemToUse).turnOff();
                executor.reset();
                return;
            }
        }
        if (itemToUse.isBroken()) {
            System.out.println(executor.getName() + " stop using " + getItemToUse().getName() + " in " + executor.getLocation()
                    + ", because it is broken!");
            numOfCurrentPhase = 0;
            executor.reset();
            return;
        }
        numOfCurrentPhase++;
        System.out.println(executor.getName() + " is using " + getItemToUse().getName() + " in " + executor.getLocation()
                + "    | " + numOfCurrentPhase + " / " + numOfPhases + " |");
        itemToUse.update();

        if (numOfCurrentPhase >= numOfPhases)
            isDone = true;

        if (isDone) {
            executor.reset();
            getItemToUse().reset();
        }
    }

    /**
     * Processes doing sports with the equipment.
     */
    private void updateToDoSport() {
        numOfCurrentPhase++;
        System.out.println(executor.getName() + " is sporting with " + getItemToUse().getName() + " outside    | "
                + numOfCurrentPhase + " / " + numOfPhases + " |");
        itemToUse.update();

        if (numOfCurrentPhase >= numOfPhases)
            isDone = true;

        if (isDone) {
            executor.reset();
            getItemToUse().reset();
        }
    }

    public HomeItem getItemToUse() {
        return itemToUse;
    }

    public int getNumOfCurrentPhase() {
        return numOfCurrentPhase;
    }

    public void setNumOfCurrentPhase(int numOfCurrentPhase) {
        this.numOfCurrentPhase = numOfCurrentPhase;
    }

    public String getActionInfo() {
        if (itemToUse != null) return Constants.getActionInfo(actionID) + itemToUse.getName() + " in " + itemToUse.getLocation();
        return Constants.getActionInfo(actionID) + "in " + executor.getLocation();
    }

    public int getActionID() {
        return actionID;
    }

    public int getNumOfPhases() {
        return numOfPhases;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean b) {
        isDone = b;
    }
}
