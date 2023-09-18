package cz.cvut.omo.smartHome.utils;

import cz.cvut.omo.smartHome.entities.creatures.Creature;
import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.controller.Action;
import cz.cvut.omo.smartHome.house.controller.FixAction;
import cz.cvut.omo.smartHome.house.events.BrokenDevice;
import cz.cvut.omo.smartHome.house.events.ElectricityOutage;
import cz.cvut.omo.smartHome.house.events.Event;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ReportGenerator {
//    private Map<Creature, List<Action>> activityAndUsage = new HashMap<>();
    private static List<Creature> creatures;
    private static List<Device> devices;
    private static List<Event> events;

    public ReportGenerator(List <Creature> creatures, List<Device> devices, List<Event> events) {
        ReportGenerator.creatures = creatures;
        ReportGenerator.devices = devices;
        ReportGenerator.events = events;
    }

    public static void generateActivityAndUsageReport() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("ActivityAndUsageReport.txt", false));

        for (Creature creature : creatures) {
            writer.append(creature.getName()).append(":").append("\n");
            for (Action action : creature.getStartedActions()) {
                HomeItem item = action.getItemToUse();
                int numOfPhase = action.getNumOfCurrentPhase();
                if (item != null) {
                    if (action instanceof FixAction)
                        writer.append("\t")
                        .append(action.getActionInfo()).append(" ------> ")
                        .append(Integer.toString(item.getManual().getNumOfPhasesToReadManual()))
                        .append(" (manual) + ")
                        .append(Integer.toString(item.getManual().getNumOfPhasesToFix()))
                        .append(" (fixing) = ")
                        .append(Integer.toString((item.getManual().getNumOfPhasesToReadManual()) + item.getManual().getNumOfPhasesToFix()))
                        .append(" hours.").append("\n");

                    else writer.append("\t").append(action.getActionInfo()).append(" ------> ")
                            .append(Integer.toString(numOfPhase))
                            .append(" hours.").append("\n");

                } else {
                    writer.append("\t").append(action.getActionInfo()).append(" ------> ").append(Integer.toString(numOfPhase)).append(" hours.").append("\n");
                }
            }
            writer.append("\n");
        }

        writer.close();
    }

    public static void generateConsumptionReport() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("ConsumptionReport.txt", false));

        for (Device device : devices) {
            writer.append(device.getInfo()).append(":").append("\n")
                    .append("\t").append("Final functionality -> ").append(String.valueOf(device.getFunctionality())).append(" %").append("\n")
                    .append("\t").append("Total usage time -> ").append(String.valueOf(device.getTotalUsageTime())).append(" hours").append("\n")
                    .append("\t").append("Total used electricity -> ").append(String.valueOf(device.getConsumption())).append(" W  | approximate price = ").append(String.valueOf((float) (device.getConsumption()  * 0.0045) )).append(" Kc").append("\n")
                    .append("\t").append("Total count of fixing -> ").append(String.valueOf(device.getTotalFixingCount())).append(" times").append("\n")
                    .append("\n");
        }
        writer.close();
    }

    public static void generateEventReport() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("EventReport.txt", false));
        List<BrokenDevice> brokenDeviceEvents = events.stream().filter(event -> event instanceof BrokenDevice).map(BrokenDevice.class::cast).collect(Collectors.toList());
        List<ElectricityOutage> electricityOutagesEvents = events.stream().filter(event -> event instanceof ElectricityOutage).map(ElectricityOutage.class::cast).collect(Collectors.toList());

        writer.append("'Broken device' event report:").append("\n");
        for (BrokenDevice event : brokenDeviceEvents) {
            if (event.getEventSolver() != null){
                writer.append("\t").append("Broken ").append(event.getBrokenDevice().getName())
                        .append(" in ").append(event.getLocation().getName()).append("\n\t\ton ")
                        .append(String.valueOf(event.getCurrentIteration())).append(" iteration \n\t\twas fixed by ")
                        .append(event.getEventSolver().getName()).append("\n\n");
            } else {
                writer.append("\t").append("Broken ").append(event.getBrokenDevice().getName())
                        .append(" in ").append(event.getLocation().getName())
                        .append("\n\t\ton ").append(String.valueOf(event.getCurrentIteration()))
                        .append(" iteration \n\t\twas NOT fixed!").append("\n\n");
            }
        }

        writer.append("\n").append("'Electricity outage' event report:").append("\n");
        for (ElectricityOutage event : electricityOutagesEvents) {
            if (event.getEventSolver() != null) {
                writer.append("\t").append("Electricity problem in ")
                        .append(event.getLocation().getName()).append("\n\t\ton ")
                        .append(String.valueOf(event.getCurrentIteration())).append(" iteration \n\t\twas solved by ")
                        .append(event.getEventSolver().getName()).append("\n\n");
            } else {
                writer.append("\t").append("Electricity problem in ")
                        .append(event.getLocation().getName()).append("\n\t\ton ").append(String.valueOf(event.getCurrentIteration()))
                        .append(" iteration \n\t\twas NOT solved!").append("\n\n");
            }
        }
        writer.close();
    }
}
