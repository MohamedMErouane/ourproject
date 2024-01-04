// TodoListController.java
package com.example.demo;

import com.example.demo.DAO.TodoDAO;
import com.example.demo.DAOimp.TodoDAOimp;
import com.example.demo.model.TodoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class TodoListController {

    @FXML
    private ListView<TodoItem> todoListView;
    @FXML
    private Button logoutButton;


    @FXML
    private TextField taskTextField;

    @FXML
    private Label todoMessage;

    private ObservableList<TodoItem> todoList;

    @FXML
    public void initialize() {
        todoList = FXCollections.observableArrayList();
        todoListView.setItems(todoList);
        TodoDAO dbHelper = new TodoDAOimp();
        dbHelper.createTasksTable();

        todoListView.setCellFactory(param -> new ListCell<TodoItem>() {
            @Override
            protected void updateItem(TodoItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

    @FXML
    private void handleDeleteTask() {
        TodoItem selectedTask = todoListView.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            // Remove the task from the UI
            todoList.remove(selectedTask);

            // Delete the task from the database using the task name
            TodoDAO dbHelper = new TodoDAOimp();
            dbHelper.deleteTask(selectedTask.getName());

            todoMessage.setText("Task deleted successfully.");
        } else {
            todoMessage.setText("Please select a task to delete.");
        }
    }


    @FXML
    private void handleAddTask() {
        String task = taskTextField.getText().trim();

        if (!task.isEmpty()) {
            TodoItem todoItem = new TodoItem(task);
            todoList.add(todoItem);

            // Clear the taskTextField
            taskTextField.clear();

            todoMessage.setText("Task added successfully.");

            // Save the task to the database
            TodoDAO dbHelper = new TodoDAOimp();
            dbHelper.insertTask(task, false); // Initially set as not completed
        } else {
            todoMessage.setText("Please enter a task.");
        }
    }


    public void handleToggleCompleted(ActionEvent actionEvent) {
        TodoItem selectedTask = todoListView.getSelectionModel().getSelectedItem();

        if (selectedTask != null) {
            try {
                // Toggle the completion status in the UI
                selectedTask.setCompleted(!selectedTask.isCompleted());

                // Update the completion status in the database
                TodoDAO dbHelper = new TodoDAOimp();
                dbHelper.updateTaskCompleted(selectedTask.getName(), selectedTask.isCompleted());

                todoMessage.setText("Task completion status updated successfully.");
            } catch (Exception e) {
                // Log the exception for debugging purposes
                e.printStackTrace();

                // Display an error message to the user
                todoMessage.setText("Error updating task completion status.");
            }
        } else {
            todoMessage.setText("Please select a task to update.");
        }
    }
    @FXML
    private void handleLogout() {
        // Load the login-view.fxml and set it in the primaryStage
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Close the todo list window
            ((Stage) logoutButton.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception (e.g., show an error message)
        }
    }

}

