package cz.cvut.omo.smartHome.house;

import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.controller.HouseController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the family's house to be controlled.
 */
public class House {
    private String address;
    private List<Room> rooms;
    private HouseController controller;
    private int totalConsumption = 0;

    public House(String address, List<Room> rooms) {
        this.address = address;
        this.rooms = rooms;
    }

    /**
     * Adds the new room to the house.
     *
     * @param room Room to be added.
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    public HouseController getController() {
        return controller;
    }

    public void setController(HouseController controller) {
        this.controller = controller;
    }

    /**
     * Each iteration generates output with information about measurements of sensors
     * and total consumption of electricity with an approximate price for it.
     */
    public void getInfo() {
        System.out.println();
        System.out.println("~.~.~.~.~.~.~.~.~.~.~ House Info ~.~.~.~.~.~.~.~.~.~.~");

        for (Room room : rooms) {
            if (room.getThermometer() != null)
                System.out.println("Thermometer: temperature in " + room.getName() +" is " + String.format("%.1f",room.getThermometer().getTemperature()) + "   (actual is " + String.format("%.1f",room.getTemperature()) + ")");
            else
                System.out.println("There is no thermometer in " + room.getName()+ "   (actual is " + String.format("%.1f",room.getTemperature()) + ")");
            List<Device> devices = room.getEntities().stream().filter(entity -> entity instanceof Device).map(Device.class::cast).collect(Collectors.toList());
            for (Device device : devices) {
                totalConsumption += device.getConsumption();
            }
        }

        System.out.println("Total consumption = " + totalConsumption + " W  |  approximate price = " + (totalConsumption / 1000) * 4.5 + " Kc");
        totalConsumption = 0;

        System.out.println("~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~.~");
    }
}
