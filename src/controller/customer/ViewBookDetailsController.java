package controller.customer;

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
    @FXML private Button notifyButton;

    private CustomerMainController mainController;
    private BrowseProductsController.Product product;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (quantitySpinner != null) {
            quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
        } else {
            System.err.println("quantitySpinner is null, check FXML configuration.");
        }
        if (addToCartButton != null) {
            addToCartButton.setOnAction(e -> handleAddToCart());
        } else {
            System.err.println("addToCartButton is null, check FXML configuration.");
        }
        if (backButton != null) {
            backButton.setOnAction(e -> handleBackToProducts());
        } else {
            System.err.println("backButton is null, check FXML configuration.");
        }
        if (readDemoButton != null) {
            readDemoButton.setOnAction(e -> handleReadDemo());
        } else {
            System.err.println("readDemoButton is null, check FXML configuration.");
        }
        if (notifyButton != null) {
            notifyButton.setOnAction(e -> handleNotify());
        } else {
            System.err.println("notifyButton is null, check FXML configuration.");
        }
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
            if (titleLabel != null) titleLabel.setText(product.getTitle());
            if (authorLabel != null) authorLabel.setText("by " + product.getAuthor());
            if (categoryLabel != null) categoryLabel.setText(product.getCategory());
            if (priceLabel != null) priceLabel.setText(String.format("%,d VNĐ", product.getPrice()));
            if (bookImage != null) {
                try {
                    Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
                    bookImage.setImage(image.isError() ? getPlaceholderImage() : image);
                } catch (Exception e) {
                    System.err.println("Lỗi khi tải hình ảnh: " + product.getImagePath() + " - " + e.getMessage());
                    bookImage.setImage(getPlaceholderImage());
                }
            }
        } else {
            if (titleLabel != null) titleLabel.setText("Không có sản phẩm");
            if (authorLabel != null) authorLabel.setText("");
            if (categoryLabel != null) categoryLabel.setText("");
            if (priceLabel != null) priceLabel.setText("");
            if (descriptionArea != null) descriptionArea.setText("");
            if (bookImage != null) bookImage.setImage(getPlaceholderImage());
            System.err.println("Product is null, displaying default UI.");
        }
    }

    private void handleAddToCart() {
        if (mainController == null || !mainController.isUserLoggedIn()) {
            showAlert("Thông báo", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return;
        }
        if (product == null) {
            showAlert("Lỗi", "Không có sản phẩm để thêm vào giỏ hàng!");
            return;
        }

        int quantity = quantitySpinner != null ? quantitySpinner.getValue() : 1;
        ObservableList<BrowseProductsController.CartItem> cartItems = mainController.getCartItems();
        BrowseProductsController.CartItem existingItem = cartItems.stream()
            .filter(item -> item.getProduct().equals(product))
            .findFirst()
            .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            cartItems.add(new BrowseProductsController.CartItem(product, quantity));
        }

        if (quantitySpinner != null) {
            quantitySpinner.getValueFactory().setValue(1); // Reset spinner
        }
        showAlert("Thành công", "Đã thêm " + quantity + " '" + product.getTitle() + "' vào giỏ hàng!");
    }

    private void handleBackToProducts() {
        if (mainController == null) {
            showAlert("Lỗi", "Không thể quay lại trang sản phẩm vì mainController chưa được thiết lập!");
            return;
        }
        mainController.loadPageWithData("/view/customer/Store/BrowseProducts.fxml", null);
    }

    private void handleReadDemo() {
        showAlert("Thông báo", "Đọc demo cho sản phẩm: " + (product != null ? product.getTitle() : "Unknown"));
    }

    private void handleNotify() {
        showAlert("Thông báo", "Bạn sẽ được thông báo khi sản phẩm '" + (product != null ? product.getTitle() : "Unknown") + "' có sẵn!");
    }

    private Image getPlaceholderImage() {
        try {
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            System.err.println("Không tìm thấy placeholder.png, trả về ảnh trống.");
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}