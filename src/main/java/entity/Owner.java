package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Owner {
    private String name;
    private List<Pet> pets;
    private int id;

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

    public void generateId() {
        id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Owner owner = (Owner) o;
        return id == owner.id &&
                Objects.equals(name, owner.name) &&
                Objects.equals(pets, owner.pets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pets);
    }

    public long getId() {
        return id;
    }
}
