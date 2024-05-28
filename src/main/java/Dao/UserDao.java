package Dao;

import Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class UserDao {
    private static UserDao instance;

    private MySQLDataAccess dbConnection;

    public UserDao() {
        dbConnection = new MySQLDataAccess();
    }
    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }
    public boolean changePassword(User user, String oldPasswordFromDatabase) throws SQLException {
        String sql = "UPDATE User SET password = ? WHERE username = ? AND password = ?";

        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, oldPasswordFromDatabase);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    public String getPasswordByUsername(String username) throws SQLException {
        String sql = "SELECT password FROM User WHERE username = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        }
        return null;
    }
}
