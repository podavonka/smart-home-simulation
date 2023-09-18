package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.utils.Constants;

public class FixStep extends FixActionStep{
    private int currentFixPhase;
    private final int numOfPhasesToFix;

    public FixStep(int numOfPhasesToFix, Creature executor, HomeItem item) {
        super(executor, item);
        this.numOfPhasesToFix = numOfPhasesToFix;
        this.currentFixPhase = 0;
    }

    /**
     * Makes the home item fixed and ready to use.
     * Updates functionality to maximum.
     */
    @Override
    protected void process() {
        if (currentFixPhase >= numOfPhasesToFix) {
            System.out.println(executor.getName() + "has done fixing " + item + "!");
            executor.getCurrentAction().setDone(true);
            executor.reset();
            if (item.getCurrentEvent() != null) item.getCurrentEvent().setEventSolver(executor);
            item.resetCurrentEvent();
            item.getFunctionalityInfo().setValue(Constants.FUNCTIONALITY_MAXIMUM);
            item.setBroken(false);
            item.setBeingUsed(false);
            item.increaseTotalFixingCount();
            return;
        }

        currentFixPhase++;
        System.out.println(executor.getName() + " is fixing " + item + " in " + executor.getLocation() + "    | " + currentFixPhase + " / " + numOfPhasesToFix + " |");
    }
}
