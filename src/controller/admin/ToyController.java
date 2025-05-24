package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ToyController {
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
    private TextField ageRecommendationField;
    
    @FXML
    private TextField brandField;
    
    @FXML
    private TextField typeField;
    
    @FXML
    private Button addToyButton;
    
    @FXML
    private Button cancelButton;

    @FXML
    public void initialize() {
        // Khởi tạo các thành phần nếu cần
        statusComboBox.getItems().addAll("Available", "Out of Stock", "Discontinued");
    }

    @FXML
    private void handleAddToy() {
        // Xử lý thêm đồ chơi mới
        // TODO: Thêm logic lưu dữ liệu
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