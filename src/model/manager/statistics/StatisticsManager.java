package model.manager.statistics;

import java.time.LocalDate;
import java.util.ArrayList;
import model.manager.order.OrderManager;
import model.manager.product.ProductManager;
import model.order.Order;
import model.user.User;
import model.user.cart.CartItem;
import model.user.manager.Admin;

public class StatisticsManager {
    private final ProductManager productManager;
    private final OrderManager orderManager;

    public StatisticsManager(ProductManager productManager, OrderManager orderManager) {
        this.productManager = productManager;
        this.orderManager = orderManager;
    }

    private boolean isAdmin(User currentUser) {
        return currentUser instanceof Admin;
    }

    /**
     * Phương thức trợ giúp để lọc các đơn hàng hợp lệ (Done hoặc Delivered) trong một khoảng thời gian.
     * Phương thức này không sử dụng toán tử lambda (`->`).
     *
     * @param currentUser Người dùng hiện tại, phải là Admin.
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Danh sách các đơn hàng hợp lệ, hoặc danh sách rỗng nếu người dùng không phải Admin.
     */
    private ArrayList<Order> getFilteredAndValidOrders(User currentUser, LocalDate startDate, LocalDate endDate) {
        if (!isAdmin(currentUser)) {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu không phải Admin
        }

        ArrayList<Order> validOrders = new ArrayList<>();
        for (Order order : orderManager.getAllOrders(currentUser)) {
            // Kiểm tra ngày tháng
            boolean isAfterStartDate = !order.getOrderDate().isBefore(startDate);
            boolean isBeforeEndDate = !order.getOrderDate().isAfter(endDate);

            // Kiểm tra trạng thái
            boolean isDoneOrDelivered = "Done".equals(order.getOrderStatus()) || "Delivered".equals(order.getOrderStatus());

            if (isAfterStartDate && isBeforeEndDate && isDoneOrDelivered) {
                validOrders.add(order);
            }
        }
        return validOrders;
    }

    /**
     * Tính tổng doanh thu trong một khoảng thời gian, chỉ tính các đơn hàng Done hoặc Delivered.
     * Phương thức này không sử dụng toán tử lambda (`->`).
     *
     * @param currentUser Người dùng hiện tại, phải là Admin.
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Tổng doanh thu, hoặc 0.0 nếu người dùng không phải Admin.
     */
    public double calculateTotalRevenue(User currentUser, LocalDate startDate, LocalDate endDate) {
        ArrayList<Order> validOrders = getFilteredAndValidOrders(currentUser, startDate, endDate);
        
        // In thông báo truy cập nếu không phải Admin
        if (validOrders.isEmpty() && !isAdmin(currentUser)) {
            System.out.println("Access Denied: Only Admin can view total revenue.");
            return 0.0;
        }
        // Nếu là Admin nhưng không có đơn hàng hợp lệ, vẫn trả về 0.0
        if (validOrders.isEmpty()) { // Kiểm tra lại sau khi đã xác định Admin
            return 0.0;
        }

        double totalRevenue = 0.0;
        for (Order order : validOrders) {
            totalRevenue += order.getTotalAmount();
        }
        return totalRevenue;
    }

    /**
     * Tính tổng chi phí của những sản phẩm có trong đơn hàng trong một khoảng thời gian,
     * chỉ tính các đơn hàng Done hoặc Delivered.
     * Phương thức này không sử dụng toán tử lambda (`->`).
     *
     * @param currentUser Người dùng hiện tại, phải là Admin.
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Tổng chi phí, hoặc 0.0 nếu người dùng không phải Admin.
     */
    public double calculateTotalPurchaseCostOfSoldProducts(User currentUser, LocalDate startDate, LocalDate endDate) {
        ArrayList<Order> validOrders = getFilteredAndValidOrders(currentUser, startDate, endDate);
        
        if (validOrders.isEmpty() && !isAdmin(currentUser)) {
            System.out.println("Access Denied: Only Admin can view total purchase cost.");
            return 0.0;
        }
        if (validOrders.isEmpty()) {
            return 0.0;
        }

        double totalPurchaseCost = 0.0;
        for (Order order : validOrders) {
            totalPurchaseCost += getTotalCostOfOrder(order);
        }
        return totalPurchaseCost;
    }
    
    private double getTotalCostOfOrder(Order order) {
        double totalCost = 0.0;
        for (CartItem item : order.getOrderItems()) {
            totalCost += productManager.getProductById(item.getProductId()).getPurchasePrice() * item.getQuantity();
        }
        return totalCost;
    }
    
    /**
     * Tính tổng số đơn hàng trong một khoảng thời gian, chỉ tính các đơn hàng Done hoặc Delivered.
     * Phương thức này không sử dụng toán tử lambda (`->`).
     *
     * @param currentUser Người dùng hiện tại, phải là Admin.
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Tổng số đơn hàng, hoặc 0 nếu người dùng không phải Admin.
     */
    public int calculateTotalOrders(User currentUser, LocalDate startDate, LocalDate endDate) {
        ArrayList<Order> validOrders = getFilteredAndValidOrders(currentUser, startDate, endDate);
        
        if (validOrders.isEmpty() && !isAdmin(currentUser)) {
            System.out.println("Access Denied: Only Admin can view total orders.");
            return 0;
        }
        return validOrders.size();
    }

    /**
     * Tính tổng số sản phẩm đã bán trong một khoảng thời gian, chỉ tính các đơn hàng Done hoặc Delivered.
     * Phương thức này không sử dụng toán tử lambda (`->`).
     *
     * @param currentUser Người dùng hiện tại, phải là Admin.
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Tổng số sản phẩm đã bán, hoặc 0 nếu người dùng không phải Admin.
     */
    public int calculateTotalProductsSold(User currentUser, LocalDate startDate, LocalDate endDate) {
        ArrayList<Order> validOrders = getFilteredAndValidOrders(currentUser, startDate, endDate);
        
        if (validOrders.isEmpty() && !isAdmin(currentUser)) {
            System.out.println("Access Denied: Only Admin can view total products sold.");
            return 0;
        }
        if (validOrders.isEmpty()) {
            return 0;
        }

        int totalProducts = 0;
        for (Order order : validOrders) {
            for (CartItem item : order.getOrderItems()) {
                totalProducts += item.getQuantity();
            }
        }
        return totalProducts;
    }

    /**
     * Tính tổng số đơn hàng bị hủy trong một khoảng thời gian.
     * Phương thức này không sử dụng toán tử lambda (`->`).
     *
     * @param currentUser Người dùng hiện tại, phải là Admin.
     * @param startDate Ngày bắt đầu của khoảng thời gian.
     * @param endDate Ngày kết thúc của khoảng thời gian.
     * @return Tổng số đơn hàng bị hủy, hoặc 0 nếu người dùng không phải Admin.
     */
    public int calculateTotalCancelledOrders(User currentUser, LocalDate startDate, LocalDate endDate) {
        if (!isAdmin(currentUser)) {
            System.out.println("Access Denied: Only Admin can view total cancelled orders.");
            return 0;
        }

        int totalCancelledOrders = 0;
        for (Order order : orderManager.getAllOrders(currentUser)) {
            // Kiểm tra ngày tháng
            boolean isAfterStartDate = !order.getOrderDate().isBefore(startDate);
            boolean isBeforeEndDate = !order.getOrderDate().isAfter(endDate);

            // Kiểm tra trạng thái
            boolean isCancelled = "Cancelled".equals(order.getOrderStatus());

            if (isAfterStartDate && isBeforeEndDate && isCancelled) {
                totalCancelledOrders++;
            }
        }
        return totalCancelledOrders;
    }
}
