package cz.cvut.omo.smartHome.entities.homeItems.devices;

/**
 * Interface that regulates the interaction of users and devices.
 */
public interface DeviceAPI {

    /**
     * Turns the device on.
     */
    void turnOn();

    /**
     * Turns the device off.
     */
    void turnOff();

    /**
     * Makes the device broken.
     */
    void crush();

    /**
     * Makes the device fixed.
     */
    void fix();
}
