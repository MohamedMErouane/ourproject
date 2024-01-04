package com.example.demo.DAOimp;
import com.example.demo.DAO.*;


import java.sql.*;

public class TodoDAOimp implements TodoDAO {
    public String jdbcUrl = "jdbc:mysql://localhost:3306/fx";

    public String jdbcUser = "root";
    public String jdbcPassword = "";

    private  static final String createTableSQL = "CREATE TABLE IF NOT EXISTS Tasks (" +
            "id INT PRIMARY KEY AUTO_INCREMENT," +
            "task VARCHAR(255) NOT NULL," +
            "completed BOOLEAN NOT NULL)";

    private static final String DELETE_TASK = "DELETE FROM tasks WHERE task = ?";

    private static final String INSERT_TASK = "INSERT INTO tasks (task, completed) VALUES (?, ?)";

    public static final String UPDATE_TASK = "UPDATE tasks SET completed = ? WHERE task = ?";


    public Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void closeConnection() {
        try (Connection databaseLink = getConnection()){
            if (databaseLink != null && !databaseLink.isClosed()) {
                databaseLink.close();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logging framework instead
        }
    }
    public void createTasksTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createTableSQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteTask(String taskName) {
        try (Connection connection = getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(DELETE_TASK)) {
                statement.setString(1, taskName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log the error, show a message to the user)
        }
    }


    public void insertTask(String task, boolean completed) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_TASK, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, task);
                statement.setBoolean(2, completed);
                statement.executeUpdate();

                // Retrieve the auto-generated task ID
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        int taskId = resultSet.getInt(1);
                        // You can store or use the taskId as needed
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    public void updateTaskCompleted(String taskName, boolean currentCompletedStatus) {
        try (Connection connection = getConnection()) {

            // Toggle the completed status
            boolean newCompletedStatus = !currentCompletedStatus;

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_TASK)) {
                statement.setBoolean(1, newCompletedStatus);
                statement.setString(2, taskName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception (e.g., log the error, show a message to the user)
        }
    }

}
