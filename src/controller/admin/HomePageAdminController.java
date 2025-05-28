package controller.admin;

import controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.user.User;

public class HomePageAdminController {
    @FXML
    private Button reportButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Button seeOrdersButton;
    
    @FXML
    private Button manageButton;


    private User currentUser = Main.currentUser;
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

            Stage curreStage = (Stage)reportButton.getScene().getWindow();
            curreStage.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {

        try {
            // Load the SeeOrdersView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the see orders page
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            
            // Show the see orders window
            stage.show();

            Stage curreStage = (Stage)logoutButton.getScene().getWindow();
            curreStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSeeOrders() {
        try {
            // Load the SeeOrdersView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/SeeOrders/OrderManagement.fxml"));
            Parent root = loader.load();
            
            // Create a new stage for the see orders page
            Stage stage = new Stage();
            stage.setTitle("Xem Đơn Hàng");
            stage.setScene(new Scene(root));
            
            // Show the see orders window
            stage.show();

            Stage curreStage = (Stage)seeOrdersButton.getScene().getWindow();
            curreStage.close();
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
            Stage curreStage = (Stage)seeOrdersButton.getScene().getWindow();
            curreStage.close();
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

            Stage curreStage = (Stage)seeOrdersButton.getScene().getWindow();
            curreStage.close();

            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
}

}
