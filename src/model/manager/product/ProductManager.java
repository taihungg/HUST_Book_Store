package model.manager.product;

import static java.lang.Math.random;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.product.Product;
import model.product.Stationery;
import model.product.Toy;
import model.product.book.Audiobook;
import model.product.book.Ebook;
import model.product.book.Printbook;
import model.product.interfaces.PhysicalProduct;
import model.user.User;
import model.user.interfaces.Manager;

public class ProductManager {
	private final ObservableList<Product> productList;
	private final Map<String, Product> productMap;
	private final Map<String, Integer> productQuantity;

    // Sample data
	private final Random random = new Random();
    private final String[] COMMON_AUTHORS = {"Nguyễn Nhật Ánh", "J.K. Rowling", "Stephen King", "Yuval Noah Harari", "Haruki Murakami", "Nguyễn Du", "Tô Hoài", "Marc Levy", "Dan Brown"};
    private final String[] COMMON_PUBLISHERS = {"NXB Trẻ", "NXB Kim Đồng", "Penguin Random House", "HarperCollins", "NXB Văn Học", "Fahasa"};
    private final String[] COMMON_CATEGORIES_BOOK = {"Văn học", "Khoa học", "Lịch sử", "Trinh thám", "Kinh tế", "Thiếu nhi", "Self-help"};
    private final String[] COMMON_LANGUAGES = {"Tiếng Việt", "English", "French", "Japanese"};
    private final String[] COMMON_BRANDS_STATIONERY_TOY = {"Thiên Long", "Plus", "Colgate", "Biti's", "Lego", "Bandai", "Hasbro"};
    private final String[] STATIONERY_TYPES = {"Bút bi", "Sổ tay", "Tẩy", "Hộp bút", "Kẹp giấy", "Bút chì", "Thước kẻ"};
    private final String[] TOY_TYPES = {"Đồ chơi xếp hình", "Mô hình", "Búp bê", "Xe đồ chơi", "Thú bông", "Đồ chơi giáo dục"};
    private final String[] STATUS_OPTIONS = {"In Stock", "Available", "Low Stock", "New Arrival"};

	public ProductManager() {
		this.productList = FXCollections.observableArrayList();
		this.productMap = new HashMap<>();
		this.productQuantity = new HashMap<>();
		loadInitialProducts();
	}

	/**
     * Thêm một sản phẩm mới vào danh sách và map quản lý.
     * Đối với PhysicalProduct, thiết lập số lượng tồn kho ban đầu.
     *
     * @param product Sản phẩm cần thêm (có thuộc tính là Property).
     * @param initialQuantity Số lượng tồn kho ban đầu (chỉ áp dụng cho PhysicalProduct).
     * @return true nếu thêm thành công, false nếu ID đã tồn tại.
     */
    public boolean addProduct(Product product, int initialQuantity, User currentUser) {
        if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can add products.");
            return false;
        }
        else {
            if (productMap.containsKey(product.getId())) {
                System.out.println("Error: Product with ID " + product.getId() + " already exists.");
                return false;
            }
            productList.add(product); // Thêm vào ObservableList -> UI cập nhật
            productMap.put(product.getId(), product); // Thêm vào Map để tra cứu nhanh

            if (product instanceof PhysicalProduct) {
                if (initialQuantity < 0) {
                    throw new IllegalArgumentException("Initial quantity cannot be negative for physical product.");
                }
                productQuantity.put(product.getId(), initialQuantity); // Lưu số lượng tồn kho
            }
            System.out.println("Product added: " + product.getTitle() + " (ID: " + product.getId() + ")");
            return true;
        }
    }

	public boolean updateProduct(Product updatedProduct, User currentUser) {
        if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can update products.");
            return false;
        }
        // Trong trường hợp này, vì các thuộc tính của Product là Property
        // và UI đã two-way bind với chúng, đối tượng Product trong productList/productMap
        // (nếu nó là cùng một instance) đã tự động được cập nhật.
        // Phương thức này chủ yếu chỉ cần xác nhận sự tồn tại và có thể dùng để lưu vào DB (nếu có).
        if (productMap.containsKey(updatedProduct.getId())) {
            // Không cần làm gì với productMap/productList nếu updatedProduct chính là instance đang được giữ
            // (vì Property của nó đã được update).
            // Dòng dưới đây có thể cần thiết nếu bạn tạo một bản sao mới và muốn thay thế instance cũ
            // productMap.put(updatedProduct.getProductId(), updatedProduct);
            // int index = productList.indexOf(updatedProduct); // Tìm index của đối tượng (dựa trên equals())
            // if (index != -1) {
            //     productList.set(index, updatedProduct); // Thay thế để kích hoạt Event trên ObservableList
            // } else {
            //     // Rất hiếm khi xảy ra nếu logic thêm/xóa đúng
            //     System.err.println("Product updated in map but not found in list (ID: " + updatedProduct.getProductId() + ")");
            // }

            System.out.println("Product updated in memory: " + updatedProduct.getTitle() + " (ID: " + updatedProduct.getId() + ")");
            return true;
        }
        System.out.println("Error: Product with ID " + updatedProduct.getId() + " not found for update.");
        return false;
    }

	/**
     * Xóa một sản phẩm khỏi danh sách và map quản lý.
     * @param productId ID của sản phẩm cần xóa.
     * @return true nếu xóa thành công, false nếu không tìm thấy ID.
     */
    public boolean removeProduct(String productId, User currentUser) {
        if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can remove products.");
            return false;
        }
        else {
            Product productToRemove = productMap.get(productId);
            if (productToRemove != null) {
                productList.remove(productToRemove); // Xóa khỏi ObservableList -> UI cập nhật
                productMap.remove(productId);       // Xóa khỏi Map

                if (productToRemove instanceof PhysicalProduct) {
                    productQuantity.remove(productId); // Xóa số lượng tồn kho nếu là PhysicalProduct
                }
                System.out.println("Product removed: " + productToRemove.getTitle() + " (ID: " + productId + ")");
                return true;
            }
            System.out.println("Error: Product with ID " + productId + " not found for removal.");
            return false;
        }
    }

	/**
     * Giảm số lượng tồn kho của một sản phẩm vật lý.
     * @param productId ID của sản phẩm cần giảm số lượng.
     * @param amount Số lượng cần giảm. Phải là số dương.
     * @return true nếu giảm thành công, false nếu không đủ hàng, sản phẩm không phải vật lý, hoặc không tìm thấy.
     */
    public boolean decreaseProductStock(String productId, int amount, User currentUser) {
        if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can decrease product stock.");
            return false;
        }
        else {
            if (amount <= 0) {
                throw new IllegalArgumentException("Decrease amount must be positive.");
            }
            Product product = productMap.get(productId);
            if (product == null) {
                System.out.println("Error: Product with ID " + productId + " not found.");
                return false;
            }
            if (!(product instanceof PhysicalProduct)) {
                System.out.println("Error: Product " + product.getTitle() + " (ID: " + productId + ") is a digital product and does not have physical stock to decrease.");
                return false;
            }
            int currentQuantity = productQuantity.getOrDefault(productId, 0);
            if (currentQuantity < amount) {
                System.out.println("Stock Error for " + product.getTitle() + ": Not enough stock. Available: " + currentQuantity + ", trying to decrease by " + amount);
                return false;
            }
            productQuantity.put(productId, currentQuantity - amount);
            System.out.println("Decreased stock for " + product.getTitle() + " by " + amount + ". New stock: " + productQuantity.get(productId));
            return true;
        }
    }

	/**
     * Tăng số lượng tồn kho của một sản phẩm vật lý.
     * @param productId ID của sản phẩm cần tăng số lượng.
     * @param amount Số lượng cần tăng. Phải là số dương.
     * @return true nếu tăng thành công, false nếu sản phẩm không tìm thấy hoặc không phải vật lý.
     */
    public boolean increaseProductStock(String productId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Increase amount must be positive.");
        }
        Product p = productMap.get(productId);
        if (p == null) {
            System.out.println("Error: Product with ID " + productId + " not found.");
            return false;
        }
        if (!(p instanceof PhysicalProduct)) {
            System.out.println("Error: Product " + p.getTitle() + " (ID: " + productId + ") is a digital product and cannot have physical stock increased.");
            return false;
        }
        int currentQuantity = productQuantity.getOrDefault(productId, 0);
        productQuantity.put(productId, currentQuantity + amount);
        System.out.println("Increased stock for " + p.getTitle() + " by " + amount + ". New stock: " + productQuantity.get(productId));
        return true;
    }

	/**
     * Thiết lập số lượng tồn kho mới cho một sản phẩm vật lý.
     * @param productId ID của sản phẩm.
     * @param newQuantity Số lượng tồn kho mới.
     * @return true nếu thiết lập thành công, false nếu sản phẩm không tìm thấy, không phải vật lý, hoặc số lượng mới không hợp lệ.
     */
    public boolean setProductQuantity(String productId, int newQuantity, User currentUser) {
        if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can set product quantity.");
            return false;
        }
        else {
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Quantity cannot be negative.");
            }
            Product product = productMap.get(productId);
            if (product == null) {
                System.out.println("Error: Product with ID " + productId + " not found.");
                return false;
            }
            if (!(product instanceof PhysicalProduct)) {
                System.out.println("Error: Product " + product.getTitle() + " (ID: " + productId + ") is a digital product and cannot have stock set directly.");
                return false;
            }
            productQuantity.put(productId, newQuantity);
            System.out.println("Set new stock for " + product.getTitle() + " (ID: " + productId + ") to: " + newQuantity);
            return true;
        }
    }

	public Product getProductById(String productId) {
		return productMap.get(productId);
	}

	public int getProductQuantity(String productId) {
		return productQuantity.getOrDefault(productId, 0);
	}
    
	public ObservableList<Product> getAllProducts(User currentUser) {
		if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can get all products.");
            return null;
        }
		else return productList;
	}

	public ObservableList<Product> searchProducts(String keyword) {
		ObservableList<Product> filteredList = FXCollections.observableArrayList();
		for (Product product : productList) {
			if (product.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
				product.getId().toLowerCase().contains(keyword.toLowerCase())) {
				filteredList.add(product);
			}
		}
		return filteredList;
	}

    // Phương thức loadInitialProducts sẽ chứa các câu lệnh khởi tạo trực tiếp
    protected void loadInitialProducts() {
        System.out.println("Generating sample products...");

        // --- 1. Tạo 100 Printbook Samples ---
        for (int i = 0; i < 100; i++) {
            String id = "PB" + (1000 + i);
            String title = "Sách In - Tiêu đề " + (i + 1) + " (Tập " + (i % 5 + 1) + ")";
            String description = "Mô tả chi tiết cho cuốn sách in " + title + ". Đây là một cuốn sách hấp dẫn với nhiều nội dung giá trị.";
            String galleryURL = "https://example.com/images/pb" + (i + 1) + ".jpg";
            double sellingPrice = 50000.0 + random.nextDouble() * 150000.0; // 50k - 200k VND
            double purchasePrice = sellingPrice * (0.6 + random.nextDouble() * 0.1); // 60-70% giá bán
            double averageRating = 3.5 + random.nextDouble() * 1.5; // 3.5 - 5.0
            int numberOfReviews = random.nextInt(500);
            String status = STATUS_OPTIONS[random.nextInt(STATUS_OPTIONS.length)];
            String isbn = "978-604-0-" + String.format("%04d", i) + "-X";
            String author = COMMON_AUTHORS[random.nextInt(COMMON_AUTHORS.length)];
            String publisher = COMMON_PUBLISHERS[random.nextInt(COMMON_PUBLISHERS.length)];
            String category = COMMON_CATEGORIES_BOOK[random.nextInt(COMMON_CATEGORIES_BOOK.length)];
            String language = COMMON_LANGUAGES[random.nextInt(COMMON_LANGUAGES.length)];
            int numberOfPages = 150 + random.nextInt(600); // 150 - 750 trang
            int weight = 300 + random.nextInt(1000); // 300 - 1300 gram

            Printbook printbook = new Printbook(
                id, title, description, galleryURL, sellingPrice, purchasePrice,
                averageRating, numberOfReviews, status, isbn, author, publisher,
                category, language, numberOfPages, weight
            );
            productList.add(printbook);
            productMap.put(id, printbook);
            productQuantity.put(id, 10 + random.nextInt(40)); // 10-50 bản tồn kho
        }

        // --- 2. Tạo 100 Ebook Samples ---
        for (int i = 0; i < 100; i++) {
            String id = "EB" + (1000 + i);
            String title = "Ebook - Tiêu đề " + (i + 1) + " (Series " + (i % 3 + 1) + ")";
            String description = "Mô tả chi tiết cho ebook " + title + ". Đọc mọi lúc mọi nơi trên thiết bị của bạn.";
            String galleryURL = "https://example.com/images/eb" + (i + 1) + ".jpg";
            double sellingPrice = 30000.0 + random.nextDouble() * 100000.0; // 30k - 130k VND
            double purchasePrice = sellingPrice * (0.4 + random.nextDouble() * 0.1); // 40-50% giá bán
            double averageRating = 3.0 + random.nextDouble() * 2.0;
            int numberOfReviews = random.nextInt(300);
            String status = "Available Online";
            String isbn = "978-999-0-" + String.format("%04d", i) + "-Y";
            String author = COMMON_AUTHORS[random.nextInt(COMMON_AUTHORS.length)];
            String publisher = COMMON_PUBLISHERS[random.nextInt(COMMON_PUBLISHERS.length)];
            String category = COMMON_CATEGORIES_BOOK[random.nextInt(COMMON_CATEGORIES_BOOK.length)];
            String language = COMMON_LANGUAGES[random.nextInt(COMMON_LANGUAGES.length)];
            int numberOfPages = 80 + random.nextInt(500);
            String downloadURL = "https://downloads.example.com/ebook" + (i + 1) + ".pdf";

            Ebook ebook = new Ebook(
                id, title, description, galleryURL, sellingPrice, purchasePrice,
                averageRating, numberOfReviews, status, isbn, author, publisher,
                category, language, numberOfPages, downloadURL
            );
            productList.add(ebook);
            productMap.put(id, ebook);
            productQuantity.put(id, 0); // Ebook không có tồn kho vật lý
        }

        // --- 3. Tạo 100 Audiobook Samples ---
        for (int i = 0; i < 100; i++) {
            String id = "AB" + (1000 + i);
            String title = "Sách Nói - Tiêu đề " + (i + 1) + " (Chương " + (i % 7 + 1) + ")";
            String description = "Mô tả chi tiết cho sách nói " + title + ". Nghe sách mọi lúc mọi nơi.";
            String galleryURL = "https://example.com/images/ab" + (i + 1) + ".jpg";
            double sellingPrice = 40000.0 + random.nextDouble() * 120000.0; // 40k - 160k VND
            double purchasePrice = sellingPrice * (0.5 + random.nextDouble() * 0.1); // 50-60% giá bán
            double averageRating = 3.8 + random.nextDouble() * 1.2;
            int numberOfReviews = random.nextInt(250);
            String status = "Streaming Available";
            String isbn = "978-000-0-" + String.format("%04d", i) + "-Z";
            String author = COMMON_AUTHORS[random.nextInt(COMMON_AUTHORS.length)];
            String publisher = COMMON_PUBLISHERS[random.nextInt(COMMON_PUBLISHERS.length)];
            String category = COMMON_CATEGORIES_BOOK[random.nextInt(COMMON_CATEGORIES_BOOK.length)];
            String language = COMMON_LANGUAGES[random.nextInt(COMMON_LANGUAGES.length)];
            String downloadURL = "https://downloads.example.com/audiobook" + (i + 1) + ".mp3";

            Audiobook audiobook = new Audiobook(
                id, title, description, galleryURL, sellingPrice, purchasePrice,
                averageRating, numberOfReviews, status, isbn, author, publisher,
                category, language, downloadURL
            );
            productList.add(audiobook);
            productMap.put(id, audiobook);
            productQuantity.put(id, 0); // Audiobook không có tồn kho vật lý
        }

        // --- 4. Tạo 100 Stationery Samples ---
        for (int i = 0; i < 100; i++) {
            String id = "ST" + (1000 + i);
            String title = STATIONERY_TYPES[random.nextInt(STATIONERY_TYPES.length)] + " " + (i + 1);
            String description = "Sản phẩm văn phòng phẩm chất lượng cao: " + title + ".";
            String galleryURL = "https://example.com/images/st" + (i + 1) + ".jpg";
            double sellingPrice = 5000.0 + random.nextDouble() * 45000.0; // 5k - 50k VND
            double purchasePrice = sellingPrice * (0.3 + random.nextDouble() * 0.2); // 30-50% giá bán
            double averageRating = 3.0 + random.nextDouble() * 2.0;
            int numberOfReviews = random.nextInt(120);
            String status = STATUS_OPTIONS[random.nextInt(STATUS_OPTIONS.length)];
            String brand = COMMON_BRANDS_STATIONERY_TOY[random.nextInt(COMMON_BRANDS_STATIONERY_TOY.length)];
            String type = STATIONERY_TYPES[random.nextInt(STATIONERY_TYPES.length)];

            Stationery stationery = new Stationery(
                id, title, description, galleryURL, sellingPrice, purchasePrice,
                averageRating, numberOfReviews, status, brand, type
            );
            productList.add(stationery);
            productMap.put(id, stationery);
            productQuantity.put(id, 20 + random.nextInt(80)); // 20-100 bản tồn kho
        }

        // --- 5. Tạo 100 Toy Samples ---
        for (int i = 0; i < 100; i++) {
            String id = "TOY" + (1000 + i);
            String title = TOY_TYPES[random.nextInt(TOY_TYPES.length)] + " " + (i + 1);
            String description = "Đồ chơi vui nhộn và bổ ích cho bé yêu: " + title + ".";
            String galleryURL = "https://example.com/images/toy" + (i + 1) + ".jpg";
            double sellingPrice = 100000.0 + random.nextDouble() * 400000.0; // 100k - 500k VND
            double purchasePrice = sellingPrice * (0.5 + random.nextDouble() * 0.15); // 50-65% giá bán
            double averageRating = 4.0 + random.nextDouble() * 1.0; // 4.0 - 5.0
            int numberOfReviews = random.nextInt(300);
            String status = STATUS_OPTIONS[random.nextInt(STATUS_OPTIONS.length)];
            String brand = COMMON_BRANDS_STATIONERY_TOY[random.nextInt(COMMON_BRANDS_STATIONERY_TOY.length)];
            int suitableAge = 2 + random.nextInt(10); // 2 - 11 tuổi

            Toy toy = new Toy(
                id, title, description, galleryURL, sellingPrice, purchasePrice,
                averageRating, numberOfReviews, status, brand, suitableAge
            );
            productList.add(toy);
            productMap.put(id, toy);
            productQuantity.put(id, 5 + random.nextInt(20)); // 5-25 bản tồn kho
        }
    }
}
