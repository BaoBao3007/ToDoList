package Dao;
import Model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public List<String> getAllCategories(String username) {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT category_name FROM Category where username ='"+username+"'";
        try (Connection connection = dbConnection.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String categoryName = resultSet.getString("category_name");
                categories.add(categoryName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public Category getCategoryById(int categoryId) throws SQLException {
        String query = "SELECT * FROM Category WHERE category_id = ?";
        try (Connection connection = dbConnection.openConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String categoryName = resultSet.getString("category_name");
                return new Category(categoryId, categoryName);

            }
        }
        return null;
    }
}
