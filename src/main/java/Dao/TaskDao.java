package Dao;


import Model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;

public class TaskDao {
    private MySQLDataAccess dbConnection;

    public TaskDao() {
        dbConnection = new MySQLDataAccess();
    }
    private static TaskDao instance = new TaskDao();
    public static TaskDao getInstance() {
        return instance;
    }
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM task";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int taskId = resultSet.getInt("task_id");
                String taskName = resultSet.getString("task_name");
                String description = resultSet.getString("description");
                LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();
                int categoryId = resultSet.getInt("category_id");
                boolean important = resultSet.getBoolean("important");
                String username = resultSet.getString("username");

                Task task = new Task(taskId, taskName, description, dueDate, categoryId, important, username);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }
    public List<Task> getImportantTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM task WHERE important = true";
        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int taskId = resultSet.getInt("task_id");
                String taskName = resultSet.getString("task_name");
                String description = resultSet.getString("description");
                LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();
                int categoryId = resultSet.getInt("category_id");
                boolean important = resultSet.getBoolean("important");
                String username = resultSet.getString("username");

                Task task = new Task(taskId, taskName, description, dueDate, categoryId, important, username);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }




    public int updateTaskImportant(int task_id, boolean important) {

        String sql = "UPDATE task SET important = ? WHERE task_id = ?";
        try (Connection connection = dbConnection.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBoolean(1, important);
            statement.setInt(2, task_id);
            statement.executeUpdate();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return 0;
    }
}
