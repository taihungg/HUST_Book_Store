package controller.admin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.product.Product;    // Import lớp Product cha
import model.product.Toy;        // Import các lớp con nếu bạn tạo mẫu ở đây
import model.product.Stationery; 

public class ProductDataService {
    private static ProductDataService instance; // Thể hiện duy nhất của service
    private ObservableList<Product> productList; // Danh sách sản phẩm quan sát được
    private ProductDataService() {
        productList = FXCollections.observableArrayList();
        // Bạn có thể tải dữ liệu ban đầu từ tệp hoặc CSDL ở đây nếu muốn
        loadInitialSampleData(); // Ví dụ: tải dữ liệu mẫu
    }
    public static synchronized ProductDataService getInstance() {
        if (instance == null) {
            instance = new ProductDataService();
        }
        return instance;
    }
    public ObservableList<Product> getProductList() {
        return productList;
    }
    public void addProduct(Product product) {
        if (product != null) {
            productList.add(product);
            System.out.println("ProductDataService: Đã thêm sản phẩm '" + product.getTitle() + "'. Tổng số: " + productList.size());
        }
    }
    public void removeProduct(Product product) {
        if (product != null) {
            boolean removed = productList.remove(product);
            if (removed) {
                System.out.println("ProductDataService: Đã xóa sản phẩm '" + product.getTitle() + "'. Tổng số: " + productList.size());
            } else {
                System.out.println("ProductDataService: Không tìm thấy sản phẩm '" + product.getTitle() + "' để xóa.");
            }
        }
    }

    private void loadInitialSampleData() {
        // Sử dụng các constructor thực tế của Toy và Stationery bạn đã cung cấp
        // Ví dụ với Toy (11 tham số)
        productList.add(new Toy(
            "T001", "Xe Đua Công Thức 1", "Mô tả xe đua...", "url_xedua.jpg",
            250000.0, 200000.0, 4.5, 100, "Còn hàng",
            "LEGO Speed", 7
        ));

        // Ví dụ với Stationery (11 tham số)
        productList.add(new Stationery(
            "S001", "Bút Bi Thiên Long", "Mô tả bút bi...", "url_butbi.jpg",
            5000.0, 3000.0, 4.2, 200, "Còn hàng",
            "Thiên Long", "Bút bi"
        ));
        // Thêm 2 đối tượng mẫu bạn đã có trong StoreController.initialize() trước đó
         productList.add(new Stationery(
            "ST001",                                     // id (String)
            "Bút Chì Kim Pentel P205",                    // title (String)
            "Bút chì kim cơ học 0.5mm, thân nhựa, bền.",    // description (String)
            "url_pentel_p205.jpg",                       // galleryURL (String)
            75000.0,                                     // sellingPrice (double)
            50000.0,                                     // purchasePrice (double)
            4.7,                                         // averageRating (double)
            150,                                         // numberOfReviews (int)
            "Còn hàng",                                  // status (String)
            "Pentel",                                    // brand (String)
            "Bút chì kim"                                // type (String)
        ));
        productList.add(new Stationery(
            "S002",                                     // id (String)
            "Bộ Bút Chì Màu Hộp Sắt 24 Màu",            // title (String)
            "Bộ bút chì màu chất lượng cao, màu sắc tươi sáng, hộp sắt bền đẹp.", // description (String)
            "http://example.com/images/color_pencils_set.jpg", // galleryURL (String)
            180000.0,                                   // sellingPrice (double)
            120000.0,                                   // purchasePrice (double)
            4.6,                                        // averageRating (double)
            95,                                         // numberOfReviews (int)
            "Còn hàng",                                 // status (String)
            "Marco Raffine",                            // brand (String)
            "Bút chì màu"                               // type (String)
        ));

        System.out.println("ProductDataService: Đã tải dữ liệu mẫu ban đầu. Tổng số: " + productList.size());
    }

    
}
