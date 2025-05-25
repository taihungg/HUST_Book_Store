package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OrderManagementController {

    @FXML
    private TextField inputOrderId;

    @FXML
    private TextField searchField;

    // Bấm nút "View"
    @FXML
    private void handleViewDetail(ActionEvent event) {
        try {
            String orderId = inputOrderId.getText();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrderDetail_fixed.fxml"));
            Parent root = loader.load();

            // Truy cập controller của OrderDetail
            OrderDetailsController controller = loader.getController();
            controller.setOrderId(orderId); // truyền dữ liệu

            // Tạo và chuyển scene
            Stage stage = new Stage();
            stage.setTitle("Order Detail");
            stage.setScene(new Scene(root));
            stage.show();

            // Đóng cửa sổ hiện tại
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        System.out.println("Searching for: " + searchField.getText());
    }

    @FXML
    private void handleDisplayAll(ActionEvent event) {
        System.out.println("Display all orders.");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}


