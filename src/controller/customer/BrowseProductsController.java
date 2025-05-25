// File: BrowseProductsController
package controller.customer;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;

public class BrowseProductsController implements SubController, Initializable {

    // Các thành phần giao diện từ FXML
    @FXML private TextField searchField;                    // Ô tìm kiếm sản phẩm
    @FXML private Button searchButton;                      // Nút tìm kiếm
    @FXML private TextField minPriceField;                  // Ô nhập giá tối thiểu
    @FXML private TextField maxPriceField;                  // Ô nhập giá tối đa
    @FXML private ComboBox<String> viewModeComboBox;        // ComboBox chọn chế độ hiển thị
    @FXML private Button notifyNotFoundButton;              // Nút thông báo không tìm thấy
    @FXML private GridPane productsGrid;                    // Lưới hiển thị sản phẩm
    @FXML private ScrollPane productScrollPane;             // Khu vực cuộn sản phẩm
    @FXML private ToggleGroup sortGroup;                    // Nhóm radio button sắp xếp
    
    // Các checkbox lọc theo danh mục
    @FXML private CheckBox fictionCheckBox;
    @FXML private CheckBox nonFictionCheckBox; 
    @FXML private CheckBox scienceCheckBox;
    @FXML private CheckBox historyCheckBox;
    @FXML private CheckBox programmingCheckBox;
    
    private CustomerMainController mainController;           // Tham chiếu đến controller chính
    private ObservableList<String> products;                // Danh sách sản phẩm hiện tại
    private String currentSearchQuery = "";                 // Từ khóa tìm kiếm hiện tại
    private boolean isGridView = true;                      // Chế độ hiển thị (true: lưới, false: danh sách)

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Khởi tạo dữ liệu mặc định
        initializeViewModeComboBox();
        initializeProductsList();
        setupEventHandlers();
        
        System.out.println("BrowseProductsController đã được khởi tạo");
    }

    @Override
    public void setMainController(CustomerMainController mainController) {
        this.mainController = mainController;
        
        // Thêm logic khởi tạo nếu cần
        if (mainController.isUserLoggedIn()) {
            // Ví dụ: Hiển thị thêm tính năng khi đã đăng nhập
            notifyNotFoundButton.setVisible(true);
            System.out.println("Người dùng đã đăng nhập - Hiển thị đầy đủ tính năng");
        } else {
            // Ẩn một số tính năng khi chưa đăng nhập
            notifyNotFoundButton.setVisible(false);
            System.out.println("Người dùng chưa đăng nhập - Hạn chế một số tính năng");
        }
    }

    /**
     * Khởi tạo ComboBox chế độ hiển thị
     */
    private void initializeViewModeComboBox() {
        viewModeComboBox.setItems(FXCollections.observableArrayList("Grid View", "List View"));
        viewModeComboBox.setValue("Grid View"); // Mặc định là chế độ lưới
    }

    /**
     * Khởi tạo danh sách sản phẩm mẫu
     */
    private void initializeProductsList() {
        products = FXCollections.observableArrayList();
        // Thêm dữ liệu sản phẩm mẫu
        products.addAll("Book Title 1", "Book Title 2", "Book Title 3", 
                       "Programming Guide", "Science Fiction Novel", "History Book");
    }

    /**
     * Thiết lập các event handler
     */
    private void setupEventHandlers() {
        // Xử lý khi thay đổi chế độ hiển thị
        viewModeComboBox.setOnAction(e -> handleViewModeChange());
        
        // Xử lý tìm kiếm khi nhấn Enter trong ô tìm kiếm
        searchField.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                handleSearch();
            }
        });
    }

    /**
     * Xử lý sự kiện tìm kiếm sản phẩm
     */
    @FXML
    private void handleSearch() {
        String searchQuery = searchField.getText().trim();
        currentSearchQuery = searchQuery;
        
        if (searchQuery.isEmpty()) {
            System.out.println("Hiển thị tất cả sản phẩm");
            // Logic hiển thị tất cả sản phẩm
            loadAllProducts();
        } else {
            System.out.println("Đang tìm kiếm: " + searchQuery);
            // Logic tìm kiếm sản phẩm
            searchProducts(searchQuery);
        }
        
        updateProductDisplay();
    }

    /**
     * Xử lý sự kiện lọc theo danh mục
     */
    @FXML
    private void handleCategoryFilter() {
        System.out.println("Áp dụng bộ lọc danh mục");
        
        // Lấy các danh mục được chọn
        StringBuilder selectedCategories = new StringBuilder("Danh mục được chọn: ");
        
        // Kiểm tra từng checkbox (cần thêm fx:id cho các checkbox trong FXML)
        if (fictionCheckBox != null && fictionCheckBox.isSelected()) {
            selectedCategories.append("Fiction, ");
        }
        if (nonFictionCheckBox != null && nonFictionCheckBox.isSelected()) {
            selectedCategories.append("Non-Fiction, ");
        }
        if (scienceCheckBox != null && scienceCheckBox.isSelected()) {
            selectedCategories.append("Science, ");
        }
        if (historyCheckBox != null && historyCheckBox.isSelected()) {
            selectedCategories.append("History, ");
        }
        if (programmingCheckBox != null && programmingCheckBox.isSelected()) {
            selectedCategories.append("Programming, ");
        }
        
        System.out.println(selectedCategories.toString());
        
        // Logic lọc sản phẩm theo danh mục
        filterProductsByCategory();
        updateProductDisplay();
    }

    /**
     * Xử lý sự kiện lọc theo giá
     */
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
            
            System.out.println("Lọc theo giá: " + minPrice + " - " + maxPrice);
            
            // Logic lọc sản phẩm theo giá
            filterProductsByPrice(minPrice, maxPrice);
            updateProductDisplay();
            
        } catch (NumberFormatException e) {
            showAlert("Lỗi", "Vui lòng nhập số hợp lệ cho giá!");
        }
    }

    /**
     * Xử lý sự kiện sắp xếp sản phẩm
     */
    @FXML
    private void handleSort() {
        if (sortGroup.getSelectedToggle() == null) {
            System.out.println("Chưa chọn tiêu chí sắp xếp");
            return;
        }
        
        RadioButton selectedSort = (RadioButton) sortGroup.getSelectedToggle();
        String sortType = selectedSort.getText();
        
        System.out.println("Sắp xếp theo: " + sortType);
        
        switch (sortType) {
            case "Sort by Title":
                sortProductsByTitle();
                break;
            case "Sort by Cost":
                sortProductsByCost();
                break;
            case "Sort by Rating":
                sortProductsByRating();
                break;
            default:
                System.out.println("Không xác định được tiêu chí sắp xếp");
        }
        
        updateProductDisplay();
    }

    /**
     * Xử lý thay đổi chế độ hiển thị
     */
    private void handleViewModeChange() {
        String selectedMode = viewModeComboBox.getValue();
        
        if ("Grid View".equals(selectedMode)) {
            isGridView = true;
            System.out.println("Chuyển sang chế độ hiển thị lưới");
        } else if ("List View".equals(selectedMode)) {
            isGridView = false;
            System.out.println("Chuyển sang chế độ hiển thị danh sách");
        }
        
        updateProductDisplay();
    }

    /**
     * Xử lý sự kiện thông báo không tìm thấy
     */
    @FXML
    private void handleNotifyNotFound() {
        if (mainController == null || !mainController.isUserLoggedIn()) {
            showAlert("Thông báo", "Vui lòng đăng nhập để sử dụng tính năng này!");
            return;
        }
        
        System.out.println("Đã kích hoạt thông báo không tìm thấy sản phẩm");
        
        // Logic thông báo khi không tìm thấy sản phẩm
        String message = "Chúng tôi sẽ thông báo cho bạn khi có sản phẩm phù hợp";
        if (!currentSearchQuery.isEmpty()) {
            message += " với từ khóa: '" + currentSearchQuery + "'";
        }
        
        showAlert("Thông báo", message);
        
        // Có thể thêm logic lưu thông tin để thông báo sau
        saveNotificationRequest(currentSearchQuery);
    }

    /**
     * Xử lý sự kiện thêm vào giỏ hàng
     */
    @FXML
    private void handleAddToCart() {
        if (mainController == null || !mainController.isUserLoggedIn()) {
            showAlert("Thông báo", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return;
        }
        
        System.out.println("Đã thêm sản phẩm vào giỏ hàng");
        showAlert("Thành công", "Đã thêm sản phẩm vào giỏ hàng!");
        
        // Logic thêm sản phẩm vào giỏ hàng
        // mainController.addToCart(product);
    }

    // Các phương thức hỗ trợ
    
    /**
     * Tải tất cả sản phẩm
     */
    private void loadAllProducts() {
        // Logic tải tất cả sản phẩm từ database hoặc service
        System.out.println("Đang tải tất cả sản phẩm...");
    }

    /**
     * Tìm kiếm sản phẩm theo từ khóa
     */
    private void searchProducts(String query) {
        // Logic tìm kiếm sản phẩm
        System.out.println("Tìm kiếm sản phẩm với từ khóa: " + query);
    }

    /**
     * Lọc sản phẩm theo danh mục
     */
    private void filterProductsByCategory() {
        // Logic lọc theo danh mục
        System.out.println("Đang lọc sản phẩm theo danh mục...");
    }

    /**
     * Lọc sản phẩm theo giá
     */
    private void filterProductsByPrice(double minPrice, double maxPrice) {
        // Logic lọc theo giá
        System.out.println("Lọc sản phẩm trong khoảng giá: " + minPrice + " - " + maxPrice);
    }

    /**
     * Sắp xếp sản phẩm theo tên
     */
    private void sortProductsByTitle() {
        // Logic sắp xếp theo tên
        System.out.println("Sắp xếp sản phẩm theo tên");
    }

    /**
     * Sắp xếp sản phẩm theo giá
     */
    private void sortProductsByCost() {
        // Logic sắp xếp theo giá
        System.out.println("Sắp xếp sản phẩm theo giá");
    }

    /**
     * Sắp xếp sản phẩm theo đánh giá
     */
    private void sortProductsByRating() {
        // Logic sắp xếp theo đánh giá
        System.out.println("Sắp xếp sản phẩm theo đánh giá");
    }

    /**
     * Cập nhật hiển thị sản phẩm
     */
    private void updateProductDisplay() {
        if (isGridView) {
            displayProductsInGrid();
        } else {
            displayProductsInList();
        }
    }

    /**
     * Hiển thị sản phẩm dạng lưới
     */
    private void displayProductsInGrid() {
        System.out.println("Hiển thị sản phẩm dạng lưới");
        // Logic hiển thị dạng lưới
    }

    /**
     * Hiển thị sản phẩm dạng danh sách
     */
    private void displayProductsInList() {
        System.out.println("Hiển thị sản phẩm dạng danh sách");
        // Logic hiển thị dạng danh sách
    }

    /**
     * Lưu yêu cầu thông báo
     */
    private void saveNotificationRequest(String searchQuery) {
        // Logic lưu yêu cầu thông báo vào database
        System.out.println("Đã lưu yêu cầu thông báo cho: " + searchQuery);
    }

    /**
     * Hiển thị thông báo alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Làm mới dữ liệu trang
     */
    public void refreshPage() {
        searchField.clear();
        minPriceField.clear();
        maxPriceField.clear();
        viewModeComboBox.setValue("Grid View");
        currentSearchQuery = "";
        
        // Bỏ chọn tất cả checkbox và radio button
        if (sortGroup.getSelectedToggle() != null) {
            sortGroup.getSelectedToggle().setSelected(false);
        }
        
        loadAllProducts();
        updateProductDisplay();
        
        System.out.println("Đã làm mới trang duyệt sản phẩm");
    }
}