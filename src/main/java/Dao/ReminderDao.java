package Dao;

import Model.Reminder;
import Model.Task;
import Model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReminderDao {
    private MySQLDataAccess dbConnection;
    private ScheduledExecutorService scheduler;
    public ReminderDao() {
        dbConnection = new MySQLDataAccess();
    }

    private static ReminderDao instance = new ReminderDao();

    public static ReminderDao getInstance() {
        if (instance == null) {
            instance = new ReminderDao();
            instance.startReminderScheduler();
        }
        return instance;
    }

    public List<Reminder> getAllReminders() {
        List<Reminder> reminders = new ArrayList<>();
        String query = "SELECT * FROM Reminder";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Reminder reminder = new Reminder(
                        resultSet.getInt("reminder_id"),
                        resultSet.getInt("task_id"),
                        resultSet.getTimestamp("reminder_date"),
                        resultSet.getString("reminder_message")
                );
                reminders.add(reminder);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reminders;
    }

    public void sendReminderEmail(String toEmail, String subject, String messageBody) {
        // Assuming you are sending email from through smtp.gmail.com
        String fromEmail = "dinhbao30072003@gmail.com"; //replace with your email
        String password = "ckcm hpzy eitm ndab"; //replace with your email password

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);
            System.out.println("Reminder Email Sent Successfully");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDueReminders() {
        List<Reminder> reminders = getAllReminders();
        LocalDate currentDate = LocalDate.now(); // Lấy ngày hiện tại
        for (Reminder reminder : reminders) {
            if (!reminder.isSent()) {
                LocalDateTime reminderDateTime = reminder.getReminderDate().toLocalDateTime(); // Chuyển đổi Timestamp thành LocalDateTime
                LocalDate reminderDate = reminderDateTime.toLocalDate(); // Chuyển đổi LocalDateTime thành LocalDate

                if (reminderDate.equals(currentDate)) {
                    // Retrieve the associated task and user information
                    Task task = getTaskById(reminder.getTaskId());
                    User user = getUserByTaskId(reminder.getTaskId());

                    // Send the reminder email
                    sendReminderEmail(user.getEmail(), "Lời nhắc: " + task.getTask_name(), reminder.getReminderMessage());

                    // Update the reminder to set it as sent
                    setReminderSent(reminder.getReminderId());
                }
            }
        }
    }
    private Task getTaskById(int taskId) {
        String query = "SELECT * FROM Task WHERE task_id = ?";
        Task task = null;

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                task = new Task(
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("description"),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getInt("category_id"),
                        resultSet.getString("status"),
                        resultSet.getBoolean("important"),
                        resultSet.getString("username"),
                        resultSet.getDate("creation_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return task;
    }

    private User getUserByTaskId(int userId) {
        String query = "SELECT u.* FROM User u JOIN Task t ON u.username = t.username WHERE t.task_id = ?";
        User user = null;

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("fullname")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private void setReminderSent(int reminderId) {
        String query = "UPDATE Reminder SET sent = 1 WHERE reminder_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reminderId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void startReminderScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable reminderTask = this::sendDueReminders;
        // Schedule the task to run once a day, with an initial delay of 0 seconds
        scheduler.scheduleAtFixedRate(reminderTask, 0, 1, TimeUnit.DAYS);
    }

    public void stopReminderScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
    public List<Task> getDeletedTasks() {
        List<Task> deletedTasks = new ArrayList<>();
        String query = "SELECT * FROM Task WHERE status = 'Deleted'";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("description"),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getInt("category_id"),
                        resultSet.getString("status"),
                        resultSet.getBoolean("important"),
                        resultSet.getString("username"),
                        resultSet.getDate("creation_date").toLocalDate()
                );
                deletedTasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedTasks;
    }

    public List<Task> getTasksByDate(LocalDate date) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM Task WHERE due_date = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, Date.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Task task = new Task(
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("description"),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getInt("category_id"),
                        resultSet.getString("status"),
                        resultSet.getBoolean("important"),
                        resultSet.getString("username"),
                        resultSet.getDate("creation_date").toLocalDate()
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void deleteReminder(Reminder reminder) {
        String query = "DELETE FROM Reminder WHERE reminder_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reminder.getReminderId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateReminder(Reminder reminder) {
        String query = "UPDATE Reminder SET task_id = ?, reminder_date = ?, reminder_message = ? WHERE reminder_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reminder.getTaskId());
            preparedStatement.setTimestamp(2, reminder.getReminderDate());
            preparedStatement.setString(3, reminder.getReminderMessage());
            preparedStatement.setInt(4, reminder.getReminderId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addReminder(Reminder reminder) {
        String query = "INSERT INTO Reminder (task_id, reminder_date, reminder_message) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reminder.getTaskId());
            preparedStatement.setTimestamp(2, reminder.getReminderDate());
            preparedStatement.setString(3, reminder.getReminderMessage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getTaskNameByTaskId(int taskId) {
        String query = "SELECT t.task_name FROM Task t INNER JOIN Reminder r ON t.task_id = r.task_id WHERE t.task_id = ?";
        String taskName = null;

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, taskId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                taskName = resultSet.getString("task_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskName;
    }
    public Task getTaskByName(String taskName) {
        // TODO: Implement logic to retrieve a task by its name from the database
        // Example:
        String query = "SELECT * FROM Task WHERE task_name = ?";
        Task task = null;

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, taskName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                task = new Task(
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("description"),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getInt("category_id"),
                        resultSet.getString("status"),
                        resultSet.getBoolean("important"),
                        resultSet.getString("username"),
                        resultSet.getDate("creation_date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return task;
    }

    public void updateTask(Task task) {
        // TODO: Implement logic to update a task in the database
        // Example:
        String query = "UPDATE Task SET task_name = ?, description = ?, due_date = ?, category_id = ?, status = ?, important = ?, username = ?, creation_date = ? WHERE task_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, task.getTask_name());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setDate(3, Date.valueOf(task.getDue_date()));
            preparedStatement.setInt(4, task.getCategory_id());
            preparedStatement.setString(5, task.getStatus());
            preparedStatement.setBoolean(6, task.isImportant());
            preparedStatement.setString(7, task.getUsername());
            preparedStatement.setDate(8, Date.valueOf(task.getCreation_date()));
            preparedStatement.setInt(9, task.getTask_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Task task) {
        // TODO: Implement logic to delete a task from the database
        // Example:
        String query = "DELETE FROM Task WHERE task_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, task.getTask_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Reminder getReminderById(int reminderId) {
        String query = "SELECT * FROM Reminder WHERE reminder_id = ?";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reminderId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Reminder reminder = new Reminder(
                            resultSet.getInt("reminder_id"),
                            resultSet.getInt("task_id"),
                            resultSet.getTimestamp("reminder_date"),
                            resultSet.getString("reminder_message")
                    );
                    return reminder;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

