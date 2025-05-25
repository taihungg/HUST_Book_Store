package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class OrderManagementController {

    @FXML
    private TextField orderIdField;

    @FXML
    private void handleViewDetail(ActionEvent event) {
        try {
            // Load FXML của OrderDetail
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OrderDetail_fixed.fxml"));
            Parent root = loader.load();

            // Lấy controller để truyền dữ liệu
            OrderDetailController controller = loader.getController();
            String orderId = orderIdField.getText();
            controller.setOrderId(orderId);

            // Đổi scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Order Detail");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
