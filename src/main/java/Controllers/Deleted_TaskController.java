package Controllers;

import Dao.Deleted_TaskDao;
import Model.Deleted_Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class Deleted_TaskController {
    @FXML
    private ListView<String> taskNameListView;

    @FXML
    private ListView<String> descriptionListView;

    @FXML
    private ListView<String> duedateListView;

    @FXML
    private ListView<String> importantListView;

    @FXML
    private ListView<String> usernameListView;

    @FXML
    private ListView<String> deletiondateListView;

    @FXML
    private void initialize() {
        Deleted_TaskDao deletedTaskDao = Deleted_TaskDao.getInstance();
        List<Deleted_Task> deletedTasks = deletedTaskDao.getAllDeletedTasks();

        for (Deleted_Task deletedTask : deletedTasks) {
            taskNameListView.getItems().add(deletedTask.getTaskName());
            descriptionListView.getItems().add(deletedTask.getDescription());
            duedateListView.getItems().add(deletedTask.getDueDate().toString());
            importantListView.getItems().add(String.valueOf(deletedTask.isImportant()));
            usernameListView.getItems().add(deletedTask.getUsername());
            deletiondateListView.getItems().add(deletedTask.getDeletionDate().toString());
        }
    }
}