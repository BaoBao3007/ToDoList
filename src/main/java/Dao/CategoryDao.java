package Dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
public class CategoryDao {
    private MySQLDataAccess dbConnection;
    public CategoryDao() {
        dbConnection = new MySQLDataAccess();
    }
    private static CategoryDao instance = new CategoryDao();
    public static CategoryDao getInstance() {
        return instance;
    }
    public String getCategoryName(int categoryId) {
        String categoryName = null;
        String sql = "SELECT category_name FROM category WHERE category_id = ?";
        try (Connection connection = dbConnection.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                categoryName = resultSet.getString("category_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return categoryName;
    }
}
