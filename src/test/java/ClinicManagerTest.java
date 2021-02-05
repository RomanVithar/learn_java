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
        cm.addPetToClient("x4","y4.2",PetType.CAT);

        List<String> names = cm.getOwners().stream().map(Owner::getName).collect(Collectors.toList());
        Assert.assertEquals("[x1, x2, x3, x4]", names.toString());
        List<String> namesPet = cm.getPets().stream().map(Pet::getName).collect(Collectors.toList());
        Assert.assertEquals("[y1, y2, y3, y4, y4.2]", namesPet.toString());
    }

    public void testGetNameClient() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.addPetToClient("x4","y4.2",PetType.CAT);
        Assert.assertNull(cm.getNameClient("x5"));
        Assert.assertEquals("x2", cm.getNameClient("y2"));
        Assert.assertEquals("x4", cm.getNameClient("y4.2"));
    }

    public void testGetPetName() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.addPetToClient("x4","y4.2",PetType.CAT);
        Assert.assertNull(cm.getListPetName("x5"));
        Assert.assertEquals("[y3]", cm.getListPetName("x3").toString());
        Assert.assertEquals("[y4, y4.2]", cm.getListPetName("x4").toString());
    }

    public void testChangeClientName() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.addPetToClient("x4","y4.2",PetType.CAT);
        cm.changeClientName("x3", "changed");
        Assert.assertEquals("changed", cm.getNameClient("y3"));
    }

    public void testChangePetName() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.addPetToClient("x4","y4.2",PetType.CAT);
        cm.changePetName("y4.2", "changed2");
        cm.changePetName("y4", "changed");
        Assert.assertEquals("[changed, changed2]", cm.getListPetName("x4").toString());
    }

    public void testDeleteClient() {
        ClinicManager cm = new ClinicManager();
        cm.addClient("x1","y1", PetType.CAT);
        cm.addClient("x2","y2", PetType.DOG);
        cm.addClient("x3","y3", PetType.RAT);
        cm.addClient("x4","y4", PetType.CAT);
        cm.addPetToClient("x4","y4.2",PetType.CAT);
        cm.deleteClient("x3");
        List<String> names = cm.getOwners().stream().map(Owner::getName).collect(Collectors.toList());
        Assert.assertEquals("[x1, x2, x4]", names.toString());
        List<String> namesPet = cm.getPets().stream().map(Pet::getName).collect(Collectors.toList());
        Assert.assertEquals("[y1, y2, y4, y4.2]", namesPet.toString());
        cm.deleteClient("x4");
        List<String> names2 = cm.getOwners().stream().map(Owner::getName).collect(Collectors.toList());
        Assert.assertEquals("[x1, x2]", names2.toString());
        List<String> namesPet2 = cm.getPets().stream().map(Pet::getName).collect(Collectors.toList());
        Assert.assertEquals("[y1, y2]", namesPet2.toString());
    }
}