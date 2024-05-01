package Controllers;


import Data.TodoData;
import Data.TodoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Important {
    private List<TodoItem> TodoItems;
    private Predicate<TodoItem> wantAllItems;
    private FilteredList<TodoItem> filteredList;
    private Predicate<TodoItem> wantTodaysItems;

    @FXML
    private ListView<TodoItem> todoSDesView;

    @FXML
    private ListView<TodoItem> todoActivity;

    @FXML
    private Label deadlineLabel;
    private ImageView imageView;
    @FXML
    private ContextMenu listContexMenu;

    public void initialize() {
        RightClick rc = new RightClick();
        listContexMenu = rc.ListContexMenu(todoSDesView);

        wantAllItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem TodoItem) {
                return true;
            }
        };

        wantTodaysItems = new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem TodoItem) {
                return (TodoItem.getCategory().equals("Important"));
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

        todoSDesView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observable, TodoItem oldValue, TodoItem newValue) {
                if(newValue != null) {
                    TodoItem item = todoSDesView.getSelectionModel().getSelectedItem();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy"); // "d M yy");
                    deadlineLabel.setText(df.format(item.getDeadline()));
                }
            }
        });
        todoSDesView.setItems(sortedList);
        todoSDesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Boolean isImportant = true;
        todoSDesView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>() {

                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            TodoItem selectedItem = (TodoItem) todoSDesView.getSelectionModel().getSelectedItem();

                            filteredList.setPredicate(wantTodaysItems);
                            if (filteredList.contains(selectedItem)) {
                                todoSDesView.getSelectionModel().select(selectedItem);
                            } else {
                                todoSDesView.getSelectionModel().selectFirst();
                            }
                            if (item.getCategory().equals("Important")) {
                                setText(item.getShortDescription());
                            }

                            if (imageView == null) {
                                imageView = new ImageView();
                            }
                            if(isImportant)
                            {
                                Image image = new Image(getClass().getResourceAsStream("../img/Star_fill.png"));
                                imageView.setImage(image);
                            }
                            else
                            {
                                Image image = new Image(getClass().getResourceAsStream("../img/Star_line.png"));
                                imageView.setImage(image);
                            }
                            // Tạo một ImageView để hiển thị ảnh

                            imageView.setFitHeight(20); // Đặt kích thước của ảnh
                            imageView.setFitWidth(20);

                            // Đặt ImageView làm đồ họa của ô list cell
                            setGraphic(imageView);
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContexMenu);
                            }
                        });

                return cell;
            }
        });


        todoActivity.setItems(sortedList);
        todoActivity.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoActivity.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> param) {
                ListCell<TodoItem> cell = new ListCell<TodoItem>() {

                    @Override
                    protected void updateItem(TodoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                        } else {

                            if(item.getCategory().equals("Important")){
                                setText(item.getCategory());
                            }
                        }
                    }
                };

                return cell;
            }
        });
    }

    @FXML
    public void handleClickListView() {
        TodoItem item = todoSDesView.getSelectionModel().getSelectedItem();
        if (filteredList.isEmpty()){
            deadlineLabel.setText("");
        }
        else {
            deadlineLabel.setText(item.getDeadline().toString());
        }



    }

}
