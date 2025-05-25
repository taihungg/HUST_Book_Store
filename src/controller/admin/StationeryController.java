package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StationeryController {
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
    private TextField materialField;
    
    @FXML
    private TextField brandField;
    
    @FXML
    private TextField typeField;
    
    @FXML
    private Button addStationeryButton;
    
    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        // Khởi tạo các thành phần nếu cần
        statusComboBox.getItems().addAll("Available", "Out of Stock", "Discontinued");
    }

    @FXML
    private void handleAddStationery() {
      
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