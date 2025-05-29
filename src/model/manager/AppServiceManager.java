package model.manager;

import model.manager.order.OrderManager;
import model.manager.product.ProductManager;
import model.manager.statistics.StatisticsManager;
import model.manager.user.UserManager;
import model.user.User;

public class AppServiceManager {
    private final ProductManager productManager;
    private final OrderManager orderManager;
    private final UserManager userManager;
    private final StatisticsManager statisticsManager;
    private User currentUser;

    public AppServiceManager() {
        this.productManager = new ProductManager();
        this.orderManager = new OrderManager(this.productManager);
        this.userManager = new UserManager();
        this.statisticsManager = new StatisticsManager(this.productManager, this.orderManager);
        this.currentUser = null;
    }
    /* 
    sẽ chỉ có hàm getter cho các thuộc tính này
    khi cần quản lí gì đó thì gọi AppServiceManager.get...Manager(user).method() tương ứng
    ví dụ muốn add sản phẩm mới thì gọi AppServiceManager.getProductManager(user).addProduct(new Product(), 0)
    gọi từ controller theo logic của add sản phẩm mới
    */
	public ProductManager getProductManager() {
		return productManager;
	}
	public OrderManager getOrderManager() {
		return orderManager;
	}   
	public UserManager getUserManager() {
		return userManager;
	}
	public StatisticsManager getStatisticsManager() {
		return statisticsManager;
	}

	public User getCurrentUser() {
		return currentUser;
	}
    
    /**
     * Xử lý yêu cầu đăng nhập của người dùng.
     * @param username Tên đăng nhập.
     * @param password Mật khẩu.
     * @return true nếu đăng nhập thành công, false nếu thất bại.
     */
    public boolean login(String username, String password) {
        // Gọi UserManager để xác thực thông tin
        // (UserManager.authenticateUser trả về User hoặc null, không dùng Optional theo yêu cầu gần đây)
        User authenticatedUser = userManager.authenticateUser(username, password);

        if (authenticatedUser != null) {
            this.currentUser = authenticatedUser; // Thiết lập người dùng đã đăng nhập
            System.out.println("AppServiceManager: User '" + username + "' authenticated and set as loggedInUser.");
            // Giỏ hàng (nếu có) sẽ nằm trong đối tượng Customer này
            return true;
        } else {
            this.currentUser = null; // Đảm bảo không có người dùng nào được đặt nếu đăng nhập thất bại
            return false;
        }
    }

    /**
     * Xử lý yêu cầu đăng xuất của người dùng.
     */
    public void logout() {
        if (this.currentUser != null) {
            System.out.println("AppServiceManager: User '" + this.currentUser.getUsername() + "' logged out.");
        }
        this.currentUser = null; // Xóa thông tin người dùng đang đăng nhập
    }
}
