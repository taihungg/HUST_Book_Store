package controller.admin;

import controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.user.User;
import model.user.manager.Admin;

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

    public static Stage mainStage = new Stage();
    


  
    @FXML
    public void initialize() {
        mainStage.setTitle("AdminPage");   
     }

    @FXML
    private void handleReportButton() {
        try {
            // Load the RevenueReportView.fxml
            if(currentUser instanceof Admin) {
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/admin/Report/RevenueReportView.fxml"));
            Parent root = loader1.load();
            
            // Create a new stage for the revenue report
            mainStage.setScene(new Scene(root));
            
            // Show the revenue report window
            mainStage.show();

            Stage curreStage = (Stage)reportButton.getScene().getWindow();
            curreStage.close();
            }
            else {
                System.out.println("You are not authorized to access this page");
                Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Only Admin accesible");
                    alert.setContentText("You are not authorized to access this page");
                    alert.showAndWait();
            }

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
            mainStage.setScene(new Scene(root));
            
            // Show the see orders window
            mainStage.show();

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
            mainStage.setScene(new Scene(root));
            
            // Show the see orders window
            mainStage.show();

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

            mainStage.setScene(new Scene(root));
            
            // Show the see orders window
            mainStage.show();
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
            mainStage.setScene(new Scene(root));
            
            // Show the see orders window
            mainStage.show();

            Stage curreStage = (Stage)seeOrdersButton.getScene().getWindow();
            curreStage.close();

            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
}

}
