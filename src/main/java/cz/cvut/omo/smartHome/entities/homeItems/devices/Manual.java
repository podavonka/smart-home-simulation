package cz.cvut.omo.smartHome.entities.homeItems.devices;

import java.util.Random;

/**
 * Represents manual for device to fix it.
 */
public class Manual {
    private int numOfPhasesToFix;
    private int numOfPhasesToReadManual;

    public Manual() {
        Random rand = new Random();
        this.numOfPhasesToFix = rand.nextInt(4) + 1;
        this.numOfPhasesToReadManual = rand.nextInt(4) + 1;
    }

    public int getNumOfPhasesToFix() {
        return numOfPhasesToFix;
    }

    public int getNumOfPhasesToReadManual() {
        return numOfPhasesToReadManual;
    }
}
