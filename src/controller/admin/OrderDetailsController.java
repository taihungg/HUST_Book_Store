package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class OrderDetailsController {
    @FXML
    private Button ExitButton;

    @FXML
    public void initialize() {
        // Initialize any necessary setup
    }

    @FXML
    private void handleExit() {
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }
} 