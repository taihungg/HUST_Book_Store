package model.user.cart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.manager.product.ProductManager;
import model.product.Product;
import model.product.interfaces.PhysicalProduct;

public class Cart {
    private final ObservableList<CartItem> items;

    public Cart() {
        this.items = FXCollections.observableArrayList();
    }

    public ObservableList<CartItem> getItems() {
        return items;
    }
    /**
     * Thêm sản phẩm vào giỏ hàng hoặc cập nhật số lượng nếu sản phẩm đã tồn tại.
     * Kiểm tra tồn kho cho PhysicalProduct.
     *
     * @param productId ID của sản phẩm cần thêm.
     * @param quantity Số lượng muốn thêm.
     * @param productManager Instance của ProductManager để kiểm tra tồn kho và lấy thông tin Product.
     * @throws IllegalArgumentException nếu quantity không hợp lệ.
     * @throws IllegalStateException nếu không đủ tồn kho cho PhysicalProduct.
     * @throws NullPointerException nếu productManager không tìm thấy sản phẩm.
     */
    public boolean addItem(String productId, int quantity, ProductManager productManager) {
    	if (quantity <= 0) {
        	System.out.println("New quantity must be positive.");
        	return false;
        }
    	int availableStock = productManager.getProductQuantity(productId);
        Product product = productManager.getProductById(productId);
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) { // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng chưa
            	int newQuantityForCart = item.getQuantity() + quantity;
                // Kiểm tra tồn kho cho PhysicalProduct
                if (product instanceof PhysicalProduct) {
                    if (newQuantityForCart > availableStock) {
                    	System.out.println("Not enough stock for " + product.getTitle() + ". Available: " + availableStock);
                    	return false;
                    } else {
                    	item.setQuantity(newQuantityForCart);
                    	return true;
                    }
                } else { // DigitalProduct
                    return true;
                }
            }
        }
        // Nếu sản phẩm không tồn tại trong giỏ hàng, thêm mới
        // Kiểm tra tồn kho ban đầu cho PhysicalProduct
        if (product instanceof PhysicalProduct) {
        	if (quantity > availableStock) {
            	System.out.println("Not enough stock for " + product.getTitle() + ". Available: " + availableStock);
            	return false;
            }
        	else {
            	items.add(new CartItem(productId, quantity));
            	return true;
            }
        } else {
        	items.add(new CartItem(productId, 1));
        	return true;
        }
    }

    /**
     * Cập nhật số lượng của một mục đã có trong giỏ hàng.
     *
     * @param productId ID của sản phẩm.
     * @param newQuantity Số lượng mới cho mục này. Phải là số dương.
     * @param productManager Instance của ProductManager để kiểm tra tồn kho cho PhysicalProduct.
     * @throws IllegalArgumentException nếu newQuantity không hợp lệ.
     * @throws IllegalStateException nếu không đủ tồn kho cho PhysicalProduct.
     * @throws NullPointerException nếu productManager không tìm thấy sản phẩm.
     */
    public boolean updateItemQuantity(String productId, int newQuantity, ProductManager productManager) {
        if (newQuantity <= 0) {
        	System.out.println("New quantity must be positive.");
        	return false;
        }
        for (CartItem item : items) {
            if (item.getProductId().equals(productId)) {
                if(newQuantity == 0) {
                	items.remove(item);
                	return true;
                }
                else {
                    Product product = productManager.getProductById(productId);
                    // Kiểm tra tồn kho cho PhysicalProduct
                    if (product instanceof PhysicalProduct) {
                        int availableStock = productManager.getProductQuantity(productId);
                        if (newQuantity > availableStock) {
                            System.out.println("Not enough stock for " + product.getTitle() + ". Available: " + availableStock);
                            return false;
                        }
                        else {
                            item.setQuantity(newQuantity);
                            return true;
                        }
                    }
                    else { // DigitalProduct
                        if (newQuantity > 1) { // Nếu quy định chỉ mua 1 bản quyền DigitalProduct
                            System.out.println("Warning: Digital product " + product.getTitle() + " typically allows only one copy. Updating to more than 1.");
                            return false;
                        }
                        else {
                            item.setQuantity(newQuantity);
                            return true;
                        }
                    }
                }
            }
        }
        return false; // Nếu không tìm thấy sản phẩm trong giỏ hàng
    }
    

    public void removeItem(CartItem itemRemove) {
        items.remove(itemRemove);
    }

    /**
     * Tính tổng giá trị của tất cả các mặt hàng trong giỏ hàng.
     *
     * @param productManager Instance của ProductManager để lấy giá sản phẩm.
     * @return Tổng giá trị giỏ hàng.
     */
    public double calculateTotal(ProductManager productManager) {
        double total = 0.0;
        for (CartItem item : items) {
            Product product = productManager.getProductById(item.getProductId());
            if (product != null) {
                total += product.getSellingPrice() * item.getQuantity();
            } else {
                System.err.println("Warning: Product " + item.getProductId() + " not found in ProductManager. Skipping from total.");
            }
        }
        return total;
    }

    /**
     * Xóa tất cả các mặt hàng khỏi giỏ hàng.
     */
    public void clear() {
        items.clear();
    }

}