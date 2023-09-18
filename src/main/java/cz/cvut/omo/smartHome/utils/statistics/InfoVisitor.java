package cz.cvut.omo.smartHome.utils.statistics;

import cz.cvut.omo.smartHome.entities.homeItems.devices.DeviceAPI;

/**
 * Represents visitor that checks statistics of the required item.
 */
public interface InfoVisitor {

    /**
     * Asks the device for statistics about it.
     *
     * @param device Device to be checked.
     */
    int visitDevice(DeviceAPI device);
}
