package entity;

public class Owner {
    private String name;
    private Pet pet;

    public Owner(String name, Pet pet) {
        this.name = name;
        this.pet = pet;
    }

    public String getName() {
        return name;
    }

    public Pet getPet() {
        return pet;
    }

    public void setName(String name) {
        this.name = name;
    }
}
