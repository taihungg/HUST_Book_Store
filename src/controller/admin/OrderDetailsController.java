package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.lang.classfile.Label;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import model.order.Order;
import model.user.cart.CartItem;
import model.manager.order.OrderManager;


public class OrderDetailsController {

    @FXML
    private Label customerUsernameLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Label orderDateLabel;

    @FXML
    private Label orderIdLabel;

    @FXML
    private ListView<CartItem> orderItemsListView;

    @FXML
    private Label orderStatusLabel;

    @FXML
    private Label paymentMethodLabel;

    @FXML
    private Label shippingAddressLabel;

    @FXML
    private Label totalAmountLabel;

    // Phương thức để nhận dữ liệu từ OrderManagement
    public void setOrderId(Order order) {
            
            
            

    }

    @FXML
    private void handleSearchOrder(ActionEvent event) {
        
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
        ((Stage) exitButton.getScene().getWindow()).close();
    }
}

