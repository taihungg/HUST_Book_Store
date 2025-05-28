package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class UserManagementController {

    @FXML
    private void handleAdd(ActionEvent event) {
        changeScene("/AddAccount.fxml", event);
    }

    @FXML
    private void handleEditOrDelete(ActionEvent event) {
        changeScene("/EditOrDeleteAccount.fxml", event);
    }

    private void changeScene(String fxml, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("view/admin/Manage/ManageUser/UserManagement_fixed.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Account Management");
            stage.setScene(new Scene(root));
            stage.show();

            // đóng cửa sổ hiện tại
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
