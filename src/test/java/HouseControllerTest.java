import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.HouseController;
import cz.cvut.omo.smartHome.house.events.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

public class HouseControllerTest {

    private HouseController HC;

    @BeforeEach
    public void setHouse() {
        HC = HouseController.getInstance();
        HC.createHouse();
    }

    static Stream<Arguments> generateReportArgumentsSource() {
        return Stream.of(
                Arguments.of("ActivityAndUsageReport.txt"),
                Arguments.of("ConsumptionReport.txt"),
                Arguments.of("ActivityAndUsageReport.txt")
        );
    }

    @ParameterizedTest(name = "File {0} has been created")
    @MethodSource("generateReportArgumentsSource")
    public void generateReport_thereIsNoStatistics_emptyReports(String fileName) {
        HC.generateReport();

        File activityAndUsageReportFile = new File(fileName);
        boolean fileExists = activityAndUsageReportFile.exists();

        Assertions.assertTrue(fileExists);
    }


    static Stream<Arguments> getRoomByNameArgumentsSource() {
        return Stream.of(
                Arguments.of("Living room", true),
                Arguments.of("Bedroom", true),
                Arguments.of("Bathroom", true),
                Arguments.of("Kitchen", true),
                Arguments.of("Rohlik", false)
        );
    }

    @ParameterizedTest(name = "Room {0} has been found")
    @MethodSource("getRoomByNameArgumentsSource")
    public void getRoomByName_getsRoomName_returnsTrueIfExists(String roomName, boolean expectedResult) {
        Room room = HC.getRoomByName(roomName);
        boolean actualResult = false;

        if (room != null) {
            actualResult = true;
        }

        Assertions.assertEquals(expectedResult, actualResult);
    }

    static Stream<Arguments> generateRoomEventArgumentsSource() {
        return Stream.of(
                Arguments.of(1),
                Arguments.of(123)
        );
    }

    @ParameterizedTest(name = "Event in {0} iteration has been found")
    @MethodSource("generateRoomEventArgumentsSource")
    public void generateRoomEvent_isSearchingForEventForCurrIter_getsEvents(int expectedIterationNumber) {
        while (!HC.getEvents().isEmpty()) {
            HC.generateRoomEvent(expectedIterationNumber);
        }
        List<Event> events = HC.getEvents();

        for (Event event : events) {
            Assertions.assertEquals(expectedIterationNumber, event.getCurrentIteration());
        }
    }
}
