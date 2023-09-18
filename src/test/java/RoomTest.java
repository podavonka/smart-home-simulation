import cz.cvut.omo.smartHome.entities.homeItems.PetToy;
import cz.cvut.omo.smartHome.entities.homeItems.SportEquipment;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class RoomTest {

    private Room room;
    private Device mockedDevice1;
    private Device mockedDevice2;
    private PetToy mockedPetToy1;
    private PetToy mockedPetToy2;
    private SportEquipment mockedSportEquipment;

    @BeforeEach
    public void setRoom() {
        room = new Room("roomName", 1);

        mockedPetToy1 = mock(PetToy.class);
        mockedPetToy2 = mock(PetToy.class);
        mockedSportEquipment = mock(SportEquipment.class);

        room.addEntity(mockedPetToy1);
        room.addEntity(mockedPetToy2);
        room.addEntity(mockedSportEquipment);
    }

    @Test
    public void getRandomDevice_addsTwoDevicesToRoom_devicesExists() {
        mockedDevice1 = mock(Device.class);
        mockedDevice2 = mock(Device.class);
        room.addEntity(mockedDevice1);
        room.addEntity(mockedDevice2);
        Device device = room.getRandomDevice();

        Assertions.assertTrue(device == mockedDevice1 || device == mockedDevice2);
    }

    @Test
    public void getRandomDevice_noDevicesAreAdded_noDevicesFound() {
        Device device = room.getRandomDevice();

        Assertions.assertNull(device);
    }
}
