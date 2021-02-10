package entity;

import java.util.Objects;

public class Pet {
    private String name;
    private PetType type;
    private Owner owner;
    private int id;

    public Pet() {
    }

    public Pet(String name, PetType type, Owner owner) {
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public Pet(String name, PetType type) {
        this(name, type, null);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public PetType getType() {
        return type;
    }

    public void generateId() {
        id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return id == pet.id &&
                Objects.equals(name, pet.name) &&
                type == pet.type &&
                Objects.equals(owner, pet.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    public long getId() {
        return id;
    }
}
