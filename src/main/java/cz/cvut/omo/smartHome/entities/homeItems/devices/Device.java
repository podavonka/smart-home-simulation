package cz.cvut.omo.smartHome.entities.homeItems.devices;

import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.devices.states.*;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.utils.statistics.ConsumptionInfo;

/**
 * Represents smart home items that can be controlled by API.
 */
public class Device extends HomeItem implements DeviceAPI, DeviceContext {
    private DeviceType type;
    private DeviceState state;

    private ConsumptionInfo consumption = new ConsumptionInfo();

    public Device(String name, Room location, DeviceType type) {
        super(name, location);
        this.type = type;
        this.state = new TurnedOffState(this);

    }

    /**
     * Updates the device's parameters after using.
     */
    public void update() {
        if (this.getLocation().hasElectricity() && this.isBeingUsed()) {
            state.reduceFunctionality();
            state.increaseConsumption();
            if (getFunctionality() <= 0) {
                setBroken(true);
                return;
            }

            System.out.println(getName() + " current functionality = " + getFunctionality() + " %");
            System.out.println(getName() + " current consumption = " + getConsumption() + " W");
        }
    }

    /**
     * Sets the device to the default state.
     */
    public void reset() {
        super.reset();
        turnOff();
    }

    public boolean hasElectricity() {
        return getLocation().hasElectricity();
    }

    public ConsumptionInfo getConsumptionInfo() {
        return consumption;
    }

    @Override
    public void turnOn() {
        this.state = new ActiveState(this);
    }

    @Override
    public void turnOff() {
        this.state = new TurnedOffState(this);
    }

    @Override
    public void crush() {
        setBroken(true);
        setBeingUsed(false);
    }

    @Override
    public void fix() {

    }

    @Override
    public void request() {

    }

    @Override
    public void setState(DeviceState state) {
        this.state = state;
    }

    public DeviceState getState() {
        return state;
    }

    @Override
    public int getDeviceConsumption() {
        return type.getConsumption();
    }

    public int getConsumption() {
        return consumption.getValue();
    }

    @Override
    public String toString() {
        return getName();
    }
}
