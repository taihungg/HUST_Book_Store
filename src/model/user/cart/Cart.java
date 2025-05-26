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
    public void addItem(String productId, int quantity, ProductManager productManager) {

    }

    /**
     * Xóa một sản phẩm khỏi giỏ hàng.
     *
     * @param productId ID của sản phẩm cần xóa.
     * @return true nếu sản phẩm được xóa, false nếu không tìm thấy.
     */
    public boolean removeItem(String productId) {
    }
    /**
     * Cập nhật số lượng của một mục đã có trong giỏ hàng.
     *
     * @param productId ID của sản phẩm.
     * @param newQuantity Số lượng mới cho mục này. Phải là số dương.
     * @param productManager Instance của ProductManager để kiểm tra tồn kho cho PhysicalProduct.
     * @throws NullPointerException nếu productManager không tìm thấy sản phẩm.
     */
    public void updateItemQuantity(String productId, int newQuantity, ProductManager productManager) {
        
    }

    /**
     * Tính tổng giá trị của tất cả các mặt hàng trong giỏ hàng.
     *
     * @param productManager Instance của ProductManager để lấy giá sản phẩm.
     * @return Tổng giá trị giỏ hàng.
     */
    public double calculateTotal(ProductManager productManager) {
        
    }

    /**
     * Xóa tất cả các mặt hàng khỏi giỏ hàng.
     */
    public void clear() {
        items.clear();
    }


}