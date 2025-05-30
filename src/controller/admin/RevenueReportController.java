package controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.manager.statistics.StatisticsManager;
import model.user.User;
import model.user.manager.Employee;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.ResourceBundle;

import controller.Main; // Giả sử Main chứa AppServiceManager và currentUser

public class RevenueReportController implements Initializable {


@FXML private DatePicker startDatePicker;
@FXML private DatePicker endDatePicker;
@FXML private Button generateReportButton;
@FXML private ComboBox<String> predefinedDateRangeComboBox;

// Summary Box Labels
@FXML private Label totalRevenueLabel; // Tổng doanh thu
@FXML private Label totalProfitsLabel; // Tổng lợi nhuận (Doanh thu - Giá vốn hàng bán)
@FXML private Label profitMarginLabel; // FXML ghi "Total products sales" -\> Tổng sản phẩm đã bán
@FXML private Label totalOrdersLabel;  // FXML ghi "Total orders successfull" -\> Tổng đơn hàng thành công
@FXML private Label totalCostsLabel1; // FXML trong VBox "Total orders cancelled" -\> Tổng đơn hàng bị hủy
@FXML private Label averageRevenuePerOrderLabel; // FXML ghi "Total products cost" -\> Tổng giá vốn hàng bán

// Monthly Cost Section Labels (Sẽ cần điều chỉnh vì StatisticsManager không cung cấp chi tiết này)
@FXML private Label profitMarginLabel1; // FXML ghi "Employee salaries" -\> Lương nhân viên
@FXML private Label totalRevenueLabel1; // FXML ghi "Other" -\> Chi phí khác
@FXML private Label totalProfitsLabel1; // FXML ghi "Total" -\> Tổng lợi nhuận (hiển thị lại)

@FXML private Button ExitButton;


private StatisticsManager statisticsManager ;
private User currentUser; // Người dùng hiện tại, giả sử là Admin
private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

@Override
public void initialize(URL location, ResourceBundle resources) {
// Lấy instance từ Main (giả định)
AppServiceManager appServiceManager = Main.appServiceManager;
currentUser = Main.currentUser;
statisticsManager = new StatisticsManager(appServiceManager.getProductManager(), appServiceManager.getOrderManager());



if (appServiceManager == null || currentUser == null) {
showAlert(Alert.AlertType.ERROR, "Lỗi Khởi Tạo", "Không thể tải dịch vụ hoặc thông tin người dùng.");
// Vô hiệu hóa các nút hoặc xử lý khác
generateReportButton.setDisable(true);
return;
}

populatePredefinedDateRanges();
setDefaultDateRange(); // Đặt khoảng ngày mặc định và tải báo cáo ban đầu
updateReport(); // Tải báo cáo ban đầu


}

private void populatePredefinedDateRanges() {
ObservableList<String> ranges = FXCollections.observableArrayList(
"Today",
"Yesterday",
"7 days ago",
"30 days ago",
"This month",
"Last month"
);
predefinedDateRangeComboBox.setItems(ranges);
predefinedDateRangeComboBox.setOnAction(event -> handlePredefinedDateRangeSelection());
}

private void setDefaultDateRange() {
// Mặc định là tháng này
predefinedDateRangeComboBox.setValue("This month");
applyPredefinedDateRange("This month");
}

@FXML
void handleGenerateReport(ActionEvent event) {
updateReport();
}

private void updateReport() {
LocalDate startDate = startDatePicker.getValue();
LocalDate endDate = endDatePicker.getValue();

if (startDate == null || endDate == null) {
showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn ngày bắt đầu và ngày kết thúc.");
return;
}

if (endDate.isBefore(startDate)) {
showAlert(Alert.AlertType.WARNING, "Ngày không hợp lệ", "Ngày kết thúc không thể trước ngày bắt đầu.");
return;
}

// Tính toán từ StatisticsManager
double totalRevenue = statisticsManager.calculateTotalRevenue(currentUser, startDate, endDate);
System.out.println("Total Revenue: " + totalRevenue);
double totalCostOfSoldProducts = statisticsManager.calculateTotalPurchaseCostOfSoldProducts(currentUser, startDate, endDate);
System.out.println("Total Cost of Sold Products: " + totalCostOfSoldProducts);
double totalProfit = totalRevenue - totalCostOfSoldProducts;
System.out.println("Total Profit: " + totalProfit);
int totalSuccessfulOrders = statisticsManager.calculateTotalDoneOrDeliveredOrders(currentUser, startDate, endDate);
System.out.println("Total Successful Orders: " + totalSuccessfulOrders);
int totalProductsSold = statisticsManager.calculateTotalProductsSold(currentUser, startDate, endDate);
System.out.println("Total Products Sold: " + totalProductsSold);
int totalCancelledOrders = statisticsManager.calculateTotalCancelledOrders(currentUser, startDate, endDate);
System.out.println("Total Cancelled Orders: " + totalCancelledOrders);

// Cập nhật các Label trong ô tổng quan
totalRevenueLabel.setText(currencyFormatter.format(totalRevenue));
totalProfitsLabel.setText(currencyFormatter.format(totalProfit));
profitMarginLabel.setText(String.valueOf(totalProductsSold)); // FXML: Total products sales
totalOrdersLabel.setText(String.valueOf(totalSuccessfulOrders)); // FXML: Total orders successfull
totalCostsLabel1.setText(String.valueOf(totalCancelledOrders)); // FXML: Total orders cancelled
averageRevenuePerOrderLabel.setText(currencyFormatter.format(totalCostOfSoldProducts)); // FXML: Total products cost

// Cập nhật các Label trong mục "Monthly Cost"
// StatisticsManager không cung cấp chi phí lương hay "Other" trực tiếp theo khoảng ngày.
// Tạm thời đặt là "N/A" hoặc 0.
int totalSalary = 0;
for (User user : Main.appServiceManager.getUserManager().getAllUsers(Main.currentUser)) {
    if (user instanceof Employee) {
        totalSalary += ((Employee) user).getSalary();
    }
}
profitMarginLabel1.setText(String.valueOf(totalSalary) + "$"); // Lương nhân viên
totalRevenueLabel1.setText("N/A");  // Chi phí khác
totalProfitsLabel1.setText(String.valueOf(totalProfit) + "$"); // Tổng lợi nhuận (hiển thị lại)


}

private void handlePredefinedDateRangeSelection() {
String selectedRange = predefinedDateRangeComboBox.getValue();
if (selectedRange == null) return;
applyPredefinedDateRange(selectedRange);
}

private void applyPredefinedDateRange(String range) {
LocalDate today = LocalDate.now();
LocalDate start = today;
LocalDate end = today;

switch (range) {
case "Today":
start = today;
end = today;
break;
case "Yesterday":
start = today.minusDays(1);
end = today.minusDays(1);
break;
case "7 days ago":
start = today.minusDays(6); // Bao gồm cả hôm nay, nên là 6 ngày trước đến hôm nay
end = today;
break;
case "30 days ago":
start = today.minusDays(29);
end = today;
break;
case "This month":
start = today.with(TemporalAdjusters.firstDayOfMonth());
end = today.with(TemporalAdjusters.lastDayOfMonth());
break;
case "Last month":
start = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
end = today.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
break;
default:
return; // Không làm gì nếu không khớp
}
startDatePicker.setValue(start);
endDatePicker.setValue(end);
updateReport(); // Tự động cập nhật báo cáo khi chọn khoảng thời gian định sẵn


}

@FXML
void handleExit(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/HomePageAdmin.fxml"));
    Parent root = loader.load();
    Scene scene = new Scene(root);
    Stage stage = (Stage) ExitButton.getScene().getWindow();
    stage.setScene(scene);
    stage.show();

    Stage currentStage = (Stage) ExitButton.getScene().getWindow();
    currentStage.close();
// Hoặc điều hướng về trang chủ admin nếu cần
// try {
//     Parent root = FXMLLoader.load(getClass().getResource("/view/admin/HomePageAdmin.fxml"));
//     Scene scene = new Scene(root);
//     stage.setScene(scene);
//     stage.show();
// } catch (IOException e) {
//     e.printStackTrace();
// }
}

private void showAlert(Alert.AlertType alertType, String title, String message) {
Alert alert = new Alert(alertType);
alert.setTitle(title);
alert.setHeaderText(null);
alert.setContentText(message);
alert.showAndWait();
}

}