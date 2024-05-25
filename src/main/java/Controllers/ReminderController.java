package Controllers;

import Dao.ReminderDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class ReminderController {

    private ReminderDao reminderDao;
    public ReminderController() {
        reminderDao = new ReminderDao();
    }

    @FXML
    private void initialize() {
    }
}
