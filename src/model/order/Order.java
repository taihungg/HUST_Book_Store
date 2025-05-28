package model.order;

import java.time.LocalDateTime;          // Để lưu danh sách các mặt hàng
import java.util.List;       // Để kiểm tra null
import java.util.Objects;
import model.user.cart.CartItem;

public class Order {
    private static int index = 0;
    private final String orderId; // ID duy nhất của đơn hàng (final vì không thay đổi)
    private final String customerUsername; // ID của khách hàng đã đặt đơn hàng (final)
    private final LocalDateTime orderDate; // Thời điểm đơn hàng được đặt (final)
    private final List<CartItem> orderItems; // Danh sách các mặt hàng trong đơn hàng (final)
    private double totalAmount; // Tổng giá trị của đơn hàng
    private String orderStatus; // Trạng thái hiện tại của đơn hàng
    private String shippingAddress; // Địa chỉ giao hàng
    private String paymentMethod; // Phương thức thanh toán

    /**
     * Constructor cho Order.
     * OrderDate sẽ được tự động thiết lập là thời gian hiện tại khi đơn hàng được tạo.
     *
     * @param orderId ID duy nhất của đơn hàng.
     * @param customerId ID của khách hàng đặt đơn hàng.
     * @param orderItems Danh sách các mặt hàng trong đơn hàng. Đây nên là một bản sao từ giỏ hàng.
     * @param totalAmount Tổng giá trị của đơn hàng.
     * @param shippingAddress Địa chỉ giao hàng.
     * @param paymentMethod Phương thức thanh toán.
     * @throws NullPointerException nếu các tham số bắt buộc là null.
     * @throws IllegalArgumentException nếu totalAmount âm.
     */
    public Order(String customerUsername, List<CartItem> orderItems,
                 double totalAmount, String shippingAddress, String paymentMethod) {
        this.orderId = generateOrderId();
        this.customerUsername = Objects.requireNonNull(customerUsername, "Customer username cannot be null.");
        this.orderDate = LocalDateTime.now(); // <<-- Tự động lấy thời gian hiện tại
        this.orderItems = Objects.requireNonNull(orderItems, "Order items cannot be null."); // Lưu bản sao item
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative.");
        }
        this.totalAmount = totalAmount;

        this.orderStatus = "Pending"; // Trạng thái mặc định khi tạo đơn hàng
        this.shippingAddress = Objects.requireNonNull(shippingAddress, "Shipping address cannot be null.");
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "Payment method cannot be null.");
    }

    private static String generateOrderId() {
        String tempId = new String();
        if(index == 0 && index < 10) tempId = "OD000" + (++index);
        else if(index >= 10 && index < 100) tempId = "OD00" + (++index);
        else if(index >= 100 && index < 1000) tempId = "OD0" + (++index);
        else if(index >= 1000 && index < 10000) tempId = "OD" + (++index);
        else tempId = "OD" + (++index);
        return tempId;
    }
    public String getOrderId() {
        return orderId;
    }
    public String getCustomerUsername() {
        return customerUsername;
    }
    public LocalDateTime getOrderDate() {
        return orderDate;
    }
    public List<CartItem> getOrderItems() {
        return orderItems;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public String getOrderStatus() {
        return orderStatus;
    }
    public String getShippingAddress() {
        return shippingAddress;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setTotalAmount(double totalAmount) {
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative.");
        }
        this.totalAmount = totalAmount;
    }

    /**
     * Đặt trạng thái mới cho đơn hàng.
     *
     * @param orderStatus Trạng thái mới.
     * @throws NullPointerException nếu orderStatus là null.
     */
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = Objects.requireNonNull(orderStatus, "Order status cannot be null.");
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = Objects.requireNonNull(shippingAddress, "Shipping address cannot be null.");
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "Payment method cannot be null.");
    }

    // --- Phương thức toString để dễ dàng in ra thông tin đơn hàng ---
    @Override
    public String toString() {
        return "Order{" +
               "orderId='" + orderId + '\'' +
               ", customerUsername='" + customerUsername + '\'' +
               ", orderDate=" + orderDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + // Định dạng hiển thị
               ", totalAmount=" + String.format("%.2f", totalAmount) + // Định dạng 2 chữ số thập phân
               ", orderStatus=" + orderStatus + 
               ", itemsCount=" + orderItems.size() +
               '}';
    }
}