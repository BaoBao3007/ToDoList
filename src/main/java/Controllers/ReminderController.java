package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import Model.Reminder;
import Model.Task;
import  Dao.ReminderDao;
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

    public void initialize() {
        // Lấy dữ liệu từ ReminderDao
        ReminderDao reminderDao = new ReminderDao();
        List<Reminder> reminders = reminderDao.getAllReminders();

        // Hiển thị tên công việc lên ListView taskName
        for (Reminder reminder : reminders) {
            int taskId = reminder.getTaskId();
            String taskName = reminderDao.getTaskNameByTaskId(taskId);
            taskNameListView.getItems().add(taskName);
        }

        // Hiển thị nội dung và ngày nhắc nhở lên ListView message và date
        for (Reminder reminder : reminders) {
            messageListView.getItems().add(reminder.getReminderMessage());
            dateListView.getItems().add(reminder.getReminderDate().toString());
        }
    }
}