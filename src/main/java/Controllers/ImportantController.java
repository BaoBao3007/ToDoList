


package Controllers;
import Dao.CategoryDao;
import Dao.TaskDao;
import Model.Task;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseButton;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
public class ImportantController   {
    @FXML
    public Label DescLabel;
    @FXML
    public ListView status;
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
    private ComboBox<String> categoryComboBox;
    private boolean isRefreshing = false;
    @FXML
    private ContextMenu listContexMenu;
    public void refreshTaskList() {
        if (isRefreshing) {
            return;
        }

        isRefreshing = true;
        List<Task> tasks = TaskDao.getInstance().getImportantTasks(GlobalData.currentUsername);
        System.out.println(GlobalData.currentUsername);
        ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);
        updateListViews(observableTasks);
        isRefreshing = false;
    }
    public void initialize() {
        loadCategories();
        categoryComboBox.setOnAction(event -> {
            String selectedCategory = categoryComboBox.getValue();

            if (selectedCategory.equals("All tasks")) {
                List<Task> tasks = TaskDao.getInstance().getImportantTasks(GlobalData.currentUsername);
                ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);
                status.setItems(observableTasks);
                cell(observableTasks );
            } else {
                List<Task> tasks = TaskDao.getInstance().getTasksByCategoryImportant(selectedCategory);
                ObservableList<Task> observableTasks = FXCollections.observableArrayList(tasks);
                status.setItems(observableTasks);
                cell( observableTasks);
            }
            TaskDao.getInstance();
        });
        List<Task> tasksss = TaskDao.getInstance().getImportantTasks(GlobalData.currentUsername);
        ObservableList<Task> observableTaskss = FXCollections.observableArrayList(tasksss);
        cell(observableTaskss);
        RightClick rc = new RightClick();
        listContexMenu = rc.ListContexMenu(task_name);
        synchronizeScrolling(task_name, task_id, category,status, important);

    }
    private void loadCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All tasks");
        categories.addAll(CategoryDao.getInstance().getAllCategories(GlobalData.currentUsername));
        ObservableList<String> observableCategories = FXCollections.observableArrayList(categories);
        categoryComboBox.setItems(observableCategories);
        categoryComboBox.getSelectionModel().selectFirst();
    }

    private void updateListViews(ObservableList<Task> tasks) {
        task_id.setItems(tasks);
        task_name.setItems(tasks);
        important.setItems(tasks);
        category.setItems(tasks);
        status.setItems(tasks);
    }

    public void cell(ObservableList<Task> observableTasks){
        task_id.setFixedCellSize(30);
        task_name.setFixedCellSize(30);
        important.setFixedCellSize(30);
        category.setFixedCellSize(30);
        status.setFixedCellSize(30);
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
                        status.getSelectionModel().select(selectedIndex);
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
                        status.getSelectionModel().select(selectedIndex);
                    }
                }
            }
        });

        status.setItems(observableTasks);
        status.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        status.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {


                return new ComboBoxCell();
            }
        });

        status.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {
            @Override
            public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
                if(newValue != null) {

                    int selectedIndex =status.getSelectionModel().getSelectedIndex();
                    if (selectedIndex >= 0) {
                        task_name.getSelectionModel().select(selectedIndex);
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
                Task selectedItem = important.getSelectionModel().getSelectedItem();
                TaskDao.getInstance().updateTaskImportant(selectedItem.getTask_id(), !selectedItem.isImportant());
                List<Task> tasksss = TaskDao.getInstance().getAllTasks(GlobalData.currentUsername);
                ObservableList<Task> observableTaskss = FXCollections.observableArrayList(tasksss);
                cell(observableTaskss);
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
                        status.getSelectionModel().select(selectedIndex);
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
                        status.getSelectionModel().select(selectedIndex);
                    }
                }
            }
        });
    }


    private class ComboBoxCell extends ListCell<Task> {
        private ComboBox<String> comboBox;

        public ComboBoxCell() {
            comboBox = new ComboBox<>();
            comboBox.getItems().addAll("Processing", "Complete", "Late");
            comboBox.setPrefWidth(149);
            comboBox.valueProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    Task task = getItem();
                    if (task != null) {
                        task.setStatus(newValue);
                        TaskDao.getInstance().updateTaskStatus(task.getTask_id(), newValue);

                    }
                }
            });

        }

        @Override
        protected void updateItem(Task item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(comboBox);
                comboBox.setValue(item.getStatus());
                comboBox.setPrefWidth(149);
            }
        }
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
