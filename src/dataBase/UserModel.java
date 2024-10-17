package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserModel {
    Connection connection;
    public UserModel(){

    }

    public void createUser() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:res/db/aps.db");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);

        if(connection != null) connection.close();

    }
}
