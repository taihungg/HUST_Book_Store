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
            String fxmlPath = "/view/admin/Manage/ManagePageView.fxml"; // Đường dẫn tuyệt đối từ gốc classpath
            java.net.URL location = getClass().getResource(fxmlPath);
        
            if (location == null) {
                System.err.println("KHÔNG TÌM THẤY FXML TẠI: " + fxmlPath);
                // Có thể thử với ClassLoader khác để kiểm tra
                location = HomePageAdminController.class.getClassLoader().getResource("view/admin/Manage/ManagePageView.fxml"); // Bỏ dấu / ở đầu nếu dùng getClassLoader().getResource() và đường dẫn là từ gốc
                if (location == null) {
                    System.err.println("Cũng KHÔNG TÌM THẤY FXML bằng ClassLoader tại: " + "view/admin/Manage/ManagePageView.fxml");
                    return; // Thoát nếu không tìm thấy
                } else {
                    System.out.println("Đã tìm thấy FXML bằng ClassLoader tại: " + location);
                }
            } else {
                System.out.println("Đã tìm thấy FXML tại: " + location);
            }
        
            FXMLLoader loader = new FXMLLoader(location); // Sử dụng URL đã kiểm tra
            Parent root = loader.load();
        
            Stage stage = new Stage();
            stage.setTitle("Manage Page");
            stage.setScene(new Scene(root));
            stage.show();
        
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình tải ManagePageView.fxml:");
            e.printStackTrace();
        }
    } 
}
