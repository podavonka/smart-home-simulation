package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Manual;
import cz.cvut.omo.smartHome.utils.Constants;

/**
 * Represents action of fixing broken home items.
 */
public class FixAction extends Action {
    private FixActionStep currentStep;

    public FixAction(Creature executor, HomeItem itemToUse, int actionID) {
        super(executor, itemToUse, actionID);

        Manual manual = (itemToUse.getManual());
        FixActionStep readManualStep = new ReadManualStep(manual.getNumOfPhasesToReadManual(), executor, itemToUse);
        FixActionStep fixStep = new FixStep(manual.getNumOfPhasesToFix(), executor, itemToUse);

        readManualStep.setNextStep(fixStep);

        this.currentStep = readManualStep;

    }

    @Override
    public void update() {
        currentStep.processStep(actionID);
    }
}
