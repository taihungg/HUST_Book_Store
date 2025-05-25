package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class OrderDetailController {

    @FXML
    private Label orderIdLabel; // Đây là label dùng để hiển thị ID truyền từ giao diện trước

    public void setOrderId(String orderId) {
        orderIdLabel.setText(orderId);
    }
}
