package entity;

public class Pet {
    private String name;
    private PetType type;
    private Owner owner;

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
}
