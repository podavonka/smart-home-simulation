package cz.cvut.omo.smartHome.entities.homeItems;

import cz.cvut.omo.smartHome.house.Room;

/**
 * Represents sensor that measures the temperature.
 */
public class Thermometer extends Sensor {
    private double temperature;

    public Thermometer(String name, Room location) {
        super(name, location);
        this.temperature = location.getTemperature();
        this.setUsable(false);
    }

    /**
     * Updates temperature indicator in the room.
     */
    public void update() {
        if (!this.isBroken() && getLocation().hasElectricity())
            temperature = getLocation().getTemperature();
    }

    public double getTemperature() {
        return temperature;
    }

}
