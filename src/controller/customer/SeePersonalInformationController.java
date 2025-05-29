package controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.user.customer.Customer;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import controller.Main;

public class SeePersonalInformationController implements Initializable {

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

   
    private Properties userData;
    private Properties originalData; // Lưu thông tin gốc để phục hồi khi cancel
    private Customer currentUser = (Customer)Main.currentUser;

    // Thông tin mặc định
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fullNameField.setText(currentUser.getName());
        emailField.setText(currentUser.getEmail());
        phoneField.setText(currentUser.getPhone());
        addressField.setText(currentUser.getAddress());
        usernameField.setText(currentUser.getUsername());
        passwordField.setText(currentUser.getPassword());
        
        setFieldsEditable(false);
        updateButtonVisibility(false);
        System.out.println("SeePersonalInformationController initialized successfully");
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
        currentUser.setName(fullNameField.getText().trim());
        currentUser.setEmail(emailField.getText().trim());
        currentUser.setPhone(phoneField.getText().trim());
        currentUser.setAddress(addressField.getText().trim());

        // Save to file
        
        // Update backup with new data
        
        setFieldsEditable(false);
        updateButtonVisibility(false);
        showAlert("Success", "Your information has been saved successfully!");
    }

    @FXML
    private void handleCancel() {
        setFieldsEditable(false);
        updateButtonVisibility(false);
        showAlert("Info", "Changes have been cancelled.");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/HomePage.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.show();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
           
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static final String EMAIL_REGEX =
    "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + // Phần tên người dùng
    "(?:[a-zA-Z0-9-]+\\.)+" +                      // Phần tên miền (có thể có subdomain)
    "[a-zA-Z]{2,}$";                               // Phần TLD (ít nhất 2 chữ cái)

// Biên dịch Pattern một lần để tăng hiệu suất nếu phương thức được gọi nhiều lần
private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

/**
* Kiểm tra xem một chuỗi email có hợp lệ không bằng cách sử dụng biểu thức chính quy.
*
* @param email Chuỗi email cần kiểm tra.
* @return true nếu email hợp lệ, false nếu không.
*/
private boolean isValidEmail(String email) {
if (email == null || email.isEmpty()) {
    return false; // Email rỗng hoặc null không hợp lệ
}
// So khớp email với biểu thức chính quy đã biên dịch
return EMAIL_PATTERN.matcher(email).matches();
}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}