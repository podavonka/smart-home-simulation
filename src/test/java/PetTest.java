import cz.cvut.omo.smartHome.entities.creatures.CreatureInfo;
import cz.cvut.omo.smartHome.entities.creatures.Pet;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.PetToy;
import cz.cvut.omo.smartHome.entities.homeItems.SportEquipment;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.Action;
import cz.cvut.omo.smartHome.utils.Constants;
import cz.cvut.omo.smartHome.utils.statistics.FunctionalityInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class PetTest {
    private Pet pet;

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
        when(mockedLivingRoom.hasElectricity()).thenReturn(true);
        when(mockedKitchen.getName()).thenReturn("Kitchen");
        when(mockedBathroom.getName()).thenReturn("Bathroom");

        rooms.add(mockedLivingRoom);
        rooms.add(mockedKitchen);
        rooms.add(mockedBathroom);

        pet = new Pet("Leonardo", mockedLivingRoom, 50, 140, 5, 2, CreatureInfo.Gender.OTHER, Pet.PetType.TURTLE);
    }

    @Test
    public void update_petIsPlayingWithToy_actionIsExecuted() {
        Room room = new Room("roomName", 1);

        Device mockedDevice1 = mock(Device.class);
        Device mockedDevice2 = mock(Device.class);
        PetToy mockedPetToy1 = mock(PetToy.class);
        PetToy mockedPetToy2 = mock(PetToy.class);
        when(mockedPetToy1.getName()).thenReturn("PetToy1");
        when(mockedPetToy2.getName()).thenReturn("PetToy2");
        SportEquipment mockedSportEquipment = mock(SportEquipment.class);

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

        Assertions.assertFalse(pet.isBusy());

        pet.chooseAction(HomeItemsList);

        Assertions.assertTrue(pet.isBusy());

        while (pet.getCurrentAction().getActionID() == Constants.PET_WANTS_TO_SLEEP_ACTION) {
            pet.chooseAction(HomeItemsList);
        }

        Assertions.assertEquals(Constants.PET_WANTS_TO_PLAY_WITH_TOY_ACTION, pet.getCurrentAction().getActionID());

        Action action = pet.getCurrentAction();

        for (int i = 1; i <= action.getNumOfPhases(); i++) {
            pet.update(rooms, HomeItemsList);
            if (i ==  action.getNumOfPhases())
                Assertions.assertTrue(action.isDone());
            else
                Assertions.assertFalse(action.isDone());
        }
    }

    @Test
    public void update_petIsEatingDevice_functionalityIsReduced() {
        Room room = new Room("roomName", 1);

        Device mockedDevice = mock(Device.class);
        when(mockedDevice.getName()).thenReturn("Device");
        when(mockedDevice.toString()).thenReturn("Device");
        FunctionalityInfo functionalityInfo = new FunctionalityInfo();
        int functionalityValueBefore = functionalityInfo.getValue();

        when(mockedDevice.getFunctionalityInfo()).thenReturn(functionalityInfo);
        SportEquipment mockedSportEquipment = mock(SportEquipment.class);

        room.addEntity(mockedDevice);
        room.addEntity(mockedSportEquipment);

        ArrayList<HomeItem> HomeItemsList = new ArrayList<>();
        HomeItemsList.add(mockedDevice);
        HomeItemsList.add(mockedSportEquipment);

        Assertions.assertFalse(pet.isBusy());

        pet.chooseAction(HomeItemsList);

        Assertions.assertTrue(pet.isBusy());

        while (pet.getCurrentAction().getActionID() == Constants.PET_WANTS_TO_SLEEP_ACTION) {
            pet.chooseAction(HomeItemsList);
        }

        Assertions.assertEquals(Constants.PET_TRIES_TO_EAT_HOME_ITEM, pet.getCurrentAction().getActionID());

        Action action = pet.getCurrentAction();

        for (int i = 1; i <= action.getNumOfPhases(); i++) {
            pet.update(rooms, HomeItemsList);
            if (i ==  action.getNumOfPhases())
                Assertions.assertTrue(action.isDone());
            else
                Assertions.assertFalse(action.isDone());
        }

        Assertions.assertEquals(functionalityValueBefore - Constants.EATEN_ITEM_FUNCTIONALITY_REDUCE, functionalityInfo.getValue());
    }
}
