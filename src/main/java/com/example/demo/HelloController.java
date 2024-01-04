package com.example.demo;


import com.example.demo.DAO.TodoDAO;
import com.example.demo.DAOimp.TodoDAOimp;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button Login;

    @FXML
    private PasswordField Password;

    @FXML
    private PasswordField Password1;

    @FXML
    private TextField Username;

    @FXML
    private TextField Username1;

    @FXML
    private Label Message;
    @FXML
    private Label Message1;

    @FXML
    private AnchorPane layer1;

    @FXML
    private AnchorPane layer2;

    @FXML
    private Hyperlink Login1;

    @FXML
    private Button Signup1;

    @FXML
    private Hyperlink Signup2;

    private TodoDAO dbHelper = new TodoDAOimp();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Login1.setVisible(false);
        Signup1.setVisible(false);
        Login.setVisible(true);
        Signup2.setVisible(true);
        Password.setVisible(true);
        Username.setVisible(true);
        Password1.setVisible(false);
        Username1.setVisible(false);
        Message1.setVisible(false);
        Message.setVisible(true);
    }

    @FXML
    public void LoginOnAction(ActionEvent e) {
        String username = Username.getText();
        String password = Password.getText();

        if (!username.isBlank() && !password.isBlank()) {
            try (Connection connection = dbHelper.getConnection()) {
                if (connection != null) {
                    String query = "SELECT * FROM users WHERE username=? AND password=?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, password);

                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                Message.setText("Login successful");
                                navigateToNewScene();
                            } else {
                                Message.setText("Invalid username or password");
                                Username.clear();
                                Password.clear();
                            }
                        }
                    }
                } else {
                    Message.setText("Failed to connect to the database");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Message.setText("Error during database operation");
            }
        } else {
            Message.setText("Please enter both username and password");
            Username.clear();
            Password.clear();
        }
    }
    @FXML
    private void handleSignup2ButtonClick() {
        String username = Username1.getText();
        String password = Password1.getText();

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            Message1.setText("Please enter both username and password");
            return;
        }

        // Connect to database
        try (Connection connection = dbHelper.getConnection()) {
            if (connection != null) {
                // Prepare SQL statement for user registration
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.executeUpdate();

                    Message1.setText("Registration Successful");

                    // Clear input fields
                    Username1.clear();
                    Password1.clear();

                    // Optionally, navigate to a new scene
                     navigateToNewScene();
                }
            } else {
                Message1.setText("Failed to connect to the database");
            }
        } catch (SQLException e) {
            // Handle database errors gracefully
            e.printStackTrace(); // Consider using a logging framework instead

            // Update the UI with an error message
            Message1.setText("Registration failed due to a database error");
        } finally {
            dbHelper.closeConnection();
        }
    }
    private void navigateToNewScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("todolist-view.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);

            // Get the current stage
            Stage currentStage = (Stage) Login.getScene().getWindow();

            // Set the new scene
            currentStage.setScene(newScene);
            currentStage.show();
        } catch (IOException e) {
            // Handle FXML load failure gracefully (e.g., show an alert, log the error)
            e.printStackTrace();

            // You can replace the following line with appropriate error handling for your application
            throw new RuntimeException("Failed to load todolist-view.fxml", e);
        }
    }


    public void btn(javafx.scene.input.MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(1));
        slide.setNode(layer2);
        slide.setToX(-225); // Set the target X position to 0 (assuming the initial position is to the left)
        layer1.setTranslateX(309);
        slide.play();
        Login1.setVisible(true);
        Signup1.setVisible(true);
        Login.setVisible(false);
        Signup2.setVisible(false);
        Password.setVisible(false);
        Username.setVisible(false);
        Message1.setVisible(true);
        Message.setVisible(false);
        if (Password1 != null) {
            Password1.setVisible(true);
        }
        if (Username1 != null) {
             Username1.setVisible(true);
        }
        slide.setOnFinished(e -> {

        });
    }

    public void btn2(javafx.scene.input.MouseEvent event) {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(1));
        slide.setNode(layer1);
        slide.setToX(0); // Set the target X position to 0 (assuming the initial position is to the left)
        layer2.setTranslateX(0);
        slide.play();
        Login1.setVisible(false);
        Signup1.setVisible(false);
        Login.setVisible(true);
        Signup2.setVisible(true);
        Password.setVisible(true);
        Username.setVisible(true);
        Message1.setVisible(false);
        Message.setVisible(true);
        if (Password1 != null) {
            Password1.setVisible(false);
        }
        if (Username1 != null) {
            Username1.setVisible(false);
        }
        slide.setOnFinished(e -> {

        });
    }
}
