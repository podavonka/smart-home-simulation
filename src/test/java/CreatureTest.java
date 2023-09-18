import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.creatures.CreatureInfo;
import cz.cvut.omo.smartHome.entities.creatures.Person;
import cz.cvut.omo.smartHome.house.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CreatureTest {
    private Creature creature;

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

        creature = new Person("Valerie", mockedLivingRoom, 20, 120, 170, 50, CreatureInfo.Gender.FEMALE);
    }

    @Test
    public void moveToAnotherRoom_changesLocation_locationExistsAndIsDifferent() {
        rooms.add(mockedKitchen);
        rooms.add(mockedBathroom);

        while (creature.getLocation() == mockedLivingRoom) {
            creature.moveToAnotherRoom(rooms);
        }

        Assertions.assertTrue(rooms.contains(creature.getLocation()) && creature.getLocation() != mockedLivingRoom);
    }

    @Test
    public void moveToAnotherRoom_changesLocation_locationIsSame() {
        creature.moveToAnotherRoom(rooms);

        Assertions.assertTrue(rooms.contains(creature.getLocation()) && creature.getLocation() == mockedLivingRoom);
    }
}
