package cz.cvut.omo.smartHome;

import cz.cvut.omo.smartHome.house.House;
import cz.cvut.omo.smartHome.house.controller.HouseController;

/**
 * Starts the simulation in the required house.
 */
public class Launcher {
    private static final HouseController houseController = HouseController.getInstance();

    public static void main(String[] args) {
        House house = houseController.createHouse();
        houseController.runSimulation();
    }
}
