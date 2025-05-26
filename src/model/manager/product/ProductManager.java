package model.manager.product;

import java.util.Map;
import java.util.HashMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.product.Product;
import model.product.interfaces.PhysicalProduct;

public class ProductManager {
	private final ObservableList<Product> productList;
	private final Map<String, Product> productMap;
	private final Map<String, Integer> productQuantity;
	public ProductManager() {
		this.productList = FXCollections.observableArrayList();
		this.productMap = new HashMap<>();
		this.productQuantity = new HashMap<>();
	}

	/**
     * Thêm một sản phẩm mới vào danh sách và map quản lý.
     * Đối với PhysicalProduct, thiết lập số lượng tồn kho ban đầu.
     *
     * @param product Sản phẩm cần thêm (có thuộc tính là Property).
     * @param initialQuantity Số lượng tồn kho ban đầu (chỉ áp dụng cho PhysicalProduct).
     * @return true nếu thêm thành công, false nếu ID đã tồn tại.
     */
    public boolean addProduct(Product product, int initialQuantity) {
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

	public boolean updateProduct(Product updatedProduct) {
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
    public boolean removeProduct(String productId) {
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

	public Product getProductById(String productId) {
		return productMap.get(productId);
	}

	public int getProductQuantity(String productId) {
		return productQuantity.getOrDefault(productId, 0);
	}

	/**
     * Giảm số lượng tồn kho của một sản phẩm vật lý.
     * @param productId ID của sản phẩm cần giảm số lượng.
     * @param amount Số lượng cần giảm. Phải là số dương.
     * @return true nếu giảm thành công, false nếu không đủ hàng, sản phẩm không phải vật lý, hoặc không tìm thấy.
     */
    public boolean decreaseProductStock(String productId, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Decrease amount must be positive.");
        }
        Product p = productMap.get(productId);
        if (p == null) {
            System.out.println("Error: Product with ID " + productId + " not found.");
            return false;
        }
        if (!(p instanceof PhysicalProduct)) {
            System.out.println("Error: Product " + p.getTitle() + " (ID: " + productId + ") is a digital product and does not have physical stock to decrease.");
            return false;
        }
        int currentQuantity = productQuantity.getOrDefault(productId, 0);
        if (currentQuantity < amount) {
            System.out.println("Stock Error for " + p.getTitle() + ": Not enough stock. Available: " + currentQuantity + ", trying to decrease by " + amount);
            return false;
        }
        productQuantity.put(productId, currentQuantity - amount);
        System.out.println("Decreased stock for " + p.getTitle() + " by " + amount + ". New stock: " + productQuantity.get(productId));
        return true;
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
    public boolean setProductQuantity(String productId, int newQuantity) {
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        Product p = productMap.get(productId);
        if (p == null) {
            System.out.println("Error: Product with ID " + productId + " not found.");
            return false;
        }
        if (!(p instanceof PhysicalProduct)) {
            System.out.println("Error: Product " + p.getTitle() + " (ID: " + productId + ") is a digital product and cannot have stock set directly.");
            return false;
        }
        productQuantity.put(productId, newQuantity);
        System.out.println("Set new stock for " + p.getTitle() + " (ID: " + productId + ") to: " + newQuantity);
        return true;
    }

	public ObservableList<Product> getAllProducts() {
		return productList;
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
}
