package controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import model.manager.AppServiceManager;
import model.product.Product;
import model.product.Toy;
import model.product.Stationery;
import model.product.interfaces.PhysicalProduct;
import model.user.User;
import model.user.customer.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController implements Initializable {

    // HomePage controls
    @FXML private Button loginButton;
    @FXML private Button logoutButton;
    @FXML private MenuBar menuBar;
    @FXML private MenuItem personalInfoMenuItem;
    @FXML private MenuItem orderHistoryMenuItem;
    @FXML private MenuItem browseProductsMenuItem;
    @FXML private MenuItem cartMenuItem;
    @FXML private ScrollPane mainScrollPane;

    // BrowseProducts controls
    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private GridPane productsGrid;
    @FXML private ToggleGroup sortGroup;
    @FXML private CheckBox vanHocCheckBox;
    @FXML private CheckBox khoaHocCheckBox;
    @FXML private CheckBox lichSuCheckBox;
    @FXML private CheckBox trinhThamCheckBox;
    @FXML private CheckBox kinhTeCheckBox;
    @FXML private CheckBox thieuNhiCheckBox;
    @FXML private CheckBox selfHelpCheckBox;
    @FXML private Label itemCountLabel;
    @FXML private CheckBox printBookCheckBox;
    @FXML private CheckBox eBookCheckBox;
    @FXML private CheckBox audioBookCheckBox;
    @FXML private CheckBox toyCheckBox;
    @FXML private CheckBox stationeryCheckBox;

    // Brand checkboxes
    @FXML private CheckBox thienLongCheckBox;
    @FXML private CheckBox plusCheckBox;
    @FXML private CheckBox colgateCheckBox;
    @FXML private CheckBox bitisCheckBox;
    @FXML private CheckBox legoCheckBox;
    @FXML private CheckBox bandaiCheckBox;
    @FXML private CheckBox hasbroCheckBox;

    private AppServiceManager appServiceManager;
    private User currentUser;
    private ObservableList<Product> displayedProducts;
    private ObservableList<Product> filteredProducts;
    private boolean sortAscending = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appServiceManager = new AppServiceManager();
        currentUser = null;
        displayedProducts = FXCollections.observableArrayList();
        filteredProducts = FXCollections.observableArrayList();
        
        updateButtonsVisibility(false);
        setupEventHandlers();
        loadInStockProducts();
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    private void updateButtonsVisibility(boolean isLoggedIn) {
        if (loginButton == null || logoutButton == null || menuBar == null) {
            throw new IllegalStateException("loginButton, logoutButton, or menuBar is not initialized");
        }
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
        menuBar.setVisible(true);
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
    }    

    private void loadInStockProducts() {
        if (appServiceManager == null || currentUser == null) {
            System.err.println("AppServiceManager or currentUser is not initialized.");
            return;
        }
        
        displayedProducts.clear();
        displayedProducts.addAll(appServiceManager.getProductManager().searchProductsForCustomer(""));
        applyAllFiltersAndSort();
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
            int comparison = 0;
            switch (sortType) {
                case "Sort by Title":
                    comparison = p1.getTitle().compareTo(p2.getTitle());
                    break;
                case "Sort by Cost":
                    comparison = Double.compare(p1.getSellingPrice(), p2.getSellingPrice());
                    break;
            }
            return sortAscending ? comparison : -comparison;
        });
        
        sortAscending = !sortAscending;
        updateProductDisplay(filteredProducts);
    }

    private void applyAllFiltersAndSort() {
        if (displayedProducts == null) return;
        
        filteredProducts.clear();
        filteredProducts.addAll(displayedProducts);
        
        // Lọc theo search query
        String searchQuery = searchField.getText().trim();
        if (!searchQuery.isEmpty()) {
            filteredProducts.addAll(appServiceManager.getProductManager().searchProductsForCustomer(searchQuery));
        } else {
            filteredProducts.addAll(displayedProducts);
        }
        
        // Lọc theo giá
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
        
        handleSort();
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
        final int maxCols = 5;
        
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
        
        Label titleLabel = new Label(product.getTitle());
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-weight: bold;");
        
        Label priceLabel = new Label(String.format("$%.2f", product.getSellingPrice()));
        priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
        
        // Thêm thông tin tùy theo loại sản phẩm
        Label infoLabel = new Label();
        if (product instanceof model.product.book.Book) {
            infoLabel.setText("Author: " + ((model.product.book.Book) product).getAuthor());
        } else if (product instanceof Toy || product instanceof Stationery) {
            String brand = product instanceof Toy ? ((Toy) product).getBrand() : ((Stationery) product).getBrand();
            String type = product instanceof Toy ? "Toy" : ((Stationery) product).getType();
            infoLabel.setText("Brand: " + brand + "\nType: " + type);
        }
        
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setOnAction(e -> handleAddToCart(product));
        
        imageView.setOnMouseClicked(e -> handleViewProductDetails(product));
        titleLabel.setOnMouseClicked(e -> handleViewProductDetails(product));
        
        imageView.setStyle("-fx-cursor: hand;");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-cursor: hand; -fx-text-fill: #2980b9;");
        
        card.getChildren().addAll(imageView, titleLabel, infoLabel, priceLabel, addToCartBtn);
        return card;
    }

    private Image getPlaceholderImage() {
        return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
    }

    private void handleAddToCart(Product product) {
        if (currentUser == null) {
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
            // TODO: Implement addToCart method in OrderManager
            // appServiceManager.getOrderManager().addToCart(currentUser, product.getId(), 1);
            showAlert("Success", "Product added to cart!");
        } catch (Exception e) {
            showAlert("Error", "Failed to add product to cart: " + e.getMessage());
        }
    }

    private void handleViewProductDetails(Product product) {
        if (product == null) return;

        Product detailedProduct = appServiceManager.getProductManager().getProductById(product.getId());
        if (detailedProduct == null) {
            showAlert("Error", "Product not found!");
            return;
        }

        String productType = detailedProduct.getProductType();
        String fxmlPath;
        
        switch (productType.toLowerCase()) {
            case "printbook":
            	fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
                break;
            case "ebook":
            	fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
                break;
            case "audiobook":
                fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
                break;
            case "stationery":
                fxmlPath = "/view/customer/Store/ViewDetails/ViewStationeryDetails.fxml";
                break;
            case "toy":
                fxmlPath = "/view/customer/Store/ViewDetails/ViewToyDetails.fxml";
                break;
            default:
                showAlert("Error", "Unknown product type!");
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof ViewBookDetailsController) {
                ((ViewBookDetailsController) controller).setAppServiceManager(appServiceManager);
                ((ViewBookDetailsController) controller).setProductId(product.getId());
            } else if (controller instanceof ViewStationeryDetailsController) {
                ((ViewStationeryDetailsController) controller).setAppServiceManager(appServiceManager);
                ((ViewStationeryDetailsController) controller).setProductId(product.getId());
            } else if (controller instanceof ViewToyDetailsController) {
                ((ViewToyDetailsController) controller).setAppServiceManager(appServiceManager);
                ((ViewToyDetailsController) controller).setProductId(product.getId());
            }
            
            Stage stage = new Stage();
            stage.setTitle("Product Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(view));
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Error", "Failed to load product details view: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent loginView = loader.load();
            
            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.initModality(Modality.APPLICATION_MODAL);
            
            Scene scene = new Scene(loginView);
            loginStage.setScene(scene);
            
            loginStage.showAndWait();
            
            if (appServiceManager.getCurrentUser() != null) {
                currentUser = appServiceManager.getCurrentUser();
                updateButtonsVisibility(true);
                
                if (currentUser instanceof Customer) {
                    loadInStockProducts();
                } else {
                    loadContentView("/view/admin/HomePageAdmin.fxml");
                }
            }
        } catch (IOException e) {
            showError("Error", "Failed to open login window: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            appServiceManager.logout();
            currentUser = null;
            updateButtonsVisibility(false);
            loadInStockProducts();
        } catch (Exception e) {
            showError("Logout failed", e.getMessage());
        }
    }

    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlPath;
        
        if (currentUser == null && !menuItem.getId().equals("browseProductsMenuItem")) {
            showAlert("Login Required", "Please log in to access this feature.");
            return;
        }
        
        switch (menuItem.getId()) {
            case "browseProductsMenuItem":
                loadInStockProducts();
                return;
            case "cartMenuItem":
                fxmlPath = "/view/customer/Cart/CartView.fxml";
                break;
            case "ordersMenuItem":
                fxmlPath = "/view/customer/Order/OrderHistory.fxml";
                break;
            case "profileMenuItem":
                fxmlPath = "/view/customer/Profile/ProfileView.fxml";
                break;
            default:
                return;
        }
        
        loadContentView(fxmlPath);
    }

    private void loadContentView(String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }
            
            FXMLLoader loader = new FXMLLoader(url);
            Parent content = loader.load();
            mainScrollPane.setContent(content);
        } catch (IOException e) {
            showError("Error", "Failed to load view: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // TODO: Implement these methods
    /*
     * 1. Thêm phương thức getProductType() vào class Product để trả về loại sản phẩm
     * 2. Thêm phương thức getAuthor(), getCategory(), getBrand(), getType() vào các class con của Product
     * 3. Thêm phương thức addToCart() vào OrderManager để thêm sản phẩm vào giỏ hàng
     * 4. Thêm phương thức setAppServiceManager() vào HomePageController để các controller con có thể truy cập
     */
}