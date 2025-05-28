package controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditOrDeleteAccountController {

    @FXML private TableView<?> userTable;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableColumn<?, ?> nameColumn;
    @FXML private TableColumn<?, ?> usernameColumn;
    @FXML private TableColumn<?, ?> passwordColumn;
    @FXML private TableColumn<?, ?> telephoneColumn;
    @FXML private Button searchButton;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField passwordField;
    @FXML private TextField usernameField;
    @FXML private TextField telephoneField;
    @FXML private TextField searchField;
    @FXML private TextField deleteIdField;
    @FXML private Button deleteButton;
    @FXML private Button modifyButton;
    @FXML private Button exitButton;

    @FXML
    private void initialize() {
        // Initialize table columns and data if needed
    }

    @FXML
    private void handleExitButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/admin/Manage/ManageUser/UserManagement_fixed.fxml"));
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}