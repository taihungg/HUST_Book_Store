package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.user.customer.Customer;
import model.manager.AppServiceManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.regex.Pattern;


public class SignUpController {
    
    @FXML
    private TextField address;

    @FXML
    private Button btnSignUp;

    @FXML
    private TextField cfpw;

    @FXML
    private TextField email;

    @FXML
    private TextField fullName;

    @FXML
    private TextField password;

    @FXML
    private TextField phone;

    @FXML
    private TextField username;
    private AppServiceManager appServiceManager = Main.appServiceManager;
    private static final String EMAIL_REGEX =
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+" +
        "[a-zA-Z]{2,}$";

private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

private boolean isValidEmail(String email) {
    if (email == null || email.isEmpty()) {
        return false;
    }
    return EMAIL_PATTERN.matcher(email).matches();
}
    @FXML
    void btnSignUpclicked(ActionEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.cfpw.getText();
        String fullName = this.fullName.getText();
        String emailText = this.email.getText();
        String phone = this.phone.getText();
        String address = this.address.getText();

        if (!isValidEmail(emailText)) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Đăng Ký", "Định dạng email không hợp lệ.");
            email.requestFocus(); // Focus vào trường email để người dùng sửa
            return;
        }

        if (password.equals(confirmPassword)) {
            appServiceManager.getUserManager().registerCustomer(fullName, emailText, phone, username, password, address);
        }
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Sign up successful");
        alert.setContentText("Please login with your username and password.");
        alert.showAndWait();

        try {
            // Chuyển về trang đăng nhập
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi chuyển trang: " + e.getMessage());
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Phương thức chung để chuyển trang
    
    
}
