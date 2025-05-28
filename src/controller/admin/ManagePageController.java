package controller.admin;

import java.io.IOException;

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
    private Button backButton;
   

    @FXML
    public void initialize() {
        // Initialize any necessary setup
    }

    @FXML
    private void handleBackAction() {
        try{
        FXMLLoader loader = new FXMLLoader( getClass().getResource("/view/admin/HomePageAdmin.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Trang chủ");
        stage.setScene(new Scene(root));
        stage.show();

        Stage currentstage = (Stage)backButton.getScene().getWindow();
        currentstage.close();
        
        }
        catch(IOException e){
            System.err.println("Lỗi khi mở trang chủ");
            e.printStackTrace();
            }
        }

    @FXML
    private void handleManageUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/ManageUser/UserManagement_fixed.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Quản lý người dùng");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) manageUserButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateInventory() {
       
       
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/UpdateStore/ProductTypeSelectionView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cập nhật kho");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) updateInventoryButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 