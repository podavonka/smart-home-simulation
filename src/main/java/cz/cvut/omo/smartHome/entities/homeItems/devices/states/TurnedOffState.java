package cz.cvut.omo.smartHome.entities.homeItems.devices.states;

/**
 * Represents state when device is turned off.
 */
public class TurnedOffState extends DeviceState {

    public TurnedOffState(DeviceContext context) {
        super(context);
        context.getFunctionalityInfo().setFunctionalityDecrease(0);
        context.getConsumptionInfo().setConsumptionIncrease(0);
    }
}
