package cz.cvut.omo.smartHome.house.events;

import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.utils.Constants;

/**
 * Represents the event of electricity outage.
 */
public class ElectricityOutage extends Event {

    public ElectricityOutage(String eventName, Room location, int currentIteration) {
        super(eventName, location, Constants.ELECTRICITY_OUTAGE, currentIteration);
        this.getLocation().setElectricity(false);
        System.out.println("! Event 'Electricity outage' generated in " + location + " !");
    }
}
