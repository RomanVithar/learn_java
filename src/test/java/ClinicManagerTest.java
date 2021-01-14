import entity.Owner;
import entity.Pet;
import entity.PetType;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class ClinicManagerTest extends TestCase {
    public void testAddClient() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);

        List<String> names = cm.getOwners().stream().map(Owner::getName).collect(Collectors.toList());
        Assert.assertEquals("[x1, x2, x3, x4]", names.toString());
        List<String> namesPet = cm.getPets().stream().map(Pet::getName).collect(Collectors.toList());
        Assert.assertEquals("[y1, y2, y3, y4]", namesPet.toString());
    }

    public void testGetNameClient() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        Assert.assertNull(cm.getNameClient("x5"));
        Assert.assertEquals("x2", cm.getNameClient("y2"));
    }

    public void testGetPetName() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        Assert.assertNull(cm.getPetName("x5"));
        Assert.assertEquals("y3", cm.getPetName("x3"));
    }

    public void testChangeClientName() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.changeClientName("x3", "changed");
        Assert.assertEquals("changed", cm.getNameClient("y3"));
    }

    public void testChangePetName() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.changePetName("y4", "changed");
        Assert.assertEquals("changed", cm.getPetName("x4"));
    }

    public void testDeleteClient() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.deleteClient("x3");
        List<String> names = cm.getOwners().stream().map(Owner::getName).collect(Collectors.toList());
        Assert.assertEquals("[x1, x2, x4]", names.toString());
        List<String> namesPet = cm.getPets().stream().map(Pet::getName).collect(Collectors.toList());
        Assert.assertEquals("[y1, y2, y4]", namesPet.toString());
    }
}