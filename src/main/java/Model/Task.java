package Model;
import java.time.LocalDate;


public class Task {
    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descriptio) {
        this.description = descriptio;
    }

    public LocalDate getDue_date() {
        return due_date;
    }

    public void setDue_date(LocalDate due_date) {
        this.due_date = due_date;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private int task_id;
    private String task_name;
    private String description;
    private LocalDate due_date;
    private int category_id;
    private boolean important;

    private String username;


    public Task(int task_id, String task_name, String description, LocalDate due_date, int category_id, boolean important, String username) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.description = description;
        this.due_date = due_date;
        this.category_id = category_id;
        this.important = important;
        this.username = username;
    }

    public Task() {
    }

}
