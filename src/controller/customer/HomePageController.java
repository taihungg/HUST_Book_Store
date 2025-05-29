package controller.customer;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
import java.util.ResourceBundle; // Added for Initializable in ViewToyDetailsController
import java.util.stream.Collectors;
import controller.customer.ViewBookDetailsController;
import controller.customer.ViewStationeryDetailsController;
import controller.customer.ViewToyDetailsController;

// Assuming Main class exists and has currentUser
import controller.Main;


public class HomePageController {

    // HomePage controls
    @FXML private Button loginButton;
    @FXML private Button logoutButton;
    @FXML private ScrollPane mainScrollPane;
    @FXML private Button personalInfoButton;
    @FXML private Button orderHistoryButton;
    @FXML private Button browseProductsButton;
    @FXML private Button cartMenuButton;

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

    public static final AppServiceManager appServiceManager = Main.appServiceManager;
    private User currentUser = Main.currentUser;
    private ObservableList<Product> availableProducts;
    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    private boolean sortAscending = true; // Default sort direction

    @FXML
    public void initialize() {
        
        updateButtonsVisibility(currentUser != null);
        setupEventHandlers();

        try {
            availableProducts = appServiceManager.getProductManager().getAvailableProductsForCustomer();
            if (availableProducts == null) {
                System.err.println("ProductManager returned null for availableProducts. Initializing as empty list.");
                availableProducts = FXCollections.observableArrayList();
            }
        } catch (Exception e) {
            System.err.println("Error initializing availableProducts: " + e.getMessage());
            e.printStackTrace();
            availableProducts = FXCollections.observableArrayList(); 
            showAlert("Application Error", "Failed to load product data. Please try again later.");
        }

        setupFilterCheckboxHandlers();
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
        if (loginButton == null || logoutButton == null) {
            System.err.println("UI components not ready in updateButtonsVisibility.");
            return;
        }
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
    }

    private void setupEventHandlers() {
        if (searchField != null) {
            searchField.setOnKeyPressed(e -> {
                if (e.getCode().toString().equals("ENTER")) {
                    handleSearch();
                }
            });
        }
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
                return; 
            }
            refreshProductView();
        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter valid numbers for price (e.g., 10.99)!");
        }
    }

    @FXML
    private void handleSort() {
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
            updateProductDisplay(filteredProducts); 
            updateItemCount();
            return;
        }
        if (filteredProducts == null) { 
            filteredProducts = FXCollections.observableArrayList();
        }

        List<Product> processingList;
        String searchQuery = (searchField != null && searchField.getText() != null) ? searchField.getText().trim().toLowerCase() : "";

        if (!searchQuery.isEmpty()) {
            processingList = appServiceManager.getProductManager().searchProductsForCustomer(searchQuery);
            if (processingList == null) { 
                System.err.println("Warning: productManager.searchProductsForCustomer returned null for query: " + searchQuery);
                processingList = new ArrayList<>();
            }
        } else {
            processingList = new ArrayList<>(availableProducts); 
        }

        try {
            double minPrice = (minPriceField != null && minPriceField.getText() != null && !minPriceField.getText().isEmpty())
                                ? Double.parseDouble(minPriceField.getText().trim()) : 0;
            double maxPrice = (maxPriceField != null && maxPriceField.getText() != null && !maxPriceField.getText().isEmpty())
                                ? Double.parseDouble(maxPriceField.getText().trim()) : Double.MAX_VALUE;

            if (minPrice <= maxPrice) {
                final double finalMinPrice = minPrice; 
                final double finalMaxPrice = maxPrice; 
                processingList.removeIf(product ->
                    product.getSellingPrice() < finalMinPrice || product.getSellingPrice() > finalMaxPrice
                );
            }
        } catch (NumberFormatException e) {
            System.out.println("Price filter skipped in refreshProductView due to NumberFormatException: " + e.getMessage());
        }

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
                return !match; 
            });
        }
        
        List<String> selectedCategories = new ArrayList<>();
        if (vanHocCheckBox != null && vanHocCheckBox.isSelected()) selectedCategories.add("Văn học");
        if (khoaHocCheckBox != null && khoaHocCheckBox.isSelected()) selectedCategories.add("Khoa học");
        if (lichSuCheckBox != null && lichSuCheckBox.isSelected()) selectedCategories.add("Lịch sử");
        if (trinhThamCheckBox != null && trinhThamCheckBox.isSelected()) selectedCategories.add("Trinh thám");
        if (kinhTeCheckBox != null && kinhTeCheckBox.isSelected()) selectedCategories.add("Kinh tế");
        if (thieuNhiCheckBox != null && thieuNhiCheckBox.isSelected()) selectedCategories.add("Thiếu nhi");
        if (selfHelpCheckBox != null && selfHelpCheckBox.isSelected()) selectedCategories.add("Self-help");


        if (!selectedCategories.isEmpty()) {
            final List<String> finalSelectedCategories = selectedCategories; 
            processingList.removeIf(product -> {
                if (product instanceof Book) {
                    Book book = (Book) product;
                    String productCategory = book.getCategory();
                    return productCategory == null || !finalSelectedCategories.contains(productCategory);
                }
                return true; 
            });
        }

        List<String> selectedBrands = new ArrayList<>();
        if (thienLongCheckBox != null && thienLongCheckBox.isSelected()) selectedBrands.add("Thiên Long");
        if (plusCheckBox != null && plusCheckBox.isSelected()) selectedBrands.add("Plus");
        if (colgateCheckBox != null && colgateCheckBox.isSelected()) selectedBrands.add("Colgate");
        if (bitisCheckBox != null && bitisCheckBox.isSelected()) selectedBrands.add("Bitis");
        if (legoCheckBox != null && legoCheckBox.isSelected()) selectedBrands.add("Lego");
        if (bandaiCheckBox != null && bandaiCheckBox.isSelected()) selectedBrands.add("Bandai");
        if (hasbroCheckBox != null && hasbroCheckBox.isSelected()) selectedBrands.add("Hasbro");

        if (!selectedBrands.isEmpty()) {
            final List<String> finalSelectedBrands = selectedBrands; 
            processingList.removeIf(product -> {
                String brand = null;
                if (product instanceof Toy) {
                    brand = ((Toy) product).getBrand();
                } else if (product instanceof Stationery) {
                    brand = ((Stationery) product).getBrand();
                }

                if (brand != null) { 
                    return !finalSelectedBrands.contains(brand); 
                }
                return true; 
            });
        }

        if (sortGroup != null && sortGroup.getSelectedToggle() != null) {
            RadioButton selectedSortRadio = (RadioButton) sortGroup.getSelectedToggle();
            String sortType = selectedSortRadio.getText();

            Comparator<Product> comparator = (p1, p2) -> {
                int comparison = 0;
                switch (sortType) {
                    case "Sort by Title": 
                        comparison = p1.getTitle().compareTo(p2.getTitle());
                        break;
                    case "Sort by Cost": 
                        comparison = Double.compare(p1.getSellingPrice(), p2.getSellingPrice());
                        break;
                    default:
                        System.out.println("Warning: Unknown sort property encountered: " + sortType);
                        break;
                }
                return sortAscending ? comparison : -comparison;
            };
            try {
                processingList.sort(comparator); 
            } catch (UnsupportedOperationException e) {
                System.err.println("Error sorting products: " + e.getMessage() + ". Attempting to sort a copy.");
                List<Product> tempList = new ArrayList<>(processingList);
                tempList.sort(comparator);
                processingList = tempList;
            }
           
            sortAscending = !sortAscending; 
        } else {
            System.out.println("Không có tiêu chí sắp xếp nào được chọn hoặc sortGroup chưa được khởi tạo.");
        }

        filteredProducts.setAll(processingList);
        updateProductDisplay(filteredProducts); 
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
        final int maxCols = 5; 

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
        VBox card = new VBox(10); 
        card.setPadding(new Insets(10)); 
        card.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-border-radius: 5; -fx-background-radius: 5;"); 

        ImageView imageView = new ImageView();
        imageView.setFitHeight(150); 
        imageView.setFitWidth(100);  
        imageView.setPreserveRatio(true);

        try {
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
        } catch (NullPointerException | IllegalArgumentException e) { 
            System.err.println("Failed to load product image for " + product.getTitle() + ": " + e.getMessage());
            imageView.setImage(getPlaceholderImage()); 
        }


        Label titleLabel = new Label(product.getTitle());
        titleLabel.setWrapText(true); 
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label priceLabel = new Label(String.format("$%.2f", product.getSellingPrice()));
        priceLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-font-size: 13px;");

        Label infoLabel = new Label(); 
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-font-size: 11px;");
        if (product instanceof Book) { 
            infoLabel.setText("Author: " + ((Book) product).getAuthor());
        } else if (product instanceof Toy) {
            infoLabel.setText("Brand: " + ((Toy) product).getBrand() + "\nType: Toy");
        } else if (product instanceof Stationery) {
            infoLabel.setText("Brand: " + ((Stationery) product).getBrand() + "\nType: Stationery");
        }


        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        addToCartBtn.setOnAction(e -> handleAddToCart(product));

        imageView.setOnMouseClicked(e -> handleViewProductDetails(product));
        titleLabel.setOnMouseClicked(e -> handleViewProductDetails(product));
        imageView.setStyle(imageView.getStyle() + "-fx-cursor: hand;"); 
        titleLabel.setStyle(titleLabel.getStyle() + "-fx-cursor: hand; -fx-text-fill: #2980b9;"); 

        card.getChildren().addAll(imageView, titleLabel, infoLabel, priceLabel, addToCartBtn);
        return card;
    }

    private Image getPlaceholderImage() {
        try {
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            System.err.println("Critical: Placeholder image not found. Using blank image. " + e.getMessage());
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

        if (product instanceof PhysicalProduct) {
            int stock = appServiceManager.getProductManager().getProductQuantity(product.getId());
            if (stock <= 0) {
                showAlert("Out of Stock", "Sorry, this product is currently out of stock.");
                return;
            }
        }

        try {
            Customer customer = (Customer) currentUser;
            Cart customerCart = customer.getCart(); 
            if (customerCart == null) {
                showAlert("Cart Error", "Your shopping cart is not available. Please try logging out and back in.");
                return;
            }

            boolean success = customerCart.addItem(
                product.getId(), 1, 
                appServiceManager.getProductManager() 
            );

            if (success) {
                showAlert("Success!", product.getTitle() + " has been added to your cart.");
            } else {
                showAlert("Failed", "Could not add " + product.getTitle() + " to cart. It might be unavailable.");
            }
        } catch (IllegalArgumentException e) {
            showAlert("Invalid Action", "Could not add to cart: " + e.getMessage());
        } catch (IllegalStateException e) { 
            showAlert("Stock Issue", "Could not add to cart: " + e.getMessage());
        } catch (Exception e) { 
            showAlert("Error", "An unexpected error occurred while adding to cart: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    // REMOVED: public static Product detailedProduct; 

    private void handleViewProductDetails(Product product) {
        if (product == null) { // Check the product passed to this method
            showAlert("Product Not Found", "The selected product could not be found.");
            return;
        }

        // Fetch the most up-to-date product details by ID, as the 'product' instance from the grid might be stale
        // However, for simplicity and if your availableProducts list is the source of truth and updated,
        // you can directly use the passed 'product'. If not, re-fetching by ID is safer.
        // For this fix, we'll assume 'product' passed in is sufficient if it's an instance of Toy, Book, etc.
        // Product detailedProductFromManager = appServiceManager.getProductManager().getProductById(product.getId());
        // if (detailedProductFromManager == null) {
        //     showAlert("Product Not Found", "The selected product could not be found in the system.");
        //     return;
        // }

        String fxmlPath;
        Object controllerInstance = null; // To hold the specific controller

        try {
            FXMLLoader loader;
            Parent view;

            if (product instanceof Printbook || product instanceof Ebook || product instanceof Audiobook) {
                fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
                loader = new FXMLLoader(getClass().getResource(fxmlPath));
                view = loader.load();
                ViewBookDetailsController bookController = loader.getController();
                bookController.setAppServiceManager(appServiceManager); // Pass AppServiceManager
                bookController.updateUIforBook((Book) product); // Pass the specific book
                controllerInstance = bookController;
            } else if (product instanceof Stationery) {
                fxmlPath = "/view/customer/Store/ViewDetails/ViewStationeryDetails.fxml";
                loader = new FXMLLoader(getClass().getResource(fxmlPath));
                view = loader.load();
                ViewStationeryDetailsController stationeryController = loader.getController();
                stationeryController.setAppServiceManager(appServiceManager);
                stationeryController.updateUIforStationery((Stationery) product);
                controllerInstance = stationeryController;
            } else if (product instanceof Toy) {
                fxmlPath = "/view/customer/Store/ViewDetails/ViewToyDetails.fxml";
                loader = new FXMLLoader(getClass().getResource(fxmlPath));
                view = loader.load();
                ViewToyDetailsController toyController = loader.getController();
                toyController.setAppServiceManager(appServiceManager); // Pass AppServiceManager first
                toyController.updateUIforToy((Toy) product); // Then pass the specific Toy
                controllerInstance = toyController;
            } else {
                showAlert("Unknown Product", "Details view is not available for this product type.");
                return;
            }

            Stage stage = new Stage();
            stage.setTitle(product.getTitle() + " - Details"); // Use the actual product's title
            stage.initModality(Modality.APPLICATION_MODAL); 
            stage.setScene(new Scene(view));
            stage.showAndWait(); 

        } catch (IOException e) {
            showAlert("Load Error", "Failed to load product details view: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassCastException cce) {
            showAlert("Data Error", "There was an issue with the product data type: " + cce.getMessage());
            cce.printStackTrace();
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close(); 
        } catch (IOException e) {
            showError("Login Error", "Failed to open login window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            appServiceManager.logout(); 
            this.currentUser = null;
            updateButtonsVisibility(false);
            refreshProductView();
        } catch (Exception e) {
            showError("Logout Failed", "An error occurred during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuButton(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        String fxmlPath = null; 

        boolean loginRequired = !(button.getId().equals("browseProductsButton")); 
        if (loginRequired && currentUser == null) {
            showAlert("Login Required", "Please log in to access this feature.");
            return;
        }

        switch (button.getId()) {
            case "browseProductsButton":
                refreshProductView(); 
                return; 
            case "cartMenuButton":
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/Store/SeeCart.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();
                break;
            case "orderHistoryButton": 
            	FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/view/customer/Account/BookOrderHistoryApp.fxml"));
                Parent root1 = loader1.load();
                Scene scene1 = new Scene(root1);
                Stage stage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage1.setScene(scene1);
                stage1.show();
                Stage currentStage1 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage1.close();
                break;
            case "personalInfoButton": 
            	FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/view/customer/Account/SeePersonalInformation.fxml"));
                Parent root2 = loader2.load();
                Scene scene2 = new Scene(root2);
                Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage2.setScene(scene2);
                stage2.show();
                Stage currentStage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage2.close();
                break;
            default:
                System.err.println("Unsupported Button ID: " + button.getId());
                showAlert("Navigation Error", "The selected button is not configured.");
                return;
        }

        /*if (fxmlPath != null) {
            loadContentView(fxmlPath);
        }*/
    }

    private void loadContentView(String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath + ". Check path and ensure it's in resources.");
            }

            FXMLLoader loader = new FXMLLoader(url);
            Parent content = loader.load();
            mainScrollPane.setContent(content); 
        } catch (IOException e) {
            showError("Navigation Error", "Failed to load view: '" + fxmlPath + "'. " + e.getMessage());
            e.printStackTrace();
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
}
