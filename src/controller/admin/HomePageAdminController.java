package controller.admin;

import controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import model.user.User; // Assuming you use this, though Main.currentUser is directly used

import java.io.IOException;
import java.net.URL;

public class HomePageAdminController {
    @FXML private Button reportButton;
    @FXML private Button logoutButton;
    @FXML private Button seeOrdersButton;
    @FXML private Button manageButton;
    @FXML private Button viewStoreButton; // Assuming you have an FXML Button with fx:id="viewStoreButton"

    // currentUser is taken directly from Main.currentUser
    // private User currentUser = Main.currentUser;

    private Stage primaryStage; // This will hold the main stage for the admin interface

    /**
     * Call this method from the code that loads HomePageAdmin.fxml to set the primary stage.
     * @param stage The primary stage of the admin interface.
     */
    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void initialize() {
        // Initialization logic if needed.
        // For example, to ensure currentUser is available:
        // if (Main.currentUser == null) {
        //     showAlert("Lỗi Người Dùng", "Không có thông tin người dùng quản trị.");
        //     // Potentially disable buttons or navigate back to login
        // }
    }

    /**
     * Loads a new FXML content into the scene of the primaryStage.
     * @param fxmlPath The path to the FXML file (relative to resources).
     * @param title The new title for the stage.
     */
    private void loadSceneContent(String fxmlPath, String title) {
        if (primaryStage == null) {
            // This could happen if setStage was not called, or if trying to get stage from a button that's no longer on scene.
            // As a fallback, try to get the stage from a known button on the current scene.
            // This is less ideal than having primaryStage set correctly initially.
            if (manageButton != null && manageButton.getScene() != null && manageButton.getScene().getWindow() != null) {
                 primaryStage = (Stage) manageButton.getScene().getWindow();
            } else {
                showAlert("Lỗi Hệ Thống", "Không thể thay đổi màn hình do lỗi cấu hình (primaryStage is null).");
                System.err.println("Error: Primary stage is null. Cannot load new scene content.");
                return;
            }
        }

        try {
            URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new IOException("Không tìm thấy file FXML: " + fxmlPath);
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            // If the new controller needs initialization (e.g., passing AppServiceManager or currentUser)
            // you would get the controller instance here and call its methods:
            // Object newController = loader.getController();
            // if (newController instanceof SomeAdminFeatureController) {
            //     ((SomeAdminFeatureController) newController).initData(Main.appServiceManager, Main.currentUser);
            // }
            
            Scene scene = new Scene(root);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            // Optional: If you want the stage to resize to the new scene's preferred size
             primaryStage.sizeToScene();
             //primaryStage.centerOnScreen(); // Optional
            if (!primaryStage.isShowing()) { // Ensure stage is visible
                primaryStage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi Tải Giao Diện", "Không thể tải giao diện: " + fxmlPath + "\nLỗi: " + e.getMessage());
        }
    }

    @FXML
    private void handleReportButton() {
        loadSceneContent("/view/admin/Report/RevenueReportView.fxml", "Báo Cáo Doanh Thu");
    }

    @FXML
    private void handleLogout() {
        // Logout closes the current admin stage and opens a new Login stage.
        if (primaryStage != null) {
            primaryStage.close();
        } else if (logoutButton != null && logoutButton.getScene() != null && logoutButton.getScene().getWindow() != null) {
            // Fallback if primaryStage wasn't set, try to get from the button itself
            Stage stageFromButton = (Stage) logoutButton.getScene().getWindow();
            stageFromButton.close();
        } else {
            System.err.println("Could not determine current stage to close for logout.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Lỗi Đăng Xuất", "Không thể mở màn hình đăng nhập.");
        }
    }

    @FXML
    private void handleSeeOrders() {
        loadSceneContent("/view/admin/SeeOrders/OrderManagement.fxml", "Xem Đơn Hàng");
    }

    @FXML
    private void handleManage() {
        loadSceneContent("/view/admin/Manage/ManagePageView.fxml", "Quản Lý");
    }

    @FXML
    private void handleViewStoreButton() {
        // Ensure you have a Button with fx:id="viewStoreButton" in your HomePageAdmin.fxml
        loadSceneContent("/view/admin/ViewStore.fxml", "Xem Kho Hàng");
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}