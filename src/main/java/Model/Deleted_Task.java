package Model;

import java.sql.Timestamp;

public class Deleted_Task {
    private int deletedTaskId;
    private int taskId;
    private String taskName;
    private String description;
    private Timestamp dueDate;
    private int categoryId;
    private boolean important;
    private String username;
    private Timestamp deletionDate;

    public Deleted_Task(int deletedTaskId, int taskId, String taskName, String description, Timestamp dueDate, int categoryId, boolean important, String username, Timestamp deletionDate) {
        this.deletedTaskId = deletedTaskId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.description = description;
        this.dueDate = dueDate;
        this.categoryId = categoryId;
        this.important = important;
        this.username = username;
        this.deletionDate = deletionDate;
    }

    public int getDeletedTaskId() {
        return deletedTaskId;
    }

    public void setDeletedTaskId(int deletedTaskId) {
        this.deletedTaskId = deletedTaskId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public Timestamp getDeletionDate() {
        return deletionDate;
    }

    public void setDeletionDate(Timestamp deletionDate) {
        this.deletionDate = deletionDate;
    }
}