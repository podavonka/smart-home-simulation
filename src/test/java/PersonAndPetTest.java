import cz.cvut.omo.smartHome.entities.creatures.CreatureInfo;
import cz.cvut.omo.smartHome.entities.creatures.Person;
import cz.cvut.omo.smartHome.entities.creatures.Pet;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.Action;
import cz.cvut.omo.smartHome.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doCallRealMethod;

public class PersonAndPetTest {
    private Pet pet;
    private Person person;

    private List<Room> rooms = new ArrayList<>();

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

    @Test
    public void update_PersonIsUsingDevice_PetCanNotEatDevice() {
        Room room = new Room("roomName", 1);
        room.setElectricity(true);
        pet = new Pet("Leonardo", room, 4, 140, 5, 2, CreatureInfo.Gender.OTHER, Pet.PetType.TURTLE);
        person = new Person("Valerie", room, 20, 120, 170, 50, CreatureInfo.Gender.FEMALE);

        Device mockedDevice = mock(Device.class);
        when(mockedDevice.hasElectricity()).thenReturn(true);
        when(mockedDevice.getLocation()).thenReturn(room);
        doCallRealMethod().when(mockedDevice).setBeingUsed(true);
        doCallRealMethod().when(mockedDevice).setBeingUsed(false);
        doCallRealMethod().when(mockedDevice).isBeingUsed();

        room.addEntity(mockedDevice);

        ArrayList<HomeItem> HomeItemsList = new ArrayList<>();
        HomeItemsList.add(mockedDevice);

        Assertions.assertFalse(pet.isBusy());
        Assertions.assertFalse(person.isBusy());

        person.useSmtAtHome(HomeItemsList);
        Assertions.assertTrue(person.isBusy());

        setUpStreams();
        pet.playWithSmt(HomeItemsList);
        restoreStreams();

        String expectedResult = "Leonardo has nothing to do in roomName (1. floor)\n";
        Assertions.assertEquals(expectedResult, outContent.toString());

        Assertions.assertEquals(Constants.PET_WANTS_TO_SLEEP_ACTION, pet.getCurrentAction().getActionID());
        Assertions.assertTrue(pet.isBusy());

        Action action = person.getCurrentAction();

        for (int i = 1; i <= action.getNumOfPhases(); i++) {
            person.update(rooms, HomeItemsList);
            if (i ==  action.getNumOfPhases())
                Assertions.assertTrue(action.isDone());
            else
                Assertions.assertFalse(action.isDone());
        }

        Assertions.assertFalse(person.isBusy());
        Assertions.assertNull(person.getCurrentAction());
    }
}