package model.manager.order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import java.util.Optional; // Bỏ import này
import java.util.UUID;
import java.util.stream.Collectors; // Vẫn dùng stream().collect() cho getAllOrders, có thể bỏ nếu muốn

// Đảm bảo các lớp Order, Cart, CartItem, ProductManager, Product, User, OrderStatus đã được định nghĩa

public class OrderManager {
    private final ObservableList<Order> orderList;
    private final Map<String, Order> orderMap;

    public OrderManager() {
        orderList = FXCollections.observableArrayList();
        orderMap = new HashMap<>();
    }
    

}