package Dao;


import Controllers.GlobalData;
import Model.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import java.time.LocalDateTime;


public class TaskDao {
    private static TaskDao instance;
    private DatabaseOperations db;
    private Map<String, Integer> categoryCache = new HashMap<>();

    private TaskDao() {
        try {
            db = new DatabaseOperations();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public static TaskDao getInstance() {
        if (instance == null) {
            instance = new TaskDao();

        }
        return instance;
    }
    public List<Task> getAllTasks(String username) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM task where username ='"+username+"'";

        try (Connection connection = MySQLDataAccess.openConnection();
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
                String Username = resultSet.getString("username");

                Task task = new Task(taskId, taskName, description, dueDate,categoryId,status,  important, Username,creation_date);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }
        public List<Task> getImportantTasks(String Username) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM task WHERE important = true and  username ='"+Username+"'";
        try (Connection connection = MySQLDataAccess.openConnection();
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
        try (Connection connection = MySQLDataAccess.openConnection();
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
    public int updateTaskStatus(int task_id, String status) {

        String sql = "UPDATE task SET status = ? WHERE task_id = ?";
        try (Connection connection = MySQLDataAccess.openConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
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

        try (Connection connection = MySQLDataAccess.openConnection();
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
    public void addTask(Task task) throws SQLException {
        String query = "INSERT INTO Task (task_name, description, due_date, category_id, important, username, creation_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = MySQLDataAccess.openConnection();
             PreparedStatement pst = db.prepareStatement(query)) {
            pst.setString(1, task.getTask_name());
            pst.setString(2, task.getDescription());
            pst.setDate(3, java.sql.Date.valueOf(task.getDue_date()));
            pst.setInt(4, task.getCategory_id());
            pst.setBoolean(5, task.isImportant());
            pst.setString(6, task.getUsername());
            pst.setDate(7, java.sql.Date.valueOf(task.getCreation_date()));
            pst.executeUpdate();
        }
    }
    public boolean updateTask(Task task) {
        String sql = "UPDATE Task SET task_name = ?, description = ?, due_date = ?, category_id = ? WHERE task_id = ?";
        try (Connection conn = MySQLDataAccess.openConnection();
             PreparedStatement stmt = db.prepareStatement(sql)) {

            stmt.setString(1, task.getTask_name());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(task.getDue_date()));
            stmt.setInt(4, task.getCategory_id());
            stmt.setInt(5, task.getTask_id());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getCategoryIdByName(String categoryName) throws SQLException {
        String query = "SELECT category_id FROM Category WHERE category_name = ?";
        try (Connection connection = MySQLDataAccess.openConnection();
             PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, categoryName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("category_id");
            }
        }
        return -1;
    }
    public boolean deleteTaskAndSaveToDeleted(Task task, LocalDateTime deletionDate) throws SQLException {
        String deleteQuery = "DELETE FROM Task WHERE task_id = ?";
        String insertQuery = "INSERT INTO Deleted_Task (task_id, task_name, description, due_date, category_id, important, username, deletion_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = MySQLDataAccess.openConnection()) {
            connection.setAutoCommit(false); // Bắt đầu

            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
                 PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

                deleteStmt.setInt(1, task.getTask_id());
                int rowsDeleted = deleteStmt.executeUpdate();

                if (rowsDeleted > 0) {
                    insertStmt.setInt(1, task.getTask_id());
                    insertStmt.setString(2, task.getTask_name());
                    insertStmt.setString(3, task.getDescription());
                    insertStmt.setObject(4, task.getDue_date() != null ? task.getDue_date() : null);
                    insertStmt.setInt(5, task.getCategory_id());
                    insertStmt.setBoolean(6, task.isImportant());
                    insertStmt.setString(7, task.getUsername());
                    insertStmt.setObject(8, deletionDate);

                    insertStmt.executeUpdate();
                } else {

                    connection.rollback();
                    return false;
                }

                connection.commit();
                return true;
            } catch (SQLException e) {
                // Rollback nếu có lỗi
                connection.rollback();
                System.err.println("Lỗi SQL: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }
    public List<Task> getTodayTasks(String username) {
        List<Task> tasks = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        String formattedCurrentDate = currentDate.format(DateTimeFormatter.ISO_DATE);
        System.out.println(formattedCurrentDate);
        String query = "SELECT * FROM task WHERE username ='" + username + "' AND due_date >= '" + formattedCurrentDate + "'";

        try (Connection connection = MySQLDataAccess.openConnection();
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
                String Username = resultSet.getString("username");

                Task task = new Task(taskId, taskName, description, dueDate,categoryId,status,  important, Username,creation_date);
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public void updateStatus() {
        List<Task> getAllTasks = getAllTasks(GlobalData.currentUsername);
        for (Task task :
                getAllTasks) {
            String sql = "UPDATE Task SET status = ? WHERE task_id = ?";
            try (Connection conn = MySQLDataAccess.openConnection();
                 PreparedStatement stmt = db.prepareStatement(sql)) {
                if (task.getDue_date().isAfter(LocalDate.now())) {
                    stmt.setString(1, task.getStatus());
                }

                else stmt.setString(1, "Late");
                stmt.setInt(2, task.getTask_id());
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

}
