package cz.cvut.omo.smartHome.entities.homeItems.devices.states;

import cz.cvut.omo.smartHome.utils.statistics.ConsumptionInfo;
import cz.cvut.omo.smartHome.utils.statistics.FunctionalityInfo;

/**
 * Represents device context to switch between states.
 */
public interface DeviceContext {
    void request();
    void setState(DeviceState state);
    FunctionalityInfo getFunctionalityInfo();
    ConsumptionInfo getConsumptionInfo();
    int getDeviceConsumption();
}
