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

public class UserManagementController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button displayAllButton;
    @FXML private TableView<?> userTable;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableColumn<?, ?> nameColumn;
    @FXML private TableColumn<?, ?> usernameColumn;
    @FXML private TableColumn<?, ?> passwordColumn;
    @FXML private TableColumn<?, ?> telephoneColumn;
    @FXML private Button addButton;
    @FXML private Button editDeleteButton;
    @FXML private Button exitButton;

    @FXML
    private void initialize() {
        // Initialize table columns and data if needed
    }

    @FXML
    private void handleAddButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/admin/Manage/ManageUser/AddAccount.fxml"));
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleEditDeleteButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/admin/Manage/ManageUser/EditOrDeleteAccount.fxml"));
        Stage stage = (Stage) editDeleteButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @FXML
    private void handleExitButton() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
