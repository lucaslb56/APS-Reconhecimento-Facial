package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
    private static String URL = "jdbc:sqlite:res/db/aps.db";

    // Cria conexão com banco
    public static Connection getConnection(){
       try { return DriverManager.getConnection(URL); }
       catch (SQLException e) {
           throw new RuntimeException(e);
       }
    }

    // Fecha conexão com banco
    public static void closeConnection(Connection conn){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Inicia tabelas da aplicação
    public static void startDB () {
        Connection conn = getConnection();
        String sql = "CREATE TABLE IF NOT EXISTS usuarios ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + " nome TEXT NOT NULL, "
                + " email TEXT NOT NULL, "
                + " nivelPermissao INTEGER NOT NULL, "
                + " bioFacial TEXT "
                + ");";
        try {
            Statement stmt = conn.createStatement();
                stmt.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        closeConnection(conn);
    }

}
