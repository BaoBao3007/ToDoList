package Controllers;

import Model.Task;
import Dao.TaskDao;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.application.Platform;


public class RightClick {

    @FXML

    MenuItem deleteMenuItem = new MenuItem("Delete");
    MenuItem editMenuItem = new MenuItem("Edit");
    MenuItem addReminder = new MenuItem("Thêm lời nhắc");

    public ContextMenu ListContexMenu(ListView<Task> ds)
    {
        ContextMenu listContexMenu = new ContextMenu();
        deleteMenuItem.setOnAction(event -> {
            Task selectedTask = ds.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                showDeleteConfirmationDialog(selectedTask, ds); // Thay đổi ở đây
            }
        });
        editMenuItem.setOnAction(actionEvent -> {
            Task selectedTask = ds.getSelectionModel().getSelectedItem();
            if (selectedTask != null) {
                try {
                    openEditTaskForm(selectedTask);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addReminder.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Task item = (Task) ds.getSelectionModel().getSelectedItem();
//                addRemider(item);
            }
        });

        listContexMenu.getItems().addAll(deleteMenuItem,editMenuItem,addReminder);
        return listContexMenu;
    }
    private void deleteTask(Task task, ListView<Task> listView) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa task này?");
        alert.setContentText(task.getTask_name());

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    LocalDateTime deletionDate = LocalDateTime.now();
                    if (TaskDao.getInstance().deleteTaskAndSaveToDeleted(task, deletionDate)) {
                        showAlert("Thành công", "Đã xóa task và chuyển vào thùng rác.");

                        // Cập nhật ListView trên JavaFX Application Thread
                        Platform.runLater(() -> listView.getItems().remove(task));

                        // Gọi phương thức refreshListView (tự triển khai) để làm mới danh sách task
                        // Ví dụ:
                        // refreshListView();
                    } else {
                        showAlert("Lỗi", "Không thể xóa task.");
                    }
                } catch (SQLException e) {
                    showAlert("Lỗi SQL", "Lỗi: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }


private void openEditTaskForm(Task selectedTask) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gui/EditTask.fxml"));
    Parent root = loader.load();

    try {
        // Truyền task đã chọn vào EditTaskController
        EditTaskController editTaskController = loader.getController();
        editTaskController.setTask(selectedTask);
        editTaskController.initializeTaskData();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (SQLException e) {
        e.printStackTrace();
        showAlert("Lỗi", "Xảy ra lỗi khi lấy thông tin category.");
    }
}
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showDeleteConfirmationDialog(Task task, ListView<Task> listView) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa task này?");
        alert.setContentText(task.getTask_name());

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    LocalDateTime deletionDate = LocalDateTime.now();
                    if (TaskDao.getInstance().deleteTaskAndSaveToDeleted(task, deletionDate)) {
                        showAlert("Thành công", "Đã xóa task và chuyển vào thùng rác.");

                        // Cập nhật ListView trên JavaFX Application Thread
                        Platform.runLater(() -> listView.getItems().remove(task));

                        // Gọi phương thức refreshListView (tự triển khai) để làm mới danh sách task
                        // Ví dụ:
                        // refreshListView();
                    } else {
                        showAlert("Lỗi", "Không thể xóa task.");
                    }
                } catch (SQLException e) {
                    showAlert("Lỗi SQL", "Lỗi: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
}
