package controller.customer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class BrowseProductsController implements SubController {

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private TextField minPriceField;

    @FXML
    private TextField maxPriceField;

    @FXML
    private ComboBox<String> viewModeComboBox;

    @FXML
    private Button notifyNotFoundButton;

    @FXML
    private CustomerMainController mainController;

    @Override
    public void setMainController(CustomerMainController mainController) {
        this.mainController = mainController;
        // Thêm logic khởi tạo nếu cần
        if (mainController.isUserLoggedIn()) {
            // Ví dụ: Chỉnh sửa giao diện khi đã đăng nhập
        }
    }

    // Thêm các phương thức xử lý sự kiện nếu cần
    @FXML
    private void handleSearch() {
        // Logic tìm kiếm
        System.out.println("Searching: " + searchField.getText());
    }

    @FXML
    private void handleNotifyNotFound() {
        // Logic thông báo không tìm thấy
        System.out.println("Notify not found triggered");
    }
}