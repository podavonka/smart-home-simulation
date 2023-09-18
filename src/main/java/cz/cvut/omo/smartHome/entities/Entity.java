package cz.cvut.omo.smartHome.entities;

import cz.cvut.omo.smartHome.house.Room;

/**
 * Represents all objects and beings in the house.
 */
public class Entity {
    private final String name;
    private Room location;

    public Entity(String name, Room location) {
        this.name = name;
        this.location = location;
        location.addEntity(this);
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Room getLocation() {
        return location;
    }
}
