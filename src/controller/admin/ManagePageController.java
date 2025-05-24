package controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ManagePageController {

    @FXML
    private Button manageUserButton;

    @FXML
    private Button updateInventoryButton;

    @FXML
    public void initialize() {
        // Initialize any necessary setup
    }

    @FXML
    private void handleManageUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/view/admin/Manage/ManageUserView.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Quản lý người dùng");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/view/admin/Manage/Update Inventory/ProductTypeSelectionView.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Cập nhật kho");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 