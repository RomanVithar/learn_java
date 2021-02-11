package jdbc;

import entity.Owner;
import entity.Pet;
import entity.PetType;
import junit.framework.TestCase;
import org.junit.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class JDBCConnectorTest extends TestCase {
    private Owner owner1;
    private Owner owner2;// названия клиентов owner<номер клиента>
    private Pet pet1x1; //названия питомцев pet<номер клиента>x<номер питомца>
    private Pet pet1x2;
    private Pet pet1x3;
    private Pet pet2x1;
    private Pet pet2x2;
    private JDBCConnector base;


    public JDBCConnectorTest() throws ClassNotFoundException, SQLException, IOException, URISyntaxException {
        base = new JDBCConnector();
    }

    private void prepareToTest() throws SQLException, IOException, URISyntaxException {
        base.dropBase();
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

    public void testWriteOwner() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Owner lOwner1 = base.readOwner(pet1x1);
        Assert.assertEquals("owner1", lOwner1.getName());
        Owner lOwner2 = base.readOwner(pet2x2);
        Assert.assertEquals("owner2", lOwner2.getName());
        Assert.assertEquals("[Pet{name='pet1x2', type=DOG, id='2'}," +
                        " Pet{name='pet1x3', type=CAT, id='3'}," +
                        " Pet{name='pet1x1', type=CAT, id='1'}]",
                lOwner1.getPets().toString());
        Assert.assertEquals("[Pet{name='pet2x2'," +
                        " type=DOG, id='5'}," +
                        " Pet{name='pet2x1', " +
                        "type=HAMSTER, id='4'}]",
                lOwner2.getPets().toString());
    }

    public void testGetIdForClient() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Assert.assertEquals(new Integer(1), base.readIdForClient(owner1));
        Assert.assertEquals(new Integer(2), base.readIdForClient(owner2));
    }

    public void testDeletePet() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.deletePet(pet1x3);
        Owner lOwner1 = base.readOwner(pet1x2);
        Assert.assertEquals("[Pet{name='pet1x2', type=DOG, id='2'}," +
                        " Pet{name='pet1x1', type=CAT, id='1'}]",
                lOwner1.getPets().toString());
    }


    public void testWritePet() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Pet pet = new Pet("pet", PetType.DOG,owner2);
        base.writePet(pet);
        Owner owner = base.readOwner(pet2x2);
        Pet petTest = owner.getPets().stream().filter(e->e.getName().equals("pet")).findAny().orElse(null);
        assert petTest != null;
        assertEquals("pet",petTest.getName());
    }

    public void testReadOwner() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Owner owner = base.readOwner(pet2x2);
        Assert.assertEquals("[Pet{name='pet2x2'," +
                " type=DOG, id='5'}," +
                " Pet{name='pet2x1', " +
                "type=HAMSTER, id='4'}]", owner.getPets().toString());
        Assert.assertEquals("owner2",owner.getName());
    }

    public void testReadPets() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        Owner owner = base.readOwner(pet2x2);
        Assert.assertEquals("[Pet{name='pet2x2'," +
                " type=DOG, id='5'}," +
                " Pet{name='pet2x1', " +
                "type=HAMSTER, id='4'}]", owner.getPets().toString());
    }


    public void testDeleteOwner() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.deleteOwner(owner2);
    }

    public void testUpdatePetName() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.updatePetName(pet1x2, "updated");
        Pet pet = base.readPets(owner1).stream().filter(e->e.getName().equals("updated")).findAny().orElse(null);
        assert pet != null;
        Assert.assertEquals("updated", pet.getName());
    }

    public void testUpdateOwnerName() throws SQLException, IOException, URISyntaxException {
        prepareToTest();
        base.updateOwnerName(owner1, "updated");
        Owner owner = base.readOwner(pet1x2);
        Assert.assertEquals("updated",owner.getName());
    }
}