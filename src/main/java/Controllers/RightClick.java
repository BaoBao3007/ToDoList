package Controllers;

import Model.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;


public class RightClick {

    @FXML

    MenuItem deleteMenuItem = new MenuItem("Delete");
    MenuItem editMenuItem = new MenuItem("Edit");
    MenuItem addReminder = new MenuItem("Thêm lời nhắc");

    public ContextMenu ListContexMenu(ListView<Task> ds)
    {
        ContextMenu listContexMenu = new ContextMenu();
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Task item = (Task) ds.getSelectionModel().getSelectedItem();
//                deleteItem(item);
            }
        });
        editMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Task item = (Task) ds.getSelectionModel().getSelectedItem();
//                editMenuItem(item);
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

//    public void deleteItem(Task item) {
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Delete todo item");
//        alert.setHeaderText("Delete item: "+ item.getShortDescription());
//        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out.");
//        Optional<ButtonType> result = alert.showAndWait();
//
//        if(result.isPresent() && (result.get() == ButtonType.OK)){
//            TodoData.getInstance().deleteTask(item);
//        }
//    }
//    public void editMenuItem(Task item) {
//
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Delete todo item");
//        alert.setHeaderText("Delete item: "+ item.getShortDescription());
//        alert.setContentText("Are you sure? Press OK to confirm, or cancel to Back out.");
//        Optional<ButtonType> result = alert.showAndWait();
//
//        if(result.isPresent() && (result.get() == ButtonType.OK)){
//            TodoData.getInstance().deleteTask(item);
//        }
//    }
}
