package cz.cvut.omo.smartHome.house;

import cz.cvut.omo.smartHome.entities.Entity;
import cz.cvut.omo.smartHome.entities.homeItems.Thermometer;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.events.BrokenDevice;
import cz.cvut.omo.smartHome.house.events.ElectricityOutage;
import cz.cvut.omo.smartHome.house.events.Event;
import cz.cvut.omo.smartHome.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents rooms in the house.
 */
public class Room {
    private final String name;
    private final int floorNumber;
    private double temperature;
    private boolean hasElectricity = true;
    private Event currentEvent;
    private List<Entity> entities = new ArrayList<>();

    public Room(String name, int floorNumber) {
        this.name = name;
        this.floorNumber = floorNumber;
        this.temperature = 24;
    }

    /**
     * Updates room parameters.
     */
    public void update() {
        Random rand = new Random();
        double temperatureChange = rand.doubles(-0.5,0.5).findFirst().getAsDouble();
        setTemperature(this.temperature + temperatureChange);
        Thermometer thermometer = getThermometer();
        if (thermometer != null) thermometer.update();
    }

    /**
     * Adds the required entity to the room.
     *
     * @param entity Entity to be added.
     */
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    /**
     * Decides whether the event will happen.
     * Events can happen with the probability 10%.
     * Chooses random event by event's ID.
     *
     * @return Generated event, if was decided that it will happen,
     *         null, if event will NOT happen.
     */
    public Event generateEvent(int currentIteration) {
        Random rand = new Random();
        int eventId = rand.nextInt(Constants.NUM_OF_EVENTS) + 1000;

        if (rand.nextDouble() <= 0.1) {
            switch (eventId) {
                case Constants.ELECTRICITY_OUTAGE:
                    if (this.hasElectricity()){
                        ElectricityOutage event = new ElectricityOutage("Electricity outage", this, currentIteration);
                        this.currentEvent = event;
                        return (event);
                    }
                    break;

                case Constants.BROKEN_DEVICE:
                    Device device = getRandomDevice();
                    if (device != null){
                        BrokenDevice event = new BrokenDevice("Broken " + device.getName(), this, device, currentIteration);
                        device.addCurrentEvent(event);
                        return (event);
                    }
                    break;

                default:
                    return null;
            }
        }

        return null;
    }

    /**
     * Chooses the device with the generated random index in the array of devices in this room.
     *
     * @return The random device.
     */
    public Device getRandomDevice() {
        Random rand = new Random();

        List<Device> devices = entities.stream()
                .filter(entity -> (entity instanceof Device))
                .filter(entity -> !((Device) entity).isBroken())
                .map(entity -> (Device)entity)
                .collect(Collectors.toList());
        if (devices.size() != 0)
            return devices.get(rand.nextInt(devices.size()));
        return null;
    }

    public void resetCurrentEvent() {
        this.currentEvent = null;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setElectricity(boolean hasElectricity) {
        this.hasElectricity = hasElectricity;
    }

    public String getName() {
        return name;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean hasElectricity() {
        return hasElectricity;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Thermometer getThermometer() {
        return (Thermometer) entities.stream()
                .filter(entity -> entity instanceof Thermometer)
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return getName() + " (" + getFloorNumber() + ". floor)";
    }
}
