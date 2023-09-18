package cz.cvut.omo.smartHome.utils.statistics;

import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;

/**
 * Represents consumption statistics of devices.
 * Consumption depends on state of the device.
 */
public class ConsumptionInfo {
    private final Device device;
    private int value = 0;
    private int consumptionIncrease;

    public ConsumptionInfo() {
        device = null;
    }

    public ConsumptionInfo(Device device) {
        this.device = device;
    }

    /**
     * Increases consumption value.
     */
    public void increase() {
        value += consumptionIncrease;
    }

    /**
     * Asks the device about its consumption using visitor.
     *
     * @param visitor Visitor to visit the device.
     */
    public void accept(InfoVisitor visitor) {
        value = visitor.visitDevice(device);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setConsumptionIncrease(int consumptionIncrease) {
        this.consumptionIncrease = consumptionIncrease;
    }

    public int getValue() {
        return value;
    }

    public int getConsumptionIncrease() {
        return consumptionIncrease;
    }
}
