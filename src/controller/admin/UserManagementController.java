package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import model.manager.user.UserManager;
import model.manager.AppServiceManager;
import model.user.User;

import java.io.IOException;

import controller.Main;

public class UserManagementController {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button displayAllButton;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> passwordColumn;
    @FXML private TableColumn<User, String> telephoneColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button exitButton;


    private UserManager userManager = Main.appServiceManager.getUserManager();
    private AppServiceManager appServiceManager = Main.appServiceManager;
    @FXML
    private void initialize() {
        userTable.setEditable(true);

        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
        telephoneColumn.setCellValueFactory(new PropertyValueFactory<User, String>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        usernameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        passwordColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        telephoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());

      

        nameColumn.setOnEditCommit((CellEditEvent<User, String> event) -> {
            User user = event.getRowValue();
            user.setName(event.getNewValue()); // User cần có setName()
        });

        usernameColumn.setOnEditCommit((CellEditEvent<User, String> event) -> {
            User user = event.getRowValue();
            user.setUsername(event.getNewValue()); // User cần có setUsername()
        });

        passwordColumn.setOnEditCommit((CellEditEvent<User, String> event) -> {
            User user = event.getRowValue();
            user.setPassword(event.getNewValue()); // User cần có setPassword()
        });

        telephoneColumn.setOnEditCommit((CellEditEvent<User, String> event) -> {
            User user = event.getRowValue();
            user.setPhone(event.getNewValue()); // User cần có setTelephone()
        });

        emailColumn.setOnEditCommit((CellEditEvent<User, String> event) -> {
            User user = event.getRowValue();
            user.setEmail(event.getNewValue()); // User cần có setEmail()
        });
        

        userTable.setItems(userManager.getAllUsers(Main.currentUser));
        
    }

    @FXML
    private void handleAddButtonAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/admin/Manage/ManageUser/AddAccount.fxml"));
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        Stage currentStage = (Stage) addButton.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void handleDeleteButtonAction(ActionEvent event) throws IOException {
        User user = userTable.getSelectionModel().getSelectedItem();
        if (user != null) {
            userManager.deleteUser(user, Main.currentUser);
            userTable.setItems(userManager.getAllUsers(Main.currentUser));
        }
    }

    @FXML
    private void handleExitButtonAction(ActionEvent event) {
       
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/ManagePageView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) exitButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    @FXML
    void handleSearchButtonAction(ActionEvent event) {

    }
}
