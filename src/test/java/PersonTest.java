import cz.cvut.omo.smartHome.entities.creatures.CreatureInfo;
import cz.cvut.omo.smartHome.entities.creatures.Person;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.PetToy;
import cz.cvut.omo.smartHome.entities.homeItems.SportEquipment;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Manual;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.Action;
import cz.cvut.omo.smartHome.house.controller.FixAction;
import cz.cvut.omo.smartHome.house.events.BrokenDevice;
import cz.cvut.omo.smartHome.utils.Constants;
import cz.cvut.omo.smartHome.utils.statistics.FunctionalityInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PersonTest {
    private Person person;

    private List<Room> rooms = new ArrayList<>();
    private Room mockedLivingRoom;
    private Room mockedKitchen;
    private Room mockedBathroom;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;


    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @BeforeEach
    public void setRooms() {
        mockedLivingRoom = mock(Room.class);
        mockedKitchen = mock(Room.class);
        mockedBathroom = mock(Room.class);

        when(mockedLivingRoom.getName()).thenReturn("Living room");
        when(mockedLivingRoom.toString()).thenReturn("Living room");
        when(mockedLivingRoom.hasElectricity()).thenReturn(true);
        when(mockedKitchen.getName()).thenReturn("Kitchen");
        when(mockedBathroom.getName()).thenReturn("Bathroom");

        rooms.add(mockedLivingRoom);
        rooms.add(mockedKitchen);
        rooms.add(mockedBathroom);

        person = new Person("Valerie", mockedLivingRoom, 20, 120, 170, 50, CreatureInfo.Gender.FEMALE);
    }

    @Test
    public void update_personIsBusyAndDeviceIsNotBroken_executesAction() {
        Device mockedDevice = mock(Device.class);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        when(mockedDevice.isBroken()).thenReturn(false);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        Action action = new Action(person, mockedDevice, Constants.USUAL_USING_ACTION);

        ArrayList<HomeItem> mockedHomeItemsList = mock(ArrayList.class);
        person.setCurrentAction(action);
        person.setBusy(true);

        Assertions.assertNotNull(person.getCurrentAction());

        for (int i = 1; i <= action.getNumOfPhases(); i++) {
            int previousNumOfCurrentPhase = action.getNumOfCurrentPhase();
            person.update(rooms, mockedHomeItemsList);
            int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

            Assertions.assertEquals(previousNumOfCurrentPhase + 1, currentNumOfCurrentPhase);
            if (i == action.getNumOfPhases())
                Assertions.assertTrue(action.isDone());
            else
                Assertions.assertFalse(action.isDone());
        }

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());
    }

    @Test
    public void update_personIsBusyAndDeviceGetsBroken_doesNotFinishAction() {
        Device mockedDevice = mock(Device.class);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        when(mockedDevice.isBroken()).thenReturn(false);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        Action action = new Action(person, mockedDevice, Constants.USUAL_USING_ACTION);

        ArrayList<HomeItem> mockedHomeItemsList = mock(ArrayList.class);
        person.setCurrentAction(action);
        person.setBusy(true);

        Assertions.assertNotNull(person.getCurrentAction());

        for (int i = 1; i <= action.getNumOfPhases() - 1; i++) {
            int previousNumOfCurrentPhase = action.getNumOfCurrentPhase();
            person.update(rooms, mockedHomeItemsList);
            int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

            Assertions.assertEquals(previousNumOfCurrentPhase + 1, currentNumOfCurrentPhase);
            Assertions.assertFalse(action.isDone());
        }

        when(mockedDevice.isBroken()).thenReturn(true);

        person.update(rooms, mockedHomeItemsList);
        int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

        Assertions.assertEquals(0, currentNumOfCurrentPhase);

        Assertions.assertFalse(action.isDone());

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());
    }

    @Test
    public void update_personIsBusyAndElectricityShutsDown_doesNotFinishAction() {
        Device mockedDevice = mock(Device.class);
        when(mockedDevice.getLocation()).thenReturn(mockedLivingRoom);
        when(mockedDevice.isBroken()).thenReturn(false);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        Action action = new Action(person, mockedDevice, Constants.USUAL_USING_ACTION);

        ArrayList<HomeItem> mockedHomeItemsList = mock(ArrayList.class);
        person.setCurrentAction(action);
        person.setBusy(true);

        Assertions.assertNotNull(person.getCurrentAction());

        for (int i = 1; i <= action.getNumOfPhases() - 1; i++) {
            int previousNumOfCurrentPhase = action.getNumOfCurrentPhase();
            person.update(rooms, mockedHomeItemsList);
            int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

            Assertions.assertEquals(previousNumOfCurrentPhase + 1, currentNumOfCurrentPhase);
            Assertions.assertFalse(action.isDone());
        }

        when(mockedLivingRoom.hasElectricity()).thenReturn(false);

        person.update(rooms, mockedHomeItemsList);
        int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

        Assertions.assertEquals(0, currentNumOfCurrentPhase);

        Assertions.assertFalse(action.isDone());

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());
    }


    @Test
    public void update_doSport_executesAction() {
        Room room = new Room("roomName", 1);

        Device mockedDevice1 = mock(Device.class);
        Device mockedDevice2 = mock(Device.class);
        PetToy mockedPetToy1 = mock(PetToy.class);
        PetToy mockedPetToy2 = mock(PetToy.class);
        SportEquipment mockedSportEquipment = mock(SportEquipment.class);
        when(mockedSportEquipment.getName()).thenReturn("SportEquipment");
        when(mockedSportEquipment.toString()).thenReturn("SportEquipment");
        doCallRealMethod().when(mockedSportEquipment).setBeingUsed(true);
        doCallRealMethod().when(mockedSportEquipment).setBeingUsed(false);
        doCallRealMethod().when(mockedSportEquipment).isBeingUsed();

        room.addEntity(mockedDevice1);
        room.addEntity(mockedDevice2);
        room.addEntity(mockedPetToy1);
        room.addEntity(mockedPetToy2);
        room.addEntity(mockedSportEquipment);

        ArrayList<HomeItem> HomeItemsList = new ArrayList<>();
        HomeItemsList.add(mockedDevice1);
        HomeItemsList.add(mockedDevice2);
        HomeItemsList.add(mockedPetToy1);
        HomeItemsList.add(mockedPetToy2);
        HomeItemsList.add(mockedSportEquipment);

        person.setBusy(false);

        Assertions.assertNull(person.getCurrentAction());
        person.doSports(HomeItemsList);
        Assertions.assertTrue(mockedSportEquipment.isBeingUsed());

        Assertions.assertTrue(person.isBusy());
        Assertions.assertNotNull(person.getCurrentAction());

        Action action = person.getCurrentAction();

        for (int i = 1; i <= action.getNumOfPhases(); i++) {
            int previousNumOfCurrentPhase = action.getNumOfCurrentPhase();
            person.update(rooms, HomeItemsList);
            int currentNumOfCurrentPhase = action.getNumOfCurrentPhase();

            Assertions.assertEquals(previousNumOfCurrentPhase + 1, currentNumOfCurrentPhase);
            if (i ==  action.getNumOfPhases())
                Assertions.assertTrue(action.isDone());
            else
                Assertions.assertFalse(action.isDone());
        }

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());

    }

    @Test
    public void update_doSportWithoutSportEquipment_actionWasNotExecuted() {
        person.setBusy(false);
        Assertions.assertNull(person.getCurrentAction());
        ArrayList<HomeItem> mockedHomeItemsList = mock(ArrayList.class);

        setUpStreams();
        person.doSports(mockedHomeItemsList);
        String expectedResult = "Valerie has nothing to do in Living room\n";
        Assertions.assertEquals(expectedResult, outContent.toString());
        restoreStreams();

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());
    }

    @Test
    public void useSmtAtHome_usesBrokenItem_itemIsFixed() {
        Device mockedDevice = mock(Device.class);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        doCallRealMethod().when(mockedDevice).setBroken(true);
        doCallRealMethod().when(mockedDevice).setBroken(false);
        doCallRealMethod().when(mockedDevice).isBroken();
        doCallRealMethod().when(mockedDevice).setBeingUsed(true);
        doCallRealMethod().when(mockedDevice).setBeingUsed(false);
        doCallRealMethod().when(mockedDevice).isBeingUsed();
        when(mockedDevice.hasElectricity()).thenReturn(true);
        FunctionalityInfo functionalityInfo = new FunctionalityInfo();
        when(mockedDevice.getFunctionalityInfo()).thenReturn(functionalityInfo);
        Manual manual = new Manual();
        when(mockedDevice.getManual()).thenReturn(manual);
        mockedDevice.setBroken(true);
        mockedDevice.setBeingUsed(false);

        mockedDevice.addCurrentEvent(mock(BrokenDevice.class));

        ArrayList<HomeItem> HomeItemsList = new ArrayList<>();
        HomeItemsList.add(mockedDevice);

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());
        Assertions.assertTrue(mockedDevice.isBroken());

        person.useSmtAtHome(HomeItemsList);
        Assertions.assertTrue(person.getCurrentAction() instanceof FixAction);
        Assertions.assertTrue(mockedDevice.isBeingUsed());
        Assertions.assertTrue(person.isBusy());

        int readManualPhases = manual.getNumOfPhasesToReadManual();

        for (int i = 0; i <= readManualPhases; i++) {
            person.update(rooms, HomeItemsList);
        }

        int fixItemPhases = manual.getNumOfPhasesToFix();

        for (int i = 0; i <= fixItemPhases; i++) {
            person.update(rooms, HomeItemsList);
        }

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());
        Assertions.assertFalse(mockedDevice.isBroken());
        Assertions.assertFalse(mockedDevice.isBeingUsed());
    }
}