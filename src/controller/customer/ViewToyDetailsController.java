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
import model.product.Toy;
import model.user.User;
import model.user.customer.Customer;
import controller.customer.HomePageController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ViewToyDetailsController implements Initializable {

    @FXML private ImageView toyImage;
    @FXML private Label titleLabel;
    @FXML private Label ageLabel;
    @FXML private Label brandLabel;
    @FXML private Label statusLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Spinner<Integer> quantitySpinner;
    @FXML private Button addToCartButton;
    @FXML private Button backButton;
    @FXML private Label priceLabel;
    private AppServiceManager appServiceManager;
    private User currentUser;
    private Toy currentToyInView ; // Local instance variable to hold the toy being viewed

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize spinner. This is called when FXML is loaded.
        // Data for the specific toy is not yet available here.
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, 1));
    }

    public void setAppServiceManager(AppServiceManager appServiceManager) {
        this.appServiceManager = appServiceManager;
        this.currentUser = appServiceManager.getCurrentUser(); // Get current user if needed
    }

    // This method is now the primary way to set the toy and update the UI
    public void updateUIforToy(Toy toy) {
        this.currentToyInView = toy; // Store the toy for later use (e.g., in addToCart)

       

        titleLabel.setText(currentToyInView.getTitle());
        ageLabel.setText(String.valueOf(currentToyInView.getSuitableAge())); // Assuming getSuitableAge() exists
        brandLabel.setText(currentToyInView.getBrand());
        descriptionArea.setText(currentToyInView.getDescription());
        priceLabel.setText(String.valueOf(currentToyInView.getSellingPrice()));

        try {
            String imagePath = currentToyInView.getGalleryURL();
            if (imagePath == null || imagePath.isEmpty()) {
                 throw new NullPointerException("Image path is null or empty for toy: " + currentToyInView.getTitle());
            }
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            toyImage.setImage(image.isError() ? getPlaceholderImage() : image);
        } catch (Exception e) {
            System.err.println("Error loading image for toy '" + currentToyInView.getTitle() + "': " + e.getMessage());
            toyImage.setImage(getPlaceholderImage());
        }

        // Update stock status
        if (appServiceManager != null && appServiceManager.getProductManager() != null) {
            int stock = appServiceManager.getProductManager().getProductQuantity(currentToyInView.getId());
            if (stock > 0) {
                statusLabel.setText("In Stock (" + stock + " items)");
                addToCartButton.setDisable(false); // Enable button
            } else {
                statusLabel.setText("Out of Stock");
                addToCartButton.setDisable(true); // Disable button
            }
        } else {
            statusLabel.setText("Stock status unknown"); // Fallback if manager not set
            addToCartButton.setDisable(true);
        }
    }

    
    @FXML
    private void handleAddToCart() {
        if (currentUser == null) {
            showAlert("Login Required", "Please log in to add products to cart!");
            return;
        }

        if (currentToyInView == null) { // Use the local instance variable
            showAlert("Error", "No product selected to add to cart!");
            return;
        }
        
        if (!(currentUser instanceof Customer)) {
            showAlert("Access Denied", "Only customers can add products to the cart.");
            return;
        }

        try {
            int quantity = quantitySpinner.getValue();
            Customer customer = (Customer) currentUser; // currentUser should be set via setAppServiceManager
            
            if (customer.getCart() == null) {
                 showAlert("Error", "Shopping cart not found. Please try again.");
                 return;
            }

            // Check stock again before adding
            int stock = appServiceManager.getProductManager().getProductQuantity(currentToyInView.getId());
            if (quantity > stock) {
                showAlert("Stock Issue", "Not enough stock available. Only " + stock + " items left.");
                return;
            }
            
            boolean success = customer.getCart().addItem(currentToyInView.getId(), quantity, appServiceManager.getProductManager());
            if (success) {
                showAlert("Success", "Added " + quantity + " '" + currentToyInView.getTitle() + "' to cart!");
                // Optionally update stock display after adding to cart
                updateUIforToy(currentToyInView); // Re-update UI to reflect new stock
            } else {
                 showAlert("Failed", "Could not add item to cart. Please try again.");
            }

        } catch (NullPointerException npe) {
            showAlert("Error", "A required component is missing: " + npe.getMessage());
            npe.printStackTrace();
        }
        catch (Exception e) {
            showAlert("Error", "Failed to add to cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack() {
       // Close the current details stage
        Stage currentStage = (Stage) backButton.getScene().getWindow();
        currentStage.close();
        // No need to reload HomePage.fxml here, as it's already open and was just modal-blocked.
    }

    private Image getPlaceholderImage() {
        try {
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            System.err.println("Placeholder image '/images/placeholder.png' not found. Using blank image.");
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