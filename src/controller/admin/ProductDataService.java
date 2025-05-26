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
            "T001", "Xe Đua Công Thức 1", "Mô tả xe đua...", "data:image/jpeg;base64,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",
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
