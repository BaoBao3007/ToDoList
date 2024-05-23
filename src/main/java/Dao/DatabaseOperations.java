package Dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DatabaseOperations {
    private MySQLDataAccess dbConnection;

    public DatabaseOperations() {
        dbConnection = new MySQLDataAccess();
    }

    public ResultSet executeQuery(String query) throws SQLException {
        Connection connection = dbConnection.openConnection();
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public int executeUpdate(String query) throws SQLException {
        Connection connection = dbConnection.openConnection();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(query);
    }

    public void close() {
        try {
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
