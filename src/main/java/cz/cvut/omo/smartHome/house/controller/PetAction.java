package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.utils.Constants;

import java.util.Random;

/**
 * Represents actions that can only pets perform.
 */
public class PetAction extends Action{

    public PetAction(Creature executor, HomeItem itemToUse, int actionID) {
        super(executor, itemToUse, actionID);
        Random rand = new Random();
        this.numOfPhases = rand.nextInt(4) + 1;
    }

    public PetAction(Creature executor, Room location, int actionID) {
        super(executor, location, actionID);
        Random rand = new Random();
        this.actionID = actionID;
        this.numOfPhases = rand.nextInt(4) + 1;
    }

    @Override
    public void update() {
        switch (actionID) {
            case Constants.PET_TRIES_TO_EAT_HOME_ITEM -> updateToEatItem();
            case Constants.PET_WANTS_TO_SLEEP_ACTION -> updateToSleep();
            case Constants.PET_WANTS_TO_PLAY_WITH_TOY_ACTION -> updateToPlay();
        }
    }

    /**
     * The pet is using item to eat it.
     */
    private void updateToEatItem() {
        numOfCurrentPhase++;
        System.out.println(executor.getName() + " is trying to eat " + getItemToUse().getName() + " in " + executor.getLocation()
                + "    | " + numOfCurrentPhase + " / " + numOfPhases + " |");

        if (numOfCurrentPhase >= numOfPhases) isDone = true;

        if (isDone) {
            executor.reset();
            getItemToUse().getFunctionalityInfo().reduce(Constants.EATEN_ITEM_FUNCTIONALITY_REDUCE);
            getItemToUse().reset();
        }
    }

    /**
     * Turns the pet to sleep.
     */
    private void updateToSleep() {
        numOfCurrentPhase++;
        System.out.println(executor.getName() + " is sleeping in " + executor.getLocation() + "    | " + numOfCurrentPhase + " / " + numOfPhases + " |");

        if (numOfCurrentPhase >= numOfPhases) isDone = true;

        if (isDone) executor.reset();
    }

    /**
     * Makes the pet playing with the toy.
     */
    private void updateToPlay() {
        numOfCurrentPhase++;
        System.out.println(executor.getName() + " is playing with " + itemToUse.getName() + " in " + executor.getLocation() + "    | " + numOfCurrentPhase + " / " + numOfPhases + " |");

        if (numOfCurrentPhase >= numOfPhases) isDone = true;

        if (isDone) {
            executor.reset();
            getItemToUse().reset();
        }
    }
}
