package cz.cvut.omo.smartHome.utils;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.house.House;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.HouseController;

/**
 * Iterator for house simulation.
 */
public class SimulationIterator implements CustomIterator {
    private static final int ITERATIONS_COUNT = 200;
    private int currentIteration = 0;
    private House house;
    private HouseController controller;

    public SimulationIterator(House house) {
        this.house = house;
        this.controller = house.getController();
    }

    @Override
    public boolean hasNext() {
        return currentIteration <= ITERATIONS_COUNT;
    }

    /**
     * Generates events and performs actions with home items by creatures.
     */
    @Override
    public void next() {
        System.out.println();
        System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-= Iteration number " + currentIteration + " =-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

        controller.generateRoomEvent(currentIteration);

        for (Room room : controller.getRooms()) {
            room.update();
        }

        for (Creature creature : controller.getCreatures()) {
            creature.update(controller.getRooms(), controller.getHomeItems());
        }

        house.getInfo();
        currentIteration++;
    }

}
