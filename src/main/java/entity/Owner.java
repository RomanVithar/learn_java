package entity;

import java.util.ArrayList;
import java.util.List;

public class Owner {
    private String name;
    private List<Pet> pets;

    public Owner() {
        pets = new ArrayList<>();
    }

    public Owner(String name, Pet pet) {
        this();
        this.name = name;
        pets.add(pet);
    }
    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public String getName() {
        return name;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setName(String name) {
        this.name = name;
    }
}
