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

            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/admin/Report/RevenueReportView.fxml"));
            Parent root = loader1.load();
            
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
        try {
            // Load the SeeOrdersView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/SeeOrders/SeeOrdersView.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the see orders page
            Stage stage = new Stage();
            stage.setTitle("Xem Đơn Hàng");
            stage.setScene(new Scene(root));
            
            // Show the see orders window
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManage() {
        
        try {
            // Load the SeeOrdersView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/ManagePageView.fxml"));
            Parent root = loader.load();     

            Stage stage = new Stage();
            stage.setTitle("Xem Đơn Hàng");
            stage.setScene(new Scene(root));
            
            // Show the see orders window
            stage.show();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    } 

    @FXML 
    private void  handleViewStoreButton () {
        try {
         
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/ViewStore.fxml"));
            Parent root = loader.load();
            
            
            // Create a new stage for the see orders page
            Stage stage = new Stage();
            stage.setTitle("Xem Kho hàng");
            stage.setScene(new Scene(root));
            
            // Show the see orders window
            stage.show();

            StoreController storeController = loader.getController();
            if (storeController != null) {
                System.out.println("HomePageAdminController: Đã lấy được activeStoreController.");
            } 
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
}

}
