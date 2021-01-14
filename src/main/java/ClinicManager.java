import entity.Owner;
import entity.Pet;
import entity.PetType;

import java.util.ArrayList;
import java.util.Objects;

public class ClinicManager {
    private final ArrayList<Owner> owners;
    private final ArrayList<Pet> pets;

    public ClinicManager() {
        owners = new ArrayList<>();
        pets = new ArrayList<>();
    }

    public void addClient(String name, String petName, PetType petType) {
        Pet pet = new Pet(petName, petType);
        Owner owner = new Owner(name, pet);
        pet.setOwner(owner);
        owners.add(owner);
        pets.add(pet);
    }

    public String getNameClient(final String petName) {
        Pet pet1 = pets.stream()
                .filter(pet -> petName.equals(pet.getName()))
                .findAny()
                .orElse(null);
        return pet1 == null ? null : pet1.getOwner().getName();
    }

    public String getPetName(String clientName) {
        Owner owner1 = owners.stream()
                .filter(owner -> clientName.equals(owner.getName()))
                .findAny()
                .orElse(null);
        return owner1 == null ? null : owner1.getPet().getName();
    }

    public void changeClientName(String oldName, String newName) {
        Objects.requireNonNull(owners.stream()
                .filter(owner -> oldName.equals(owner.getName()))
                .findAny()
                .orElse(null)).setName(newName);
    }

    public void changePetName(String oldName, String newName) {
        Objects.requireNonNull(pets.stream()
                .filter(pet -> oldName.equals(pet.getName()))
                .findAny()
                .orElse(null)).setName(newName);
    }

    public void deleteClient(String nameClient) {
        for (int i = 0; i < owners.size(); i++) {
            if (owners.get(i).getName().equals(nameClient)) {
                pets.remove(owners.get(i).getPet());
                owners.remove(i);
                break;
            }
        }
    }

    public ArrayList<Owner> getOwners() {
        return owners;
    }

    public ArrayList<Pet> getPets() {
        return pets;
    }
}
