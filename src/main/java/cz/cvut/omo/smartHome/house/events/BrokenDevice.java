package cz.cvut.omo.smartHome.house.events;

import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.utils.Constants;

/**
 * Represents the event of broken device.
 */
public class BrokenDevice extends Event {
    private final Device brokenDevice;

    public BrokenDevice(String eventName, Room location, Device device, int currnetIteration) {
        super(eventName, location, Constants.BROKEN_DEVICE, currnetIteration);
        this.brokenDevice = device;
        System.out.println("! Event '" + eventName + "' generated in " + location + " !");
        breakDevice();
    }

    /**
     * Makes the device broken.
     */
    private void breakDevice() {
        brokenDevice.crush();
    }

    public Device getBrokenDevice() {
        return brokenDevice;
    }

    @Override
    public String toString() {
        return "Broken " + brokenDevice;
    }
}
