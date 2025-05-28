package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.product.Product;
import model.product.Stationery;
import model.product.Toy;
import model.product.book.Audiobook;
import model.product.book.Book; // Assuming this is the base class for books
import model.product.book.Ebook;
import model.product.book.Printbook;
import model.product.interfaces.PhysicalProduct;
import model.user.User;
import model.user.cart.Cart;
import model.user.customer.Customer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Assuming Main class exists and has currentUser
import controller.Main;


public class HomePageController {

    // HomePage controls
    @FXML private Button loginButton;
    @FXML private Button logoutButton;
    @FXML private MenuBar menuBar;
    @FXML private MenuItem personalInfoMenuItem; // Ensure ID is set in FXML e.g., profileMenuItem
    @FXML private MenuItem orderHistoryMenuItem; // Ensure ID is set in FXML e.g., ordersMenuItem
    @FXML private MenuItem browseProductsMenuItem;
    @FXML private MenuItem cartMenuItem;
    @FXML private ScrollPane mainScrollPane;

    // BrowseProducts controls
    @FXML private TextField searchField;
    @FXML private Button searchButton; // searchButton seems unused if searchField handles ENTER
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private GridPane productsGrid;
    @FXML private ToggleGroup sortGroup;
    @FXML private Label itemCountLabel;

    // Category checkboxes (for Books)
    @FXML private CheckBox vanHocCheckBox;
    @FXML private CheckBox khoaHocCheckBox;
    @FXML private CheckBox lichSuCheckBox;
    @FXML private CheckBox trinhThamCheckBox;
    @FXML private CheckBox kinhTeCheckBox;
    @FXML private CheckBox thieuNhiCheckBox;
    @FXML private CheckBox selfHelpCheckBox;

    // Type checkboxes
    @FXML private CheckBox printBookCheckBox;
    @FXML private CheckBox eBookCheckBox;
    @FXML private CheckBox audioBookCheckBox;
    @FXML private CheckBox toyCheckBox;
    @FXML private CheckBox stationeryCheckBox;

    // Brand checkboxes
    @FXML private CheckBox thienLongCheckBox;
    @FXML private CheckBox plusCheckBox;
    @FXML private CheckBox colgateCheckBox; // Note: Colgate might not be a typical book/toy/stationery brand
    @FXML private CheckBox bitisCheckBox;   // Note: Bitis might not be a typical book/toy/stationery brand
    @FXML private CheckBox legoCheckBox;
    @FXML private CheckBox bandaiCheckBox;
    @FXML private CheckBox hasbroCheckBox;

    public static final AppServiceManager appServiceManager = new AppServiceManager();
    private User currentUser;
    // Initialize availableProducts AFTER appServiceManager and potentially Main.currentUser is set.
    // Made it non-final and initialize in initialize() for clarity if Main.currentUser is needed.
    private ObservableList<Product> availableProducts;
    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    private boolean sortAscending = true; // Default sort direction

    @FXML
    public void initialize() {
        currentUser = null; // Or appServiceManager.getCurrentUser() if session persists
        updateButtonsVisibility(currentUser != null);
        setupEventHandlers();

        // Load available products
        // Ensure Main.currentUser is appropriately set if getAllProductsForManager depends on it.
        try {
            availableProducts = appServiceManager.getProductManager().getAllProductsForManager(Main.currentUser);
            if (availableProducts == null) {
                // This case should ideally be handled by ProductManager returning an empty list instead of null.
                System.err.println("ProductManager returned null for availableProducts. Initializing as empty list.");
                availableProducts = FXCollections.observableArrayList();
                // Optionally, show an error to the user or log critical failure.
                // showAlert("Data Error", "Could not load product catalog.");
            }
        } catch (Exception e) {
            System.err.println("Error initializing availableProducts: " + e.getMessage());
            e.printStackTrace();
            availableProducts = FXCollections.observableArrayList(); // Fallback to empty list
            showAlert("Application Error", "Failed to load product data. Please try again later.");
        }


        // Setup action handlers for filter checkboxes
        setupFilterCheckboxHandlers();

        // Initial display of products
        refreshProductView();
    }

    private void setupFilterCheckboxHandlers() {
        // Link all filter checkboxes to handleFilterChange
        // Category filters
        if (vanHocCheckBox != null) vanHocCheckBox.setOnAction(event -> handleFilterChange());
        if (khoaHocCheckBox != null) khoaHocCheckBox.setOnAction(event -> handleFilterChange());
        if (lichSuCheckBox != null) lichSuCheckBox.setOnAction(event -> handleFilterChange());
        if (trinhThamCheckBox != null) trinhThamCheckBox.setOnAction(event -> handleFilterChange());
        if (kinhTeCheckBox != null) kinhTeCheckBox.setOnAction(event -> handleFilterChange());
        if (thieuNhiCheckBox != null) thieuNhiCheckBox.setOnAction(event -> handleFilterChange());
        if (selfHelpCheckBox != null) selfHelpCheckBox.setOnAction(event -> handleFilterChange());

        // Type filters
        if (printBookCheckBox != null) printBookCheckBox.setOnAction(event -> handleFilterChange());
        if (eBookCheckBox != null) eBookCheckBox.setOnAction(event -> handleFilterChange());
        if (audioBookCheckBox != null) audioBookCheckBox.setOnAction(event -> handleFilterChange());
        if (toyCheckBox != null) toyCheckBox.setOnAction(event -> handleFilterChange());
        if (stationeryCheckBox != null) stationeryCheckBox.setOnAction(event -> handleFilterChange());

        // Brand filters
        if (thienLongCheckBox != null) thienLongCheckBox.setOnAction(event -> handleFilterChange());
        if (plusCheckBox != null) plusCheckBox.setOnAction(event -> handleFilterChange());
        if (colgateCheckBox != null) colgateCheckBox.setOnAction(event -> handleFilterChange());
        if (bitisCheckBox != null) bitisCheckBox.setOnAction(event -> handleFilterChange());
        if (legoCheckBox != null) legoCheckBox.setOnAction(event -> handleFilterChange());
        if (bandaiCheckBox != null) bandaiCheckBox.setOnAction(event -> handleFilterChange());
        if (hasbroCheckBox != null) hasbroCheckBox.setOnAction(event -> handleFilterChange());
    }


    public User getCurrentUser() {
        return currentUser;
    }

    public AppServiceManager getAppServiceManager() {
        return appServiceManager;
    }

    private void updateButtonsVisibility(boolean isLoggedIn) {
        if (loginButton == null || logoutButton == null || menuBar == null) {
            System.err.println("UI components not ready in updateButtonsVisibility.");
            return;
        }
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
        // menuBar visibility might depend on more than just login status (e.g. always visible)
        // menuBar.setVisible(true); // Or based on your logic
    }

    private void setupEventHandlers() {
        if (searchField != null) {
            searchField.setOnKeyPressed(e -> {
                if (e.getCode().toString().equals("ENTER")) {
                    handleSearch();
                }
            });
        }
        // If searchButton is used, add its handler:
        // if (searchButton != null) searchButton.setOnAction(event -> handleSearch());
    }

    @FXML
    private void handleFilterChange() {
        refreshProductView();
    }

    @FXML
    private void handleSearch() {
        refreshProductView();
    }

    @FXML
    private void handlePriceFilter() {
        try {
            double minPrice = (minPriceField != null && !minPriceField.getText().isEmpty()) ? Double.parseDouble(minPriceField.getText().trim()) : 0;
            double maxPrice = (maxPriceField != null && !maxPriceField.getText().isEmpty()) ? Double.parseDouble(maxPriceField.getText().trim()) : Double.MAX_VALUE;

            if (minPrice > maxPrice) {
                showAlert("Input Error", "Minimum price cannot be greater than maximum price!");
                return; // Do not refresh if input is invalid
            }
            refreshProductView();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for price (e.g., 10.99)!");
            // Do not refresh if input is invalid
        }
    }

    @FXML
    private void handleSort() {
        // The actual sorting and sortAscending toggle is now done in refreshProductView.
        // This handler just triggers the refresh.
        if (sortGroup == null || sortGroup.getSelectedToggle() == null) {
            System.out.println("Sort action ignored: No sort criteria selected or sortGroup not initialized.");
            return;
        }
        refreshProductView();
    }

    private void refreshProductView() {
        if (availableProducts == null) {
            System.err.println("availableProducts is null in refreshProductView. Cannot proceed.");
            if (filteredProducts != null) filteredProducts.clear();
            else filteredProducts = FXCollections.observableArrayList();
            updateProductDisplay(filteredProducts); // Update with empty list
            updateItemCount();
            return;
        }
        if (filteredProducts == null) { // Should be initialized in constructor or field
            filteredProducts = FXCollections.observableArrayList();
        }

        List<Product> processingList;
        String searchQuery = (searchField != null && searchField.getText() != null) ? searchField.getText().trim().toLowerCase() : "";

        // 1. Start with search results or all available products
        if (!searchQuery.isEmpty()) {
            // Assuming searchProductsForCustomer searches globally or within a relevant context.
            // It does not take a User object, unlike getAllProductsForManager.
            processingList = appServiceManager.getProductManager().searchProductsForCustomer(searchQuery);
            if (processingList == null) { // Robustness for null return
                System.err.println("Warning: productManager.searchProductsForCustomer returned null for query: " + searchQuery);
                processingList = new ArrayList<>();
            }
        } else {
            processingList = new ArrayList<>(availableProducts); // Make a mutable copy
        }

        // 2. Apply Price Filter
        try {
            double minPrice = (minPriceField != null && minPriceField.getText() != null && !minPriceField.getText().isEmpty())
                                ? Double.parseDouble(minPriceField.getText().trim()) : 0;
            double maxPrice = (maxPriceField != null && maxPriceField.getText() != null && !maxPriceField.getText().isEmpty())
                                ? Double.parseDouble(maxPriceField.getText().trim()) : Double.MAX_VALUE;

            if (minPrice <= maxPrice) {
                final double finalMinPrice = minPrice; // For lambda
                final double finalMaxPrice = maxPrice; // For lambda
                processingList.removeIf(product ->
                    product.getSellingPrice() < finalMinPrice || product.getSellingPrice() > finalMaxPrice
                );
            }
            // If minPrice > maxPrice, error is shown by handlePriceFilter, and refresh might not even be called.
            // If called directly, this step is skipped for invalid range.
        } catch (NumberFormatException e) {
            // This case is usually caught by handlePriceFilter.
            // If refreshProductView is called with unvalidated text fields, this log helps.
            System.out.println("Price filter skipped in refreshProductView due to NumberFormatException: " + e.getMessage());
        }

        // 3. Apply Type, Category, and Brand Filters

        // Product Type Filter
        List<Class<? extends Product>> selectedProductTypes = new ArrayList<>();
        if (printBookCheckBox != null && printBookCheckBox.isSelected()) selectedProductTypes.add(Printbook.class);
        if (eBookCheckBox != null && eBookCheckBox.isSelected()) selectedProductTypes.add(Ebook.class);
        if (audioBookCheckBox != null && audioBookCheckBox.isSelected()) selectedProductTypes.add(Audiobook.class);
        if (toyCheckBox != null && toyCheckBox.isSelected()) selectedProductTypes.add(Toy.class);
        if (stationeryCheckBox != null && stationeryCheckBox.isSelected()) selectedProductTypes.add(Stationery.class);

        if (!selectedProductTypes.isEmpty()) {
            processingList.removeIf(product -> {
                boolean match = false;
                for (Class<? extends Product> type : selectedProductTypes) {
                    if (type.isInstance(product)) {
                        match = true;
                        break;
                    }
                }
                return !match; // Remove if not an instance of any selected type
            });
        }
        
        // Category Filter (Applied to Book instances)
        List<String> selectedCategories = new ArrayList<>();
        // Ensure category names match exactly what product.getCategory() would return for a Book
        if (vanHocCheckBox != null && vanHocCheckBox.isSelected()) selectedCategories.add("Văn học");
        if (khoaHocCheckBox != null && khoaHocCheckBox.isSelected()) selectedCategories.add("Khoa học");
        if (lichSuCheckBox != null && lichSuCheckBox.isSelected()) selectedCategories.add("Lịch sử");
        if (trinhThamCheckBox != null && trinhThamCheckBox.isSelected()) selectedCategories.add("Trinh thám");
        if (kinhTeCheckBox != null && kinhTeCheckBox.isSelected()) selectedCategories.add("Kinh tế");
        if (thieuNhiCheckBox != null && thieuNhiCheckBox.isSelected()) selectedCategories.add("Thiếu nhi");
        if (selfHelpCheckBox != null && selfHelpCheckBox.isSelected()) selectedCategories.add("Self-help");


        if (!selectedCategories.isEmpty()) {
            final List<String> finalSelectedCategories = selectedCategories; // For lambda
            processingList.removeIf(product -> {
                if (product instanceof Book) {
                    Book book = (Book) product;
                    // ASSUMPTION: Book class has getCategory() method returning a String.
                    String productCategory = book.getCategory();
                    return productCategory == null || !finalSelectedCategories.contains(productCategory);
                }
                // If category filters are active, and this product is not a book, remove it.
                // This means selecting a book category implies you only want to see books matching that category.
                return true; 
            });
        }

        // Brand Filter (Applied to Toy and Stationery instances)
        List<String> selectedBrands = new ArrayList<>();
        // Ensure brand names match exactly what product.getBrand() would return
        if (thienLongCheckBox != null && thienLongCheckBox.isSelected()) selectedBrands.add("Thiên Long");
        if (plusCheckBox != null && plusCheckBox.isSelected()) selectedBrands.add("Plus");
        if (colgateCheckBox != null && colgateCheckBox.isSelected()) selectedBrands.add("Colgate");
        if (bitisCheckBox != null && bitisCheckBox.isSelected()) selectedBrands.add("Bitis");
        if (legoCheckBox != null && legoCheckBox.isSelected()) selectedBrands.add("Lego");
        if (bandaiCheckBox != null && bandaiCheckBox.isSelected()) selectedBrands.add("Bandai");
        if (hasbroCheckBox != null && hasbroCheckBox.isSelected()) selectedBrands.add("Hasbro");

        if (!selectedBrands.isEmpty()) {
            final List<String> finalSelectedBrands = selectedBrands; // For lambda
            processingList.removeIf(product -> {
                String brand = null;
                if (product instanceof Toy) {
                    brand = ((Toy) product).getBrand();
                } else if (product instanceof Stationery) {
                    brand = ((Stationery) product).getBrand();
                }

                if (brand != null) { // If it's a Toy or Stationery with a brand
                    return !finalSelectedBrands.contains(brand); // Remove if brand not selected
                }
                // If it's not a Toy or Stationery, or brand filters are active, remove it.
                // This means selecting a brand implies you only want to see products of that brand.
                return true; 
            });
        }

        // 4. Apply Sort
        if (sortGroup != null && sortGroup.getSelectedToggle() != null) {
            RadioButton selectedSortRadio = (RadioButton) sortGroup.getSelectedToggle();
            String sortType = selectedSortRadio.getText();

            Comparator<Product> comparator = (p1, p2) -> {
                int comparison = 0;
                switch (sortType) {
                    case "Sort by Title": // Ensure this text matches your RadioButton's text
                        comparison = p1.getTitle().compareTo(p2.getTitle());
                        break;
                    case "Sort by Cost": // Ensure this text matches your RadioButton's text
                        comparison = Double.compare(p1.getSellingPrice(), p2.getSellingPrice());
                        break;
                    default:
                        System.out.println("Warning: Unknown sort property encountered: " + sortType);
                        break;
                }
                return sortAscending ? comparison : -comparison;
            };
            try {
                // Use List.sort() or Collections.sort() for java.util.List
                processingList.sort(comparator); 
            } catch (UnsupportedOperationException e) {
                // This can happen if processingList is unmodifiable (e.g., from Arrays.asList).
                // Ensure it's a mutable list (e.g., ArrayList, which it is in this case)
                System.err.println("Error sorting products: " + e.getMessage() + ". Attempting to sort a copy.");
                // Fallback: create a new sorted list (though processingList should be mutable here)
                List<Product> tempList = new ArrayList<>(processingList);
                tempList.sort(comparator);
                processingList = tempList;
            }
           
            sortAscending = !sortAscending; // Toggle for the next sort operation
        } else {
            // Optional: if no sort selected, apply a default sort (e.g., by title ascending)
            // Or leave unsorted (as per current availableProducts order after filtering)
            System.out.println("Không có tiêu chí sắp xếp nào được chọn hoặc sortGroup chưa được khởi tạo.");
        }

        // 5. Update UI
        filteredProducts.setAll(processingList);
        updateProductDisplay(filteredProducts); // This re-populates the GridPane
        updateItemCount();
    }


    private void updateItemCount() {
        if (itemCountLabel != null) {
            itemCountLabel.setText(String.valueOf(filteredProducts.size()));
        }
    }

    private void updateProductDisplay(ObservableList<Product> products) {
        if (productsGrid == null) {
            System.err.println("productsGrid is null in updateProductDisplay. Cannot update UI.");
            return;
        }

        productsGrid.getChildren().clear();
        int row = 0;
        int col = 0;
        final int maxCols = 5; // Define how many columns you want in the grid

        if (products == null) return;

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
        VBox card = new VBox(10); // Spacing between elements in VBox
        card.setPadding(new Insets(10)); // Padding around the card
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;"); // Basic styling

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150); // Standard height for product image
        imageView.setFitWidth(100);  // Standard width for product image
        imageView.setPreserveRatio(true);

        try {
            // Ensure product.getGalleryURL() returns a valid path relative to resources
            // e.g., "/images/products/myproduct.png"
            String imagePath = product.getGalleryURL();
            if (imagePath == null || imagePath.isEmpty()) {
                throw new NullPointerException("Image path is null or empty for product: " + product.getTitle());
            }
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            if (image.isError()) {
                 System.err.println("Error loading image: " + imagePath + " - " + image.getException().getMessage());
                 imageView.setImage(getPlaceholderImage());
            } else {
                imageView.setImage(image);
            }
        } catch (NullPointerException | IllegalArgumentException e) { // Catch common issues with paths
            System.err.println("Failed to load product image for " + product.getTitle() + ": " + e.getMessage());
            imageView.setImage(getPlaceholderImage()); // Fallback to placeholder
        }


        Label titleLabel = new Label(product.getTitle());
        titleLabel.setWrapText(true); // Allow text to wrap
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label priceLabel = new Label(String.format("$%.2f", product.getSellingPrice()));
        priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label infoLabel = new Label(); // Additional info (author, brand, etc.)
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-font-size: 11px;");
        if (product instanceof Book) { // Use instanceof model.product.book.Book if that's the specific class
            infoLabel.setText("Author: " + ((Book) product).getAuthor());
        } else if (product instanceof Toy) {
            infoLabel.setText("Brand: " + ((Toy) product).getBrand() + "\nType: Toy");
        } else if (product instanceof Stationery) {
            infoLabel.setText("Brand: " + ((Stationery) product).getBrand() + "\nType: Stationery");
        }


        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        addToCartBtn.setOnAction(e -> handleAddToCart(product));

        // Make image and title clickable to view details
        imageView.setOnMouseClicked(e -> handleViewProductDetails(product));
        titleLabel.setOnMouseClicked(e -> handleViewProductDetails(product));
        imageView.setStyle(imageView.getStyle() + "-fx-cursor: hand;"); // Add hand cursor
        titleLabel.setStyle(titleLabel.getStyle() + "-fx-cursor: hand; -fx-text-fill: #2980b9;"); // Make title look like a link

        card.getChildren().addAll(imageView, titleLabel, infoLabel, priceLabel, addToCartBtn);
        return card;
    }

    private Image getPlaceholderImage() {
        try {
            // Ensure placeholder.png is in the specified path in your resources
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            System.err.println("Critical: Placeholder image not found. Using blank image. " + e.getMessage());
            // Return a 1x1 transparent pixel as an absolute fallback
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
        }
    }

    private void handleAddToCart(Product product) {
        if (currentUser == null) {
            showAlert("Login Required", "Please log in to add products to your cart!");
            return;
        }

        if (!(currentUser instanceof Customer)) {
            showAlert("Access Denied", "Only customers can add products to the cart.");
            return;
        }

        // Check stock for physical products
        if (product instanceof PhysicalProduct) {
            int stock = appServiceManager.getProductManager().getProductQuantity(product.getId());
            if (stock <= 0) {
                showAlert("Out of Stock", "Sorry, this product is currently out of stock.");
                return;
            }
        }

        try {
            Customer customer = (Customer) currentUser;
            Cart customerCart = customer.getCart(); // Ensure cart is initialized for the customer upon login/creation
            if (customerCart == null) {
                // This should ideally not happen if customer objects are managed correctly.
                showAlert("Cart Error", "Your shopping cart is not available. Please try logging out and back in.");
                return;
            }

            boolean success = customerCart.addItem(
                product.getId(), 1, // Adding one item at a time
                appServiceManager.getProductManager() // Pass manager for stock checks if addItem needs it
            );

            if (success) {
                showAlert("Success!", product.getTitle() + " has been added to your cart.");
            } else {
                // addItem might return false for various reasons (e.g., internal stock check failed again)
                showAlert("Failed", "Could not add " + product.getTitle() + " to cart. It might be unavailable.");
            }
        } catch (IllegalArgumentException e) {
            showAlert("Invalid Action", "Could not add to cart: " + e.getMessage());
        } catch (IllegalStateException e) { // e.g. for not enough stock if checked inside cart.addItem
            showAlert("Stock Issue", "Could not add to cart: " + e.getMessage());
        } catch (Exception e) { // Catch-all for unexpected errors
            showAlert("Error", "An unexpected error occurred while adding to cart: " + e.getMessage());
            e.printStackTrace(); // Log for debugging
        }
    }

    private void handleViewProductDetails(Product product) {
        if (product == null) return;

        // Fetch the most up-to-date product details
        Product detailedProduct = appServiceManager.getProductManager().getProductById(product.getId());
        if (detailedProduct == null) {
            showAlert("Product Not Found", "The selected product could not be found.");
            return;
        }

        String fxmlPath;
        // Determine FXML path based on product type
        if (detailedProduct instanceof Printbook || detailedProduct instanceof Ebook || detailedProduct instanceof Audiobook) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
        } else if (detailedProduct instanceof Stationery) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewStationeryDetails.fxml";
        } else if (detailedProduct instanceof Toy) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewToyDetails.fxml";
        } else {
            showAlert("Unknown Product", "Details view is not available for this product type.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            // Pass data to the details controller (Example, adjust as per your detail controller methods)
            // This part was commented out in your original code, uncomment and adapt if needed.
            
            Object controller = loader.getController();
            if (controller instanceof ViewBookDetailsController) {
                ((ViewBookDetailsController) controller).setAppServiceManager(appServiceManager);
                ((ViewBookDetailsController) controller).setProductId(detailedProduct.getId());
                // ((ViewBookDetailsController) controller).loadProductDetails(); // if you have such a method
            } else if (controller instanceof ViewStationeryDetailsController) {
                ((ViewStationeryDetailsController) controller).setAppServiceManager(appServiceManager);
                ((ViewStationeryDetailsController) controller).setProductId(detailedProduct.getId());
                // ((ViewStationeryDetailsController) controller).loadProductDetails();
            } else if (controller instanceof ViewToyDetailsController) {
                ((ViewToyDetailsController) controller).setAppServiceManager(appServiceManager);
                ((ViewToyDetailsController) controller).setProductId(detailedProduct.getId());
                // ((ViewToyDetailsController) controller).loadProductDetails();
            }
            

            Stage stage = new Stage();
            stage.setTitle(detailedProduct.getTitle() + " - Details");
            stage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            stage.setScene(new Scene(view));
            stage.showAndWait(); // Show and wait for it to be closed

        } catch (IOException e) {
            showAlert("Load Error", "Failed to load product details view: " + e.getMessage());
            e.printStackTrace();
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
            loginStage.setScene(new Scene(loginView));
            loginStage.showAndWait(); // Wait for login window to close

            // After login window closes, check if user logged in successfully
            User loggedInUser = appServiceManager.getCurrentUser(); // Assuming AppServiceManager tracks current user
            if (loggedInUser != null) {
                this.currentUser = loggedInUser;
                updateButtonsVisibility(true);
                // Refresh product view, as available products or cart might change
                refreshProductView();
                if (!(currentUser instanceof Customer)) { // e.g. Admin user
                     loadContentView("/view/admin/HomePageAdmin.fxml"); // Or relevant admin view
                }
            }
        } catch (IOException e) {
            showError("Login Error", "Failed to open login window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            appServiceManager.logout(); // Perform logout operations
            this.currentUser = null;
            updateButtonsVisibility(false);
            // Refresh product view for logged-out state
            refreshProductView();
            // Optionally, load a default view or clear current content
            // loadContentView("/view/customer/Store/BrowseProducts.fxml"); // Or similar if you want to reset view
        } catch (Exception e) {
            showError("Logout Failed", "An error occurred during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlPath = null; // Initialize to null

        // Check for login requirement for restricted menu items
        boolean loginRequired = !(menuItem.getId().equals("browseProductsMenuItem")); // Example: browse is public
        if (loginRequired && currentUser == null) {
            showAlert("Login Required", "Please log in to access this feature.");
            return;
        }

        switch (menuItem.getId()) {
            case "browseProductsMenuItem":
                // If already on browse view, just refresh. If not, load it.
                // Assuming mainScrollPane shows product grid or a wrapper.
                // If current content IS the product browser, refreshProductView is enough.
                // If it could be something else, you might need to load the product browser FXML.
                refreshProductView(); // Simplest: just refresh current view's data
                return; // Exit after handling
            case "cartMenuItem":
                fxmlPath = "/view/customer/Cart/CartView.fxml";
                break;
            case "ordersMenuItem": // Ensure FXML ID matches "ordersMenuItem"
                fxmlPath = "/view/customer/Order/OrderHistory.fxml";
                break;
            case "profileMenuItem": // Ensure FXML ID matches "profileMenuItem"
                fxmlPath = "/view/customer/Profile/ProfileView.fxml";
                break;
            default:
                System.err.println("Unsupported MenuItem ID: " + menuItem.getId());
                showAlert("Navigation Error", "The selected menu item is not configured.");
                return;
        }

        if (fxmlPath != null) {
            loadContentView(fxmlPath);
        }
    }

    private void loadContentView(String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath + ". Check path and ensure it's in resources.");
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent content = loader.load();
            mainScrollPane.setContent(content); // Load new content into the ScrollPane
        } catch (IOException e) {
            showError("Navigation Error", "Failed to load view: '" + fxmlPath + "'. " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Use INFORMATION for general messages/success
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text for simpler alerts
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
}
