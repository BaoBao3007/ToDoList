package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDataAccess {
    static final String JDBC_URL = "jdbc:" + "mysql://gateway01.ap-southeast-1.prod.aws.tidbcloud.com:4000/TO_DO_LIST?sslMode=VERIFY_IDENTITY";
    static final String USER = "349U3FBRT6ZmZme.root";
    static final String PASSWORD = "oJEuK7kqGu6eEo9y";
    private Connection connection = null;


    public Connection openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

}