package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.utils.Constants;

public class ReadManualStep extends FixActionStep{
    private int currentReadManualPhase;
    private final int numOfPhasesToReadManual;

    public ReadManualStep(int numOfPhasesToReadManual, Creature executor, HomeItem item) {
        super(executor, item);
        this.numOfPhasesToReadManual = numOfPhasesToReadManual;
        this.currentReadManualPhase = 0;
    }

    /**
     * Processes reading of the manual that is necessary to fix devices.
     */
    @Override
    protected void process() {
        if (currentReadManualPhase >= numOfPhasesToReadManual) {
            System.out.println(executor.getName() + " done reading manual for " + item + " in " + executor.getLocation());
            this.level = Constants.FIX_ITEM_ACTION;
            return;
        }

        currentReadManualPhase++;
        System.out.println(executor.getName() + " is reading manual for " + item + " in " + executor.getLocation() + "    |" + " " + currentReadManualPhase + " / " + numOfPhasesToReadManual + " |");
    }
}
