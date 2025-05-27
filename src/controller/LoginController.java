package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button button_forgot_password;
    
    @FXML
    private Button button_sign_up;
    
    @FXML
    private Label messageLabel;
    
    // Danh sách tài khoản được thiết lập sẵn
    private Map<String, UserAccount> accounts;
    
    // Class để lưu thông tin tài khoản
    private static class UserAccount {
        String username;
        String password;
        String role; // "admin" hoặc "customer"
        
        UserAccount(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    }
    
    @FXML
    private void initialize() {
        // Khởi tạo danh sách tài khoản
        accounts = new HashMap<String, UserAccount>();
        accounts.put("admin", new UserAccount("admin", "admin123", "admin"));
        accounts.put("customer1", new UserAccount("customer1", "pass123", "customer"));
        accounts.put("customer2", new UserAccount("customer2", "pass456", "customer"));
    }
    
    @FXML
    void btnLoginclicked(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Kiểm tra input rỗng
        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Vui lòng nhập đầy đủ thông tin!", "error");
            return;
        }
        
        // Kiểm tra tài khoản
        UserAccount account = accounts.get(username);
        
        if (account == null || !account.password.equals(password)) {
            showMessage("Tên đăng nhập hoặc mật khẩu không đúng!", "error");
            return;
        }
        
        // Chuyển trang theo role
        try {
            if ("admin".equals(account.role)) {
                changePage(event, "/view/admin/HomePageAdmin.fxml");
            } else if ("customer".equals(account.role)) {
                changePage(event, "/view/customer/CustomerMain.fxml");
            }
        } catch (Exception e) {
            showMessage("Có lỗi xảy ra!", "error");
        }
    }
    
    @FXML
    void handleForgotPasswordAction(ActionEvent event) {
        try {
            changePage(event, "/view/ForgotPassword.fxml");
        } catch (Exception e) {
            showMessage("Không thể mở trang!", "error");
        }
    }
    
    @FXML
    void handleSignUpAction(ActionEvent event) {
        try {
            changePage(event, "/view/SignUp.fxml");
        } catch (Exception e) {
            showMessage("Không thể mở trang!", "error");
        }
    }
    
    // Phương thức chung để chuyển trang
    private void changePage(ActionEvent event, String fxmlPath) throws IOException {
        Parent newPage = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene newScene = new Scene(newPage);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();
    }
    
    private void showMessage(String message, String type) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            if ("error".equals(type)) {
                messageLabel.setStyle("-fx-text-fill: red;");
            } else {
                messageLabel.setStyle("-fx-text-fill: green;");
            }
        }
    }
}