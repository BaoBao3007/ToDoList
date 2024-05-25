


package Controllers;
import Dao.CategoryDao;
import Dao.ReminderDao;
import Dao.TaskDao;
import Model.Reminder;
import Model.Task;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class TaskController   {
    @FXML
    public Label DescLabel;
    private List<Task> Tasks;

    @FXML
    private ListView<Task> task_name;
    @FXML
    private ListView<Task> task_id;
    @FXML
    private ListView<Task> category;

    @FXML
    private ListView<Task> important;
    @FXML
    private Label deadlineLabel;

    @FXML
    private ContextMenu listContexMenu;
    @FXML
    private ComboBox<String> categoryComboBox;

    public void initialize() {
        loadCategories();
        categoryComboBox.setOnAction(event -> {
            String selectedCategory = categoryComboBox.getValue();
            if (selectedCategory.equals("All tasks")) {
                List<Task> allTasks = TaskDao.getInstance().getAllTasks();
                ObservableList<Task> observableAllTasks = FXCollections.observableArrayList(allTasks);
                updateListViews(observableAllTasks);
            } else {
                List<Task> tasksByCategory = TaskDao.getInstance().getTasksByCategory(selectedCategory);
                ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasksByCategory);
                updateListViews(observableTasks);
            }
        });
        RightClick rc = new RightClick();
        listContexMenu = rc.ListContexMenu(task_name);
        cell();
        synchronizeScrolling(task_name, task_id, category, important);
    }
    private void loadCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All tasks");
        categories.addAll(CategoryDao.getInstance().getAllCategories());
        ObservableList<String> observableCategories = FXCollections.observableArrayList(categories);
        categoryComboBox.setItems(observableCategories);
        categoryComboBox.getSelectionModel().selectFirst();
    }

    private void updateListViews(ObservableList<Task> tasks) {
        task_id.setItems(tasks);
        task_name.setItems(tasks);
        important.setItems(tasks);
        category.setItems(tasks);
    }
    public void cell(){
        List<Task> tasks = TaskDao.getInstance().getAllTasks();
        ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);
        task_id.setItems(observableTasks);
        task_id.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        task_id.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                ListCell<Task> cell = new ListCell<Task>() {

                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);

                        } else {
                            setText(String.valueOf(item.getTask_id()));
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

        task_id.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                if(newValue != null) {

                    int selectedIndex =task_id.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        task_name.getSelectionModel().select(selectedIndex);
                        category.getSelectionModel().select(selectedIndex);
                        important.getSelectionModel().select(selectedIndex);
                    }
                }
            }
        });

        task_name.setItems(observableTasks);
        task_name.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        task_name.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                ListCell<Task> cell = new ListCell<Task>() {

                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Task selectedItem = (Task) task_name.getSelectionModel().getSelectedItem();
                            setText(item.getTask_name());
                            if (observableTasks.contains(selectedItem)) {
                                task_name.getSelectionModel().select(selectedItem);
                            } else {
                                task_name.getSelectionModel().selectFirst();
                            }
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

        task_name.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                if(newValue != null) {
                    Task item = task_name.getSelectionModel().getSelectedItem();
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy"); // "d M yy");
                    deadlineLabel.setText(df.format(item.getDue_date()));
                    DescLabel.setText(item.getDescription());

                    int selectedIndex =task_name.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        task_id.getSelectionModel().select(selectedIndex);
                        category.getSelectionModel().select(selectedIndex);
                        important.getSelectionModel().select(selectedIndex);
                    }
                }
            }
        });


        important.setItems(observableTasks);
        important.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
         String STAR_FILL_PATH = "../img/Star_fill.png";
         String STAR_LINE_PATH = "../img/Star_line.png";


        important.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new ListCell<Task>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            Image image;
                            if (item.isImportant()) {
                                image = new Image(getClass().getResourceAsStream(STAR_FILL_PATH));
                            } else {
                                image = new Image(getClass().getResourceAsStream(STAR_LINE_PATH));
                            }

                            imageView.setImage(image);
                            imageView.setFitHeight(15);
                            imageView.setFitWidth(15);
                            setGraphic(imageView);
                        }
                    }
                };
            }
        });
        important.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                // Get the selected item
                Task selectedItem = important.getSelectionModel().getSelectedItem();
                TaskDao.getInstance().updateTaskImportant(selectedItem.getTask_id(), !selectedItem.isImportant());
                cell();
            }
        });
        important.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                if(newValue != null) {

                    int selectedIndex =important.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        task_name.getSelectionModel().select(selectedIndex);
                        task_id.getSelectionModel().select(selectedIndex);
                        category.getSelectionModel().select(selectedIndex);
                    }
                }
            }
        });


        category.setItems(observableTasks);
        category.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        category.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                ListCell<Task> cell = new ListCell<Task>() {

                    @Override
                    protected void updateItem(Task item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty) {
                            setText(null);
                        } else {
                            String category_name = CategoryDao.getInstance().getCategoryName(item.getCategory_id());
                            setText(category_name);
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

        category.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                if(newValue != null) {

                    int selectedIndex =category.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        task_name.getSelectionModel().select(selectedIndex);
                        task_id.getSelectionModel().select(selectedIndex);
                        important.getSelectionModel().select(selectedIndex);
                    }
                }
            }
        });
    }



    private void synchronizeScrolling(ListView<Task>... listViews) {
        for (ListView<Task> listView : listViews) {
            ScrollBar scrollBar = getScrollBar(listView);
            if (scrollBar != null) {
                scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        for (ListView<Task> lv : listViews) {
                            if (lv != listView) {
                                ScrollBar otherScrollBar = getScrollBar(lv);
                                if (otherScrollBar != null) {
                                    otherScrollBar.setValue(newValue.doubleValue());
                                }
                            }
                        }
                    }
                });
            }
        }
    }
    private ScrollBar getScrollBar(ListView<Task> listView) {
        for (Node node : listView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollBar = (ScrollBar) node;
                if (scrollBar.getOrientation() == Orientation.VERTICAL) {
                    return scrollBar;
                }
            }
        }
        return null;
    }
}
