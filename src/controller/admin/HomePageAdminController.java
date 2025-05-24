package controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomePageAdminController {
    @FXML
    private Button reportButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button seeOrdersButton;
    
    @FXML
    private Button manageButton;

    @FXML
    public void initialize() {
        // Initialize any necessary setup
    }

    @FXML
    private void handleReportButton() {
        try {
            // Load the RevenueReportView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/view/admin/Report/RevenueReportView.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the revenue report
            Stage stage = new Stage();
            stage.setTitle("Báo Cáo Doanh Thu");
            stage.setScene(new Scene(root));
            
            // Show the revenue report window
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSeeOrders() {
       
    }

    @FXML
    private void handleManage() {
        try {
            // Load the ManagePageView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/view/admin/Manage/ManagePageView.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the manage page
            Stage stage = new Stage();
            stage.setTitle("Manage Page");
            stage.setScene(new Scene(root));
            
            // Show the manage page window
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 