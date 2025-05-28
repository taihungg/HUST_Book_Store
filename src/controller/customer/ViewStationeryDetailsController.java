package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.manager.AppServiceManager;
import model.product.Product;
import model.user.User;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewStationeryDetailsController implements Initializable {

    @FXML private ImageView bookImage;
    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label ageLabel;
    @FXML private Label brandLabel;
    @FXML private Label typeLabel;
    @FXML private Label statusLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addToCartButton;
    @FXML private Button backButton;

    private AppServiceManager appServiceManager;
    private User currentUser;
    private Product currentProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
        addToCartButton.setOnAction(e -> handleAddToCart());
        backButton.setOnAction(e -> handleBackToProducts());
    }

    public void setAppServiceManager(AppServiceManager appServiceManager) {
        this.appServiceManager = appServiceManager;
        this.currentUser = appServiceManager.getCurrentUser();
    }

    public void setProductId(String productId) {
        if (appServiceManager == null) {
            System.err.println("AppServiceManager chưa được khởi tạo");
            return;
        }
        
        currentProduct = appServiceManager.getProductManager().getProductById(productId);
        if (currentProduct != null) {
            updateUI();
        }
    }

    private void updateUI() {
        if (currentProduct == null) return;

        // Cập nhật thông tin cơ bản
        titleLabel.setText(currentProduct.getTitle());
        authorLabel.setText(currentProduct.getAuthor());
        ageLabel.setText(currentProduct.getAgeRange());
        brandLabel.setText(currentProduct.getBrand());
        typeLabel.setText(currentProduct.getType());
        descriptionArea.setText(currentProduct.getDescription());

        // Cập nhật hình ảnh
        try {
            Image image = new Image(getClass().getResourceAsStream(currentProduct.getGalleryURL()));
            bookImage.setImage(image.isError() ? getPlaceholderImage() : image);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải hình ảnh: " + currentProduct.getGalleryURL());
            bookImage.setImage(getPlaceholderImage());
        }

        // Cập nhật trạng thái và nút thêm vào giỏ hàng
        updateProductStatus();
    }

    private void updateProductStatus() {
        if (currentProduct == null) return;

        int stock = appServiceManager.getProductManager().getProductQuantity(currentProduct.getId());
        if (stock > 0) {
            statusLabel.setText("In Stock (" + stock + " items)");
            addToCartButton.setVisible(true);
        } else {
            statusLabel.setText("Out of Stock");
            addToCartButton.setVisible(false);
        }
    }

    private void handleAddToCart() {
        if (currentUser == null) {
            showAlert("Notification", "Please log in to add products to cart!");
            return;
        }

        if (currentProduct == null) {
            showAlert("Error", "No product to add to cart!");
            return;
        }

        try {
            int quantity = quantitySpinner.getValue();
            appServiceManager.getOrderManager().addToCart(currentUser, currentProduct.getId(), quantity);
            showAlert("Success", "Added " + quantity + " '" + currentProduct.getTitle() + "' to cart!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add to cart: " + e.getMessage());
        }
    }

    private void handleBackToProducts() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/view/customer/Store/BrowseProducts.fxml"));
            javafx.scene.Parent view = loader.load();
            HomePageController controller = loader.getController();
            controller.setAppServiceManager(appServiceManager);
            
            javafx.scene.Scene scene = view.getScene();
            if (scene != null) {
                scene.setRoot(view);
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to return to products: " + e.getMessage());
        }
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