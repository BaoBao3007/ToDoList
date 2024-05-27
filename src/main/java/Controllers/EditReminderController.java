package Controllers;

import Model.Reminder;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditReminderController {
    @FXML
    private TextField reminderTextField;

    @FXML
    private DatePicker datePicker;

    private Reminder editedReminder;
    private Stage dialogStage;

    public void setEditedReminder(Reminder reminder) {
        editedReminder = reminder;
        reminderTextField.setText(reminder.getReminderMessage());
        datePicker.setValue(reminder.getReminderDate().toLocalDateTime().toLocalDate());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void showEditReminderDialog(Stage primaryStage, VBox editReminderPane) {
        dialogStage = new Stage();
        dialogStage.setTitle("Chỉnh sửa nhắc nhở");
        dialogStage.initOwner(primaryStage);

        // Thiết lập nút "Lưu" và nút "Hủy"
        Button saveButton = new Button("Lưu");
        saveButton.setOnAction(event -> saveChanges());
        Button cancelButton = new Button("Hủy");
        cancelButton.setOnAction(event -> cancelChanges());

        // Xử lý sự kiện khi nhấn nút "Lưu"
        dialogStage.setOnCloseRequest(event -> cancelChanges());

        VBox dialogPaneContent = new VBox(editReminderPane, saveButton, cancelButton);
        dialogPaneContent.setSpacing(10.0);

        dialogStage.setScene(new Scene(dialogPaneContent));
        dialogStage.showAndWait();
    }

    private void saveChanges() {
        String reminderMessage = reminderTextField.getText().trim();
        if (editedReminder != null) {
            editedReminder.setReminderMessage(reminderMessage);
            editedReminder.setReminderDate(java.sql.Timestamp.valueOf(datePicker.getValue().atStartOfDay()));
            // Lưu các thay đổi vào cơ sở dữ liệu hoặc thực hiện các thao tác chỉnh sửa
            // ...
        }
        dialogStage.close();
    }

    private void cancelChanges() {
        dialogStage.close();
    }
}