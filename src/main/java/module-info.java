module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    exports Gui;
    opens Gui to javafx.fxml;
    exports Dao;
    opens Dao to javafx.fxml;
}