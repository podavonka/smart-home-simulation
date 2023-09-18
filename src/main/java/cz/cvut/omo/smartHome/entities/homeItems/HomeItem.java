package cz.cvut.omo.smartHome.entities.homeItems;

import cz.cvut.omo.smartHome.entities.Entity;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Manual;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.events.BrokenDevice;
import cz.cvut.omo.smartHome.house.events.Event;
import cz.cvut.omo.smartHome.utils.statistics.FunctionalityInfo;

/**
 * Represents all items in the house.
 */
public abstract class HomeItem extends Entity {
    private boolean isBroken = false;
    private boolean isBeingUsed = false;
    private boolean isUsable = true;
    private int totalUsageTime = 0;
    private int totalFixingCount = 0;
    private Event currentEvent;
    private Manual manual;
    private FunctionalityInfo functionality = new FunctionalityInfo();


    public HomeItem(String name, Room location) {
        super(name, location);
        functionality.setFunctionalityDecrease(5);
        this.manual = new Manual();
    }

    /**
     * Updates functionality of the home item.
     * If it is NOT in order, marks it as broken.
     */
    public void update() {
        if (this.isBeingUsed()) {
            getFunctionalityInfo().reduce();
            if (getFunctionality() <= 0) {
                setBroken(true);
                setBeingUsed(false);

                return;
            }

            System.out.println(getName() + " current functionality = " + getFunctionality());
        }
    }

    /**
     * Stops using of this home item.
     */
    public void reset() {
        isBeingUsed = false;
        isUsable = true;
    }

    public void addCurrentEvent(BrokenDevice event) {
        this.currentEvent = event;
    }

    public void resetCurrentEvent() {
        this.currentEvent = null;
    }

    public void increaseTotalUsageTime() {
        this.totalUsageTime += 1;
    }

    public void increaseTotalFixingCount() {
        this.totalFixingCount += 1;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public void setUsable(boolean usable) {
        isUsable = usable;
    }

    public void setBeingUsed(boolean beingUsed) {
        isBeingUsed = beingUsed;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public boolean isBeingUsed() {
        return isBeingUsed;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public int getTotalUsageTime() {
        return totalUsageTime;
    }

    public Manual getManual() {
        return manual;
    }

    public int getTotalFixingCount() {
        return totalFixingCount;
    }

    public int getFunctionality() {
        return functionality.getValue();
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public String getInfo() {
        return getName() + " in " + getLocation();
    }

    public FunctionalityInfo getFunctionalityInfo() {
        return functionality;
    }
}
