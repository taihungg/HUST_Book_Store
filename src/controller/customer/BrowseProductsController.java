package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

interface SubController {
    void setMainController(CustomerMainController mainController);
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

    //region Internal State
    private CustomerMainController mainController;
    private List<Product> allProducts;
    private ObservableList<Product> displayedProducts;

    //end region

    //region Nested Classes (Product and CartItem)
    public static class Product {
        private String title;
        private String author;
        private double price;
        private String category;
        private String imagePath;

        public Product(String title, String author, double price, String category, String imagePath) {
            this.title = title;
            this.author = author;
            this.price = price;
            this.category = category;
            this.imagePath = imagePath;
        }

        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public double getPrice() { return price; }
        public String getCategory() { return category; }
        public String getImagePath() { return imagePath; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return Double.compare(product.price, price) == 0 &&
                   title.equals(product.title) &&
                   author.equals(product.author) &&
                   category.equals(product.category);
        }

        @Override
        public int hashCode() {
            int result = title.hashCode();
            result = 31 * result + author.hashCode();
            result = 31 * result + category.hashCode();
            result = 31 * result + Double.hashCode(price);
            return result;
        }

        @Override
        public String toString() {
            return "Product{title='" + title + "', author='" + author + "', price=" + price + "}";
        }
    }

    public static class CartItem {
        private Product product;
        private int quantity;
        private double totalPrice;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
            this.totalPrice = product.getPrice() * quantity;
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public double getTotalPrice() { return totalPrice; }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
            this.totalPrice = product.getPrice() * quantity;
        }

        @Override
        public String toString() {
            return "CartItem{item='" + product.getTitle() + "', price=" + String.format("%.2f", product.getPrice()) +
                   ", quantity=" + quantity + ", total=" + String.format("%.2f", totalPrice) + "}";
        }
    }
    //end region

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeProductsList();
        setupEventHandlers();
        displayedProducts = FXCollections.observableArrayList(allProducts);
        applyAllFiltersAndSort();
        System.out.println("BrowseProductsController đã được khởi tạo.");
    }

    @Override
    public void setMainController(CustomerMainController mainController) {
        this.mainController = mainController;
        System.out.println("Đã thiết lập MainController cho BrowseProductsController.");
    }

    private void initializeProductsList() {
        allProducts = new ArrayList<>();
        allProducts.add(new Product("My Hero Academia", "Kohei Horikoshi", 29.99, "Fiction", "/images/book1.jpg"));
        allProducts.add(new Product("Dragon Ball Super", "Toyotarou", 24.99, "Fiction", "/images/book2.jpg"));
        allProducts.add(new Product("Rent A Girlfriend", "Miyajima Reiji", 19.99, "Fiction", "/images/book3.jpg"));
        allProducts.add(new Product("Harry Potter", "J.K.Rowling", 34.99, "Fiction", "/images/book4.jpg"));
        allProducts.add(new Product("Huyền Thoại Cổ Ngọc", "Ocean Nguyễn", 22.99, "Fiction", "/images/book5.jpg"));
        allProducts.add(new Product("Thiên Long Bát Bộ", "Kim Dung", 39.99, "Fiction", "/images/book6.jpg"));
        allProducts.add(new Product("Sapiens: A Brief History of Humankind", "Yuval Noah Harari", 18.50, "Non-Fiction", "/images/book7.jpg"));
        allProducts.add(new Product("Cosmos", "Carl Sagan", 15.00, "Science", "/images/book8.jpg"));
        allProducts.add(new Product("Clean Code", "Robert C. Martin", 40.00, "Programming", "/images/book9.jpg"));
        allProducts.add(new Product("The Guns of August", "Barbara W. Tuchman", 20.00, "History", "/images/book10.jpg"));
        allProducts.add(new Product("Becoming", "Michelle Obama", 25.00, "Non-Fiction", "/images/book11.jpg"));

    }

    private void setupEventHandlers() {
        searchField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                handleSearch();
            }
        });
        fictionCheckBox.setOnAction(e -> handleCategoryFilter());
        nonFictionCheckBox.setOnAction(e -> handleCategoryFilter());
        scienceCheckBox.setOnAction(e -> handleCategoryFilter());
        historyCheckBox.setOnAction(e -> handleCategoryFilter());
        programmingCheckBox.setOnAction(e -> handleCategoryFilter());


        if (sortGroup != null) {
            sortGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null) {
                    handleSort();
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
                showAlert("Lỗi", "Giá tối thiểu không thể lớn hơn giá tối đa!");
                return;
            }
            applyAllFiltersAndSort();
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Vui lòng nhập số hợp lệ cho giá!");
        }
    }

    @FXML
    private void handleSort() {
        if (sortGroup.getSelectedToggle() == null) {
            System.out.println("Chưa chọn tiêu chí sắp xếp.");
            return;
        }
        applyAllFiltersAndSort();
    }

    private void applyAllFiltersAndSort() {
        List<Product> currentProducts = new ArrayList<>(allProducts);
        String searchQuery = searchField.getText().trim().toLowerCase();
        if (!searchQuery.isEmpty()) {
            currentProducts.removeIf(product -> !(product.getTitle().toLowerCase().contains(searchQuery) ||
                                                  product.getAuthor().toLowerCase().contains(searchQuery)));
        }
        List<String> selectedCategories = new ArrayList<>();
        if (fictionCheckBox.isSelected()) selectedCategories.add("Fiction");
        if (nonFictionCheckBox.isSelected()) selectedCategories.add("Non-Fiction");
        if (scienceCheckBox.isSelected()) selectedCategories.add("Science");
        if (historyCheckBox.isSelected()) selectedCategories.add("History");
        if (programmingCheckBox.isSelected()) selectedCategories.add("Programming");
        if (!selectedCategories.isEmpty()) {
            currentProducts.removeIf(product -> !selectedCategories.contains(product.getCategory()));
        }
        String minPriceText = minPriceField.getText().trim();
        String maxPriceText = maxPriceField.getText().trim();
        try {
            double minPrice = minPriceText.isEmpty() ? 0 : Double.parseDouble(minPriceText);
            double maxPrice = maxPriceText.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxPriceText);
            if (minPrice <= maxPrice) {
                currentProducts.removeIf(product -> !(product.getPrice() >= minPrice && product.getPrice() <= maxPrice));
            }
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Vui lòng nhập số hợp lệ cho giá!");
        }
        if (sortGroup.getSelectedToggle() != null) {
            RadioButton selectedSort = (RadioButton) sortGroup.getSelectedToggle();
            String sortType = selectedSort.getText();
            switch (sortType) {
                case "Sort by Title":
                    Collections.sort(currentProducts, Comparator.comparing(Product::getTitle));
                    break;
                case "Sort by Cost":
                    Collections.sort(currentProducts, Comparator.comparing(Product::getPrice));
                    break;
            }
        }
        displayedProducts.setAll(currentProducts);
        updateProductDisplay();
        itemCountLabel.setText(String.valueOf(displayedProducts.size()));
    }

    private void updateProductDisplay() {
        productsGrid.getChildren().clear();
        int row = 0;
        int col = 0;
        final int maxCols = 3;
        for (Product product : displayedProducts) {
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
        ImageView imageView = new ImageView();
        imageView.setFitHeight(160);
        imageView.setFitWidth(120);
        imageView.setPreserveRatio(true);
        try {
            Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
            imageView.setImage(image.isError() ? getPlaceholderImage() : image);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh: " + product.getImagePath() + " - " + e.getMessage());
            imageView.setImage(getPlaceholderImage());
        }
        imageView.setOnMouseClicked(e -> handleViewProductDetails(product));
        imageView.setStyle("-fx-cursor: hand;");
        Label titleLabel = new Label(product.getTitle());
        titleLabel.getStyleClass().add("product-title");
        titleLabel.setWrapText(true);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label authorLabel = new Label("by " + product.getAuthor());
        authorLabel.getStyleClass().add("product-author");
        Label priceLabel = new Label(String.format("%.2f USD", product.getPrice()));
        priceLabel.getStyleClass().add("product-price");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setOnAction(e -> handleAddToCart(product));
        productCard.getChildren().addAll(imageView, titleLabel, authorLabel, priceLabel, addToCartBtn);
        return productCard;
    }

    private Image getPlaceholderImage() {
        try {
            return new Image(getClass().getResourceAsStream("/images/placeholder.png"));
        } catch (Exception e) {
            System.err.println("Không tìm thấy placeholder.png, trả về ảnh trống.");
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
        }
    }

    private void handleAddToCart(Product product) {
        if (mainController == null || !mainController.isUserLoggedIn()) {
            showAlert("Thông báo", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return;
        }
        ObservableList<CartItem> sharedCartItems = mainController.getCartItems();
        CartItem existingItem = sharedCartItems.stream()
            .filter(item -> item.getProduct().equals(product))
            .findFirst()
            .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1);
        } else {
            sharedCartItems.add(new CartItem(product, 1));
        }
        System.out.println("Đã thêm sản phẩm '" + product.getTitle() + "' vào giỏ hàng.");
        showAlert("Thành công", "Đã thêm sản phẩm '" + product.getTitle() + "' vào giỏ hàng!");
    }

    private void handleViewProductDetails(Product product) {
        System.out.println("Chuyển đến trang chi tiết sản phẩm: " + product.getTitle());
        if (mainController != null) {
            try {
                mainController.loadPageWithData("/view/customer/Store/ViewDetails/ViewBookDetails.fxml", product);
            } catch (Exception e) {
                System.err.println("Lỗi khi chuyển đến trang chi tiết: " + e.getMessage());
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
        searchField.clear();
        minPriceField.clear();
        maxPriceField.clear();
        if (fictionCheckBox != null) fictionCheckBox.setSelected(false);
        if (nonFictionCheckBox != null) nonFictionCheckBox.setSelected(false);
        if (scienceCheckBox != null) scienceCheckBox.setSelected(false);
        if (historyCheckBox != null) historyCheckBox.setSelected(false);
        if (programmingCheckBox != null) {
            programmingCheckBox.setSelected(false);
        }
        if (sortGroup != null && sortGroup.getSelectedToggle() != null) {
            sortGroup.getSelectedToggle().setSelected(false);
        }
        applyAllFiltersAndSort();
        System.out.println("Đã làm mới toàn bộ dữ liệu sản phẩm:");
    }
}