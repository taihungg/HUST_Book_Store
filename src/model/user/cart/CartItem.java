package model.user.cart;

import java.util.Objects;

public class CartItem {

    private final String productId; // ID của sản phẩm (không phải đối tượng Product đầy đủ)
    private int quantity;        // Số lượng mà khách hàng muốn mua

    public CartItem(String productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cart item quantity must be positive.");
        }
        else if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null for CartItem.");
        }
        this.quantity = quantity;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Cart item quantity must be positive.");
        }
        this.quantity = quantity;
    }

    // --- Phương thức tiện ích để so sánh CartItem ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return productId.equals(cartItem.productId); // Hai CartItem bằng nhau nếu có cùng Product ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }
}