package jdbc;

import entity.Owner;
import entity.Pet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class JDBCConnector {
    private Connection connection;
    //TODO: переделать с паттерном фабрика для записи значений для базы из .properties
    private final String url = "jdbc:postgresql://localhost:5432/learn_base";
    private final String username = "postgres";
    private final String password = "4852123";

    public JDBCConnector() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void writeOwner(Owner owner) throws SQLException {
        String queryForClients = "insert into clients(name) values(?)";
        String queryForPets = "insert into pets(client_id, name, type) VALUES (?,?,?)";
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(queryForClients,
                new String[]{"client_id"});
        statement.setString(1, owner.getName());
        statement.execute();
        ResultSet gk = statement.getGeneratedKeys();
        Integer clientId = null;
        if (gk.next()) {
            clientId = gk.getInt("client_id");
        }
        if (clientId != null) {
            statement = connection.prepareStatement(queryForPets);
            for (Pet pet : owner.getPets()) {
                statement.setInt(1, clientId);
                statement.setString(2, pet.getName());
                statement.setString(3, pet.getType().toString());
                statement.execute();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } else {
            System.out.println("Обработка ошибки нам не дали ключа!");
        }
        statement.close();
    }

    public void writePet(Pet pet) throws SQLException {
        if (pet == null) {
            //TODO: ошибка
            return;
        }
        String query = "insert into pets(client_id, name, type) values(?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        Integer id = pet.getOwner().getId();
        if (id == null) {
            id = readIdForClient(pet.getOwner());
        }
        statement.setInt(1, id);
        statement.setString(2, pet.getName());
        statement.setString(3, pet.getType().toString());
        connection.setAutoCommit(false);
        statement.execute();
        connection.commit();
        connection.setAutoCommit(true);
        statement.close();
    }

    public Owner readOwner(Pet pet) throws SQLException {
        Owner owner = new Owner();
        if (pet.getType() == null || pet.getName() == null) {
            //TODO:это костыль на всякий случай, должно проверять и использовать что есть
            System.out.println("Ошибка");
            return null;
        }
        String getOwnerQuery = "select * " +
                "from clients cl " +
                "where (" +
                "          select distinct client_id " +
                "          from pets " +
                "          where pets.name = ? " +
                "            and pets.type = ? " +
                "      ) = client_id ";
        PreparedStatement statement = connection.prepareStatement(getOwnerQuery);
        statement.setString(1, pet.getName());
        statement.setString(2, pet.getType().toString());
        ResultSet res = statement.executeQuery();
        if (res.next()) {
            owner.setName(res.getString("name"));
            owner.setId(res.getInt("client_id"));
        }
        owner.setPets(new ArrayList<>(readPets(owner)));
        statement.close();
        return owner;
    }

    public Set<Pet> readPets(Owner owner) throws SQLException {
        PreparedStatement statement;
        ResultSet res;
        Integer id = readIdForClient(owner);
        if (id == null) {
            System.out.println("Mistake");
            return null;
        }
        Set<Pet> set = new HashSet<>();
        String getPetsQuery = "select * " +
                "from pets " +
                "where client_id = ?";
        statement = connection.prepareStatement(getPetsQuery);
        statement.setInt(1, id);
        res = statement.executeQuery();
        Pet tempPet;
        while (res.next()) {
            tempPet = new Pet(res.getInt("pet_id"),
                    res.getString("name"),
                    res.getString("type"),
                    owner);
            set.add(tempPet);
        }
        statement.close();
        return set;
    }

    public Integer readIdForClient(Owner owner) throws SQLException {
        if (owner.getName() == null) {
            //TODO:это костыль на всякий случай, должно проверять и использовать что есть
            System.out.println("Ошибка");
            return null;
        }
        Integer id = null;
        String getOwnerId = "select distinct client_id " +
                "from clients " +
                "where name = ?";
        PreparedStatement statement = connection.prepareStatement(getOwnerId);
        statement.setString(1, owner.getName());
        ResultSet res = statement.executeQuery();
        if (res.next()) {
            id = res.getInt("client_id");
        }
        statement.close();
        if (id == null) {
            //TODO: обработка ошибки
        }
        return id;
    }

    public void deletePet(Pet pet) throws SQLException {
        if (pet.getType() == null || pet.getName() == null) {
            //TODO:это костыль на всякий случай, должно проверять и использовать что есть
            System.out.println("Ошибка");
            return;
        }
        String query = "delete  " +
                "from pets " +
                "where name = ? and type = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, pet.getName());
        statement.setString(2, pet.getType().toString());
        statement.execute();
        statement.close();
    }

    public void deleteOwner(Owner owner) throws SQLException {
        connection.setAutoCommit(false);
        for (Pet pet : owner.getPets()) {
            deletePet(pet);
        }
        String query = "delete " +
                "from clients " +
                "where name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, owner.getName());
        statement.execute();
        statement.close();
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void updatePetName(Pet pet, String name) throws SQLException {
        if (pet.getType() == null || pet.getName() == null) {
            //TODO:это костыль на всякий случай, должно проверять и использовать что есть
            System.out.println("Ошибка");
            return;
        }
        String query = "update pets " +
                "set name = ? " +
                "where name = ? and type = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setString(2, pet.getName());
        statement.setString(3, pet.getType().toString());
        statement.execute();
        statement.close();
    }

    public void updateOwnerName(Owner owner, String name) throws SQLException {
        if (owner.getName() == null) {
            //TODO:это костыль на всякий случай, должно проверять и использовать что есть
            System.out.println("Ошибка");
            return;
        }
        String query = "update clients " +
                "set name = ? " +
                "where name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        statement.setString(2, owner.getName());
        statement.execute();
        statement.close();
    }

    public void dropBase() throws SQLException, IOException, URISyntaxException {
        StringBuilder query = new StringBuilder();
        File file = new File(Objects.requireNonNull(getClass()
                .getClassLoader()
                .getResource("create_send"))
                .getFile());
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
            query.append(line);
        }
        Statement statement = connection.createStatement();
        statement.execute(query.toString());
        statement.close();
    }
}
