package model.manager.product;

import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Predicate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.product.Product;
import model.product.Stationery;
import model.product.Toy;
import model.product.book.Audiobook;
import model.product.book.Ebook;
import model.product.book.Printbook;
import model.product.interfaces.DigitalProduct;
import model.product.interfaces.PhysicalProduct;
import model.user.User;
import model.user.interfaces.Manager;

public class ProductManager {
	private final ObservableList<Product> productList;
	private final Map<String, Product> productMap;
	private final Map<String, Integer> productQuantity;
    private final FilteredList<Product> availableProducts;

	public ProductManager() {
		this.productList = FXCollections.observableArrayList();
		this.productMap = new HashMap<>();
		this.productQuantity = new HashMap<>();
        this.availableProducts = new FilteredList<>(productList, new Predicate<Product>() {
            @Override
            public boolean test(Product product) {
                return isProductAvailableForShop(product);
            }
        });

        productList.addListener(new javafx.collections.ListChangeListener<Product>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends Product> change) {
                // Yêu cầu "Danh sách Hiển thị" kiểm tra lại tất cả các sản phẩm
                availableProducts.setPredicate(new Predicate<Product>() {
                    @Override
                    public boolean test(Product product) {
                        return isProductAvailableForShop(product);
                    }
                });
            }
        });

        loadSampleData();
	}

    private boolean isProductAvailableForShop(Product product) {
        return "In Stock".equalsIgnoreCase(product.getStatus()) 
        || "Available".equalsIgnoreCase(product.getStatus()) 
        || "Coming soon".equalsIgnoreCase(product.getStatus());
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

            if (product instanceof PhysicalProduct) {
                if (initialQuantity < 0) {
                    throw new IllegalArgumentException("Initial quantity cannot be negative for physical product.");
                }
                productList.add(product); // Thêm vào ObservableList -> UI cập nhật
                productMap.put(product.getId(), product); // Thêm vào Map để tra cứu nhanh
                productQuantity.put(product.getId(), initialQuantity); // Lưu số lượng tồn kho
                System.out.println("Product added: " + product.getTitle() + " (ID: " + product.getId() + ")");
                return true;
            }
            else {
                productList.add(product); // Thêm vào ObservableList -> UI cập nhật
                productMap.put(product.getId(), product); // Thêm vào Map để tra cứu nhanh
                System.out.println("Product added: " + product.getTitle() + " (ID: " + product.getId() + ")");
                return true;
            }
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
            if (productQuantity.get(productId) == 0) {
                // Nếu số lượng về 0, cập nhật trạng thái của đối tượng Product
                if (!"Out of Stock".equalsIgnoreCase(product.getStatus())) { // Tránh cập nhật nếu đã Out of Stock
                    product.setStatus("Out of Stock");
                    System.out.println("Cập nhật trạng thái của '" + product.getTitle() + "' thành 'Out of Stock' do hết hàng.");
                }
            }
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
    public boolean increaseProductStock(String productId, int amount, User currentUser) {
        if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can increase product stock.");
            return false;
        }
        else {
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
            if (productQuantity.get(productId) == 0) {
                // Nếu số lượng về 0, cập nhật trạng thái của đối tượng Product
                if (!"Out of Stock".equalsIgnoreCase(product.getStatus())) { // Tránh cập nhật nếu đã Out of Stock
                    product.setStatus("Out of Stock");
                    System.out.println("Cập nhật trạng thái của '" + product.getTitle() + "' thành 'Out of Stock' do hết hàng.");
                }
            }
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

    // --- Phương thức lấy danh sách sản phẩm cho khách hàng (Store Front) ---
    public ObservableList<Product> getAvailableProductsForCustomer() {
        return availableProducts;
    }

    //chỗ lấy tất cả sản phẩm trong store của manager thì gọi hàm này
	public ObservableList<Product> getAllProductsForManager(User currentUser) {
		if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can get all products.");
            return FXCollections.observableArrayList(); // Return empty list instead of null
        }
		else return productList;
	}

    //chỗ search trong store của customer thì gọi hàm này
	public ObservableList<Product> searchProductsForCustomer(String keyword) {
		ObservableList<Product> filteredList = FXCollections.observableArrayList();
		for (Product product : availableProducts) {
			if (product.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
				product.getId().toLowerCase().contains(keyword.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
				filteredList.add(product);
			}
		}
		return filteredList;
	}

    //chỗ search trong store của manager thì gọi hàm này
    public ObservableList<Product> searchProductsForManager(String keyword, User currentUser) {
		if(!(currentUser instanceof Manager)) {
            System.out.println("Error: Only managers can search products.");
            return FXCollections.observableArrayList(); // Return empty list instead of null
        }
		else {
            ObservableList<Product> filteredList = FXCollections.observableArrayList();
            for (Product product : productList) {
			if (product.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
				product.getId().toLowerCase().contains(keyword.toLowerCase()) ||
                product.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
				filteredList.add(product);
				}
			}
			return filteredList;
		}
	}

    private void loadSampleData() {
        Audiobook audiobooks1 = new Audiobook(
            "AB001", "Harry Potter and the Sorcerer's Stone (Audio)", "The first book in the Harry Potter series, narrated by Jim Dale.",
            "@../../../../images/ab1.jpg", 15.99, 8.00, "Available",
            "978-0739326006", "J.K. Rowling", "Pottermore Publishing", "Fantasy", "English", "http://example.com/hp1_audio.mp3"
        );
        Audiobook audiobooks2 = new Audiobook(
            "AB002", "Dế Mèn Phiêu Lưu Ký (Sách nói)", "Cuộc phiêu lưu của chú Dế Mèn qua thế giới côn trùng.",
            "@../../../../images/ab2.jpg", 7.50, 4.00, "Available",
            "978-6045610029", "Tô Hoài", "Nhà xuất bản Kim Đồng", "Truyện thiếu nhi", "Tiếng Việt", "http://example.com/demen_audio.mp3"
        );
        Audiobook audiobooks3 = new Audiobook(
            "AB003", "Atomic Habits (Audio)", "Tiny Changes, Remarkable Results. An easy and proven way to build good habits.",
            "@../../../../images/ab3.jpg", 12.99, 6.50, "Available",
            "978-0593181822", "James Clear", "Penguin Audio", "Self-help", "English", "http://example.com/atomic_habits_audio.mp3"
        );
        Audiobook audiobooks4 = new Audiobook(
            "AB004", "Nhà Giả Kim (Sách nói)", "Câu chuyện truyền cảm hứng về việc theo đuổi ước mơ.",
            "@../../../../images/ab4.jpg", 8.99, 4.50, "Out of Stock", // Trạng thái hết hàng
            "978-6049987826", "Paulo Coelho", "Nhà xuất bản Văn Học", "Triết học", "Tiếng Việt", "http://example.com/nhagia_kim_audio.mp3"
        );
        Audiobook audiobooks5 = new Audiobook(
            "AB005", "The Lord of the Rings: The Fellowship of the Ring (Audio)", "The epic first volume of Tolkien's high-fantasy saga.",
            "@../../../../images/ab5.jpg", 18.00, 9.00, "Available",
            "978-0618059039", "J.R.R. Tolkien", "HarperCollins Publishers", "Fantasy", "English", "http://example.com/lotr_audio.mp3"
        );
        Audiobook audiobooks6 = new Audiobook(
            "AB006", "Sapiens: Lược Sử Loài Người (Sách nói)", "Khám phá lịch sử phát triển của loài người.",
            "@../../../../images/ab6.jpg", 14.00, 7.00, "Available",
            "978-6045620004", "Yuval Noah Harari", "NXB Thế Giới", "Lịch sử", "Tiếng Việt", "http://example.com/sapiens_audio.mp3"
        );
        Audiobook audiobooks7 = new Audiobook(
            "AB007", "Dune (Audiobook)", "A landmark science fiction novel about politics, religion, and ecology.",
            "@../../../../images/ab7.jpg", 17.50, 8.75, "Available",
            "978-1524794269", "Frank Herbert", "Macmillan Audio", "Science Fiction", "English", "http://example.com/dune_audio.mp3"
        );
        Audiobook audiobooks8 = new Audiobook(
            "AB008", "Đắc Nhân Tâm (Sách nói)", "Những nguyên tắc vàng trong giao tiếp và ứng xử.",
            "@../../../../images/ab8.jpg", 9.50, 4.75, "Available",
            "978-6045610012", "Dale Carnegie", "NXB Trẻ", "Kỹ năng sống", "Tiếng Việt", "http://example.com/dacnhantam_audio.mp3"
        );
        Audiobook audiobooks9 = new Audiobook(
            "AB009", "The Midnight Library (Audio)", "A magical story about second chances and parallel lives.",
            "@../../../../images/ab9.jpg", 11.99, 6.00, "Discontinued", // Trạng thái ngừng kinh doanh
            "978-0593340578", "Matt Haig", "Viking Audio", "Fantasy", "English", "http://example.com/midnight_library_audio.mp3"
        );
        Audiobook audiobooks10 = new Audiobook(
            "AB010", "Conan Doyle: Sherlock Holmes Collection (Audio)", "Classic detective stories.",
            "@../../../../images/ab10.jpg", 16.50, 8.25, "Available",
            "978-1441753232", "Arthur Conan Doyle", "Brilliance Audio", "Trinh thám", "English", "http://example.com/sherlock_audio.mp3"
        );

        Audiobook audiobooks11 = new Audiobook(
    "AB011", "The Secret History (Audio)", "A dark academic novel of classical studies and obsession.",
    "@../../../../images/ab11.jpg", 16.50, 8.50, "Available",
    "978-0593414576", "Donna Tartt", "Penguin Audio", "Mystery", "English", "http://example.com/secret_hist_audio.mp3"
        );
        Audiobook audiobooks12 = new Audiobook(
            "AB012", "Tuổi Thơ Dữ Dội (Sách nói)", "Tập truyện về tuổi thơ và chiến tranh đầy cảm động.",
            "@../../../../images/ab12.jpg", 9.80, 5.00, "Available",
            "978-6045610036", "Phùng Quán", "NXB Trẻ", "Văn học", "Tiếng Việt", "http://example.com/tuoitho_dudoi_audio.mp3"
        );
        Audiobook audiobooks13 = new Audiobook(
            "AB013", "Project Hail Mary (Audio)", "An astronaut's mission to save Earth from a cosmic threat.",
            "@../../../../images/ab13.jpg", 14.99, 7.50, "Available",
            "978-0593135207", "Andy Weir", "Audible Studios", "Science Fiction", "English", "http://example.com/hail_mary_audio.mp3"
        );
        Audiobook audiobooks14 = new Audiobook(
            "AB014", "Bên Kia Rừng (Sách nói)", "Những câu chuyện dân gian độc đáo của Việt Nam.",
            "@../../../../images/ab14.jpg", 7.00, 3.50, "Out of Stock",
            "978-6045620018", "Truyện cổ tích", "NXB Văn Học", "Truyện cổ tích", "Tiếng Việt", "http://example.com/benkiarung_audio.mp3"
        );
        Audiobook audiobooks15 = new Audiobook(
            "AB015", "Circe (Audio)", "A mythological retelling of the Greek goddess Circe's life.",
            "@../../../../images/ab15.jpg", 13.50, 6.75, "Available",
            "978-0316556550", "Madeline Miller", "Hachette Audio", "Mythology", "English", "http://example.com/circe_audio.mp3"
        );
        Audiobook audiobooks16 = new Audiobook(
            "AB016", "Muôn Kiếp Nhân Sinh (Sách nói)", "Những câu chuyện về luật nhân quả và nghiệp báo.",
            "@../../../../images/ab16.jpgg", 10.50, 5.25, "Available",
            "978-6045630012", "Nguyên Phong", "First News", "Tâm linh", "Tiếng Việt", "http://example.com/muonkiep_audio.mp3"
        );
        Audiobook audiobooks17 = new Audiobook(
            "AB017", "Educated (Audiobook)", "A memoir about a young woman's journey from an isolated upbringing to academia.",
            "@../../../../images/ab17.jpg", 12.00, 6.00, "Available",
            "978-0399590511", "Tara Westover", "Random House Audio", "Memoir", "English", "http://example.com/educated_audio.mp3"
        );
        Audiobook audiobooks18 = new Audiobook(
            "AB018", "Bản Giao Hưởng Định Mệnh (Sách nói)", "Câu chuyện về cuộc sống, tình yêu và những khúc mắc.",
            "@../../../../images/ab18.jpg", 8.00, 4.00, "Available",
            "978-6045610043", "Marc Levy", "NXB Phụ Nữ", "Lãng mạn", "Tiếng Việt", "http://example.com/giaohuong_audio.mp3"
        );
        Audiobook audiobooks19 = new Audiobook(
            "AB019", "The Martian (Audiobook)", "An astronaut's struggle for survival on Mars.",
            "@../../../../images/ab19.jpg", 11.99, 6.00, "Discontinued",
            "978-0804139045", "Andy Weir", "Crown Audio", "Science Fiction", "English", "http://example.com/martian_audio.mp3"
        );
        Audiobook audiobooks20 = new Audiobook(
            "AB020", "Chuyện Con Mèo Dạy Hải Âu Bay (Sách nói)", "Tác phẩm kinh điển về tình bạn và trách nhiệm.",
            "@../../../../images/ab20.jpg", 6.50, 3.25, "Available",
            "978-6045600055", "Luis Sepúlveda", "NXB Trẻ", "Truyện thiếu nhi", "Tiếng Việt", "http://example.com/meo_haiau_audio.mp3"
        );

        Ebook ebooks1 = new Ebook(
            "EB001", "The Alchemist (Ebook)", "An inspiring tale about following your dreams.",
            "@../../../../images/ebook1.jpg", 6.99, 3.50, "Available",
            "978-0062315007", "Paulo Coelho", "HarperCollins", "Fiction", "English", 208, "http://example.com/alchemist_ebook.epub"
        );
        Ebook ebooks2 = new Ebook(
            "EB002", "Đắc Nhân Tâm (Ebook)", "Nghệ thuật thu phục lòng người.",
            "@../../../../images/ebook2.jpg", 4.99, 2.50, "Available",
            "978-6045610012", "Dale Carnegie", "Alpha Books", "Kỹ năng sống", "Tiếng Việt", 320, "http://example.com/dacnhantam_ebook.pdf"
        );
        Ebook ebooks3 = new Ebook(
            "EB003", "1984 (Ebook)", "A dystopian social science fiction novel.",
            "@../../../../images/ebook3.jpg", 5.50, 2.75, "Available",
            "978-0451524935", "George Orwell", "Signet Classic", "Dystopian", "English", 328, "http://example.com/1984_ebook.epub"
        );
        Ebook ebooks4 = new Ebook(
            "EB004", "Tôi Thấy Hoa Vàng Trên Cỏ Xanh (Ebook)", "Tập truyện dài về tuổi thơ ở một làng quê Việt Nam.",
            "@../../../../images/ebook4.jpg", 3.99, 2.00, "Available",
            "978-6045620001", "Nguyễn Nhật Ánh", "NXB Trẻ", "Văn học", "Tiếng Việt", 280, "http://example.com/hoavang_ebook.pdf"
        );
        Ebook ebooks5 = new Ebook(
            "EB005", "The Great Gatsby (Ebook)", "A classic American novel exploring the American Dream.",
            "@../../../../images/ebook5.jpg", 4.00, 2.00, "Out of Stock", // Trạng thái hết hàng (giả sử có thể là do vấn đề bản quyền e-book)
            "978-0743273565", "F. Scott Fitzgerald", "Scribner", "Classics", "English", 180, "http://example.com/gatsby_ebook.epub"
        );
        Ebook ebooks6 = new Ebook(
            "EB006", "Bí Mật Của May Mắn (Ebook)", "Câu chuyện về hai hiệp sĩ tìm kiếm cỏ bốn lá may mắn.",
            "@../../../../images/ebook6.jpg", 3.00, 1.50, "Available",
            "978-6045600000", "Alex Rovira", "First News", "Kỹ năng sống", "Tiếng Việt", 120, "http://example.com/bimay_mayman_ebook.pdf"
        );
        Ebook ebooks7 = new Ebook(
            "EB007", "Dune (Ebook)", "Frank Herbert's seminal science fiction masterpiece.",
            "@../../../../images/ebook7.jpg", 7.99, 4.00, "Available",
            "978-0441013593", "Frank Herbert", "Ace Books", "Science Fiction", "English", 600, "http://example.com/dune_ebook.epub"
        );
        Ebook ebooks8 = new Ebook(
            "EB008", "Cà Phê Sáng Với Thượng Đế (Ebook)", "Tập hợp những câu chuyện ngắn truyền cảm hứng.",
            "@../../../../images/ebook8.jpg", 4.50, 2.25, "Available",
            "978-6045630005", "John P. Stapp", "NXB Tổng hợp TP.HCM", "Tâm lý", "Tiếng Việt", 250, "http://example.com/caphesang_ebook.pdf"
        );
        Ebook ebooks9 = new Ebook(
            "EB009", "Educated (Ebook)", "A memoir about a young woman's journey from an isolated upbringing to academia.",
            "@../../../../images/ebook9.jpg", 6.00, 3.00, "Available",
            "978-0399590504", "Tara Westover", "Random House", "Memoir", "English", 352, "http://example.com/educated_ebook.epub"
        );
        Ebook ebooks10 = new Ebook(
            "EB010", "Ông Già Và Biển Cả (Ebook)", "Tác phẩm kinh điển về cuộc đấu tranh của con người với thiên nhiên.",
            "@../../../../images/ebook10.jpg", 3.50, 1.75, "Available",
            "978-6045600017", "Ernest Hemingway", "NXB Văn Học", "Văn học", "Tiếng Việt", 128, "http://example.com/onggia_ebook.pdf"
        );

        Ebook ebooks11 = new Ebook(
    "EB011", "The Night Circus (Ebook)", "A magical love story set in a mysterious circus.",
    "@../../../../images/ebook11.jpg", 6.00, 3.00, "Available",
    "978-0385534633", "Erin Morgenstern", "Anchor Books", "Fantasy", "English", 392, "http://example.com/night_circus_ebook.epub"
        );
        Ebook ebooks12 = new Ebook(
            "EB012", "Cây Chuối Non Đi Giày Xanh (Ebook)", "Truyện dài về tuổi thơ trong trẻo của Nguyễn Nhật Ánh.",
            "@../../../../images/ebook12.jpg", 4.20, 2.10, "Available",
            "978-6045610009", "Nguyễn Nhật Ánh", "NXB Trẻ", "Văn học", "Tiếng Việt", 310, "http://example.com/caychuoinon_ebook.pdf"
        );
        Ebook ebooks13 = new Ebook(
            "EB013", "Where the Crawdads Sing (Ebook)", "A compelling story of a lonely girl growing up in the marshes.",
            "@../../../../images/ebook13.jpg", 7.50, 3.75, "Available",
            "978-0735219113", "Delia Owens", "G.P. Putnam's Sons", "Fiction", "English", 368, "http://example.com/crawdads_ebook.epub"
        );
        Ebook ebooks14 = new Ebook(
            "EB014", "Tôi Tự Học (Ebook)", "Hành trình tự học và khám phá bản thân.",
            "h@../../../../images/ebook14.jpg", 5.00, 2.50, "Out of Stock",
            "978-6045620025", "Nguyễn Duy Cần", "NXB Tổng Hợp TP.HCM", "Kỹ năng sống", "Tiếng Việt", 280, "http://example.com/toituhoc_ebook.pdf"
        );
        Ebook ebooks15 = new Ebook(
            "EB015", "The Song of Achilles (Ebook)", "A stunning retelling of the Trojan War and the love between Achilles and Patroclus.",
            "@../../../../images/ebook15.jpg", 6.75, 3.40, "Available",
            "978-0062060624", "Madeline Miller", "Ecco", "Mythology", "English", 368, "http://example.com/achilles_ebook.epub"
        );
        Ebook ebooks16 = new Ebook(
            "EB016", "Suối Nguồn (Ebook)", "Một câu chuyện về chủ nghĩa cá nhân và sự sáng tạo.",
            "@../../../../images/ebook16.jpg", 8.00, 4.00, "Available",
            "978-6045630030", "Ayn Rand", "NXB Tổng Hợp TP.HCM", "Triết học", "Tiếng Việt", 800, "http://example.com/suoinuon_ebook.pdf"
        );
        Ebook ebooks17 = new Ebook(
            "EB017", "Circe (Ebook)", "A modern classic of the ancient world.",
            "@../../../../images/ebook17.jpg", 7.20, 3.60, "Available",
            "978-0316556345", "Madeline Miller", "Little, Brown and Company", "Mythology", "English", 393, "http://example.com/circe_ebook.epub"
        );
        Ebook ebooks18 = new Ebook(
            "EB018", "Một Lít Nước Mắt (Ebook)", "Câu chuyện có thật về nghị lực sống của cô gái trẻ.",
            "@../../../../images/ebook18.jpg", 4.00, 2.00, "Available",
            "978-6045610050", "Kito Aya", "NXB Kim Đồng", "Tâm lý", "Tiếng Việt", 240, "http://example.com/motlitnuocmat_ebook.pdf"
        );
        Ebook ebooks19 = new Ebook(
            "EB019", "The Henna Artist (Ebook)", "A vivid historical novel set in 1950s Jaipur.",
            "@../../../../images/ebook19.jpg", 5.50, 2.75, "Discontinued",
            "978-1982156822", " Alka Joshi", "Mira Books", "Historical Fiction", "English", 368, "http://example.com/henna_artist_ebook.epub"
        );
        Ebook ebooks20 = new Ebook(
            "EB020", "Thám Tử Lừng Danh Conan - Tập 100 (Ebook)", "Tập mới nhất trong series trinh thám nổi tiếng.",
            "@../../../../images/ebook20.jpg", 3.00, 1.50, "Available",
            "978-6045640001", "Aoyama Gosho", "NXB Kim Đồng", "Manga", "Tiếng Việt", 192, "http://example.com/conan100_ebook.pdf"
        );

        Printbook printbooks1 = new Printbook(
            "PB001", "Nhà Giả Kim (Bản in)", "Câu chuyện phiêu lưu và khám phá bản thân qua sa mạc.",
            "@../../../../images/pbook1.jpg", 10.00, 5.00, "Available",
            "978-6045610005", "Paulo Coelho", "Nhà xuất bản Văn Học", "Triết học", "Tiếng Việt", 224, 350
        );
        Printbook printbooks2 = new Printbook(
            "PB002", "Harry Potter and the Philosopher's Stone (Print)", "The first book in the magical series.",
            "@../../../../images/pbook2.jpg", 20.00, 10.00, "Available",
            "978-0747532743", "J.K. Rowling", "Bloomsbury Publishing", "Fantasy", "English", 223, 400
        );
        Printbook printbooks3 = new Printbook(
            "PB003", "Tắt Đèn (Bản in)", "Tiểu thuyết hiện thực phê phán xã hội Việt Nam thời Pháp thuộc.",
            "@../../../../images/pbook3.jpg", 7.00, 3.50, "Out of Stock", // Hết hàng
            "978-6045600021", "Ngô Tất Tố", "NXB Văn Học", "Văn học", "Tiếng Việt", 180, 280
        );
        Printbook printbooks4 = new Printbook(
            "PB004", "The Psychology of Money (Print)", "Timeless lessons on wealth, greed, and happiness.",
            "@../../../../images/pbook4.jpg", 14.99, 7.50, "Available",
            "978-0857197689", "Morgan Housel", "Harriman House", "Finance", "English", 256, 380
        );
        Printbook printbooks5 = new Printbook(
            "PB005", "Số Đỏ (Bản in)", "Tiểu thuyết châm biếm xã hội Việt Nam đầu thế kỷ 20.",
            "@../../../../images/pbook5.jpg", 8.50, 4.25, "Available",
            "978-6045600038", "Vũ Trọng Phụng", "NXB Văn Học", "Văn học", "Tiếng Việt", 300, 450
        );
        Printbook printbooks6 = new Printbook(
            "PB006", "The Lord of the Rings: The Fellowship of the Ring (Print)", "The epic fantasy novel.",
            "@../../../../images/pbook6.jpg", 25.00, 12.50, "Available",
            "978-0618260275", "J.R.R. Tolkien", "Houghton Mifflin Harcourt", "Fantasy", "English", 480, 700
        );
        Printbook printbooks7 = new Printbook(
            "PB007", "Cho Tôi Một Vé Đi Tuổi Thơ (Bản in)", "Những câu chuyện trong trẻo về tuổi thơ.",
            "@../../../../images/pbook7.jpg", 9.00, 4.50, "Available",
            "978-6045610008", "Nguyễn Nhật Ánh", "NXB Trẻ", "Văn học", "Tiếng Việt", 280, 420
        );
        Printbook printbooks8 = new Printbook(
            "PB008", "Thinking, Fast and Slow (Print)", "A groundbreaking book on human cognition.",
            "@../../../../images/pbook8.jpg", 18.00, 9.00, "Available",
            "978-0374533557", "Daniel Kahneman", "Farrar, Straus and Giroux", "Psychology", "English", 499, 650
        );
        Printbook printbooks9 = new Printbook(
            "PB009", "Cây Chuối Non Đi Giày Xanh (Bản in)", "Truyện dài của Nguyễn Nhật Ánh.",
            "@../../../../images/pbook9.jpg", 9.50, 4.75, "Discontinued", // Ngừng xuất bản
            "978-6045610009", "Nguyễn Nhật Ánh", "NXB Trẻ", "Văn học", "Tiếng Việt", 310, 480
        );
        Printbook printbooks10 = new Printbook(
            "PB010", "Gone Girl (Print)", "A thrilling psychological suspense novel.",
            "@../../../../images/pbook10.jpg", 11.00, 5.50, "Available",
            "978-0307588371", "Gillian Flynn", "Crown", "Thriller", "English", 419, 580
        );

        Printbook printbooks11 = new Printbook(
    "PB011", "Ông Già Khó Tính Và Thần Chết (Bản in)", "Tiểu thuyết lãng mạn pha lẫn yếu tố kỳ ảo.",
    "http://example.com/onggiakhotinh_print.jpg", 11.00, 5.50, "Available",
    "978-6045610067", "Fredrik Backman", "NXB Hội Nhà Văn", "Văn học", "Tiếng Việt", 350, 520
        );
        Printbook printbooks12 = new Printbook(
            "PB012", "The Secret History (Print)", "A gripping novel about a group of classics students at an elite New England college.",
            "http://example.com/secret_history_print.jpg", 17.50, 8.75, "Available",
            "978-0679733190", "Donna Tartt", "Vintage Books", "Literary Fiction", "English", 559, 800
        );
        Printbook printbooks13 = new Printbook(
            "PB013", "Nếu Biết Trăm Năm Là Hữu Hạn (Bản in)", "Những suy ngẫm về cuộc sống và cái chết.",
            "http://example.com/tramnam_print.jpg", 9.00, 4.50, "Available",
            "978-6045620032", "Phạm Lữ Ân", "NXB Hội Nhà Văn", "Triết học", "Tiếng Việt", 280, 400
        );
        Printbook printbooks14 = new Printbook(
            "PB014", "Verity (Print)", "A suspenseful thriller from the author of It Ends With Us.",
            "http://example.com/verity_print.jpg", 12.00, 6.00, "Out of Stock",
            "978-1538724736", "Colleen Hoover", "Grand Central Publishing", "Thriller", "English", 336, 450
        );
        Printbook printbooks15 = new Printbook(
            "PB015", "Hoàng Tử Bé (Bản in)", "Tác phẩm kinh điển dành cho mọi lứa tuổi.",
            "http://example.com/hoangtude_print.jpg", 6.50, 3.25, "Available",
            "978-6045600062", "Antoine de Saint-Exupéry", "NXB Kim Đồng", "Văn học", "Tiếng Việt", 96, 180
        );
        Printbook printbooks16 = new Printbook(
            "PB016", "The Da Vinci Code (Print)", "A thrilling mystery novel involving religious history.",
            "http://example.com/davinci_print.jpg", 13.00, 6.50, "Available",
            "978-0385504209", "Dan Brown", "Doubleday", "Thriller", "English", 454, 600
        );
        Printbook printbooks17 = new Printbook(
            "PB017", "Đời Ngắn Đừng Ngủ Dài (Bản in)", "Những bài học về cách sống ý nghĩa.",
            "http://example.com/doingan_print.jpg", 8.00, 4.00, "Available",
            "978-6045610074", "Robin Sharma", "NXB Trẻ", "Kỹ năng sống", "Tiếng Việt", 260, 380
        );
        Printbook printbooks18 = new Printbook(
            "PB018", "It Ends With Us (Print)", "A powerful and emotional romance novel.",
            "http://example.com/it_ends_us_print.jpg", 11.50, 5.75, "Available",
            "978-1501110368", "Colleen Hoover", "Atria Books", "Romance", "English", 384, 500
        );
        Printbook printbooks19 = new Printbook(
            "PB019", "Rừng Na Uy (Bản in)", "Một câu chuyện tình yêu đầy ám ảnh của Haruki Murakami.",
            "http://example.com/rungnauy_print.jpg", 10.50, 5.25, "Discontinued",
            "978-6045600079", "Haruki Murakami", "NXB Hội Nhà Văn", "Văn học", "Tiếng Việt", 370, 550
        );
        Printbook printbooks20 = new Printbook(
            "PB020", "The Nightingale (Print)", "A historical novel set during World War II in France.",
            "http://example.com/nightingale_print.jpg", 14.00, 7.00, "Available",
            "978-0312577223", "Kristin Hannah", "St. Martin's Press", "Historical Fiction", "English", 448, 620
        );

        Stationery stationeries1 = new Stationery(
            "ST001", "Bút bi Thiên Long TL-08", "Bút bi mực xanh, đầu bi 0.5mm, viết êm.",
            "@../../../../images/st1.jpg", 0.50, 0.20, "Available", "Thiên Long", "Bút bi"
        );
        Stationery stationeries2 = new Stationery(
            "ST002", "Sổ tay A5 Moleskine Cổ điển", "Sổ tay bìa cứng A5, 240 trang giấy kẻ ngang.",
            "@../../../../images/st2.jpg", 12.00, 6.00, "Available", "Moleskine", "Sổ tay"
        );
        Stationery stationeries3 = new Stationery(
            "ST003", "Tập học sinh Campus 96 trang", "Tập 96 trang, giấy trắng tự nhiên, kẻ ngang.",
            "@../../../../images/st3.jpg", 0.70, 0.30, "Out of Stock", // Hết hàng
            "Campus", "Tập học sinh"
        );
        Stationery stationeries4 = new Stationery(
            "ST004", "Bút chì Staedtler Noris HB", "Bút chì gỗ cao cấp, độ cứng HB, dễ tẩy.",
            "@../../../../images/st4.jpgg", 0.80, 0.35, "Available", "Staedtler", "Bút chì"
        );
        Stationery stationeries5 = new Stationery(
            "ST005", "Băng keo trong 3M Scotch", "Băng keo dính siêu chắc, không ố vàng.",
            "@../../../../images/st5.jpg", 2.50, 1.00, "Available", "3M", "Băng keo"
        );
        Stationery stationeries6 = new Stationery(
            "ST006", "Bộ bút lông màu Faber-Castell 12 màu", "Bút lông màu tươi sáng, an toàn cho trẻ em.",
            "@../../../../images/st6.jpg", 5.00, 2.50, "Available", "Faber-Castell", "Bút lông màu"
        );
        Stationery stationeries7 = new Stationery(
            "ST007", "Kéo học sinh SDI", "Kéo nhỏ gọn, lưỡi thép không gỉ.",
            "@../../../../images/st7.jpg", 1.50, 0.70, "Available", "SDI", "Kéo"
        );
        Stationery stationeries8 = new Stationery(
            "ST008", "Bảng kẹp tài liệu A4 Deli", "Bảng kẹp cứng, có kẹp kim loại chắc chắn.",
            "@../../../../images/st8.jpg", 3.00, 1.50, "Available", "Deli", "Bảng kẹp"
        );
        Stationery stationeries9 = new Stationery(
            "ST009", "Tẩy Pentel Hi-Polymer", "Tẩy siêu sạch, ít bụi, không làm rách giấy.",
            "@../../../../images/st9.jpg", 1.00, 0.45, "Discontinued", // Ngừng sản xuất
            "Pentel", "Tẩy"
        );
        Stationery stationeries10 = new Stationery(
            "ST010", "Giấy ghi chú Post-it 3x3 inch", "Giấy dán tiện lợi, màu vàng chanh.",
            "@../../../../images/st10.jpg", 4.00, 2.00, "Available", "Post-it", "Giấy ghi chú"
        );

        Stationery stationeries11 = new Stationery(
    "ST011", "Bộ bút chì màu Marco Raffine 24 màu", "Bộ bút chì màu chuyên nghiệp, sắc nét, dễ pha trộn.",
    "@../../../../images/st11.jpg", 15.00, 7.50, "Available", "Marco", "Bộ bút chì màu"
        );
        Stationery stationeries12 = new Stationery(
            "ST012", "Hộp bút Unicorn hình cô gái", "Hộp bút vải mềm, hình ảnh dễ thương, kích thước lớn.",
            "@../../../../images/st12.jpg", 7.00, 3.50, "Available", "Unicorn", "Hộp bút"
        );
        Stationery stationeries13 = new Stationery(
            "ST013", "Giấy A4 Double A 80gsm (500 tờ)", "Giấy in A4 chất lượng cao, trắng mịn, không kẹt giấy.",
            "@../../../../images/st13.jpg", 6.00, 3.00, "Out of Stock",
            "Double A", "Giấy in"
        );
        Stationery stationeries14 = new Stationery(
            "ST014", "Máy tính Casio FX-570ES Plus", "Máy tính khoa học phổ biến cho học sinh, sinh viên.",
            "@../../../../images/st14.jpg", 18.00, 9.00, "Available", "Casio", "Máy tính cầm tay"
        );
        Stationery stationeries15 = new Stationery(
            "ST015", "Bút dạ quang Thiên Long (5 màu)", "Bộ 5 bút dạ quang với các màu sắc nổi bật, không lem.",
            "@../../../../images/st15.jpg", 3.50, 1.75, "Available", "Thiên Long", "Bút dạ quang"
        );
        Stationery stationeries16 = new Stationery(
            "ST016", "Sổ tay lò xo A6 họa tiết", "Sổ tay nhỏ gọn, bìa cứng, nhiều họa tiết đáng yêu.",
            "@../../../../images/st16.jpg", 4.50, 2.25, "Available", "Cute Design", "Sổ tay"
        );
        Stationery stationeries17 = new Stationery(
            "ST017", "Gôm/Tẩy hình thú ngộ nghĩnh", "Gôm tẩy nhiều hình dạng động vật, không độc hại.",
            "@../../../../images/st17.jpg", 1.20, 0.60, "Available", "Kiddo Fun", "Tẩy"
        );
        Stationery stationeries18 = new Stationery(
            "ST018", "Kẹp bướm Pgrand đen 32mm", "Hộp 12 cái kẹp bướm, giữ tài liệu chắc chắn.",
            "@../../../../images/st18.jpg", 2.00, 1.00, "Available", "Pgrand", "Kẹp giấy"
        );
        Stationery stationeries19 = new Stationery(
            "ST019", "Dao rọc giấy SDI loại lớn", "Dao rọc giấy lưỡi sắc, có khóa an toàn.",
            "@../../../../images/st19.jpg", 3.00, 1.50, "Discontinued",
            "SDI", "Dao rọc giấy"
        );
        Stationery stationeries20 = new Stationery(
            "ST020", "Bảng trắng mini để bàn 20x30cm", "Bảng trắng kèm bút và bông lau, tiện lợi cho ghi chú.",
            "@../../../../images/st20.jpg", 10.00, 5.00, "Available", "OfficeMate", "Bảng trắng"
        );

        Toy toys1 = new Toy(
            "TOY001", "Xe ô tô địa hình điều khiển từ xa", "Xe điều khiển 4 bánh, chạy địa hình, sạc pin.",
            "@../../../../images/toy1.jpg", 25.00, 12.00, "Available", " điều khiển từ xa", 6
        );
        Toy toys2 = new Toy(
            "TOY002", "Bộ xếp hình Lego City Cảnh sát biển", "Bộ Lego 300 chi tiết, xây dựng đồn cảnh sát biển.",
            "@../../../../images/toy2.jpg", 35.00, 18.00, "Available", "Lego", 7
        );
        Toy toys3 = new Toy(
            "TOY003", "Búp bê Barbie Công chúa", "Búp bê Barbie với váy lộng lẫy và phụ kiện.",
            "@../../../../images/toy3.jpg", 18.00, 9.00, "Out of Stock", // Hết hàng
            "Mattel", 3
        );
        Toy toys4 = new Toy(
            "TOY004", "Bộ đồ chơi bác sĩ trẻ em", "Đồ chơi nhập vai bác sĩ, có ống nghe, kim tiêm giả.",
            "@../../../../images/toy4.jpg", 15.00, 7.50, "Available", "Fisher-Price", 3
        );
        Toy toys5 = new Toy(
            "TOY005", "Rubik 3x3 tốc độ cao", "Rubik xoay mượt mà, chuyên nghiệp.",
            "@../../../../images/toy5.jpg", 8.00, 4.00, "Available", "Gan Cube", 8
        );
        Toy toys6 = new Toy(
            "TOY006", "Bộ đất nặn Play-Doh Cửa hàng kem", "Đất nặn mềm, nhiều màu sắc, kèm khuôn làm kem.",
            "@../../../../images/toy6.jpg", 22.00, 11.00, "Available", "Play-Doh", 4
        );
        Toy toys7 = new Toy(
            "TOY007", "Robot biến hình Optimus Prime", "Mô hình robot có thể biến hình thành xe tải.",
            "@../../../../images/toy7.jpg", 40.00, 20.00, "Available", "Hasbro", 8
        );
        Toy toys8 = new Toy(
            "TOY008", "Bộ xếp hình gỗ 100 khối", "Các khối gỗ với nhiều hình dạng và màu sắc.",
            "@../../../../images/toy8.jpg", 10.00, 5.00, "Available", "Melissa & Doug", 2
        );
        Toy toys9 = new Toy(
            "TOY009", "Đàn piano điện tử trẻ em", "Đàn piano nhỏ gọn, có nhiều chế độ âm thanh.",
            "@../../../../images/toy9.jpg", 30.00, 15.00, "Discontinued", // Ngừng sản xuất
            "Casio Kids", 5
        );
        Toy toys10 = new Toy(
            "TOY010", "Bộ đồ chơi câu cá nam châm", "Bộ đồ chơi câu cá với cần câu và cá nam châm.",
            "@../../../../images/toy10.jpg", 12.00, 6.00, "Available", "Kids Delight", 3
        );

        Toy toys11 = new Toy(
    "TOY011", "Bộ đồ chơi xếp hình gỗ hình khối", "Các khối gỗ cơ bản giúp phát triển tư duy không gian.",
    "@../../../../images/toy11.jpg", 18.00, 9.00, "Available", "Melissa & Doug", 2
        );
        Toy toys12 = new Toy(
            "TOY012", "Búp bê LOL Surprise! Glam Series", "Búp bê bất ngờ với nhiều lớp phụ kiện lấp lánh.",
            "@../../../../images/toy12.jpg", 25.00, 12.50, "Available", "MGA Entertainment", 5
        );
        Toy toys13 = new Toy(
            "TOY013", "Xe tải công trình điều khiển từ xa", "Xe tải lớn với các chức năng điều khiển đổ vật liệu.",
            "@../../../../images/toy13.jpg", 45.00, 22.50, "Out of Stock",
            "Top Race", 8
        );
        Toy toys14 = new Toy(
            "TOY014", "Bộ cờ tỷ phú Monopoly Classic", "Trò chơi cờ tỷ phú kinh điển cho gia đình và bạn bè.",
            "@../../../../images/toy14.jpg", 20.00, 10.00, "Available", "Hasbro", 8
        );
        Toy toys15 = new Toy(
            "TOY015", "Đồ chơi mô hình khủng long Tyrannosaurus Rex", "Mô hình khủng long T-Rex có khớp nối, âm thanh.",
            "@../../../../images/toy15.jpg", 16.00, 8.00, "Available", "Jurassic World", 4
        );
        Toy toys16 = new Toy(
            "TOY016", "Bộ đồ chơi nấu ăn nhà bếp mini", "Bếp đồ chơi đầy đủ dụng cụ, phát triển kỹ năng nấu ăn.",
            "@../../../../images/toy16.jpg", 30.00, 15.00, "Available", "Little Tikes", 3
        );
        Toy toys17 = new Toy(
            "TOY017", "Gấu bông Teddy Bear khổng lồ", "Gấu bông mềm mại, kích thước lớn (1 mét).",
            "@../../../../images/toy17.jpg", 50.00, 25.00, "Available", "Gund", 0 // Mọi lứa tuổi
        );
        Toy toys18 = new Toy(
            "TOY018", "Bộ ghép hình 1000 mảnh phong cảnh", "Trò chơi thử thách trí tuệ với bức tranh phong cảnh đẹp.",
            "@../../../../images/toy18.jpg", 15.00, 7.50, "Available", "Ravensburger", 12
        );
        Toy toys19 = new Toy(
            "TOY019", "Xe đẩy em bé đồ chơi", "Xe đẩy nhỏ gọn cho búp bê, có thể gập lại.",
            "@../../../../images/toy19.jpg", 10.00, 5.00, "Discontinued",
            "Baby Alive", 3
        );
        Toy toys20 = new Toy(
            "TOY020", "Bộ đồ chơi xếp hình nam châm Magna-Tiles", "Các mảnh ghép nam châm màu sắc, giúp xây dựng các hình khối 3D.",
            "@../../../../images/toy20.jpg", 60.00, 30.00, "Available", "Magna-Tiles", 3
        );






        productList.addAll(
            audiobooks1, audiobooks2, audiobooks3, audiobooks4, audiobooks5,
            audiobooks6, audiobooks7, audiobooks8, audiobooks9, audiobooks10,
            ebooks1, ebooks2, ebooks3, ebooks4, ebooks5,
            ebooks6, ebooks7, ebooks8, ebooks9, ebooks10,
            printbooks1, printbooks2, printbooks3, printbooks4, printbooks5,
            printbooks6, printbooks7, printbooks8, printbooks9, printbooks10,
            stationeries1, stationeries2, stationeries3, stationeries4, stationeries5,
            stationeries6, stationeries7, stationeries8, stationeries9, stationeries10,
            toys1, toys2, toys3, toys4, toys5,
            toys6, toys7, toys8, toys9, toys10,
            stationeries11, stationeries12, stationeries13, stationeries14, stationeries15,
            stationeries16, stationeries17, stationeries18, stationeries19, stationeries20,
            printbooks11, printbooks12, printbooks13, printbooks14, printbooks15,
            printbooks16, printbooks17, printbooks18, printbooks19, printbooks20,
            toys11, toys12, toys13, toys14, toys15,
            toys16, toys17, toys18, toys19, toys20
        );

        productMap.put(audiobooks1.getId(), audiobooks1);
        productMap.put(audiobooks2.getId(), audiobooks2);
        productMap.put(audiobooks3.getId(), audiobooks3);
        productMap.put(audiobooks4.getId(), audiobooks4);
        productMap.put(audiobooks5.getId(), audiobooks5);
        productMap.put(audiobooks6.getId(), audiobooks6);
        productMap.put(audiobooks7.getId(), audiobooks7);
        productMap.put(audiobooks8.getId(), audiobooks8);
        productMap.put(audiobooks9.getId(), audiobooks9);
        productMap.put(audiobooks10.getId(), audiobooks10);
        productMap.put(ebooks1.getId(), ebooks1);
        productMap.put(ebooks2.getId(), ebooks2);
        productMap.put(ebooks3.getId(), ebooks3);
        productMap.put(ebooks4.getId(), ebooks4);
        productMap.put(ebooks5.getId(), ebooks5);
        productMap.put(ebooks6.getId(), ebooks6);
        productMap.put(ebooks7.getId(), ebooks7);
        productMap.put(ebooks8.getId(), ebooks8);
        productMap.put(ebooks9.getId(), ebooks9);
        productMap.put(ebooks10.getId(), ebooks10);
        productMap.put(printbooks1.getId(), printbooks1);
        productMap.put(printbooks2.getId(), printbooks2);
        productMap.put(printbooks3.getId(), printbooks3);
        productMap.put(printbooks4.getId(), printbooks4);
        productMap.put(printbooks5.getId(), printbooks5);
        productMap.put(printbooks6.getId(), printbooks6);
        productMap.put(printbooks7.getId(), printbooks7);
        productMap.put(printbooks8.getId(), printbooks8);
        productMap.put(printbooks9.getId(), printbooks9);
        productMap.put(printbooks10.getId(), printbooks10);
        productMap.put(stationeries1.getId(), stationeries1);
        productMap.put(stationeries2.getId(), stationeries2);
        productMap.put(stationeries3.getId(), stationeries3);
        productMap.put(stationeries4.getId(), stationeries4);
        productMap.put(stationeries5.getId(), stationeries5);
        productMap.put(stationeries6.getId(), stationeries6);
        productMap.put(stationeries7.getId(), stationeries7);
        productMap.put(stationeries8.getId(), stationeries8);
        productMap.put(stationeries9.getId(), stationeries9);
        productMap.put(stationeries10.getId(), stationeries10);
        productMap.put(toys1.getId(), toys1);
        productMap.put(toys2.getId(), toys2);
        productMap.put(toys3.getId(), toys3);
        productMap.put(toys4.getId(), toys4);
        productMap.put(toys5.getId(), toys5);
        productMap.put(toys6.getId(), toys6);
        productMap.put(toys7.getId(), toys7);
        productMap.put(toys8.getId(), toys8);
        productMap.put(toys9.getId(), toys9);
        productMap.put(toys10.getId(), toys10);
        productMap.put(audiobooks11.getId(), audiobooks11);
        productMap.put(audiobooks12.getId(), audiobooks12);
        productMap.put(audiobooks13.getId(), audiobooks13);
        productMap.put(audiobooks14.getId(), audiobooks14);
        productMap.put(audiobooks15.getId(), audiobooks15);
        productMap.put(audiobooks16.getId(), audiobooks16);
        productMap.put(audiobooks17.getId(), audiobooks17);
        productMap.put(audiobooks18.getId(), audiobooks18);
        productMap.put(audiobooks19.getId(), audiobooks19);
        productMap.put(audiobooks20.getId(), audiobooks20);
        productMap.put(ebooks11.getId(), ebooks11);
        productMap.put(ebooks12.getId(), ebooks12);
        productMap.put(ebooks13.getId(), ebooks13);
        productMap.put(ebooks14.getId(), ebooks14);
        productMap.put(ebooks15.getId(), ebooks15);
        productMap.put(ebooks16.getId(), ebooks16);
        productMap.put(ebooks17.getId(), ebooks17);
        productMap.put(ebooks18.getId(), ebooks18);
        productMap.put(ebooks19.getId(), ebooks19);
        productMap.put(ebooks20.getId(), ebooks20);
        productMap.put(printbooks11.getId(), printbooks11);
        productMap.put(printbooks12.getId(), printbooks12);
        productMap.put(printbooks13.getId(), printbooks13);
        productMap.put(printbooks14.getId(), printbooks14);
        productMap.put(printbooks15.getId(), printbooks15);
        productMap.put(printbooks16.getId(), printbooks16);
        productMap.put(printbooks17.getId(), printbooks17);
        productMap.put(printbooks18.getId(), printbooks18);
        productMap.put(printbooks19.getId(), printbooks19);
        productMap.put(printbooks20.getId(), printbooks20);
        productMap.put(stationeries11.getId(), stationeries11);
        productMap.put(stationeries12.getId(), stationeries12);
        productMap.put(stationeries13.getId(), stationeries13);
        productMap.put(stationeries14.getId(), stationeries14);
        productMap.put(stationeries15.getId(), stationeries15);
        productMap.put(stationeries16.getId(), stationeries16);
        productMap.put(stationeries17.getId(), stationeries17);
        productMap.put(stationeries18.getId(), stationeries18);
        productMap.put(stationeries19.getId(), stationeries19);
        productMap.put(stationeries20.getId(), stationeries20);
        productMap.put(toys11.getId(), toys11);
        productMap.put(toys12.getId(), toys12);
        productMap.put(toys13.getId(), toys13);
        productMap.put(toys14.getId(), toys14);
        productMap.put(toys15.getId(), toys15);
        productMap.put(toys16.getId(), toys16);
        productMap.put(toys17.getId(), toys17);
        productMap.put(toys18.getId(), toys18);
        productMap.put(toys19.getId(), toys19);
        productMap.put(toys20.getId(), toys20);

        Random random = new Random();
        for(Product product : productList){
            if(product instanceof PhysicalProduct){
                productQuantity.put(product.getId(), 10 + random.nextInt(40));
            }
        }
    }
}
