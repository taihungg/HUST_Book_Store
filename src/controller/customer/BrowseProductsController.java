package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import model.manager.AppServiceManager;
import model.product.Product;
import model.product.interfaces.PhysicalProduct;
import model.user.User;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BrowseProductsController implements Initializable {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private GridPane productsGrid;
    @FXML private ScrollPane productScrollPane;
    @FXML private ToggleGroup sortGroup;
    @FXML private CheckBox fictionCheckBox;
    @FXML private CheckBox nonFictionCheckBox;
    @FXML private CheckBox scienceCheckBox;
    @FXML private CheckBox historyCheckBox;
    @FXML private CheckBox programmingCheckBox;
    @FXML private Label itemCountLabel;

    private AppServiceManager appServiceManager;
    private User currentUser;
    private ObservableList<Product> displayedProducts;
    private ObservableList<Product> filteredProducts;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayedProducts = FXCollections.observableArrayList();
        filteredProducts = FXCollections.observableArrayList();
        setupEventHandlers();
    }

    public void setAppServiceManager(AppServiceManager appServiceManager) {
        this.appServiceManager = appServiceManager;
        this.currentUser = appServiceManager.getCurrentUser();
        loadInStockProducts();
    }

    private void loadInStockProducts() {
        if (appServiceManager == null || currentUser == null) {
            System.err.println("AppServiceManager or currentUser is not initialized.");
            return;
        }
        
        displayedProducts.clear();
        displayedProducts.addAll(appServiceManager.getProductManager().getAllProducts(currentUser));
        applyAllFiltersAndSort();
    }

    private void setupEventHandlers() {
        // Search field enter key handler
        if (searchField != null) {
            searchField.setOnKeyPressed(e -> {
                if (e.getCode().toString().equals("ENTER")) {
                    handleSearch();
                }
            });
        }

        // Category checkbox handlers
        setupCategoryCheckbox(fictionCheckBox, "fiction");
        setupCategoryCheckbox(nonFictionCheckBox, "non-fiction");
        setupCategoryCheckbox(scienceCheckBox, "science");
        setupCategoryCheckbox(historyCheckBox, "history");
        setupCategoryCheckbox(programmingCheckBox, "programming");

        // Sort group handler
        if (sortGroup != null) {
            sortGroup.selectedToggleProperty().addListener((_, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    handleSort();
                }
            });
        }
    }

    private void setupCategoryCheckbox(CheckBox checkBox, String category) {
        if (checkBox != null) {
            checkBox.setOnAction(_ -> {
                if (checkBox.isSelected()) {
                    filteredProducts.removeIf(product -> !product.getTitle().toLowerCase().contains(category));
                } else {
                    loadInStockProducts();
                }
                updateProductDisplay(filteredProducts);
            });
        }
    }

    @FXML
    private void handleSearch() {
        applyAllFiltersAndSort();
    }

    @FXML
    private void handlePriceFilter() {
        try {
            double minPrice = minPriceField.getText().isEmpty() ? 0 : Double.parseDouble(minPriceField.getText().trim());
            double maxPrice = maxPriceField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceField.getText().trim());
            
            if (minPrice > maxPrice) {
                showAlert("Error", "Minimum price cannot be greater than maximum price!");
                return;
            }
            applyAllFiltersAndSort();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for price!");
        }
    }

    @FXML
    private void handleSort() {
        if (sortGroup.getSelectedToggle() == null) return;
        
        RadioButton selectedSort = (RadioButton) sortGroup.getSelectedToggle();
        String sortType = selectedSort.getText();
        
        FXCollections.sort(filteredProducts, (p1, p2) -> {
            switch (sortType) {
                case "Sort by Title":
                    return p1.getTitle().compareTo(p2.getTitle());
                case "Sort by Cost":
                    return Double.compare(p1.getSellingPrice(), p2.getSellingPrice());
                default:
                    return 0;
            }
        });
        
        updateProductDisplay(filteredProducts);
    }

    private void applyAllFiltersAndSort() {
        if (displayedProducts == null) return;
        
        filteredProducts.clear();
        filteredProducts.addAll(displayedProducts);
        
        // Apply search filter
        String searchQuery = searchField.getText().trim().toLowerCase();
        if (!searchQuery.isEmpty()) {
            filteredProducts.removeIf(product -> 
                !product.getTitle().toLowerCase().contains(searchQuery) &&
                !product.getId().toLowerCase().contains(searchQuery)
            );
        }
        
        // Apply price filter
        try {
            double minPrice = minPriceField.getText().isEmpty() ? 0 : Double.parseDouble(minPriceField.getText().trim());
            double maxPrice = maxPriceField.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceField.getText().trim());
            if (minPrice <= maxPrice) {
                filteredProducts.removeIf(product -> 
                    product.getSellingPrice() < minPrice || product.getSellingPrice() > maxPrice
                );
            }
        } catch (NumberFormatException e) {
            // Ignore invalid price values
        }
        
        // Apply sorting
        handleSort();
        
        // Update display
        updateProductDisplay(filteredProducts);
        updateItemCount();
    }

    private void updateItemCount() {
        if (itemCountLabel != null) {
            itemCountLabel.setText(String.valueOf(filteredProducts.size()));
        }
    }

    private void updateProductDisplay(List<Product> products) {
        if (productsGrid == null) return;
        
        productsGrid.getChildren().clear();
        int row = 0;
        int col = 0;
        final int maxCols = 3;
        
        for (Product product : products) {
            VBox productCard = createProductCard(product);
            productsGrid.add(productCard, col, row);
            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-radius: 5;");
        
        // Product Image
        ImageView imageView = new ImageView();
        imageView.setFitHeight(150);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        try {
            Image image = new Image(getClass().getResourceAsStream(product.getGalleryURL()));
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(getPlaceholderImage());
        }
        
        // Product Title
        Label titleLabel = new Label(product.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-weight: bold;");
        
        // Product Price
        Label priceLabel = new Label(String.format("$%.2f", product.getSellingPrice()));
        priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
        
        // Add to Cart Button
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setOnAction(e -> handleAddToCart(product));
        
        // View Details Button
        Button viewDetailsBtn = new Button("View Details");
        viewDetailsBtn.setOnAction(e -> handleViewProductDetails(product));
        
        card.getChildren().addAll(imageView, titleLabel, priceLabel, addToCartBtn, viewDetailsBtn);
        return card;
    }

    private Image getPlaceholderImage() {
        return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
    }

    private void handleAddToCart(Product product) {
        if (currentUser.getUsername().equals("guest")) {
            showAlert("Login Required", "Please log in to add products to cart!");
            return;
        }
        
        if (product instanceof PhysicalProduct) {
            int stock = appServiceManager.getProductManager().getProductQuantity(product.getId());
            if (stock <= 0) {
                showAlert("Out of Stock", "This product is currently out of stock!");
                return;
            }
        }
        
        try {
            appServiceManager.getOrderManager().addToCart(currentUser, product.getId(), 1);
            showAlert("Success", "Product added to cart!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add product to cart: " + e.getMessage());
        }
    }

    private void handleViewProductDetails(Product product) {
        // TODO: Implement product details view
        showAlert("Product Details", "Viewing details for: " + product.getTitle());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}