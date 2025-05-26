package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class RevenueReportController {
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<String> predefinedDateRangeComboBox;
    @FXML private Button generateReportButton;
    @FXML private Label totalRevenueLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label averageRevenuePerOrderLabel;
    @FXML private LineChart<String, Number> revenueOverTimeChart;
    @FXML private TableView<?> categoryRevenueTable;
    @FXML private TableView<?> topProductsTable;
    @FXML private Button ExitButton;

    @FXML
    public void initialize() {
        // Initialize date pickers with current date
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

        // Initialize predefined date ranges
        ObservableList<String> dateRanges = FXCollections.observableArrayList(
            "Hôm nay",
            "Tuần này",
            "Tháng này",
            "Quý này",
            "Năm nay"
        );
        predefinedDateRangeComboBox.setItems(dateRanges);

        // Set up event handlers
        generateReportButton.setOnAction(event -> generateReport());
        predefinedDateRangeComboBox.setOnAction(event -> handlePredefinedDateRange());
        ExitButton.setOnAction(event -> handleExit());
    }

    private void generateReport() {
        // TODO: Implement report generation logic
        // This is where you would fetch data from your backend
        // and update the UI components
    }

    private void handlePredefinedDateRange() {
        String selected = predefinedDateRangeComboBox.getValue();
        LocalDate today = LocalDate.now();
        
        switch (selected) {
            case "Hôm nay":
                startDatePicker.setValue(today);
                endDatePicker.setValue(today);
                break;
            case "Tuần này":
                startDatePicker.setValue(today.minusDays(today.getDayOfWeek().getValue() - 1));
                endDatePicker.setValue(today);
                break;
            case "Tháng này":
                startDatePicker.setValue(today.withDayOfMonth(1));
                endDatePicker.setValue(today);
                break;
            case "Quý này":
                int quarter = (today.getMonthValue() - 1) / 3;
                startDatePicker.setValue(today.withMonth(quarter * 3 + 1).withDayOfMonth(1));
                endDatePicker.setValue(today);
                break;
            case "Năm nay":
                startDatePicker.setValue(today.withDayOfYear(1));
                endDatePicker.setValue(today);
                break;
        }
        
        generateReport();
    }

    @FXML
    private void handleExit() {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/HomePageAdmin.fxml"));
        Parent root = loader.load();
        Stage currentstage = new Stage();
        currentstage.setTitle("Trang chủ");
        currentstage.setScene(new Scene(root));
        currentstage.show();

        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
        }
        catch(IOException e){
            System.err.println("Lỗi khi mở trang chủ");
            e.printStackTrace();

            }

        
    }

    @FXML
    private void handleSeeOrders() {
        
    }
} 