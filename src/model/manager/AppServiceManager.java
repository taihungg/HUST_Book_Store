package model.manager;

import model.manager.order.OrderManager;
import model.manager.product.ProductManager;
import model.manager.user.UserManager;

public class AppServiceManager {
    private final ProductManager productManager;
    private final OrderManager orderManager;
    private final UserManager userManager;

    public AppServiceManager() {
        this.productManager = new ProductManager();
        this.orderManager = new OrderManager();
        this.userManager = new UserManager();
    }
    /* 
    sẽ chỉ có hàm getter cho các thuộc tính này
    khi cần quản lí gì đó thì gọi AppServiceManager.get...Manager().method() tương ứng
    ví dụ muốn add sản phẩm mới thì gọi AppServiceManager.getProductManager().addProduct(new Product(), 0)
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
}
