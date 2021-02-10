package jdbc;

import entity.Owner;
import entity.Pet;
import entity.PetType;

import java.sql.*;

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

    public void writeOwner(Owner owner) {
        try {
            owner.generateId();
            Statement statement = connection.createStatement();
            statement.execute("insert into clients(client_id, name) values (\'" +
                    owner.getId() + "\', \'" +
                    owner.getName() + "\')");
            for (Pet pet : owner.getPets()) {
                pet.generateId();
                statement.execute("insert into pets(pet_id, client_id, name, type) VALUES (\'" +
                        pet.getId() + "\', \'" +
                        owner.getId() + "\', \'" +
                        pet.getName() + "\', \'" +
                        pet.getType() + "\')");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
