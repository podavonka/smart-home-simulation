package cz.cvut.omo.smartHome.house.controller;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.creatures.CreatureInfo;
import cz.cvut.omo.smartHome.entities.creatures.Person;
import cz.cvut.omo.smartHome.entities.creatures.Pet;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.PetToy;
import cz.cvut.omo.smartHome.entities.homeItems.SportEquipment;
import cz.cvut.omo.smartHome.entities.homeItems.Thermometer;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.entities.homeItems.devices.DeviceType;
import cz.cvut.omo.smartHome.house.House;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.events.Event;
import cz.cvut.omo.smartHome.utils.Constants;
import cz.cvut.omo.smartHome.utils.ReportGenerator;
import cz.cvut.omo.smartHome.utils.SimulationIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents controller to manage the house.
 * Uses singleton pattern.
 *
 * HouseController can run simulation for the single current house.
 */
public class HouseController implements Observable {
    private static HouseController houseController = null;
    private List<Room> rooms;
    private List<HomeItem> homeItems;
    private List<Creature> creatures;
    private List<Event> events = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    private SimulationIterator iterator;

    /**
     * Creates the new instance of the HouseController, if it does not exist.
     *
     * @return Single instance of the HouseController.
     */
    public static HouseController getInstance() {
        if (houseController == null) houseController = new HouseController();
        return houseController;
    }

    /**
     * Creates entities for the new house and adds them in it.
     *
     * @return New house with given parameters.
     */
    public House createHouse() {
        getRooms();
        getCreatures();
        getHomeItems();

        House house = new House("Technicka 2", rooms);
        house.setController(this);
        this.iterator = new SimulationIterator(house);

        return house;
    }

    /**
     * Runs simulation in the required house.
     */
    public void runSimulation() {
        while (iterator.hasNext()) iterator.next();

        generateReport();
    }

    /**
     * Generates events for all rooms in the house.
     * Event can happen in the room with the probability 10%.
     *
     * @param currentIteration Number of current iteration.
     */
    public void generateRoomEvent(int currentIteration) {
        for (Room room : rooms) {
            Event event = room.generateEvent(currentIteration);
            if (event != null) {
                notifyAllObservers(event);
                events.add(event);
            }
        }
    }

    /**
     * Generates reports about activity and usage, consumption and events.
     */
    public void generateReport() {
        List<Device> devices = homeItems.stream().filter(homeItem -> homeItem instanceof Device).map(Device.class::cast).collect(Collectors.toList());
        ReportGenerator reportGenerator = new ReportGenerator(creatures, devices, events);
        try {
            ReportGenerator.generateActivityAndUsageReport();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            ReportGenerator.generateConsumptionReport();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        try {
            ReportGenerator.generateEventReport();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Room getRoomByName(String name) {
        for (Room room : rooms) {
            if (room.getName().equals(name))
                return room;
        }
        return null;
    }

    public List<HomeItem> getHomeItems() {
        if (this.homeItems == null) {
            List<HomeItem> homeItems = new ArrayList<>();
            homeItems.add(new Device("Big TV", getRoomByName("Living room"), DeviceType.TV));
            homeItems.add(new Device("PS5", getRoomByName("Living room"), DeviceType.PLAY_STATION));
            homeItems.add(new Device("Small TV", getRoomByName("Kitchen"), DeviceType.TV));
            homeItems.add(new Device("Wall TV", getRoomByName("Bedroom"), DeviceType.TV));
            homeItems.add(new Device("Fridge", getRoomByName("Kitchen"), DeviceType.FRIDGE));
            homeItems.add(new Device("Thermostat", getRoomByName("Living room"), DeviceType.THERMOSTAT));
            homeItems.add(new Device("Thermostat", getRoomByName("Bedroom"), DeviceType.THERMOSTAT));
            homeItems.add(new Device("Thermostat", getRoomByName("Bathroom"), DeviceType.THERMOSTAT));
            homeItems.add(new Device("Thermostat", getRoomByName("Kitchen"), DeviceType.THERMOSTAT));
            homeItems.add(new Device("Kettle", getRoomByName("Kitchen"), DeviceType.KETTLE));
            homeItems.add(new Device("Tigran's iPad Pro", getRoomByName("Bathroom"), DeviceType.TABLET));
            homeItems.add(new Device("Bob's iPad Pro", getRoomByName("Living room"), DeviceType.TABLET));
            homeItems.add(new Device("David's iPad Pro", getRoomByName("Bedroom"), DeviceType.TABLET));
            homeItems.add(new Device("Bedside lamp", getRoomByName("Bedroom"), DeviceType.LAMP));
            homeItems.add(new Device("Office lamp", getRoomByName("Office"), DeviceType.LAMP));
            homeItems.add(new Device("Bathroom lamp", getRoomByName("Bathroom"), DeviceType.LAMP));
            homeItems.add(new Device("Computer", getRoomByName("Office"), DeviceType.COMPUTER));
            homeItems.add(new Device("Music speaker", getRoomByName("Bedroom"), DeviceType.SPEAKER));
            homeItems.add(new Device("Music speaker", getRoomByName("Living room"), DeviceType.SPEAKER));
            homeItems.add(new PetToy("Pet Toy", getRoomByName("Living room")));
            homeItems.add(new PetToy("Pet Toy", getRoomByName("Bedroom")));
            homeItems.add(new PetToy("Pet Toy", getRoomByName("Kitchen")));
            homeItems.add(new SportEquipment("Red bike", getRoomByName("Garage")));
            homeItems.add(new SportEquipment("Black bike", getRoomByName("Garage")));
            for (Room room : rooms) {
                if (!room.getName().equals("Garage")) {
                    homeItems.add(new Thermometer("Thermometer", room));
                }
            }
            this.homeItems = homeItems;
        }
        return this.homeItems;
    }

    public List<Creature> getCreatures() {
        if (this.creatures == null) {
            List<Creature> creatures = new ArrayList<>();
            creatures.add(new Person("Valerie", getRandomRoom(), 20, 120, 170, 50, CreatureInfo.Gender.FEMALE));
            creatures.add(new Person("Tigran", getRandomRoom(), 21, 3, 171, 60, CreatureInfo.Gender.MALE));
            creatures.add(new Person("Tatiana", getRandomRoom(), 22, 100, 165, 50, CreatureInfo.Gender.FEMALE));
            creatures.add(new Person("Nikita", getRandomRoom(), 20, 112, 193, 80, CreatureInfo.Gender.MALE));
            creatures.add(new Person("Bohdan", getRandomRoom(), 30, 90, 182, 72, CreatureInfo.Gender.MALE));
            creatures.add(new Person("David", getRandomRoom(), 19, 78, 180, 68, CreatureInfo.Gender.MALE));

            creatures.add(new Pet("Leonardo", getRandomRoom(), 50, 140, 5, 2, CreatureInfo.Gender.OTHER, Pet.PetType.TURTLE));
            creatures.add(new Pet("Bob", getRandomRoom(), 8, 70, 40, 5, CreatureInfo.Gender.MALE, Pet.PetType.DOG));
            creatures.add(new Pet("Ozzy", getRandomRoom(), 1, 2, 5, 1, CreatureInfo.Gender.FEMALE, Pet.PetType.HAMSTER));
            this.creatures = creatures;
        }
        return this.creatures;
    }

    public Room getRandomRoom() {
        Random rand = new Random();
        return this.rooms.stream().filter(room -> !room.getName().equals("Garage"))
                .collect(Collectors.toList()).get(rand.nextInt(rooms.size() - 1));
    }

    public List<Room> getRooms() {
        if (this.rooms == null) {
            List<Room> rooms = new ArrayList<>();
            rooms.add(new Room("Kitchen", 0));
            rooms.add(new Room("Living room", 0));
            rooms.add(new Room("Bedroom", 0));
            rooms.add(new Room("Bathroom", 0));
            rooms.add(new Room("Office", 0));
            rooms.add(new Room("Garage", 0));
            this.rooms = rooms;
        }
        return this.rooms;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void attachObservers(List<Observer> observers) {
        for (Observer observer : creatures) {
            attachObserver(observer);
        }
    }

    @Override
    public void detachObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObservers(Event event) {
        if (event.getEventID() == Constants.ELECTRICITY_OUTAGE)
            observers.stream()
                    .filter(observer -> observer instanceof Person)
                    .filter(observer -> ((Person) observer).getLocation() == event.getLocation())
                    .forEach(observer -> observer.processEvent(event));
    }
}
