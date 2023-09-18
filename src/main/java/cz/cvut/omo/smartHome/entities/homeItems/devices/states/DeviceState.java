package cz.cvut.omo.smartHome.entities.homeItems.devices.states;

/**
 * Represents states of devices.
 * Devices can switch between them in different contexts.
 */
public abstract class DeviceState {
    protected DeviceContext context;

    public DeviceState(DeviceContext context) {
        this.context = context;
    }

    public void reduceFunctionality() {
        context.getFunctionalityInfo().reduce();
    }

    public void increaseConsumption() {
        context.getConsumptionInfo().increase();
    }

    void setContext(DeviceContext context) {
        this.context = context;
    }

    public DeviceContext getContext() {
        return context;
    }

}
