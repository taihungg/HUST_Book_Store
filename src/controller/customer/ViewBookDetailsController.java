package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewBookDetailsController implements Initializable, SubController {

    @FXML private ImageView bookImage;
    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label categoryLabel;
    @FXML private Label priceLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addToCartButton;
    @FXML private Button backButton;
    @FXML private Button readDemoButton;

    private CustomerMainController mainController;
    private BrowseProductsController.Product product;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo spinner với giá trị mặc định 1, min 1, max 99
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
    }

    @Override
    public void setMainController(CustomerMainController mainController) {
        this.mainController = mainController;
    }

    public void setProductData(BrowseProductsController.Product product) {
        this.product = product;
        updateUI();
    }

    private void updateUI() {
        if (product != null) {
            titleLabel.setText(product.getTitle());
            authorLabel.setText(product.getAuthor());
            categoryLabel.setText(product.getCategory());
            priceLabel.setText(String.format("%.2f USD", product.getPrice()));
            descriptionArea.setText("Description of " + product.getTitle());

            try {
                Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
                bookImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Lỗi khi tải hình ảnh: " + e.getMessage());
                bookImage.setImage(new Image(getClass().getResourceAsStream("/images/placeholder.jpg")));
            }
        }
    }

    @FXML
    private void handleBackToProducts() {
        if (mainController == null) {
            showAlert("Lỗi", "Không thể quay lại trang sản phẩm vì mainController chưa được thiết lập!");
            return;
        }
        // Sửa lỗi: Thay loadContentView bằng loadPageWithData
        mainController.loadPageWithData("/view/customer/Store/BrowseProducts.fxml", null);
    }

    @FXML
    private void handleAddToCart() {
        if (mainController == null || !mainController.isUserLoggedIn()) {
            showAlert("Thông báo", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return;
        }
        if (product == null) {
            showAlert("Lỗi", "Không có sản phẩm để thêm vào giỏ hàng!");
            return;
        }

        int quantity = quantitySpinner.getValue();
        BrowseProductsController.CartItem cartItem = new BrowseProductsController.CartItem(product, quantity);

        ObservableList<BrowseProductsController.CartItem> cartItems = FXCollections.observableArrayList();
        cartItems.add(cartItem);

        mainController.loadPageWithData("/view/customer/Store/SeeCart.fxml", cartItems);
    }

    @FXML
    private void handleReadDemo() {
        System.out.println("Đọc demo cho sản phẩm: " + (product != null ? product.getTitle() : "Unknown"));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}