package cz.cvut.omo.smartHome.entities.creatures;

import cz.cvut.omo.smartHome.entities.homeItems.HomeItem;
import cz.cvut.omo.smartHome.entities.homeItems.PetToy;
import cz.cvut.omo.smartHome.entities.homeItems.SportEquipment;
import cz.cvut.omo.smartHome.house.Room;
import cz.cvut.omo.smartHome.house.controller.PetAction;
import cz.cvut.omo.smartHome.utils.Constants;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Represents family's pets.
 */
public class Pet extends Creature {
    public enum PetType { DOG, CAT, HAMSTER, TURTLE }

    private PetType type;
    private boolean isObedient = false;

    public Pet(String name, Room location, int age, int IQ, int height, int weight, CreatureInfo.Gender gender, PetType type) {
        super(name, location, age, IQ, height, weight, gender);
        this.type = type;
    }

    /**
     * If there is a toy at the current location, plays with it.
     * If there is NOT a toy, chooses another free item and tries to eat it.
     * Otherwise the pet does nothing.
     *
     * @param homeItems Available home items at the current location.
     */
    public void playWithSmt(List<HomeItem> homeItems) {
        Random rand = new Random();
        List<HomeItem> notOccupiedItems = homeItems.stream()
                .filter(homeItem -> !homeItem.isBeingUsed())
                .filter(homeItem -> !(homeItem instanceof SportEquipment))
                .collect(Collectors.toList());

        List<HomeItem> notOccupiedToys = homeItems.stream()
                .filter(homeItem -> homeItem instanceof PetToy)
                .filter(homeItem -> !homeItem.isBeingUsed())
                .collect(Collectors.toList());

        if (notOccupiedToys.size() != 0) {
            HomeItem item = notOccupiedToys.get(rand.nextInt(notOccupiedToys.size()));

            this.setBusy(true);
            item.setBeingUsed(true);
            currentAction = new PetAction(this, item, Constants.PET_WANTS_TO_PLAY_WITH_TOY_ACTION);
        } else if (notOccupiedItems.size() != 0) {
            HomeItem item = notOccupiedItems.get(rand.nextInt(notOccupiedItems.size()));

            this.setBusy(true);
            item.setBeingUsed(true);
            currentAction = new PetAction(this, item, Constants.PET_TRIES_TO_EAT_HOME_ITEM);
        } else {
            this.setBusy(true);
            currentAction = new PetAction(this, getLocation(), Constants.PET_WANTS_TO_SLEEP_ACTION);
            System.out.println(getName() + " has nothing to do in " + getLocation());
        }
        startedActions.add(currentAction);
    }

    /**
     * Turns the pet to sleep.
     */
    private void sleepAction() {
        this.setBusy(true);
        currentAction = new PetAction(this, getLocation(), Constants.PET_WANTS_TO_SLEEP_ACTION);
    }

    @Override
    public void chooseAction(List<HomeItem> homeItems) {
        Random rand = new Random();
        if (rand.nextDouble() <= 0.3
                || (rand.nextDouble() <= 0.3 && creatureInfo.getAge() >= 5)
                || (rand.nextDouble() <= 0.5 && creatureInfo.getAge() >= 8)) {
            sleepAction();
        } else {
            playWithSmt(homeItems);
        }
    }
}
