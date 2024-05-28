package Dao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
public class DatabaseOperations {
    private MySQLDataAccess dbConnection;

    public DatabaseOperations() throws SQLException {
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

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return MySQLDataAccess.openConnection().prepareStatement(query);
    }

    public void close() {
        try {
            dbConnection.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
