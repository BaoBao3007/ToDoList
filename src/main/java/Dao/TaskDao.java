package Dao;


import Model.Task;
import Model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
                LocalDate dueDate = LocalDate.now();
                if(resultSet.getDate("due_date") != null)
                {
                    dueDate = resultSet.getDate("due_date").toLocalDate();
                }

                LocalDate creation_date = LocalDate.now();

                if(resultSet.getDate("creation_date") != null)
                {
                    creation_date = resultSet.getDate("creation_date").toLocalDate();
                }
                String status = "";
                if(resultSet.getString("status")!=null)
                    status=resultSet.getString("status");
                int categoryId = resultSet.getInt("category_id");
                boolean important = resultSet.getBoolean("important");
                String username = resultSet.getString("username");

                Task task = new Task(taskId, taskName, description, dueDate,categoryId,status,  important, username,creation_date);
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

                LocalDate dueDate = LocalDate.now().plusDays(7);
                if(resultSet.getDate("due_date")!=null)
                     dueDate = resultSet.getDate("due_date").toLocalDate();
                LocalDate creation_date = LocalDate.now().plusDays(7);
                if(resultSet.getDate("creation_date")!=null)
                    creation_date = resultSet.getDate("creation_date").toLocalDate();
                int categoryId = resultSet.getInt("category_id");
                String status = resultSet.getString("status");
                boolean important = resultSet.getBoolean("important");
                String username = resultSet.getString("username");

                Task task = new Task(taskId, taskName, description, dueDate, categoryId,status,  important, username,creation_date);
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
    public List<Task> getTasksByCategory(String categoryName) {
        String sql = "SELECT * FROM Task t INNER JOIN Category c ON t.category_id = c.category_id WHERE c.category_name = ?";
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int taskId = resultSet.getInt("t.task_id");
                String taskName = resultSet.getString("t.task_name");
                String description = resultSet.getString("t.description");
                LocalDate dueDate = LocalDate.now().plusDays(7);
                if(resultSet.getDate("t.due_date")!=null)
                    dueDate = resultSet.getDate("t.due_date").toLocalDate();
                LocalDate creation_date = LocalDate.now().plusDays(7);
                if(resultSet.getDate("t.creation_date")!=null)
                    creation_date = resultSet.getDate("t.creation_date").toLocalDate();
                int categoryId = resultSet.getInt("t.category_id");
                String status = resultSet.getString("t.status");
                boolean important = resultSet.getBoolean("t.important");
                String username = resultSet.getString("t.username");


                Task task = new Task(taskId, taskName, description, dueDate, categoryId,status,important, username,creation_date);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

}
