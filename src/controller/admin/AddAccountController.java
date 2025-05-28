package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.user.manager.Employee;

import java.io.IOException;

import controller.Main;

public class AddAccountController {

    @FXML
    private Button addEmployeeButton;

    @FXML
    private TextField emailField;

    @FXML
    private Button exitButton;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField usernameField;

    @FXML
    private void initialize() {
        // Initialize table columns and data if needed
    }

    @FXML
    private void handleExitButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/admin/Manage/ManageUser/UserManagement_fixed.fxml"));
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Stage currentStage = (Stage) exitButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void handleAddEmployeeAction(ActionEvent event) throws IOException  {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String salary = salaryField.getText();
        
        Employee newEmployee = new Employee(name, email, phone, username, password, Double.parseDouble(salary));

        Main.userManager.addUser(newEmployee, Main.currentUser);

        Parent root = FXMLLoader.load(getClass().getResource("/view/admin/Manage/ManageUser/UserManagement_fixed.fxml"));
        Stage stage = (Stage) addEmployeeButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Stage currentStage = (Stage) addEmployeeButton.getScene().getWindow();
        currentStage.close();


    }
}