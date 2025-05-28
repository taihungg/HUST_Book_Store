package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import controller.Main;
import model.manager.AppServiceManager;
import model.manager.user.UserManager;
import model.user.User;
import model.user.customer.Customer;
import model.user.manager.Admin;

public class LoginController {

    @FXML
    private Button btnLogin;

    @FXML
    private Button button_forgot_password;

    @FXML
    private Button button_sign_up;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField usernameField;

    private AppServiceManager appServiceManager = Main.appServiceManager;
    
    // Class để lưu thông tin tài khoản
    
    

    @FXML
    void btnLoginclicked(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        
        // Kiểm tra input rỗng
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
            return;
        }
        User authenticatedUser = Main.userManager.getUserByUsername(username);
        // Kiểm tra tài khoản
        
        if (authenticatedUser == null ) {
            showAlert(AlertType.ERROR, "Lỗi Đăng Nhập", "Tên đăng nhập hoặc mật khẩu không đúng!");
            return;
        }

        Main.currentUser = authenticatedUser;
        System.out.println("Main.currentUser đã được cập nhật");
        
        // Chuyển trang theo role
        try {
            if (authenticatedUser.getUsername().equals(username) && authenticatedUser.getPassword().equals(password)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/HomePageAdmin.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Please check your username and password.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleForgotPasswordAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ForgotPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Please check your username and password.");
            alert.showAndWait();
        }

    }

    @FXML
    void handleSignUpAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Credentials");
            alert.setContentText("Please check your username and password.");
            alert.showAndWait();
        }
    }
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
