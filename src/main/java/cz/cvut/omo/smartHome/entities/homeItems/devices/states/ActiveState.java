package cz.cvut.omo.smartHome.entities.homeItems.devices.states;

import java.util.Random;

/**
 * Represents state when the device is turned on and in use.
 */
public class ActiveState extends DeviceState {

    public ActiveState(DeviceContext context) {
        super(context);
        Random rand = new Random();
        context.getFunctionalityInfo().setFunctionalityDecrease(5);
        context.getConsumptionInfo().setConsumptionIncrease(context.getDeviceConsumption() + rand.nextInt(5));
    }
}
