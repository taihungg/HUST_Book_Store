package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class OrderDetailController {

    @FXML
    private TextField orderIdField;

    @FXML
    private TextField userIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField productIdField;

    @FXML
    private TextField billField;

    @FXML
    private TextField timeField;

    @FXML
    private TextField dateField;

    @FXML
    private TextField statusField;

    @FXML
    private TextField searchOrderIdField;

    // Phương thức để nhận dữ liệu từ OrderManagement
    public void setOrderId(String orderId) {
        orderIdField.setText(orderId);
    }

    @FXML
    private void handleSearchOrder(ActionEvent event) {
        String id = searchOrderIdField.getText();
        System.out.println("Search for Order ID in detail: " + id);
        // Có thể thêm logic truy xuất DB để load chi tiết vào các field
    }

    @FXML
    private void handleApprove(ActionEvent event) {
        System.out.println("Order approved.");
    }

    @FXML
    private void handleReject(ActionEvent event) {
        System.out.println("Order rejected.");
    }

    @FXML
    private void handleExit(ActionEvent event) {
        ((Stage) orderIdField.getScene().getWindow()).close();
    }
}

