package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField galleryUrlField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private TextField authorField;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField categoryField;
    @FXML
    private Button addBookButton;
    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        statusComboBox.getItems().addAll("Available", "Out of Stock", "Discontinued");
    }

    @FXML
    private void handleAddBook() {
        // TODO: Implement add book logic
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
} 