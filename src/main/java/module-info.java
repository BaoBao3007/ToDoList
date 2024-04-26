module com.example.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    exports Gui;
    opens Gui to javafx.fxml;
    exports Dao;
    opens Dao to javafx.fxml;
}