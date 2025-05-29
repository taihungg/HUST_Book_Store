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
    private final double totalAmount; // Tổng giá trị của đơn hàng
    private final String shippingAddress; // Địa chỉ giao hàng
    private final String paymentMethod; // Phương thức thanh toán
    private final String phoneNumber; // Số điện thoại khách hàng
    private String orderStatus; // Trạng thái hiện tại của đơn hàng

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
                 double totalAmount, String shippingAddress, String paymentMethod, String phoneNumber, String orderStatus) {
        this.orderId = generateOrderId();
        this.customerUsername = Objects.requireNonNull(customerUsername, "Customer username cannot be null.");
        this.orderDate = LocalDateTime.now(); // <<-- Tự động lấy thời gian hiện tại
        this.orderItems = Objects.requireNonNull(orderItems, "Order items cannot be null."); // Lưu bản sao item
        if (totalAmount < 0) {
            throw new IllegalArgumentException("Total amount cannot be negative.");
        }
        this.totalAmount = totalAmount;
        this.shippingAddress = Objects.requireNonNull(shippingAddress, "Shipping address cannot be null.");
        this.paymentMethod = Objects.requireNonNull(paymentMethod, "Payment method cannot be null.");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "Phone number cannot be null.");
        this.orderStatus = Objects.requireNonNull(orderStatus, "Order status cannot be null.");
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
    public String getPhoneNumber() {
        return phoneNumber;
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