package Controllers;

import Dao.MySQLDataAccess;
import Dao.TaskDao;
import Dao.CategoryDao;
import Model.Task;
import Model.Category;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ResourceBundle;


import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;


import javafx.util.StringConverter;

public class AddNewTaskController implements Initializable {

    private MySQLDataAccess dbConnection;
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private ImageView Exit;

    @FXML
    private BorderPane window;

    @FXML
    private TextField Description;

    @FXML
    private TextArea DetailsArea;

    @FXML
    private DatePicker Deadline;

    @FXML
    private ComboBox Categories;

    private TaskDao taskDao = TaskDao.getInstance();
    private CategoryDao categoryDao = CategoryDao.getInstance();



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Exit.setOnMouseClicked(e -> {
            Stage stage = (Stage) window.getScene().getWindow();
            stage.close();

        });
        loadCategories();
    }
    private void loadCategories() {
        try {
            List<String> categoryNames = categoryDao.getAllCategories(GlobalData.currentUsername); // Lấy danh sách categoryNames từ CategoryDao
            List<Category> categories = new ArrayList<>();

            // Chuyển đổi danh sách categoryNames thành danh sách categories
            for (String categoryName : categoryNames) {
                int categoryId = getCategoryIdByName(categoryName);
                categories.add(new Category(categoryId, categoryName)); // Tạo đối tượng Category và thêm vào danh sách
            }

            // Đặt StringConverter cho ComboBox để hiển thị tên category
            categoryComboBox.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category != null ? category.getCategory_name() : "";
                }

                @Override
                public Category fromString(String string) {
                    // Không cần triển khai phương thức này
                    return null;
                }
            });

            categoryComboBox.getItems().addAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi tải danh sách categories.");
        }
    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) window.getScene().getWindow();
        stage.close();
    }

    @FXML
    void ok(ActionEvent event) {
        try {
            String taskName = Description.getText();
            String details = DetailsArea.getText();
            LocalDate deadline = Deadline.getValue();
            Category selectedCategory = categoryComboBox.getValue();

            if (taskName.isEmpty() || details.isEmpty() || deadline == null) {
                showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.");
                return;
            }
            int categoryId;
            if(selectedCategory==null)
            {
                categoryId = 0;
            }
            else
            categoryId = selectedCategory.getCategory_id();

            // Tạo đối tượng Task
            Task newTask = new Task(0, taskName, details, deadline, categoryId, "Processing", false, GlobalData.currentUsername, LocalDate.now());
            taskDao.addTask(newTask);
            TaskController taskController = TaskController.getInstance();
            if (taskController != null) {
                taskController.refreshTaskList();
            }


            // Đóng cửa sổ sau khi thêm thành công
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert("Lỗi SQL", "Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void cleartext(){
        Description.clear();
        DetailsArea.clear();
        Deadline.getEditor().clear();
        Categories.getItems().clear();


    }
    public void Error(){
        if(Deadline.getValue()==null || Description.getText().isEmpty() || DetailsArea.getText().isEmpty()){ }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public int getCategoryIdByName(String categoryName) throws SQLException {
        String query = "SELECT category_id FROM Category WHERE category_name = ?";
        try (Connection connection = dbConnection.openConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, categoryName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("category_id");
                }
            }
        }
        return -1;
    }

}
