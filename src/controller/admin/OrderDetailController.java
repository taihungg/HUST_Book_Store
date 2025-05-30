package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label; // <<--- Đảm bảo là dòng này
import controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import model.order.Order;
import model.product.Product;
import model.user.cart.CartItem;
import model.manager.AppServiceManager;
import model.manager.order.OrderManager;


public class OrderDetailController {

    @FXML
    private Label customerUsernameLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Label orderDateLabel;

    @FXML
    private Label orderIdLabel;

    @FXML
    private ListView<String> orderItemsListView;

    @FXML
    private Label orderStatusLabel;

    @FXML
    private Label paymentMethodLabel;

    @FXML
    private Label shippingAddressLabel;

    @FXML
    private Label totalAmountLabel;

    private AppServiceManager appServiceManager = Main.appServiceManager;

    // Phương thức để nhận dữ liệu từ OrderManagement
    public void setOrder(Order order) {
        orderIdLabel.setText(order.getOrderId());
        orderDateLabel.setText(order.getOrderDate().toString());
        orderStatusLabel.setText(order.getOrderStatus());
        totalAmountLabel.setText(String.valueOf(order.getTotalAmount()));
        shippingAddressLabel.setText(order.getShippingAddress());
        paymentMethodLabel.setText(order.getPaymentMethod());
        customerUsernameLabel.setText(order.getCustomerUsername());
        
        if (order != null && order.getOrderItems() != null && appServiceManager != null && appServiceManager.getProductManager() != null) {
            ObservableList<String> productDisplayList = FXCollections.observableArrayList();
            for (CartItem item : order.getOrderItems()) {
                Product product = appServiceManager.getProductManager().getProductById(item.getProductId());
                if (product != null) {
                    // Chỉ lấy title:
                    productDisplayList.add(product.getTitle());
        
                    // Hoặc tạo chuỗi hiển thị phức tạp hơn:
                    // productDisplayList.add(String.format("%s - Số lượng: %d", product.getTitle(), item.getQuantity()));
                } else {
                    productDisplayList.add("Sản phẩm không tìm thấy (ID: " + item.getProductId() + ")");
                }
            }
            orderItemsListView.setItems(productDisplayList);
        } else {
            orderItemsListView.setItems(FXCollections.emptyObservableList()); // Xử lý trường hợp null
        }
            
            

    }

    @FXML
    private void handleSearchOrder(ActionEvent event) {
        System.out.println("Search order.");
        
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

