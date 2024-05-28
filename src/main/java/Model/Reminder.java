package Model;

import java.sql.Timestamp;

public class Reminder {
    private Timestamp reminderDate;
    private String reminderMessage;
    private boolean sent;
    private int reminderId;
    private int taskId;


    public Reminder(int reminderId, int taskId, Timestamp reminderDate, String reminderMessage) {
        this.reminderId = reminderId;
        this.taskId = taskId;
        this.reminderDate = reminderDate;
        this.reminderMessage = reminderMessage;
        this.sent = false;
    }

    public Reminder() {

    }


    public void setReminderDate(Timestamp reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderMessage() {
        return reminderMessage;
    }

    public void setReminderMessage(String reminderMessage) {
        this.reminderMessage = reminderMessage;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Timestamp getReminderDate() {
        return reminderDate;
    }

}