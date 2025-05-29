package controller.admin;

import controller.Main; // Giả sử bạn có lớp Main để lấy AppServiceManager
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.manager.AppServiceManager; // Thay thế bằng import đúng của bạn

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class RevenueReportController implements Initializable {

    //<editor-fold desc="FXML Declarations">
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button generateReportButton;
    @FXML private ComboBox<String> predefinedDateRangeComboBox;

    @FXML private Label totalRevenueLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label averageRevenuePerOrderLabel;
    @FXML private Label totalCostsLabel;
    @FXML private Label totalProfitsLabel;
    @FXML private Label profitMarginLabel;

    @FXML private VBox totalCostsContainerVBox;
    @FXML private VBox detailedCostsPane;
    @FXML private Label totalSalaryLabel;
    @FXML private Label totalBookPurchaseCostLabel;

    @FXML private CategoryAxis timeAxis;
    @FXML private NumberAxis revenueAxis;

    @FXML private Button ExitButton;
    //</editor-fold>

    private AppServiceManager appServiceManager  = Main.appServiceManager; // Giả sử bạn lấy nó từ Main hoặc được inject

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Lấy instance của AppServiceManager
        // Ví dụ: this.appServiceManager = Main.getAppServiceManager();
        // Hoặc nếu controller này được tạo và quản lý bởi một framework DI,
        // appServiceManager có thể được inject.
        // Vì mục đích demo, chúng ta sẽ giả sử nó được set từ bên ngoài hoặc Main
        if (Main.appServiceManager != null) { // Kiểm tra nếu Main.appServiceManager là static và có thể truy cập
             this.appServiceManager = Main.appServiceManager;
        } else {
            // Xử lý trường hợp appServiceManager không có sẵn, có thể hiển thị lỗi hoặc disable các chức năng
            System.err.println("AppServiceManager is not available in RevenueReportController.");
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể khởi tạo dịch vụ quản lý ứng dụng.");
        }


        // Thiết lập các giá trị mặc định cho DatePicker
        endDatePicker.setValue(LocalDate.now());
        startDatePicker.setValue(LocalDate.now().minusMonths(1)); // Ví dụ: 1 tháng trước

        // Thiết lập ComboBox chọn nhanh khoảng thời gian
        ObservableList<String> dateRanges = FXCollections.observableArrayList(
                "Today",
                "Yesterday",
                "7 days ago",
                "30 days ago",
                "This month",
                "Last month",
                "This year"
        );
        predefinedDateRangeComboBox.setItems(dateRanges);
        predefinedDateRangeComboBox.setOnAction(this::handlePredefinedDateRangeChange);

        // Thiết lập sự kiện click cho phần Tổng Chi Phí để hiển thị/ẩn chi tiết
        if (totalCostsContainerVBox != null && detailedCostsPane != null) {
            totalCostsContainerVBox.setOnMouseClicked(this::handleToggleDetailedCosts);
        } else {
            System.err.println("totalCostsContainerVBox hoặc detailedCostsPane chưa được inject từ FXML.");
        }


        // Thiết lập sự kiện cho nút "Xem báo cáo"
       

        // Thiết lập sự kiện cho nút "Thoát"
        if (ExitButton != null) {
            ExitButton.setOnAction(this::handleExit);
        }

        // Tải dữ liệu báo cáo ban đầu dựa trên ngày mặc định
        loadReportData();
    }

    @FXML
    private void handleGenerateReport(ActionEvent event) {
        loadReportData();
    }

    @FXML
    private void handlePredefinedDateRangeChange(ActionEvent event) {
        String selectedRange = predefinedDateRangeComboBox.getValue();
        if (selectedRange == null) return;

        LocalDate today = LocalDate.now();
        LocalDate start = today;
        LocalDate end = today;

        switch (selectedRange) {
            case "Today":
                start = today;
                end = today;
                break;
            case "Yesterday":
                start = today.minusDays(1);
                end = today.minusDays(1);
                break;
            case "7 days ago":
                start = today.minusDays(6); // Bao gồm cả hôm nay
                end = today;
                break;
            case "30 days ago":
                start = today.minusDays(29); // Bao gồm cả hôm nay
                end = today;
                break;
            case "This month":
                start = today.withDayOfMonth(1);
                end = today.withDayOfMonth(today.lengthOfMonth());
                break;
            case "Last month":
                LocalDate lastMonth = today.minusMonths(1);
                start = lastMonth.withDayOfMonth(1);
                end = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
                break;
            case "This year":
                start = today.withDayOfYear(1);
                end = today.withDayOfYear(today.lengthOfYear());
                break;
        }
        startDatePicker.setValue(start);
        endDatePicker.setValue(end);
        loadReportData(); // Tải lại báo cáo với khoảng thời gian mới
    }

    @FXML
    private void handleToggleDetailedCosts(MouseEvent event) {
        if (detailedCostsPane != null) {
            boolean isVisible = detailedCostsPane.isVisible();
            detailedCostsPane.setVisible(!isVisible);
            detailedCostsPane.setManaged(!isVisible); // Quan trọng để layout tự điều chỉnh

            if (detailedCostsPane.isVisible()) {
                // Khi hiển thị, tải dữ liệu chi tiết chi phí
                loadDetailedCosts();
            }
        }
    }

    private void loadReportData() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng chọn ngày bắt đầu và ngày kết thúc.");
            return;
        }
        if (startDate.isAfter(endDate)) {
            showAlert(Alert.AlertType.WARNING, "Ngày không hợp lệ", "Ngày bắt đầu không thể sau ngày kết thúc.");
            return;
        }

        System.out.println("Đang tạo báo cáo từ " + startDate + " đến " + endDate);

        // TODO: Gọi các phương thức từ AppServiceManager (hoặc ReportManager) để lấy dữ liệu
        // Ví dụ:
        // double revenue = appServiceManager.getReportManager().getTotalRevenue(startDate, endDate);
        // int orders = appServiceManager.getReportManager().getTotalOrders(startDate, endDate);
        // double costs = appServiceManager.getReportManager().getTotalCosts(startDate, endDate); // Tổng chi phí chung
        // double profits = revenue - costs; // Tính toán lợi nhuận

        // Dữ liệu giả để demo
        
        double revenue = Math.random() * 50000000 + 10000000; // 10M - 60M
        int orders = (int) (Math.random() * 500 + 50); // 50 - 550
        double costs = revenue * (Math.random() * 0.4 + 0.3); // Chi phí từ 30-70% doanh thu
        double profits = revenue - costs;
        double avgRevenuePerOrder = (orders > 0) ? revenue / orders : 0;
        double profitMargin = (revenue > 0) ? (profits / revenue) * 100 : 0;
         
        updateSummaryLabels(revenue, orders, avgRevenuePerOrder, costs, profits, profitMargin);
        
        // Nếu detailedCostsPane đang hiển thị, cũng cập nhật nó
        if (detailedCostsPane != null && detailedCostsPane.isVisible()) {
            loadDetailedCosts();
        }
    }

    private void updateSummaryLabels(double totalRevenue, int totalOrders, double avgRevenue, double totalCosts, double totalProfits, double profitMarginVal) {
        Locale vietnameseLocale = new Locale("vi", "VN");
        totalRevenueLabel.setText(String.format(vietnameseLocale, "%,.0f VNĐ", totalRevenue));
        totalOrdersLabel.setText(String.valueOf(totalOrders));
        averageRevenuePerOrderLabel.setText(String.format(vietnameseLocale, "%,.0f VNĐ", avgRevenue));
        totalCostsLabel.setText(String.format(vietnameseLocale, "%,.0f VNĐ", totalCosts));
        totalProfitsLabel.setText(String.format(vietnameseLocale, "%,.0f VNĐ", totalProfits));
        profitMarginLabel.setText(String.format(vietnameseLocale, "%.1f%%", profitMarginVal));
    }
    
    private void loadDetailedCosts() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            // Không tải nếu ngày không hợp lệ hoặc detailedCostsPane không hiển thị
            totalSalaryLabel.setText("N/A");
            totalBookPurchaseCostLabel.setText("N/A");
            return;
        }

        // TODO: Lấy dữ liệu chi tiết chi phí từ AppServiceManager/ReportManager
        // double salaryCosts = appServiceManager.getReportManager().getTotalSalaryCosts(startDate, endDate);
        // double bookPurchaseCosts = appServiceManager.getReportManager().getTotalBookPurchaseCosts(startDate, endDate);

        // Dữ liệu giả để demo
        double totalCostsVal = Double.parseDouble(totalCostsLabel.getText().replaceAll("[^\\d.]", "")); // Lấy lại tổng chi phí từ label
        double salaryCosts = totalCostsVal * (Math.random() * 0.3 + 0.4); // Lương chiếm 40-70% tổng chi phí
        double bookPurchaseCosts = totalCostsVal - salaryCosts; // Phần còn lại là chi phí nhập sách (đơn giản hóa)


        Locale vietnameseLocale = new Locale("vi", "VN");
        totalSalaryLabel.setText(String.format(vietnameseLocale, "%,.0f VNĐ", salaryCosts));
        totalBookPurchaseCostLabel.setText(String.format(vietnameseLocale, "%,.0f VNĐ", bookPurchaseCosts));
    }


    


    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        if (stage != null) {
            // Thay vì đóng, bạn có thể muốn quay lại màn hình admin chính nếu đây là một phần của nó
            // Ví dụ: loadSceneContent("/view/admin/HomePageAdmin.fxml", "Admin Dashboard");
            // Hoặc nếu đây là cửa sổ riêng biệt thì đóng là hợp lý
            stage.close();
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
