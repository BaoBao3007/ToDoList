package Controllers;

import Dao.CategoryDao;
import Model.Category;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CategoryController {
    public ListView<String> categoryIdListView;
    @FXML
    private Pane categoryPane;

    @FXML
    private ListView<String> categoryNameListView;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {
        // Lấy dữ liệu từ CategoryDao
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.getAllCategories(GlobalData.currentUsername);

        // Hiển thị tên danh mục lên ListView categoryName
        categoryNameListView.getItems().clear();
        for (Category category : categories) {
            categoryIdListView.getItems().add(String.valueOf(category.getCategory_id()));
            categoryNameListView.getItems().add(category.getCategory_name());
        }

        // Tạo menu context cho các item trong ListView
        ContextMenu contextMenu = new ContextMenu();
        MenuItem addMenuItem = new MenuItem("Add");
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(addMenuItem,editMenuItem, deleteMenuItem);

        // Khi chuột phải vào một item, hiển thị menu context
        categoryNameListView.setContextMenu(contextMenu);
        categoryIdListView.setContextMenu(contextMenu);
        categoryNameListView.setOnContextMenuRequested(event -> {
            int selectedIndex = categoryNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                contextMenu.show(categoryNameListView, event.getScreenX(), event.getScreenY());
            }
        });
        addMenuItem.setOnAction(event -> {
            int selectedIndex = categoryNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                editCategory(selectedIndex);
            }
        });
        // Xử lý sự kiện khi chọn Edit
        editMenuItem.setOnAction(event -> {
            int selectedIndex = categoryNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                editCategory(selectedIndex);
            }
        });

        // Xử lý sự kiện khi chọn Delete
        deleteMenuItem.setOnAction(event -> {
            int selectedIndex = categoryNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                deleteCategory(selectedIndex);
            }
        });
    }
    private void addCategory() {
        // Tạo một Dialog hoặc Pane mới để nhập thông tin danh mục
        try {
            // Nếu bạn đang sử dụng JavaFX Scene Builder, bảo đảm rằng bạn đã tạo file FXML cho AddCategory view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gui/Category.fxml"));
            Pane addCategoryPane = loader.load();
            CategoryController addCategoryController = loader.getController();

            // Setup và hiển thị dialog hoặc Stage
            Stage addCategoryStage = new Stage();
            addCategoryStage.setTitle("Thêm Danh Mục Mới");
            addCategoryStage.setScene(new Scene(addCategoryPane));
            addCategoryStage.initModality(Modality.WINDOW_MODAL); // Set parent window modal if needed
            addCategoryStage.initOwner(primaryStage);
            addCategoryStage.showAndWait();

            // Sau khi dialog đóng, tiến hành refresh lại danh sách danh mục
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    private void deleteCategory(int selectedIndex) {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.getAllCategories(GlobalData.currentUsername);
        if (selectedIndex < categories.size()) {
            Category selectedCategory = categories.get(selectedIndex);
            categoryDao.deleteCategory(selectedCategory.getCategory_id());
            initialize();
        }
    }

    private void editCategory(int selectedIndex) {
        CategoryDao categoryDao = new CategoryDao();
        List<Category> categories = categoryDao.getAllCategories(GlobalData.currentUsername);
        if (selectedIndex < categories.size()) {
            Category selectedCategory = categories.get(selectedIndex);
            // Cài đặt logic để chỉnh sửa danh mục,
            // có thể mở một Dialog hoặc Scene mới để hiển thị form chỉnh sửa.
            // Sau đó, gọi categoryDao.updateCategory(selectedCategory);
            // gọi initialize() để cập nhật dữ liệu.
        }
    }
}