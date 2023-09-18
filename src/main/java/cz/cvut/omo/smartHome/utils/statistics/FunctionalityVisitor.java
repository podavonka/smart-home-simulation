package cz.cvut.omo.smartHome.utils.statistics;

import cz.cvut.omo.smartHome.entities.homeItems.devices.DeviceAPI;

/**
 * Represents visitor that checks functionality of required objects.
 */
public class FunctionalityVisitor implements InfoVisitor {
    @Override
    public int visitDevice(DeviceAPI device) {
        return 0;
    }
}
