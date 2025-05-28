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
import model.product.Stationery;
import model.user.User;
import model.user.customer.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewStationeryDetailsController implements Initializable {

    @FXML
    private Button addToCartButton;

  

    
    @FXML
    private Button backButton;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label brandLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button notifyButton1;

    @FXML
    private Label priceLabel;

    @FXML
    private Spinner<Integer> quantitySpinner;

    @FXML
    private Label statusLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label typeLabel;

    private AppServiceManager appServiceManager;
    private User currentUser;
    private Stationery currentProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
    }

    public void setAppServiceManager(AppServiceManager appServiceManager) {
        this.appServiceManager = appServiceManager;
        this.currentUser = appServiceManager.getCurrentUser();
    }

    

    public void updateUIforStationery(Stationery stationery) {
     
        currentProduct = stationery;

        titleLabel.setText(currentProduct.getTitle());
        brandLabel.setText(currentProduct.getBrand());
        typeLabel.setText(currentProduct.getType());
        descriptionArea.setText(currentProduct.getDescription());
        priceLabel.setText(String.valueOf(currentProduct.getSellingPrice()));
        statusLabel.setText(currentProduct.getStatus());

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
        } else {
            statusLabel.setText("Out of Stock");
            addToCartButton.setVisible(false);
        }    }

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