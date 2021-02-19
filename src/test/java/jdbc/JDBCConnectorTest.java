package jdbc;

import com.learning.dao.OwnerPetDAO;
import com.learning.entity.Owner;
import com.learning.entity.Pet;
import com.learning.entity.PetType;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCConnectorTest extends TestCase {
    private Owner owner1;
    private Owner owner2;// названия клиентов owner<номер клиента>
    private Pet pet1x1; //названия питомцев pet<номер клиента>x<номер питомца>
    private Pet pet1x2;
    private Pet pet1x3;
    private Pet pet2x1;
    private Pet pet2x2;
    private OwnerPetDAO base;


    public JDBCConnectorTest() throws ClassNotFoundException, SQLException, IOException, URISyntaxException {
        base = new OwnerPetDAO();
    }

    private void prepareToTest() throws SQLException, IOException, URISyntaxException {
        base.drop();
        owner1 = new Owner("owner1");
        owner2 = new Owner("owner2");
        pet1x1 = new Pet("pet1x1", PetType.CAT, owner1);
        pet1x2 = new Pet("pet1x2", PetType.DOG, owner1);
        pet1x3 = new Pet("pet1x3", PetType.CAT, owner1);
        pet2x1 = new Pet("pet2x1", PetType.HAMSTER, owner2);
        pet2x2 = new Pet("pet2x2", PetType.DOG, owner2);
        owner1.addPet(pet1x1);
        owner1.addPet(pet1x2);
        owner1.addPet(pet1x3);
        owner2.addPet(pet2x1);
        owner2.addPet(pet2x2);
        base.writeOwner(owner1);
        base.writeOwner(owner2);
    }

    public boolean equalsOwners(Owner o1, Owner o2) {
        if (!o1.getName().equals(o2.getName())) {
            return false;
        }
        return equalsListPets(o1.getPets(), o2.getPets());
    }

    public boolean equalsListPets(List<Pet> p1, List<Pet> p2) {
        int count = 0;
        for (Pet pet : p1) {
            for (Pet petEquals : p2) {
                if (pet.getName().equals(petEquals.getName())
                        && pet.getType() == petEquals.
                        getType()) {
                    count++;
                }
            }
        }
        return p1.size() == count;
    }

    public void testWriteOwner() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Owner lOwner1 = base.readOwner(pet1x1);
        Owner lOwner2 = base.readOwner(pet2x2);
        Assert.assertTrue(equalsOwners(owner1, lOwner1));
        Assert.assertTrue(equalsOwners(owner2, lOwner2));
    }

    public void testGetIdForClient() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Assert.assertEquals(Integer.valueOf(1), base.readIdForClient(owner1));
        Assert.assertEquals(Integer.valueOf(2), base.readIdForClient(owner2));
    }

    public void testDeletePet() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.deletePet(pet1x3);
        owner1.getPets().remove(pet1x3);
        Owner lOwner1 = base.readOwner(pet1x2);
        Assert.assertTrue(equalsOwners(owner1, lOwner1));
    }


    public void testWritePet() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Pet pet = new Pet("pet", PetType.DOG, owner2);
        base.writePet(pet);
        Owner owner = base.readOwner(pet2x2);
        Pet petTest = owner.getPets().stream().filter(e -> e.getName().equals("pet")).findAny().orElse(null);
        assert petTest != null;
        assertEquals("pet", petTest.getName());
    }

    public void testReadOwner() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Owner owner = base.readOwner(pet2x2);
        Assert.assertTrue(equalsOwners(owner, owner2));
    }

    public void testReadPets() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        List<Pet> pets = new ArrayList<>(base.readPets(owner1));
        Assert.assertTrue(equalsListPets(pets, owner1.getPets()));
    }


    public void testDeleteOwner() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.deleteOwner(owner2);
    }

    public void testUpdatePetName() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.updatePetName(pet1x2, "updated");
        Pet pet = base.readPets(owner1).stream().filter(e -> e.getName().equals("updated")).findAny().orElse(null);
        assert pet != null;
        Assert.assertEquals("updated", pet.getName());
    }

    public void testUpdateOwnerName() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.updateOwnerName(owner1, "updated");
        Owner owner = base.readOwner(pet1x2);
        Assert.assertEquals("updated", owner.getName());
        base.drop();
    }
}