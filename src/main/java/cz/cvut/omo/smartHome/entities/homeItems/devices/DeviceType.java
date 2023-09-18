package cz.cvut.omo.smartHome.entities.homeItems.devices;

/**
 * Represents types of devices and their manuals.
 */
public enum DeviceType {
    TV, PLAY_STATION, COMPUTER, TABLET, FRIDGE, KETTLE, LAMP, THERMOSTAT, SPEAKER;

    /**
     * @return Consumption of electricity depending on device type.
     */
    public int getConsumption() {
        int consumption;
        switch (this) {
            case TV -> consumption = 45;
            case PLAY_STATION -> consumption = 95;
            case COMPUTER -> consumption = 400;
            case TABLET -> consumption = 20;
            case FRIDGE -> consumption = 135;
            case KETTLE -> consumption = 25;
            case LAMP -> consumption = 15;
            case THERMOSTAT -> consumption = 10;
            case SPEAKER -> consumption = 30;
            default -> consumption = 5;
        }
        return consumption;
    }
}
