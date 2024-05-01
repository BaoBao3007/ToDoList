package Controllers;

import Data.TodoData;
import Data.TodoItem;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class Today implements Initializable {

    private List<TodoItem> todoItems;
    private Predicate<TodoItem> wantAllItems;
    private FilteredList<TodoItem> filteredList;
    private Predicate<TodoItem> wantTodaysItems;
    @FXML
    private ListView<TodoItem> todoSDesView;

    @FXML
    private ListView<TodoItem> todoactivityView;

    @FXML
    private ContextMenu listContexMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        RightClick rc = new RightClick();
        listContexMenu = rc.ListContexMenu(todoSDesView);

        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return true;
            }
        };

        wantTodaysItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return (todoItem.getCategory().equals("In Progress"));
            }
        };

        filteredList = new FilteredList<TodoItem>(TodoData.getInstance().getTodoItems(), wantAllItems);

        SortedList<TodoItem> sortedList = new SortedList<TodoItem>(filteredList,
                new Comparator<TodoItem>() {
                    @Override
                    public int compare(TodoItem o1, TodoItem o2) {
                        return o1.getCategory().compareTo(o2.getCategory());
                    }
                });


        todoSDesView.setItems(sortedList);
        todoSDesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        todoactivityView.setItems(sortedList);
        todoactivityView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        todoactivityView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>() {
                };


                return cell;
            }
        });




        todoSDesView.setCellFactory(CheckBoxListCell.forListView(TodoItem::selectedProperty, new StringConverter<TodoItem>() {
            @Override
            public String toString(TodoItem object) {
                return categoryFilter();
            }

            @Override
            public TodoItem fromString(String string) {
                return null;
            }

        }));
    }

    public String categoryFilter(){

        TodoItem selectedItem = (TodoItem) todoSDesView.getSelectionModel().getSelectedItem();

            filteredList.setPredicate(wantTodaysItems);
            if(filteredList.contains(selectedItem)){
                todoSDesView.getSelectionModel().select(selectedItem);
            }else{
                todoSDesView.getSelectionModel().selectFirst();
            }
        todoSDesView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>() {

                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                        } else {
                            if(item.getCategory().equals("In Progress")){
                                setText(item.getShortDescription());
                                }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty){
                                cell.setContextMenu(null);
                            }else {
                                cell.setContextMenu(listContexMenu);
                            }
                        });

                return cell;
            }
        });


        return null;
    }




}
