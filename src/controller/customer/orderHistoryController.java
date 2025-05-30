package controller.customer;

import controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField; // Thêm import này
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.order.Order; // Giả sử bạn có lớp Order
import model.user.customer.Customer; // Giả sử bạn có lớp Customer

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat; // Để định dạng tiền tệ
import java.time.LocalDate;
import java.time.LocalDateTime; // Nếu cột dateCol dùng LocalDateTime
import java.time.format.DateTimeFormatter; // Để định dạng ngày tháng
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class orderHistoryController implements Initializable {

@FXML private Button btnExit;
@FXML private DatePicker endDate;
@FXML private DatePicker startDate;
@FXML private Button resetButton;
@FXML private Button searchButton;
@FXML private HBox spacer; // Không cần xử lý gì trong controller

@FXML private TextField productSearchField; // THÊM fx:id="productSearchField" cho TextField tìm sản phẩm trong FXML

@FXML private TableView<Order> orderHistoryTable;
@FXML private TableColumn<Order, String> idCol;
@FXML private TableColumn<Order, String> productCol; // Sẽ hiển thị tóm tắt sản phẩm
@FXML private TableColumn<Order, LocalDate> dateCol;  // Hoặc LocalDateTime
@FXML private TableColumn<Order, Integer> quanCol;   // Tổng số lượng sản phẩm
@FXML private TableColumn<Order, Double> totalCol;
@FXML private TableColumn<Order, Void> detailsCol;   // Cột chứa nút "View Details"

private AppServiceManager appServiceManager;
private Customer currentUser;
private ObservableList<Order> observableOrderList;
private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

@Override
public void initialize(URL location, ResourceBundle resources) {
appServiceManager = Main.appServiceManager;
currentUser = (Customer) Main.currentUser;

if (appServiceManager == null || currentUser == null) {
    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tải dữ liệu người dùng hoặc dịch vụ.");
    return;
}

observableOrderList = FXCollections.observableArrayList();
setupTableColumns();
loadOrderHistory();
}

private void setupTableColumns() {
idCol.setCellValueFactory(new PropertyValueFactory<>("orderId")); // Giả sử Order có getOrderId()
productCol.setCellValueFactory(new PropertyValueFactory<>("productSummary")); // Giả sử Order có getProductSummary()
dateCol.setCellValueFactory(new PropertyValueFactory<>("orderDate")); // Giả sử Order có getOrderDate() trả về LocalDate
quanCol.setCellValueFactory(new PropertyValueFactory<>("totalItemsQuantity")); // Giả sử Order có getTotalItemsQuantity()
totalCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount") ); // Giả sử Order có getTotalAmount()

// Định dạng cột ngày
dateCol.setCellFactory(column -> new TableCell<Order, LocalDate>() {
    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(dateFormatter.format(item));
        }
    }
});

// Định dạng cột tổng tiền
totalCol.setCellFactory(column -> new TableCell<Order, Double>() {
    @Override
    protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
        } else {
            setText(currencyFormatter.format(item));
        }
    }
});

// Thiết lập cột "View Details" với Button
detailsCol.setCellFactory(param -> new TableCell<Order, Void>() {
    private final Button viewButton = new Button("Xem");

    {
        viewButton.setOnAction(event -> {
            Order order = getTableView().getItems().get(getIndex());
            handleViewOrderDetails(order);
        });
        viewButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Kiểu dáng nút
    }

    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(viewButton);
            setAlignment(Pos.CENTER);
        }
    }
});

orderHistoryTable.setItems(observableOrderList);
}

private void loadOrderHistory() {
if (appServiceManager.getOrderManager() != null) {
List<Order> orders = appServiceManager.getOrderManager().getOrderHistory(currentUser.getUsername(), Main.currentUser);
observableOrderList.setAll(orders);
} else {
showAlert(Alert.AlertType.ERROR, "Lỗi", "OrderManager không khả dụng.");
observableOrderList.clear();
}
}

@FXML
void btnExitclicked(ActionEvent event) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/HomePage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

        Stage orderHistoryStage = (Stage) btnExit.getScene().getWindow();
        orderHistoryStage.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
// Hoặc điều hướng về trang chủ nếu cần
// try {
//     Parent root = FXMLLoader.load(getClass().getResource("/view/customer/HomePage.fxml"));
//     stage.setScene(new Scene(root));
// } catch (IOException e) {
//     e.printStackTrace();
// }
}

@FXML
void handleReset(ActionEvent event) {
if (productSearchField != null) productSearchField.clear();
if (startDate != null) startDate.setValue(null);
if (endDate != null) endDate.setValue(null);
loadOrderHistory(); // Tải lại toàn bộ lịch sử đơn hàng
}

@FXML
void handleSearch(ActionEvent event) {
String productNameQuery = (productSearchField != null) ? productSearchField.getText() : "";
LocalDate start = (startDate != null) ? startDate.getValue() : null;
LocalDate end = (endDate != null) ? endDate.getValue() : null;

// Kiểm tra nếu end date < start date (nếu cả hai đều được chọn)
if (start != null && end != null && end.isBefore(start)) {
    showAlert(Alert.AlertType.WARNING, "Ngày không hợp lệ", "Ngày kết thúc không thể trước ngày bắt đầu.");
    return;
}

if (appServiceManager.getOrderManager() != null) {
    List<Order> searchResult = appServiceManager.getOrderManager().getOrdersByDateRange(start, end, Main.currentUser);
    observableOrderList.setAll(searchResult);
} else {
    showAlert(Alert.AlertType.ERROR, "Lỗi", "OrderManager không khả dụng.");
}
}

private void handleViewOrderDetails(Order order) {
if (order == null) return;
try {
// Thay thế bằng đường dẫn FXML và Controller chi tiết đơn hàng của bạn
FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/OrderDetail.fxml"));
Parent root = loader.load();

    // Nếu OrderDetailController của bạn cần đối tượng Order để hiển thị
    // OrderDetailController detailController = loader.getController();
    // detailController.setOrder(order);

    Stage detailStage = new Stage();
    detailStage.setTitle("Chi tiết đơn hàng: " + order.getOrderId());
    detailStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ gốc
    // detailStage.initOwner(((Node)btnExit).getScene().getWindow()); // Đặt owner nếu muốn
    detailStage.setScene(new Scene(root));
    detailStage.showAndWait(); // Hiển thị và chờ cho đến khi cửa sổ này đóng

} catch (IOException e) {
    e.printStackTrace();
    showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể mở chi tiết đơn hàng.");
}
}

private void showAlert(Alert.AlertType alertType, String title, String message) {
Alert alert = new Alert(alertType);
alert.setTitle(title);
alert.setHeaderText(null);
alert.setContentText(message);
alert.showAndWait();
}


}
