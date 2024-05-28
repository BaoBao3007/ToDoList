package Controllers;

import Dao.ReminderDao;
import Model.Reminder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReminderController {
    @FXML
    private Pane reminderPane;

    @FXML
    private ListView<String> taskNameListView;

    @FXML
    private ListView<String> messageListView;

    @FXML
    private ListView<String> dateListView;

    @FXML
    private Label deadlineLabel;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initialize() {
        // Lấy dữ liệu từ ReminderDao
        ReminderDao reminderDao = new ReminderDao();
        List<Reminder> reminders = reminderDao.getAllRemindersByusermane(GlobalData.currentUsername);

        // Hiển thị tên công việc lên ListView taskName và kích hoạt sự kiện khi chọn item
        taskNameListView.getItems().clear();
        for (Reminder reminder : reminders) {
            int taskId = reminder.getTaskId();
            String taskName = reminderDao.getTaskNameByTaskId(taskId);
            taskNameListView.getItems().add(taskName);
        }
        taskNameListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Khi chọn một item, cập nhật việc chọn cho các ListView khác
            int selectedIndex = taskNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                messageListView.getSelectionModel().select(selectedIndex);
                dateListView.getSelectionModel().select(selectedIndex);
            }
        });

        // Hiển thị nội dung và ngày nhắc nhở lên ListView message và date
        messageListView.getItems().clear();
        dateListView.getItems().clear();
        for (Reminder reminder : reminders) {
            messageListView.getItems().add(reminder.getReminderMessage());
            dateListView.getItems().add(reminder.getReminderDate().toString());
        }

        // Tạo menu context cho các item trong ListView
        ContextMenu contextMenu = new ContextMenu();
        MenuItem editMenuItem = new MenuItem("Edit");
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().addAll(editMenuItem, deleteMenuItem);

        // Khi chuột phải vào một item, hiển thị menu context
        taskNameListView.setContextMenu(contextMenu);
        taskNameListView.setOnContextMenuRequested(event -> {
            int selectedIndex = taskNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                contextMenu.show(taskNameListView, event.getScreenX(), event.getScreenY());
            }
        });

        // Xử lý sự kiện khi chọn Edit
        editMenuItem.setOnAction(event -> {
            int selectedIndex = taskNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                editReminder(selectedIndex);
            }
        });

        // Xử lý sự kiện khi chọn Delete
        deleteMenuItem.setOnAction(event -> {
            int selectedIndex = taskNameListView.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                deleteReminder(selectedIndex);
            }
        });
    }

    private void deleteReminder(int selectedIndex) {
        ReminderDao reminderDao = new ReminderDao();
        List<Reminder> reminders = reminderDao.getAllReminders();
        if (selectedIndex < reminders.size()) {
            Reminder selectedReminder = reminders.get(selectedIndex);
            reminderDao.deleteReminder(selectedReminder);
            initialize();
        }
    }

    private void editReminder(int selectedIndex) {
        ReminderDao reminderDao = new ReminderDao();
        List<Reminder> reminders = reminderDao.getAllReminders();
        if (selectedIndex < reminders.size()) {
            Reminder selectedReminder = reminders.get(selectedIndex);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Gui/editReminder.fxml"));
            try {
                VBox editReminderPane = loader.load();
                EditReminderController editReminderController = loader.getController();
                editReminderController.setEditedReminder(selectedReminder);
                editReminderController.showEditReminderDialog(primaryStage, editReminderPane);
                initialize(); // Cập nhật lại dữ liệu sau khi chỉnh sửa
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}