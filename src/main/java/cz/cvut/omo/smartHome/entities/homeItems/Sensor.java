package cz.cvut.omo.smartHome.entities.homeItems;

import cz.cvut.omo.smartHome.house.Room;

/**
 * Represents home items that takes measurements.
 */
public abstract class Sensor extends HomeItem {

    public Sensor(String name, Room location) {
        super(name, location);
    }
}
