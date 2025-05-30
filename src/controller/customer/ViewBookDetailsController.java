package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.product.Product;
import model.product.book.Book;
import model.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import model.user.cart.Cart;
import model.user.customer.Customer;

public class ViewBookDetailsController implements Initializable {

    @FXML private ImageView bookImage;
    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label categoryLabel;
    @FXML private Label priceLabel;
    @FXML private Label statusLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addToCartButton;
    @FXML private Button backButton;
    @FXML private Button readDemoButton;

    private AppServiceManager appServiceManager;
    private User currentUser;
    private Book currentProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
        
    }

    public void setAppServiceManager(AppServiceManager appServiceManager) {
        this.appServiceManager = appServiceManager;
        this.currentUser = appServiceManager.getCurrentUser();
    }

    

    public void updateUIforBook(Book book) {

        currentProduct = book;
        titleLabel.setText(currentProduct.getTitle());
        authorLabel.setText("by " + currentProduct.getAuthor());
        categoryLabel.setText(currentProduct.getCategory());
        priceLabel.setText(String.format("%.2f USD", currentProduct.getSellingPrice()));
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
        int stock = appServiceManager.getProductManager().getProductQuantity(currentProduct.getId());
        if (stock > 0) {
            statusLabel.setText("In Stock (" + stock + " items)");
            addToCartButton.setVisible(true);
            quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, stock, 1));
        } else {
            statusLabel.setText("Out of Stock");
            addToCartButton.setVisible(false);
        }
    }

   
    @FXML
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
            Customer customer = (Customer) appServiceManager.getCurrentUser();
            customer.getCart().addItem(currentProduct.getId(), quantity, appServiceManager.getProductManager());
            showAlert("Success", "Added " + quantity + " '" + currentProduct.getTitle() + "' to cart!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add to cart: " + e.getMessage());
        }
    }
    @FXML
    private void handleBackToProducts() {
      

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();
       
        
    }
    @FXML
    private void handleReadDemo() {
        if (currentProduct == null) return;
        showAlert("Notification", "Reading demo for product: " + currentProduct.getTitle());
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