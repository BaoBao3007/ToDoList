package Controllers;

import Dao.CategoryDao;
import Dao.TaskDao;
import Model.Category;
import Model.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;
import javafx.util.StringConverter;


public class EditTaskController {
    @FXML
    private ComboBox<Category> categoryComboBox;
    @FXML
    private TextField Description;
    @FXML
    private TextArea DetailsArea;
    @FXML
    private DatePicker Deadline;
    @FXML
    private ComboBox<Category> Categories;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    private Task task;
    private TaskDao taskDao = TaskDao.getInstance();
    private CategoryDao categoryDao = CategoryDao.getInstance();

    public void setTask(Task task) {
        this.task = task;
    }
    @FXML
    public void initializeTaskData() throws SQLException {
        try {
            if (task != null) {
                Description.setText(task.getTask_name());
                DetailsArea.setText(task.getDescription());
                Deadline.setValue(task.getDue_date());
                // Chọn category trong ComboBox dựa trên category_id của task
                Category selectedCategory = categoryDao.getCategoryById(task.getCategory_id());
                categoryComboBox.getSelectionModel().select(selectedCategory);
            }

            loadCategories(); // Load categories vào ComboBox
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi lấy thông tin category.");
        }
    }
    @FXML
    private void ok(ActionEvent event) {
        try {
            // Update Task
            task.setTask_name(Description.getText());
            task.setDescription(DetailsArea.getText());
            task.setDue_date(Deadline.getValue());
            task.setCategory_id(categoryComboBox.getValue().getCategory_id()); // Lấy categoryId từ ComboBox
            // Cập nhật các trường khác nếu cần

            if (taskDao.updateTask(task)) {
                showAlert("Success", "Task Updated Successfully");
                TaskController taskController = TaskController.getInstance(); // Lấy instance của TaskController
                if (taskController != null) {
                    taskController.refreshTaskList(); // Gọi phương thức refreshTaskList
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi cập nhật task.");
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void loadCategories() {
        try {
            List<String> categoryNames = categoryDao.getAllCategories(); // Sử dụng phương thức getAllCategories()
            List<Category> categories = new ArrayList<>();

            // Chuyển đổi danh sách categoryNames thành danh sách categories
            for (String categoryName : categoryNames) {
                int categoryId = taskDao.getCategoryIdByName(categoryName); // Sửa lại ở đây
                categories.add(new Category(categoryId, categoryName));
            }


            // Thiết lập StringConverter cho ComboBox
            categoryComboBox.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category != null ? category.getCategory_name() : "";
                }

                @Override
                public Category fromString(String string) {
                    return null; // Không cần triển khai phương thức này
                }
            });

            categoryComboBox.getItems().addAll(categories);
            // Chọn category trong ComboBox dựa trên task.category_id
            // Ví dụ: categoryComboBox.getSelectionModel().select(categoryIndex);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Xảy ra lỗi khi tải danh sách categories.");
        }
    }


}
