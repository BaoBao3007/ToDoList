package Controllers;

import Dao.Deleted_TaskDao;
import Model.Deleted_Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Deleted_TaskController {
    @FXML
    private ListView<String> taskNameListView;

    @FXML
    private ListView<String> descriptionListView;

    @FXML
    private ListView<String> duedateListView;


    @FXML
    private ListView<String> deletiondateListView;

    @FXML
    private void initialize() {
        Deleted_TaskDao deletedTaskDao = Deleted_TaskDao.getInstance();
        List<Deleted_Task> deletedTasks = deletedTaskDao.getAllDeletedTasks(GlobalData.currentUsername);

        for (Deleted_Task deletedTask : deletedTasks) {
            taskNameListView.getItems().add(deletedTask.getTaskName());
            descriptionListView.getItems().add(deletedTask.getDescription());
            duedateListView.getItems().add(convertToLocalDate(deletedTask.getDueDate()).toString());
            deletiondateListView.getItems().add(convertToLocalDate(deletedTask.getDeletionDate()).toString());
        }
    }
    public static LocalDate convertToLocalDate(Timestamp timestamp) {
        long milliseconds = timestamp.getTime();
        Instant instant = Instant.ofEpochMilli(milliseconds);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();

        return localDate;
    }

}