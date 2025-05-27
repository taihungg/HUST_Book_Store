package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class SeePersonalInformationController implements SubController, Initializable {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button saveButton;
    @FXML private Button editButton;
    @FXML private Button cancelButton;
    @FXML private Button backButton;

    private CustomerMainController mainController;
    private static final String USER_DATA_FILE = "user_data.properties";
    private Properties userData;
    private Properties originalData; // Lưu thông tin gốc để phục hồi khi cancel

    // Thông tin mặc định
    private static final String DEFAULT_FULLNAME = "Nguyen Van A";
    private static final String DEFAULT_EMAIL = "nguyenvana@example.com";
    private static final String DEFAULT_PHONE = "0123456789";
    private static final String DEFAULT_ADDRESS = "123 Nguyen Trai Street, District 1, Ho Chi Minh City";
    private static final String DEFAULT_USERNAME = "nguyenvana";
    private static final String DEFAULT_PASSWORD = "********";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userData = new Properties();
        originalData = new Properties();
        loadUserData();
        setFieldsEditable(false);
        updateButtonVisibility(false);
        System.out.println("SeePersonalInformationController initialized successfully");
    }

    @Override
    public void setMainController(CustomerMainController mainController) {
        this.mainController = mainController;
    }

    private void loadUserData() {
        File file = new File(USER_DATA_FILE);
        
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                userData.load(fis);
            } catch (IOException e) {
                System.err.println("Error loading user data: " + e.getMessage());
                setDefaultUserData();
            }
        } else {
            setDefaultUserData();
            saveUserData(); // Tạo file với dữ liệu mặc định
        }
        
        populateFields();
        backupOriginalData();
    }

    private void setDefaultUserData() {
        userData.setProperty("fullName", DEFAULT_FULLNAME);
        userData.setProperty("email", DEFAULT_EMAIL);
        userData.setProperty("phone", DEFAULT_PHONE);
        userData.setProperty("address", DEFAULT_ADDRESS);
        userData.setProperty("username", DEFAULT_USERNAME);
        userData.setProperty("password", DEFAULT_PASSWORD);
    }

    private void populateFields() {
        fullNameField.setText(userData.getProperty("fullName", DEFAULT_FULLNAME));
        emailField.setText(userData.getProperty("email", DEFAULT_EMAIL));
        phoneField.setText(userData.getProperty("phone", DEFAULT_PHONE));
        addressField.setText(userData.getProperty("address", DEFAULT_ADDRESS));
        usernameField.setText(userData.getProperty("username", DEFAULT_USERNAME));
        passwordField.setText(userData.getProperty("password", DEFAULT_PASSWORD));
    }

    private void backupOriginalData() {
        originalData.clear();
        originalData.putAll(userData);
    }

    private void restoreOriginalData() {
        userData.clear();
        userData.putAll(originalData);
        populateFields();
    }

    private void saveUserData() {
        try (FileOutputStream fos = new FileOutputStream(USER_DATA_FILE)) {
            userData.store(fos, "User Personal Information");
            System.out.println("User data saved successfully");
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
            showAlert("Error", "Failed to save user data: " + e.getMessage());
        }
    }

    private void setFieldsEditable(boolean editable) {
        // Basic Information fields được edit
        fullNameField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
        addressField.setEditable(editable);
        
        // Account Information được edit
        usernameField.setEditable(editable);
        passwordField.setEditable(editable);
        
        // Update style để hiển thị trạng thái
        String editableStyle = "-fx-background-color: white; -fx-background-radius: 5; -fx-border-color: #3498db; -fx-border-radius: 5;";
        
        String basicFieldsStyle = editableStyle;
        fullNameField.setStyle(basicFieldsStyle);
        emailField.setStyle(basicFieldsStyle);
        phoneField.setStyle(basicFieldsStyle);
        addressField.setStyle(basicFieldsStyle);
        usernameField.setStyle(basicFieldsStyle);
        passwordField.setStyle(basicFieldsStyle);
    }

    private void updateButtonVisibility(boolean isEditing) {
        saveButton.setVisible(isEditing);
        cancelButton.setVisible(isEditing);
        editButton.setVisible(!isEditing);
        backButton.setVisible(!isEditing);
    }

    @FXML
    private void handleEdit() {
        setFieldsEditable(true);
        updateButtonVisibility(true);
        showAlert("Info", "You can now edit your basic information. Account information cannot be changed.");
    }

    @FXML
    private void handleSave() {
        // Validate input
        if (fullNameField.getText().trim().isEmpty() || 
            emailField.getText().trim().isEmpty() || 
            phoneField.getText().trim().isEmpty() || 
            addressField.getText().trim().isEmpty()) {
            showAlert("Error", "Please fill in all required fields.");
            return;
        }

        // Validate email format
        String email = emailField.getText().trim();
        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address.");
            return;
        }

        // Update userData with new values
        userData.setProperty("fullName", fullNameField.getText().trim());
        userData.setProperty("email", emailField.getText().trim());
        userData.setProperty("phone", phoneField.getText().trim());
        userData.setProperty("address", addressField.getText().trim());

        // Save to file
        saveUserData();
        
        // Update backup with new data
        backupOriginalData();
        
        setFieldsEditable(false);
        updateButtonVisibility(false);
        showAlert("Success", "Your information has been saved successfully!");
    }

    @FXML
    private void handleCancel() {
        restoreOriginalData();
        setFieldsEditable(false);
        updateButtonVisibility(false);
        showAlert("Info", "Changes have been cancelled.");
    }

    @FXML
    private void handleBack() {
        if (saveButton.isVisible()) {
            // Nếu đang trong chế độ edit, restore về original data
            restoreOriginalData();
        }
        
        if (mainController != null) {
            mainController.loadPageWithData("/view/customer/Store/BrowseProducts.fxml", null);
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".") && 
               email.indexOf("@") < email.lastIndexOf(".");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}