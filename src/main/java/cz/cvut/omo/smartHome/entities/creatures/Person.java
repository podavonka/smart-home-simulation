package cz.cvut.omo.smartHome.entities.creatures;

import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.PetToy;
import cz.cvut.omo.smartHome.entities.homeItems.SportEquipment;
import cz.cvut.omo.smartHome.entities.homeItems.devices.Device;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.Action;
import cz.cvut.omo.smartHome.house.controller.FixAction;
import cz.cvut.omo.smartHome.utils.Constants;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents family members.
 */
public class Person extends Creature {
    private boolean isAdult = false;

    public Person(String name, Room location, int age, int IQ, int height, int weight, CreatureInfo.Gender gender) {
        super(name, location, age, IQ, height, weight, gender);
        if (age >= 18) isAdult = true;
    }

    /**
     * If there is free item, chooses one and do action with it.
     * Otherwise the person does nothing.
     *
     * @param homeItems Available home items at the current location.
     */
    public void useSmtAtHome(List<HomeItem> homeItems) {
        Random rand = new Random();
        List<HomeItem> notOccupiedItems = homeItems.stream()
                .filter(homeItem -> !homeItem.isBeingUsed())
                .filter(homeItem -> !(homeItem instanceof SportEquipment))
                .filter(homeItem -> !(homeItem instanceof PetToy))
                .collect(Collectors.toList());

        if (notOccupiedItems.size() != 0) {
            HomeItem item = notOccupiedItems.get(rand.nextInt(notOccupiedItems.size()));
            this.setBusy(true);

            if (item instanceof Device) {
                if (!((Device) item).hasElectricity()) {
                    currentEvent = getLocation().getCurrentEvent();
                    currentAction = new Action(this, getLocation(), Constants.ELECTRICITY_OUTAGE);
                } else if (item.isBroken()) {
                    item.setBeingUsed(true);
                    currentEvent = item.getCurrentEvent();
                    currentAction = new FixAction(this, item, Constants.FIX_ACTION);
                } else {
                    item.setBeingUsed(true);
                    ((Device) item).turnOn();
                    currentAction = new Action(this, item, Constants.USUAL_USING_ACTION);
                }
            } else {
                if (item.isBroken()) {
                    item.setBeingUsed(true);
                    currentEvent = item.getCurrentEvent();
                    currentAction = new FixAction(this, item, Constants.FIX_ACTION);
                } else {
                    item.setBeingUsed(true);
                    currentAction = new Action(this, item, Constants.USUAL_USING_ACTION);
                }
            }
            startedActions.add(currentAction);
        } else {
            System.out.println(getName() + " has nothing to do in " + getLocation());
        }
    }

    /**
     * Chooses free equipment and do sports using it.
     *
     * @param homeItems Available home items at the current location.
     */
    public void doSports(List<HomeItem> homeItems) {
        Random rand = new Random();
        List<SportEquipment> notOccupiedSportEquipment = homeItems.stream()
                .filter(homeItem -> !homeItem.isBeingUsed())
                .filter(homeItem -> homeItem instanceof SportEquipment)
                .map(SportEquipment.class::cast)
                .collect(Collectors.toList());

        if (notOccupiedSportEquipment.size() != 0) {
            SportEquipment equipment = notOccupiedSportEquipment.get(rand.nextInt(notOccupiedSportEquipment.size()));

            if (!equipment.isBroken()) {
                this.setBusy(true);
                equipment.setBeingUsed(true);
                currentAction = new Action(this, equipment, Constants.DO_SPORT_ACTION);
                startedActions.add(currentAction);
            }
        } else {
            System.out.println(getName() + " has nothing to do in " + getLocation());
        }
    }

    /**
     * Asks whether the person has already turned 18 years old.
     *
     * @return true, if the person is over 18 years old,
     *         false, if the person is NOT.
     */
    public boolean isAdult() {
        return isAdult;
    }

    @Override
    public void update(List<Room> rooms, List<HomeItem> homeItems) {
        if (!isBusy) {
            moveToAnotherRoom(rooms.stream().filter(room -> !Objects.equals(room.getName(), "Garage"))
                    .collect(Collectors.toList()));
            chooseAction(homeItems);
        }
        if (currentAction != null) currentAction.update();
    }

    @Override
    public void chooseAction(List<HomeItem> homeItems) {
        Random rand = new Random();
        if (rand.nextDouble() <= 0.3) {
            doSports(homeItems);
        } else {
            useSmtAtHome(homeItems.stream()
                    .filter(homeItem -> homeItem.getLocation() == getLocation())
                    .filter(HomeItem::isUsable)
                    .collect(Collectors.toList()));
        }
    }
}
