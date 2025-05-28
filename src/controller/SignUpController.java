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

import java.io.IOException;

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
    @FXML
    void btnSignUpclicked(ActionEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.cfpw.getText();
        String fullName = this.fullName.getText();
        String email = this.email.getText();
        String phone = this.phone.getText();
        String address = this.address.getText();
        if (password.equals(confirmPassword)) {
            appServiceManager.getUserManager().registerCustomer(fullName, email, phone, username, password, address);
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
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi chuyển trang: " + e.getMessage());
        }
    }
    
    // Phương thức chung để chuyển trang
    
    
}
