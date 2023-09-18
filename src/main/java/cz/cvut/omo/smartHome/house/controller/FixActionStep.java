package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;

import javax.swing.*;

public abstract class FixActionStep {
    protected Creature executor;
    protected HomeItem item;
    protected int level;
    protected FixActionStep nextStep;

    public FixActionStep(Creature executor, HomeItem item) {
        this.executor = executor;
        this.item = item;
    }

    public void setNextStep(FixActionStep step) {
        this.nextStep = step;
    }

    public void processStep(int level) {
        if (this.level <= level) {
            process();
        } else {
            if (nextStep != null) {
                nextStep.processStep(level);
            }
        }
    }

    abstract protected void process();
}
