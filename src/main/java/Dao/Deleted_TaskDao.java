package Dao;

import Model.Deleted_Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Deleted_TaskDao {
    private MySQLDataAccess dbConnection;

    public Deleted_TaskDao() {
        dbConnection = new MySQLDataAccess();
    }

    private static Deleted_TaskDao instance = new Deleted_TaskDao();

    public static Deleted_TaskDao getInstance() {
        return instance;
    }

    public List<Deleted_Task> getAllDeletedTasks(String username) {
        List<Deleted_Task> deletedTasks = new ArrayList<>();
        String query = "SELECT * FROM Deleted_Task where username ='"+username+"'";

        try (Connection connection = dbConnection.openConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Deleted_Task deletedTask = new Deleted_Task(
                        resultSet.getInt("deleted_task_id"),
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("due_date"),
                        resultSet.getInt("category_id"),
                        resultSet.getBoolean("important"),
                        resultSet.getString("username"),
                        resultSet.getTimestamp("deletion_date")
                );
                deletedTasks.add(deletedTask);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedTasks;
    }

    public String getTaskNameByTaskId(int taskId) {
        String taskName = null;
        String query = "SELECT task_name FROM Task WHERE task_id = ?";

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
}