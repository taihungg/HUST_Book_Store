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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/view/admin/RevenueReportView.fxml"));
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
        // Implement logout functionality
    }

    @FXML
    private void handleSeeOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/view/admin/OrderDetails.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Order Details");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManage() {
        // Implement manage functionality
    }
} 