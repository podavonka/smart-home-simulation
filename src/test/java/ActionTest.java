import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.creatures.CreatureInfo;
import cz.cvut.omo.smartHome.entities.creatures.Person;
import cz.cvut.omo.smartHome.entities.homeItems.SportEquipment;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.Action;
import cz.cvut.omo.smartHome.house.events.Event;
import cz.cvut.omo.smartHome.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ActionTest {

    private Creature person;
    private List<Room> rooms = new ArrayList<>();
    private Room mockedLivingRoom;
    private Room mockedKitchen;
    private Room mockedBathroom;

    @BeforeEach
    public void setRooms() {
        mockedLivingRoom = mock(Room.class);
        mockedKitchen = mock(Room.class);
        mockedBathroom = mock(Room.class);

        when(mockedLivingRoom.getName()).thenReturn("Living room");
        when(mockedKitchen.getName()).thenReturn("Kitchen");
        when(mockedBathroom.getName()).thenReturn("Bathroom");

        rooms.add(mockedLivingRoom);
        rooms.add(mockedKitchen);
        rooms.add(mockedBathroom);

        person = new Person("Valerie", mockedLivingRoom, 20, 120, 170, 50, CreatureInfo.Gender.FEMALE);
    }

    @Test
    public void update_usualUsingActionWithElectricity_increasesPhaseNum() {
        Device mockedDevice = mock(Device.class);
        when(mockedLivingRoom.hasElectricity()).thenReturn(true);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        Action action = new Action(person, mockedDevice, Constants.USUAL_USING_ACTION);

        int previousNumOfCurrentPhase = action.getNumOfCurrentPhase();
        action.update();
        int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

        Assertions.assertEquals(previousNumOfCurrentPhase + 1, currentNumOfCurrentPhase);
    }

    @Test
    public void update_usualUsingActionWithoutElectricity_actionWasNotExecuted() {
        Device mockedDevice = mock(Device.class);
        when(mockedLivingRoom.hasElectricity()).thenReturn(false);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        Action action = new Action(person, mockedDevice, Constants.USUAL_USING_ACTION);
        action.setNumOfCurrentPhase(action.getNumOfPhases() - 1);

        action.update();
        int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

        Assertions.assertEquals(0, currentNumOfCurrentPhase);
    }

    @Test
    public void update_usualUsingActionWithBrokenItem_actionWasNotExecuted() {
        Device mockedDevice = mock(Device.class);
        when(mockedLivingRoom.hasElectricity()).thenReturn(true);
        when(mockedDevice.isBroken()).thenReturn(true);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        Action action = new Action(person, mockedDevice, Constants.USUAL_USING_ACTION);

        action.update();
        int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

        Assertions.assertEquals(0, currentNumOfCurrentPhase);
    }

    @Test
    public void update_usualUsingAction_actionIsDone() {
        Device mockedDevice = mock(Device.class);
        when(mockedLivingRoom.hasElectricity()).thenReturn(true);
        when(mockedDevice.isBroken()).thenReturn(false);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        Action action = new Action(person, mockedDevice, Constants.USUAL_USING_ACTION);
        action.setNumOfCurrentPhase(action.getNumOfPhases() - 1);

        action.update();

        Assertions.assertTrue(action.isDone());
    }

    @Test
    public void update_doSportAction_increasesPhaseNum() {
        SportEquipment mockedSportEquipment = mock(SportEquipment.class);
        when(mockedSportEquipment.getName()).thenReturn("SportEquipment");
        when(mockedSportEquipment.toString()).thenReturn("SportEquipment");
        when(mockedSportEquipment.getLocation()).thenReturn(mockedLivingRoom);
        Action action = new Action(person, mockedSportEquipment, Constants.DO_SPORT_ACTION);

        int previousNumOfCurrentPhase = action.getNumOfCurrentPhase();
        action.update();
        int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

        Assertions.assertEquals(previousNumOfCurrentPhase + 1, currentNumOfCurrentPhase);
    }

    @Test
    public void update_doSportAction_actionIsDone() {
        SportEquipment mockedSportEquipment = mock(SportEquipment.class);
        when(mockedSportEquipment.getName()).thenReturn("SportEquipment");
        when(mockedSportEquipment.toString()).thenReturn("SportEquipment");
        when(mockedSportEquipment.getLocation()).thenReturn(mockedLivingRoom);
        Action action = new Action(person, mockedSportEquipment, Constants.DO_SPORT_ACTION);
        action.setNumOfCurrentPhase(action.getNumOfPhases() - 1);

        action.update();

        Assertions.assertTrue(action.isDone());
    }

    @Test
    public void update_eventAction_increasesPhaseNum() {
        Device mockedDevice = mock(Device.class);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        Event mockedElectricityEvent = mock(Event.class);
        Action action = new Action(person, mockedLivingRoom, Constants.ELECTRICITY_OUTAGE);

        person.setCurrentEvent(mockedElectricityEvent);

        int previousNumOfCurrentPhase = action.getNumOfCurrentPhase();
        action.update();
        int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

        Assertions.assertEquals(previousNumOfCurrentPhase + 1, currentNumOfCurrentPhase);
    }

    @Test
    public void update_eventAction_actionIsDone() {
        Device mockedDevice = mock(Device.class);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        Event mockedElectricityEvent = mock(Event.class);
        Action action = new Action(person, mockedLivingRoom, Constants.ELECTRICITY_OUTAGE);
        action.setNumOfCurrentPhase(action.getNumOfPhases() - 1);
        person.setCurrentEvent(mockedElectricityEvent);

        action.update();

        Assertions.assertTrue(action.isDone());
    }
}