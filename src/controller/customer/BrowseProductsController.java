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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

interface SubController {
    void setMainController(HomePageController mainController);
}

public class BrowseProductsController implements SubController, Initializable {

    //region FXML Components
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
    //end region

    //region Trạng thái nội bộ
    private HomePageController mainController;
    private AppServiceManager appServiceManager;
    private ObservableList<Product> displayedProducts;
    private ObservableList<Product> filteredProducts;
    //end region

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandlers();
        displayedProducts = FXCollections.observableArrayList();
        filteredProducts = FXCollections.observableArrayList();
        System.out.println("BrowseProductsController initialized.");
    }

    @Override
    public void setMainController(HomePageController mainController) {
        this.mainController = mainController;
        System.out.println("MainController set for BrowseProductsController.");
    }

    public void setAppServiceManager(AppServiceManager appServiceManager) {
        this.appServiceManager = appServiceManager;
        loadInStockProducts();
    }

    private void loadInStockProducts() {
        if (appServiceManager == null) {
            System.err.println("AppServiceManager is not initialized.");
            return;
        }
        
        ObservableList<Product> allProducts = appServiceManager.getProductManager().getAllProducts();
        displayedProducts.clear();
        
        for (Product product : allProducts) {
            if ("In Stock".equalsIgnoreCase(product.getStatus())) {
                displayedProducts.add(product);
            }
        }
        
        applyAllFiltersAndSort();
    }

    private void setupEventHandlers() {
        if (searchField != null) {
            searchField.setOnKeyPressed(e -> {
                if (e.getCode().toString().equals("ENTER")) {
                    handleSearch();
                }
            });
        }
        
        if (fictionCheckBox != null) {
            fictionCheckBox.setOnAction(_ -> {
                if (fictionCheckBox.isSelected()) {
                    filteredProducts.removeIf(product -> !product.getTitle().toLowerCase().contains("fiction"));
                } else {
                    loadInStockProducts(); // Tải lại danh sách gốc
                }
            });
        }
        
        if (nonFictionCheckBox != null) {
            nonFictionCheckBox.setOnAction(_ -> {
                if (nonFictionCheckBox.isSelected()) {
                    filteredProducts.removeIf(product -> !product.getTitle().toLowerCase().contains("non-fiction"));
                } else {
                    loadInStockProducts();
                }
            });
        }
        
        if (scienceCheckBox != null) {
            scienceCheckBox.setOnAction(_ -> {
                if (scienceCheckBox.isSelected()) {
                    filteredProducts.removeIf(product -> !product.getTitle().toLowerCase().contains("science"));
                } else {
                    loadInStockProducts();
                }
            });
        }
        
        if (historyCheckBox != null) {
            historyCheckBox.setOnAction(_ -> {
                if (historyCheckBox.isSelected()) {
                    filteredProducts.removeIf(product -> !product.getTitle().toLowerCase().contains("history"));
                } else {
                    loadInStockProducts();
                }
            });
        }
        
        if (programmingCheckBox != null) {
            programmingCheckBox.setOnAction(_ -> {
                if (programmingCheckBox.isSelected()) {
                    filteredProducts.removeIf(product -> !product.getTitle().toLowerCase().contains("programming"));
                } else {
                    loadInStockProducts();
                }
            });
        }

        if (sortGroup != null) {
            sortGroup.selectedToggleProperty().addListener((_, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    RadioButton selectedSort = (RadioButton) newToggle;
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
            });
        }
    }

    @FXML
    private void handleSearch() {
        applyAllFiltersAndSort();
    }

    @FXML
    private void handleCategoryFilter() {
        applyAllFiltersAndSort();
    }

    @FXML
    private void handlePriceFilter() {
        String minPriceText = minPriceField.getText().trim();
        String maxPriceText = maxPriceField.getText().trim();
        try {
            double minPrice = minPriceText.isEmpty() ? 0 : Double.parseDouble(minPriceText);
            double maxPrice = maxPriceText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceText);
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
        if (sortGroup.getSelectedToggle() == null) {
            System.out.println("No sorting criteria selected.");
            return;
        }
        applyAllFiltersAndSort();
    }

    private void applyAllFiltersAndSort() {
        if (displayedProducts == null) {
            return;
        }
        
        filteredProducts.clear();
        filteredProducts.addAll(displayedProducts);
        
        // Lọc theo tìm kiếm
        String searchQuery = searchField != null ? searchField.getText().trim().toLowerCase() : "";
        if (!searchQuery.isEmpty()) {
            filteredProducts.removeIf(product -> !(product.getTitle().toLowerCase().contains(searchQuery) ||
                                                  product.getId().toLowerCase().contains(searchQuery)));
        }
        
        // Lọc theo giá
        String minPriceText = minPriceField != null ? minPriceField.getText().trim() : "";
        String maxPriceText = maxPriceField != null ? maxPriceField.getText().trim() : "";
        try {
            double minPrice = minPriceText.isEmpty() ? 0 : Double.parseDouble(minPriceText);
            double maxPrice = maxPriceText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceText);
            if (minPrice <= maxPrice) {
                filteredProducts.removeIf(product -> !(product.getSellingPrice() >= minPrice && product.getSellingPrice() <= maxPrice));
            }
        } catch (NumberFormatException e) {
            // Bỏ qua giá trị giá không hợp lệ
        }
        
        // Sắp xếp
        if (sortGroup != null && sortGroup.getSelectedToggle() != null) {
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
        }
        
        updateProductDisplay(filteredProducts);
        if (itemCountLabel != null) {
            itemCountLabel.setText(String.valueOf(filteredProducts.size()));
        }
    }

    private void updateProductDisplay(List<Product> products) {
        if (productsGrid == null) {
            return;
        }
        
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
        VBox productCard = new VBox();
        productCard.setAlignment(javafx.geometry.Pos.CENTER);
        productCard.getStyleClass().add("product-card");
        productCard.setPadding(new Insets(10));
        productCard.setSpacing(5);
        
        // Hình ảnh
        ImageView imageView = new ImageView();
        imageView.setFitHeight(160);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        try {
            Image image = new Image(getClass().getResourceAsStream(product.getGalleryURL()));
            imageView.setImage(image.isError() ? getPlaceholderImage() : image);
        } catch (Exception e) {
            System.err.println("Error loading image: " + product.getGalleryURL() + " - " + e.getMessage());
            imageView.setImage(getPlaceholderImage());
        }
        imageView.setOnMouseClicked(e -> handleViewProductDetails(product));
        imageView.setStyle("-fx-cursor: hand;");
        
        // Tiêu đề
        Label titleLabel = new Label(product.getTitle());
        titleLabel.getStyleClass().add("product-title");
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        
        // Mô tả
        Label descriptionLabel = new Label(product.getDescription());
        descriptionLabel.getStyleClass().add("product-description");
        descriptionLabel.setWrapText(true);
        descriptionLabel.setMaxWidth(120);
        
        // Giá
        Label priceLabel = new Label(String.format("%.2f USD", product.getSellingPrice()));
        priceLabel.getStyleClass().add("product-price");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        
        // Thông tin tồn kho cho sản phẩm vật lý
        Label stockLabel = new Label();
        if (product instanceof PhysicalProduct && appServiceManager != null) {
            int quantity = appServiceManager.getProductManager().getProductQuantity(product.getId());
            stockLabel.setText("Stock: " + quantity);
            stockLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");
        }
        
        // Nút thêm vào giỏ hàng
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setOnAction(e -> handleAddToCart(product));
        
        // Thêm tất cả thành phần vào card
        productCard.getChildren().addAll(imageView, titleLabel, descriptionLabel, priceLabel);
        if (product instanceof PhysicalProduct) {
            productCard.getChildren().add(stockLabel);
        }
        productCard.getChildren().add(addToCartBtn);
        
        return productCard;
    }

    private Image getPlaceholderImage() {
        try {
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            System.err.println("Placeholder image not found, returning empty image.");
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
        }
    }

    private void handleAddToCart(Product product) {
        if (mainController == null || !mainController.isUserLoggedIn()) {
            showAlert("Login Required", "Please log in to add products to cart!");
            return;
        }
        
        // Kiểm tra tồn kho cho sản phẩm vật lý
        if (product instanceof PhysicalProduct && appServiceManager != null) {
            int availableStock = appServiceManager.getProductManager().getProductQuantity(product.getId());
            if (availableStock <= 0) {
                showAlert("Out of Stock", "This product is currently out of stock!");
                return;
            }
        }
        
        // Thêm vào giỏ hàng sử dụng OrderManager
        if (appServiceManager != null) {
            try {
                appServiceManager.getOrderManager().addToCart(product.getId(), 1);
                System.out.println("Added product '" + product.getTitle() + "' to cart.");
                showAlert("Success", "Added product '" + product.getTitle() + "' to cart!");
            } catch (Exception e) {
                System.err.println("Error adding to cart: " + e.getMessage());
                showAlert("Error", "Failed to add product to cart. Please try again.");
            }
        }
    }

    private void handleViewProductDetails(Product product) {
        System.out.println("Viewing product details: " + product.getTitle());
        if (mainController != null) {
            try {
                mainController.loadPageWithData("/view/customer/Store/ViewDetails/ViewBookDetails.fxml", product);
            } catch (Exception e) {
                System.err.println("Error loading product details: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void refreshPage() {
        if (searchField != null) searchField.clear();
        if (minPriceField != null) minPriceField.clear();
        if (maxPriceField != null) maxPriceField.clear();
        if (fictionCheckBox != null) fictionCheckBox.setSelected(false);
        if (nonFictionCheckBox != null) nonFictionCheckBox.setSelected(false);
        if (scienceCheckBox != null) scienceCheckBox.setSelected(false);
        if (historyCheckBox != null) historyCheckBox.setSelected(false);
        if (programmingCheckBox != null) programmingCheckBox.setSelected(false);
        if (sortGroup != null && sortGroup.getSelectedToggle() != null) {
            sortGroup.getSelectedToggle().setSelected(false);
        }
        loadInStockProducts();
        System.out.println("Product data refreshed.");
    }
}