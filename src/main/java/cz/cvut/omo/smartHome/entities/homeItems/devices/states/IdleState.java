package cz.cvut.omo.smartHome.entities.homeItems.devices.states;

/**
 * Represents state when the device is turned on, but not in use.
 */
public class IdleState extends DeviceState {

    public IdleState(DeviceContext context) {
        super(context);
        context.getFunctionalityInfo().setFunctionalityDecrease(2);
        context.getConsumptionInfo().setConsumptionIncrease(2);
    }
}
