package model.manager.order;

import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import model.manager.product.ProductManager;
import model.order.Order;
import model.product.Product;
import model.product.interfaces.PhysicalProduct;
import model.user.User;
import model.user.cart.Cart;
import model.user.cart.CartItem;
import model.user.customer.Customer;
import model.user.interfaces.Manager;
import model.user.manager.Admin;
import model.user.manager.Employee;

public class OrderManager {
    private final ObservableList<Order> orderList;
    private final Map<String, Order> orderMap;

    private final ProductManager productManager; // Cần ProductManager để kiểm tra tồn kho và lấy thông tin sản phẩm

    public OrderManager(ProductManager productManager) {
        this.orderList = FXCollections.observableArrayList();
        this.orderMap = new HashMap<>();
        this.productManager = Objects.requireNonNull(productManager, "ProductManager cannot be null.");
    }

    /**
     * Đặt một đơn hàng mới từ giỏ hàng của khách hàng.
     *
     * @param customerCart Giỏ hàng của khách hàng.
     * @param customer Người dùng (Customer object) đặt đơn hàng.
     * @param shippingAddress Địa chỉ giao hàng.
     * @param paymentMethod Phương thức thanh toán.
     * @param currentUser Người dùng đang thực hiện hành động (có thể là khách hàng hoặc nhân viên).
     * @return true nếu đơn hàng được đặt thành công, false nếu thất bại (ví dụ: không đủ tồn kho, không có quyền).
     */
    public boolean placeOrder(Cart customerCart, Customer customer, String shippingAddress, String paymentMethod, String phoneNumber, User currentUser) {
        // --- Kiểm tra quyền hạn ---
        if (currentUser == null) {
            System.out.println("Access Denied: No user logged in to place an order.");
            return false;
        }
        // Admin/Employee/Manager có thể đặt hàng cho người khác. Customer chỉ đặt hàng cho chính mình.
        if (currentUser instanceof Customer && !currentUser.getUsername().equals(customer.getUsername())) {
            System.out.println("Access Denied: Customer " + currentUser.getUsername() + " cannot place order for another customer.");
            return false;
        }

        // --- Kiểm tra giỏ hàng ---
        if (customerCart == null || customerCart.getItems().isEmpty()) {
            System.out.println("Error: Cannot place an empty order.");
            return false;
        }

        // --- Kiểm tra tồn kho cho tất cả các mặt hàng vật lý trong giỏ hàng ---
        for (CartItem item : customerCart.getItems()) {
            Product productInCart = productManager.getProductById(item.getProductId()); // Lấy sản phẩm
            if (productInCart == null) {
                System.out.println("Order Failed: Product " + item.getProductId() + " not found in ProductManager.");
                return false;
            }
            if (productInCart instanceof PhysicalProduct) {
                int quantityInCart = item.getQuantity();
                int availableStock = productManager.getProductQuantity(productInCart.getId());
                if (availableStock < quantityInCart) {
                    System.out.println("Order Failed: Not enough stock for " + productInCart.getTitle() + ". Available: " + availableStock);
                    return false;
                }
            }
        }

        // --- Bước 1: Giảm số lượng tồn kho cho các sản phẩm vật lý ---
        for (CartItem item : customerCart.getItems()) {
            Product productInCart = productManager.getProductById(item.getProductId());
            if (productInCart != null && productInCart instanceof PhysicalProduct) {
                // Giảm tồn kho, không cần actingUser ở đây vì quyền đã được kiểm tra ở cấp OrderManager.placeOrder
                productManager.decreaseProductStock(item.getProductId(), item.getQuantity(), currentUser);
            }
        }

        // --- Bước 2: Tạo đối tượng Order mới ---
        String orderStatus = (paymentMethod.equals("Debit") || paymentMethod.equals("Credit")) ? "Pending" : "Approved";
        Order newOrder = new Order(
            customer.getUsername(),
            new ArrayList<CartItem>(customerCart.getItems()), // Tạo bản sao danh sách item
            customerCart.calculateTotal(productManager), // Tính tổng tiền
            shippingAddress,
            paymentMethod,
            phoneNumber,
            orderStatus
        );

        // --- Bước 3: Thêm đơn hàng vào danh sách quản lý ---
        orderList.add(newOrder);
        orderMap.put(newOrder.getOrderId(), newOrder);

        // --- Bước 4: Xóa giỏ hàng sau khi đặt hàng thành công ---
        customerCart.clear();

        System.out.println("Order " + newOrder.getOrderId() + " placed successfully by " + currentUser.getUsername() + " for customer " + customer.getUsername() + ".");
        return true;
    }

    /**
     * Cập nhật trạng thái đơn hàng.
     *
     * @param orderId ID của đơn hàng.
     * @param newStatus Trạng thái mới.
     * @param currentUser Người dùng đang thực hiện hành động.
     * @return true nếu cập nhật thành công, false nếu không có quyền hoặc lỗi.
     */
    public boolean updateOrderStatus(String orderId, String newStatus, User currentUser) {
        // --- Kiểm tra quyền hạn ---
        // Chỉ Admin hoặc Employee/Manager mới được cập nhật trạng thái đơn hàng
        if (currentUser == null || (!(currentUser instanceof Manager))) {
            System.out.println("Access Denied: User " + (currentUser != null ? currentUser.getUsername() : "N/A") + " does not have permission to update order status.");
            return false;
        }
        else if(orderMap.get(orderId).getOrderStatus().equals("CANCELLED") || orderMap.get(orderId).getOrderStatus().equals("DELIVERED") || orderMap.get(orderId).getOrderStatus().equals("Done")) {
            System.out.println("Error: Order " + orderId + " is " + orderMap.get(orderId).getOrderStatus() + " and cannot be updated.");
            return false;
        }

        Order order = orderMap.get(orderId);
        if (order == null) {
            System.out.println("Error: Order " + orderId + " not found.");
            return false;
        }
        order.setOrderStatus(newStatus);
        System.out.println("Order " + orderId + " status updated to " + newStatus + " by " + currentUser.getUsername() + ".");
        return true;
    }

    /**
     * Hủy một đơn hàng.
     *
     * @param orderId ID của đơn hàng cần hủy.
     * @param actingUser Người dùng đang thực hiện hành động.
     * @return true nếu hủy thành công, false nếu không có quyền hoặc lỗi.
     */
    public boolean cancelOrder(String orderId, User currentUser) {
        // --- Lấy đơn hàng để kiểm tra ---
        Order orderToCancel = orderMap.get(orderId);
        if (orderToCancel == null) {
            System.out.println("Error: Order " + orderId + " not found.");
            return false;
        }

        // --- KIỂM TRA QUYỀN HẠN ĐÃ CẬP NHẬT ---
        if (currentUser == null) {
            System.out.println("Access Denied: No user logged in to cancel order.");
            return false;
        }

        // Điều kiện cho phép hủy:
        // 1. Acting user là Admin, Employee, hoặc Manager (có thể hủy bất kỳ đơn hàng nào)
        // HOẶC
        // 2. Acting user là Customer VÀ ID của họ khớp với customerId của đơn hàng
        boolean hasPermission = false;
        if (currentUser instanceof Admin || currentUser instanceof Employee || currentUser instanceof Manager) {
            hasPermission = true;
        } else if (currentUser instanceof Customer && currentUser.getUsername().equals(orderToCancel.getCustomerUsername())) {
            hasPermission = true;
        }

        if (!hasPermission) {
            System.out.println("Access Denied: User " + currentUser.getUsername() + " does not have permission to cancel order " + orderId + ".");
            return false;
        }
        // --- HẾT KIỂM TRA QUYỀN HẠN ---


        // --- Kiểm tra trạng thái đơn hàng (như cũ) ---
        if (orderToCancel.getOrderStatus().equals("CANCELLED") || orderToCancel.getOrderStatus().equals("DELIVERED")) {
            System.out.println("Error: Order " + orderId + " cannot be cancelled from status " + orderToCancel.getOrderStatus());
            return false;
        }

        // --- Thực hiện hủy đơn hàng (như cũ) ---
        orderToCancel.setOrderStatus("CANCELLED");
        // Hoàn lại tồn kho nếu cần (sản phẩm vật lý)
        for (CartItem item : orderToCancel.getOrderItems()) {
            Product product = productManager.getProductById(item.getProductId());
            if (product != null && product instanceof PhysicalProduct) {
                productManager.increaseProductStock(item.getProductId(), item.getQuantity(), currentUser);
            }
        }
        System.out.println("Order " + orderId + " cancelled by " + currentUser.getUsername() + ".");
        return true;
    }

    /**
     * Lấy danh sách tất cả các đơn hàng.
     *
     * @param currentUser Người dùng đang thực hiện hành động.
     * @return ObservableList chứa tất cả các đơn hàng, hoặc rỗng nếu không có quyền.
     */
    public ObservableList<Order> getAllOrders(User currentUser) {
        if (currentUser == null || !(currentUser instanceof Manager)) {
            System.out.println("Access Denied: User " + (currentUser != null ? currentUser.getUsername() : "N/A") + " does not have permission to view all orders.");
            return FXCollections.observableArrayList(); // Trả về danh sách rỗng nếu không có quyền
        }
        return orderList;
    }

    /**
     * Lấy đơn hàng theo ID.
     *
     * @param orderId ID của đơn hàng.
     * @return Đối tượng Order nếu tìm thấy, null nếu không.
     */
    public Order getOrderById(String orderId) {
        return orderMap.get(orderId);
    }

    /**
     * Lấy lịch sử đơn hàng của một người dùng dựa trên ID của họ.
     *
     * @param userId     ID của người dùng mà bạn muốn xem lịch sử đơn hàng.
     * @param actingUser Người dùng đang thực hiện hành động (để kiểm tra quyền truy cập).
     * @return ObservableList<Order> chứa lịch sử đơn hàng của người dùng,
     * hoặc một danh sách rỗng nếu không có quyền hoặc không tìm thấy đơn hàng.
     */
    public ObservableList<Order> getOrderHistory(String username, User currentUser) {
        // --- Kiểm tra quyền hạn ---
        // Admin, Employee, Manager có thể xem lịch sử đơn hàng của bất kỳ ai.
        // Khách hàng chỉ có thể xem lịch sử đơn hàng của chính mình.
        if (currentUser == null) {
            System.out.println("Access Denied: No user logged in.");
            return FXCollections.observableArrayList();
        }
        if (!(currentUser instanceof Manager) && !currentUser.getUsername().equals(username)) {
            System.out.println("Access Denied: User " + currentUser.getUsername() + " does not have permission to view order history for user ID " + username + ".");
            return FXCollections.observableArrayList();
        }

        // --- Lọc danh sách đơn hàng ---
        ObservableList<Order> userOrders = FXCollections.observableArrayList();
        for (Order order : orderList) {
            if (order.getCustomerUsername().equals(username)) {
                userOrders.add(order);
            }
        }
        return userOrders;
    }

    /**
     * Lấy danh sách các đơn hàng trong một khoảng thời gian cụ thể.
     * Chỉ Admin, Employee, Manager mới được phép truy cập.
     *
     * @param startDate Ngày bắt đầu của khoảng thời gian (bao gồm).
     * @param endDate Ngày kết thúc của khoảng thời gian (bao gồm).
     * @param actingUser Người dùng đang thực hiện hành động.
     * @return ObservableList chứa các đơn hàng trong khoảng thời gian, hoặc danh sách rỗng nếu không có quyền hoặc không tìm thấy.
     * @throws IllegalArgumentException nếu startDate sau endDate.
     */
    public ObservableList<Order> getOrdersByDateRange(LocalDate startDate, LocalDate endDate, User currentUser) {
        Objects.requireNonNull(startDate, "Start date cannot be null.");
        Objects.requireNonNull(endDate, "End date cannot be null.");

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        // --- Kiểm tra quyền hạn ---
        // Chỉ Admin, Employee, Manager mới được xem danh sách đơn hàng theo khoảng thời gian
        if (currentUser == null || (!(currentUser instanceof Manager))) {
            System.out.println("Access Denied: User " + (currentUser != null ? currentUser.getUsername() : "N/A") + " does not have permission to view orders by date range.");
            return FXCollections.observableArrayList(); // Trả về danh sách rỗng nếu không có quyền
        }

        // --- Lọc đơn hàng theo khoảng thời gian ---
        ObservableList<Order> filteredOrders = FXCollections.observableArrayList();
        for (Order order : orderList) {
            LocalDate orderLocalDate = order.getOrderDate().toLocalDate(); // Chuyển LocalDateTime thành LocalDate
            if (!orderLocalDate.isBefore(startDate) && !orderLocalDate.isAfter(endDate)) {
                // Điều kiện: (orderDate >= startDate) AND (orderDate <= endDate)
                filteredOrders.add(order);
            }
        }
        return filteredOrders;
    }
}