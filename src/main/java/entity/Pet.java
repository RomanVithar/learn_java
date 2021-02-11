package entity;

import java.util.Objects;

public class Pet {
    private String name;
    private PetType type;
    private Owner owner;
    private Integer id;

    public Pet() {
    }

    public Pet(String name, PetType type, Owner owner) {
        this();
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public Pet(Integer id, String name, String type, Owner owner) {
        this();
        PetType t = null;
        switch (type) {
            case "DOG":
                t = PetType.DOG;
                break;
            case "CAT":
                t = PetType.CAT;
                break;
            case "RAT":
                t = PetType.RAT;
                break;
            case "HAMSTER":
                t = PetType.HAMSTER;
                break;
        }
        this.name = name;
        this.type = t;
        this.owner = owner;
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(name, pet.name) &&
                type == pet.type &&
                Objects.equals(owner, pet.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", id='" + id + '\'' +
                '}';
    }
}
