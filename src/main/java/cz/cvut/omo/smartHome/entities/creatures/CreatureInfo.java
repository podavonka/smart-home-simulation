package cz.cvut.omo.smartHome.entities.creatures;

public class CreatureInfo {
    public enum Gender { MALE, FEMALE, OTHER };

    private int age;
    private int IQ;
    private int height;
    private int weight;
    private Gender gender;

    public CreatureInfo(int age, int IQ, int height, int weight, Gender gender) {
        this.age = age;
        this.IQ = IQ;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public int getIQ() {
        return IQ;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public Gender getGender() {
        return gender;
    }
}
