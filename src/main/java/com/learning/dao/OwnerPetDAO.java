package com.learning.dao;

import com.learning.entity.Owner;
import com.learning.entity.Pet;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class OwnerPetDAO {
    private static Connection connection;
    //private static Logger LOGGER;

    static {
//        try (FileInputStream ins = new FileInputStream("log.config")) {
//            LogManager.getLogManager().readConfiguration(ins);
//            LOGGER = Logger.getLogger(OwnerPetDAO.class.getName());
//        } catch (Exception ignore) {
//        }

        String url = null;
        String username = null;
        String password = null;

        //LOGGER.log(Level.INFO, "чтение properties для бд");
        try (InputStream in = OwnerPetDAO.class
                .getClassLoader().getResourceAsStream("persistence.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            //LOGGER.log(Level.WARNING, "ошибка в чтение properties", e);
            e.printStackTrace();
        }

        //LOGGER.log(Level.INFO, "подключение к бд создание connection");
        try {
            Class.forName("org.postgresql.Driver");
            assert url != null;
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException throwables) {
            //LOGGER.log(Level.WARNING, "ошибка в получении connection", throwables);
            throwables.printStackTrace();
        }
        //LOGGER.log(Level.INFO, "static выполнен успешно. connection получен");
    }

    public void writeOwner(Owner owner) throws SQLException {
        //LOGGER.log(Level.INFO, "Вызов метода writeOwner");
        String queryForClients = "insert into clients(name) values(?)";
        String queryForPets = "insert into pets(client_id, name, type) VALUES (?,?,?)";
        connection.setAutoCommit(false);
        Integer clientId = null;
        //LOGGER.log(Level.INFO, "получение PreparedStatement");
        try (PreparedStatement statement = connection.prepareStatement(queryForClients,
                new String[]{"client_id"})) {
            //LOGGER.log(Level.INFO, "PreparedStatement получен");
            statement.setString(1, owner.getName());
            //LOGGER.log(Level.INFO, "Выполнение запроса на внесение записей в бд");
            statement.execute();
            ResultSet gk = statement.getGeneratedKeys();
            if (gk.next()) {
                clientId = gk.getInt("client_id");
            }
        }
        assert clientId != null;
        //LOGGER.log(Level.INFO, "Начало записи питомцев клиента");
        try (PreparedStatement statement = connection.prepareStatement(queryForPets)) {
            for (Pet pet : owner.getPets()) {
                statement.setInt(1, clientId);
                statement.setString(2, pet.getName());
                statement.setString(3, pet.getType().toString());
                statement.execute();
            }
            connection.commit();
            connection.setAutoCommit(true);
        }

        //LOGGER.log(Level.INFO, "writeOwner завершён успешно");
    }

    public void writePet(Pet pet) throws SQLException {
        assert pet != null;
        String query = "insert into pets(client_id, name, type) values(?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
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
        }
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
        try (PreparedStatement statement = connection.prepareStatement(getOwnerQuery)) {
            statement.setString(1, pet.getName());
            statement.setString(2, pet.getType().toString());
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                owner.setName(res.getString("name"));
                owner.setId(res.getInt("client_id"));
            }
            owner.setPets(new ArrayList<>(readPets(owner)));
        }
        return owner;
    }

    public Set<Pet> readPets(Owner owner) throws SQLException {
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
        try (PreparedStatement statement = connection.prepareStatement(getPetsQuery)) {
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
        }
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
        try (PreparedStatement statement = connection.prepareStatement(getOwnerId)) {
            statement.setString(1, owner.getName());
            ResultSet res = statement.executeQuery();
            if (res.next()) {
                id = res.getInt("client_id");
            }
        }
        assert id != null;
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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pet.getName());
            statement.setString(2, pet.getType().toString());
            statement.execute();
        }
    }

    public void deleteOwner(Owner owner) throws SQLException {
        connection.setAutoCommit(false);
        for (Pet pet : owner.getPets()) {
            deletePet(pet);
        }
        String query = "delete " +
                "from clients " +
                "where name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, owner.getName());
            statement.execute();
        }
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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, pet.getName());
            statement.setString(3, pet.getType().toString());
            statement.execute();
        }
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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, owner.getName());
            statement.execute();
        }
    }

    public void drop() throws SQLException, IOException {
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
        try (Statement statement = connection.createStatement()) {
            statement.execute(query.toString());
        }
    }

    public Set<Owner> readOwners() {
        String query = "select * from clients";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet res = statement.executeQuery();
            Set<Owner> set = new HashSet<>();
            while (res.next()){
                set.add(new Owner(res.getInt("client_id"), res.getString("name")));
            }
            return set;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
