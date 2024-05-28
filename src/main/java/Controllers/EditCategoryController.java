package Controllers;

import Model.Category;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditCategoryController {
    @FXML
    private TextField categoryNameTextField;

    private Category editedCategory;
    private Stage dialogStage;

    public void setEditedCategory(Category category) {
        editedCategory = category;
        categoryNameTextField.setText(category.getCategory_name());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void showEditCategoryDialog(Stage primaryStage, VBox editCategoryPane) {
        dialogStage = new Stage();
        dialogStage.setTitle("Chỉnh sửa danh mục");
        dialogStage.initOwner(primaryStage);

        // Thiết lập nút "Lưu" và nút "Hủy"
        Button saveButton = new Button("Lưu");
        saveButton.setOnAction(event -> saveChanges());
        Button cancelButton = new Button("Hủy");
        cancelButton.setOnAction(event -> cancelChanges());

        // Xử lý sự kiện khi nhấn nút "Lưu"
        dialogStage.setOnCloseRequest(event -> cancelChanges());

        VBox dialogPaneContent = new VBox(editCategoryPane, saveButton, cancelButton);
        dialogPaneContent.setSpacing(10.0);

        dialogStage.setScene(new Scene(dialogPaneContent));
        dialogStage.showAndWait();
    }

    @FXML
    private void saveChanges() {
        String categoryName = categoryNameTextField.getText().trim();
        if (editedCategory != null && !categoryName.isEmpty()) {
            editedCategory.setCategory_name(categoryName);
            // Đây là nơi lưu các thay đổi vào cơ sở dữ liệu. Bạn cần tích hợp với CategoryDao
            // CategoryDao categoryDao = new CategoryDao();
            // categoryDao.updateCategory(editedCategory);
            // ...
        }
        dialogStage.close();
    }

    @FXML
    private void cancelChanges() {
        dialogStage.close();
    }
}