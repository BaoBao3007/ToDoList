package Dao;

import Model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private MySQLDataAccess dbConnection;

    public CategoryDao() {
        dbConnection = new MySQLDataAccess();
    }

    private static CategoryDao instance = new CategoryDao();

    public static CategoryDao getInstance() {
        if (instance == null) {
            instance = new CategoryDao();
        }
        return instance;
    }

    public List<Category> getAllCategories(String Username) {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM Category where username ='"+Username+"'";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("category_name"),
                        resultSet.getString("username")
                );
                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // You should handle the error properly
        }

        return categories;
    }

    public void addCategory(Category category) {
        String query = "INSERT INTO Category (category_name, username) VALUES (?, ?)";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, category.getCategory_name());
            preparedStatement.setString(2, category.getUsername());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // You should handle the error properly
        }
    }

    public void updateCategory(Category category) {
        String query = "UPDATE Category SET category_name = ?, username = ? WHERE category_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, category.getCategory_name());
            preparedStatement.setString(2, category.getUsername());
            preparedStatement.setInt(3, category.getCategory_id());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // You should handle the error properly
        }
    }

    public void deleteCategory(int categoryId) {
        String query = "DELETE FROM Category WHERE category_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, categoryId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            // You should handle the error properly
        }
    }

    public Category getCategoryById(int categoryId) {
        String query = "SELECT * FROM Category WHERE category_id = ?";
        Category category = null;

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                category = new Category(
                        resultSet.getInt("category_id"),
                        resultSet.getString("category_name"),
                        resultSet.getString("username")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // You should handle the error properly
        }

        return category;
    }
    public List<Category> getAllCategoriesByUsername(String username) {
        List<Category> categories = new ArrayList<>();
        // Giả sử cấu trúc cơ sở dữ liệu có một cột là user_id hoặc tên đăng nhập liên kết với Category.
        // Nếu không, cần thực hiện JOIN với bảng user hoặc một bảng phù hợp khác để lấy dữ liệu category.
        // Điều chỉnh câu truy vấn SQL nếu cơ sở dữ liệu của bạn có cấu trúc khác.
        String sql = "SELECT * FROM Category WHERE username = ?";
        try (Connection connection = dbConnection.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                String categoryName = resultSet.getString("category_name");

                // Tạo đối tượng Category từ dữ liệu ResultSet và thêm vào danh sách
                Category category = new Category(categoryId, categoryName);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Nên xử lý lỗi tốt hơn hoặc re-throw ngoại lệ.
        }
        return categories;
    }

    public String getCategoryName(int categoryId) {
        String categoryName = null;
        String query = "SELECT category_name FROM Category WHERE category_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Chúng tôi kiểm tra xem kết quả có dữ liệu hay không và thì gán giá trị
            if (resultSet.next()) {
                categoryName = resultSet.getString("category_name");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Nên xử lý lỗi tốt hơn ở đây, có thể thông qua việc log hoặc re-throw
        }

        return categoryName;
    }
}