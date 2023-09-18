package cz.cvut.omo.smartHome.utils.statistics;

import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;

/**
 * Represents functionality statistics of devices.
 * Functionality depends on time that the device was used.
 */
public class FunctionalityInfo {
    private final Device device;
    private int value = 100;
    private int functionalityDecrease;

    public FunctionalityInfo() {
        device = null;
    }

    public FunctionalityInfo(Device device, int value) {
        this.device = device;
        this.value = value;
    }

    /**
     * Reduces functionality value.
     */
    public void reduce() {
        value -= functionalityDecrease;
    }

    /**
     * Reduces functionality value.
     *
     * @param reduceAmount Decrease value.
     */
    public void reduce(int reduceAmount) {
        value -= reduceAmount;
    }

    /**
     * Asks the device about its functionality using visitor.
     *
     * @param visitor Visitor to visit the device.
     */
    public void accept(InfoVisitor visitor) {
        value = visitor.visitDevice(device);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setFunctionalityDecrease(int functionalityDecrease) {
        this.functionalityDecrease = functionalityDecrease;
    }

    public int getValue() {
        return value;
    }

    public int getFunctionalityDecrease() {
        return functionalityDecrease;
    }
}
