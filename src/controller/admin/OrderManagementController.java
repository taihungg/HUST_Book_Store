package controller.admin;

import java.time.LocalDate;
import java.time.LocalDateTime;

import controller.Main;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import model.order.Order;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateTimeStringConverter;
import javafx.scene.Parent;
import javafx.util.converter.LocalDateStringConverter;
public class OrderManagementController {

    
    @FXML
    private TableColumn<Order, String> addressCol;

    @FXML
    private TableColumn<Order, Double> amoutCol;

    @FXML
    private TableColumn<Order, LocalDate> dateCol;

    @FXML
    private Button exitButton;

    @FXML
    private TableView<Order> orderTable;

    @FXML
    private TableColumn<Order, String> orderidCol;

    @FXML
    private TableColumn<Order, String> payCol;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<Order, String> statusCol;

    @FXML
    private TableColumn<Order, String> usernameCol;

    
    @FXML
    void initialize(){
        orderTable.setEditable(true);
        System.out.println(Main.appServiceManager.getOrderManager().getAllOrders(Main.currentUser));


        orderidCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderId"));
        addressCol.setCellValueFactory(new PropertyValueFactory<Order, String>("shippingAddress"));
        amoutCol.setCellValueFactory(new PropertyValueFactory<Order, Double>("totalAmount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<Order, LocalDate>("orderDate"));
        
        
        // CellFactory này vẫn dùng LocalDateTimeStringConverter vì cột của bạn là LocalDateTime
        usernameCol.setCellValueFactory(new PropertyValueFactory<Order, String>("customerUsername"));
        orderidCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        amoutCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        dateCol.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        payCol.setCellFactory(TextFieldTableCell.forTableColumn());
        statusCol.setCellFactory(TextFieldTableCell.forTableColumn());
        statusCol.setOnEditCommit(event -> {
            Order order = event.getRowValue();
            order.setOrderStatus(event.getNewValue());
            orderTable.refresh();
        });
        usernameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        
        
        orderTable.setItems(Main.appServiceManager.getOrderManager().getAllOrders(Main.currentUser));
        System.out.println(Main.appServiceManager.getOrderManager().getAllOrders(Main.currentUser));

    }
    @FXML
    private void handleViewDetail(ActionEvent event) {
        Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/SeeOrders/OrderDetail_fixed.fxml"));
                Parent root = loader.load();
                OrderDetailsController controller = loader.getController();
                controller.setOrder(selectedOrder);
                Stage stage = new Stage();
                stage.setTitle("Order Details");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    

    @FXML
    private void handleSearch(ActionEvent event) {
       String searchText = searchField.getText();
       if(searchText.isEmpty()){
        orderTable.setItems(Main.appServiceManager.getOrderManager().getAllOrders(Main.currentUser));
       }
       else{
        Order order = Main.appServiceManager.getOrderManager().getOrderById(searchText);
        orderTable.setItems(FXCollections.observableArrayList(order));
       }
    }

    

    @FXML
    private void handleExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/HomePageAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Home Page");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) exitButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


