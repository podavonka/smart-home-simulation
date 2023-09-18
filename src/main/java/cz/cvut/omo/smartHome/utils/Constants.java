package cz.cvut.omo.smartHome.utils;

/**
 * Represents useful constants to control the house.
 */
public class Constants {

    /**
    * Event constants.
    */
    public static final int NUM_OF_EVENTS = 2;
    public static final int ELECTRICITY_OUTAGE = 1000;
    public static final int BROKEN_DEVICE = 1001;
    public static final int PHASES_TO_FIND_ROOM_WITHOUT_ELECTRICITY = 1;


    /**
     * Person's action constants.
     */
    public static final int USUAL_USING_ACTION = 101;
    public static final int DO_SPORT_ACTION = 102;
    public static final int FIX_ACTION = 103;

    /**
     * Person's fix action constant.
     */
    public static final int READ_MANUAL_ACTION = 107;
    public static final int FIX_ITEM_ACTION = 108;

    /**
     * Pet's action constants.
     */
    public static final int PET_WANTS_TO_SLEEP_ACTION = 104;
    public static final int PET_WANTS_TO_PLAY_WITH_TOY_ACTION = 105;
    public static final int PET_TRIES_TO_EAT_HOME_ITEM = 106;
    public static final int EATEN_ITEM_FUNCTIONALITY_REDUCE = 5;

    /**
     * Devices' information constants.
     */
    public static final int FUNCTIONALITY_MAXIMUM = 100;

    public static String getActionInfo(int actionID) {
        String info = "";
        switch (actionID) {
            case PET_WANTS_TO_SLEEP_ACTION -> info += "Sleeping ";
            case PET_WANTS_TO_PLAY_WITH_TOY_ACTION -> info += "Playing with ";
            case PET_TRIES_TO_EAT_HOME_ITEM -> info += "Trying to eat ";
            case USUAL_USING_ACTION -> info += "Using ";
            case DO_SPORT_ACTION -> info += "Sporting with ";
            case FIX_ACTION -> info += "Fixing ";
            case ELECTRICITY_OUTAGE -> info += "Solving problem with electricity ";
            default -> info += "Using ";
        }
        return info;
    }
}
