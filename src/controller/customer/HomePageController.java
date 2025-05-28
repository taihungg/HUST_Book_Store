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
import model.product.book.Audiobook;
import model.product.book.Ebook;
import model.product.book.Printbook;
import model.product.Stationery;
import model.product.interfaces.PhysicalProduct;
import model.user.User;
import model.user.cart.Cart;
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
    private ObservableList<Product> availableProducts;
    private ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    private boolean sortAscending = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentUser = null;
        updateButtonsVisibility(false);
        setupEventHandlers();
        
        // Load products ngay sau khi initialize nếu AppServiceManager đã có
        if (appServiceManager != null) {
            loadAvailableProducts();
        }
    }
    
    private void loadAvailableProducts() {
        if (appServiceManager == null) {
            System.err.println("AppServiceManager chưa được khởi tạo. Không thể tải sản phẩm.");
            return;
        }
        
        try {
            // Gán trực tiếp availableProducts từ ProductManager
            availableProducts = appServiceManager.getProductManager().getAvailableProductsForCustomer();
            
            if (availableProducts == null) {
                availableProducts = FXCollections.observableArrayList();
                System.err.println("Không có sản phẩm nào được trả về từ ProductManager");
            } else {
                System.out.println("Đã tải " + availableProducts.size() + " sản phẩm có sẵn.");
            }
            
            // Đảm bảo availableProducts.size() > 0
            if (availableProducts.isEmpty()) {
                System.err.println("Danh sách sản phẩm trống. Kiểm tra dữ liệu trong ProductManager.");
                // Có thể thêm một số sản phẩm mẫu hoặc thông báo lỗi
            }
            
            applyAllFiltersAndSort();
        } catch (Exception e) {
            System.err.println("Lỗi khi tải sản phẩm: " + e.getMessage());
            e.printStackTrace();
            availableProducts = FXCollections.observableArrayList();
        }
    }
    
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public AppServiceManager getAppServiceManager() {
        return appServiceManager;
    }
    
    private void updateButtonsVisibility(boolean isLoggedIn) {
        // Thêm kiểm tra null để đảm bảo các FXML component đã được inject
        if (loginButton == null || logoutButton == null || menuBar == null) {
            // Không ném exception, chỉ in cảnh báo để ứng dụng vẫn có thể chạy
            System.err.println("Cảnh báo: Một số thành phần UI chưa được khởi tạo trong updateButtonsVisibility.");
            return; 
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

    @FXML
    private void handleSearch() {
        applyAllFiltersAndSort();
    }

    @FXML
    private void handlePriceFilter() {
        try {
            // Thêm kiểm tra null trước khi gọi getText()
            double minPrice = (minPriceField != null && !minPriceField.getText().isEmpty()) ? Double.parseDouble(minPriceField.getText().trim()) : 0;
            double maxPrice = (maxPriceField != null && !maxPriceField.getText().isEmpty()) ? Double.parseDouble(maxPriceField.getText().trim()) : Double.MAX_VALUE;
            
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
        // Thêm kiểm tra null cho sortGroup
        if (sortGroup == null || sortGroup.getSelectedToggle() == null) {
            System.out.println("Không có tiêu chí sắp xếp nào được chọn hoặc sortGroup chưa được khởi tạo.");
            return;
        }
        
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
        updateItemCount(); // Cập nhật số lượng sau khi sắp xếp
    }

    private void applyAllFiltersAndSort() {
        if (availableProducts == null) return;
        
        filteredProducts.clear();
        filteredProducts.addAll(availableProducts);
        
        // Lọc theo search query
        // Đảm bảo searchField đã được inject trước khi sử dụng
        String searchQuery = (searchField != null) ? searchField.getText().trim().toLowerCase() : "";
        if (!searchQuery.isEmpty()) {
            // Sử dụng removeIf để lọc trên filteredProducts hiện có
            filteredProducts.removeIf(product -> !(product.getTitle().toLowerCase().contains(searchQuery) ||
                                                  product.getId().toLowerCase().contains(searchQuery)));
        } 
        
        // Lọc theo giá
        try {
            // Thêm kiểm tra null trước khi gọi getText()
            double minPrice = (minPriceField != null && !minPriceField.getText().isEmpty()) ? Double.parseDouble(minPriceField.getText().trim()) : 0;
            double maxPrice = (maxPriceField != null && !maxPriceField.getText().isEmpty()) ? Double.parseDouble(maxPriceField.getText().trim()) : Double.MAX_VALUE;
            
            if (minPrice <= maxPrice) {
                filteredProducts.removeIf(product -> 
                    product.getSellingPrice() < minPrice || product.getSellingPrice() > maxPrice
                );
            }
        } catch (NumberFormatException e) {
            // Ignore invalid price values
        }

        // Sau khi lọc, gọi sắp xếp
        handleSort(); 
        // **Đã loại bỏ:** updateProductDisplay(filteredProducts); và updateItemCount();
        // Vì handleSort() sẽ gọi chúng sau khi sắp xếp xong.
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
        // **Cải thiện:** Chỉ cần một if-else if-else để xử lý các trường hợp cụ thể, tránh lặp lại.
        if (product instanceof model.product.book.Book) {
            infoLabel.setText("Author: " + ((model.product.book.Book) product).getAuthor());
        } else if (product instanceof Toy) { // Sử dụng instanceof cụ thể hơn
            infoLabel.setText("Brand: " + ((Toy) product).getBrand() + "\nType: Toy");
        } else if (product instanceof Stationery) { // Sử dụng instanceof cụ thể hơn
            infoLabel.setText("Brand: " + ((Stationery) product).getBrand() + "\nType: Stationery");
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
        try {
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            // Log lỗi nếu ảnh không tìm thấy nhưng không ném exception làm crash ứng dụng
            System.err.println("Cảnh báo: Không tìm thấy ảnh placeholder. Sử dụng ảnh trống.");
            // Trả về một ảnh trong suốt 1x1 pixel để tránh NullPointerException
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
        }
    }

    private void handleAddToCart(Product product) {
        // Kiểm tra đăng nhập
        if (currentUser == null) {
            showAlert("Login Required", "Please log in to add products to cart!");
            return;
        }

        // Kiểm tra currentUser là Customer
        if (!(currentUser instanceof Customer)) {
            showAlert("Error", "Only customers can add products to cart!");
            return;
        }

        // Kiểm tra tồn kho cho PhysicalProduct
        if (product instanceof PhysicalProduct) {
            int stock = appServiceManager.getProductManager().getProductQuantity(product.getId());
            if (stock <= 0) {
                showAlert("Out of Stock", "This product is currently out of stock!");
                return;
            }
        }

        try {
            // Lấy giỏ hàng của khách hàng
            Customer customer = (Customer) currentUser;
            Cart customerCart = customer.getCart();
            if (customerCart == null) {
                showAlert("Error", "Customer cart is not initialized!");
                return;
            }

            // Thêm sản phẩm vào giỏ hàng
            boolean success = customerCart.addItem(
                product.getId(), 1, // Số lượng mặc định là 1
                appServiceManager.getProductManager()
            );

            if (success) {
                showAlert("Success", "Product added to cart!");
            } else {
                showAlert("Error", "Failed to add product to cart. Check stock or product availability.");
            }
        } catch (IllegalArgumentException e) {
            showAlert("Error", "Invalid input: " + e.getMessage());
        } catch (IllegalStateException e) {
            showAlert("Error", "Not enough stock: " + e.getMessage());
        } catch (Exception e) {
            showAlert("Error", "Failed to add product to cart: " + e.getMessage());
            e.printStackTrace(); // In stack trace để debug
        }
    }

    private void handleViewProductDetails(Product product) {
        if (product == null) return;

        Product detailedProduct = appServiceManager.getProductManager().getProductById(product.getId());
        if (detailedProduct == null) {
            showAlert("Error", "Product not found!");
            return;
        }

        String fxmlPath;

        if (detailedProduct instanceof Printbook) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
        } else if (detailedProduct instanceof Ebook) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
        } else if (detailedProduct instanceof Audiobook) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
        } else if (detailedProduct instanceof Stationery) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewStationeryDetails.fxml";
        } else if (detailedProduct instanceof Toy) {
            fxmlPath = "/view/customer/Store/ViewDetails/ViewToyDetails.fxml";
        } else {
            showAlert("Error", "Unknown product type!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            
            Object controller = loader.getController();
            // Đảm bảo rằng các controller details của bạn có các phương thức này
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
            e.printStackTrace(); // In stack trace để debug
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
                    loadAvailableProducts();
                } else {
                    loadContentView("/view/admin/HomePageAdmin.fxml");
                }
            }
        } catch (IOException e) {
            showError("Error", "Failed to open login window: " + e.getMessage());
            e.printStackTrace(); // In stack trace để debug
        }
    }

    @FXML
    private void handleLogout() {
        try {
            appServiceManager.logout();
            currentUser = null;
            updateButtonsVisibility(false);
            loadAvailableProducts();
        } catch (Exception e) {
            showError("Logout failed", e.getMessage());
            e.printStackTrace(); // In stack trace để debug
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
                loadAvailableProducts();
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
                // Log nếu có MenuItem không được xử lý
                System.err.println("MenuItem không được hỗ trợ: " + menuItem.getId());
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
            e.printStackTrace(); // In stack trace để debug
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