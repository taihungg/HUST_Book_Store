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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import model.manager.AppServiceManager;
import controller.Main;

public class ForgotPasswordController {

    @FXML
    private Button btnChangePassword;

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private TextField confirmNewPasswordTextField;
    
    @FXML
    private Button btnBack;

    private AppServiceManager appServiceManager = Main.appServiceManager;

    @FXML
    void btnChangePasswordclicked(ActionEvent event) {
        String username = userNameTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmNewPassword = confirmNewPasswordTextField.getText();
        if (appServiceManager.getUserManager().getUserByUsername(username) == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Username not found");
            alert.setContentText("Please check your username and try again.");
            alert.showAndWait();
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("New password and confirm new password do not match");
            alert.setContentText("Please check your new password and confirm new password and try again.");
            alert.showAndWait();
            return;
        }
        appServiceManager.getUserManager().getUserByUsername(username).setPassword(newPassword);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Password changed successfully");
        alert.setContentText("Please login with your new password.");
        alert.showAndWait();

    	try {
            // Chuyển về trang đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi chuyển trang: " + e.getMessage());
        }
    }
    
    @FXML
    void btnBackclicked(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginView = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.initModality(Modality.APPLICATION_MODAL); // Quan trọng: Chặn tương tác với cửa sổ khác
            loginStage.setScene(new Scene(loginView));

            loginStage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close(); 
        }
    	catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi chuyển trang: " + e.getMessage());
        }
    }
    
}


