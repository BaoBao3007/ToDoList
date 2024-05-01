package Controllers;

import Data.TodoData;
import Data.TodoItem;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class RightClick {

    @FXML

    MenuItem deleteMenuItem = new MenuItem("Delete");
    MenuItem editMenuItem = new MenuItem("Edit");
    public ContextMenu ListContexMenu(ListView<TodoItem> ds)
    {
        ContextMenu listContexMenu = new ContextMenu();
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = (TodoItem) ds.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                TodoItem item = (TodoItem) ds.getSelectionModel().getSelectedItem();
                editMenuItem(item);
            }
        });
        listContexMenu.getItems().addAll(deleteMenuItem,editMenuItem);
        return listContexMenu;
    }

    public void deleteItem(TodoItem item) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete todo item");
        alert.setHeaderText("Delete item: "+ item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }
    public void editMenuItem(TodoItem item) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete todo item");
        alert.setHeaderText("Delete item: "+ item.getShortDescription());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out.");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && (result.get() == ButtonType.OK)){
            TodoData.getInstance().deleteTodoItem(item);
        }
    }
}
