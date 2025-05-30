package controller.customer;

import java.io.IOException;
import java.util.List;


import controller.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.order.Order;
import model.user.cart.CartItem;
import model.user.customer.Customer;
import javafx.scene.control.Toggle;

public class placeOrderController {

    @FXML
    private TextField addressField;

    @FXML
    private Button backToCartButton;

    @FXML
    private RadioButton cashOption;

    @FXML
    private RadioButton creditCardOption;

    @FXML
    private RadioButton debitCardOption;

    @FXML
    private TextField nameField;

    @FXML
    private ProgressIndicator orderProgressIndicator;

    @FXML
    private Label orderTotalLabel;

    @FXML
    private ToggleGroup paymentGroup;

    @FXML
    private TextField phoneField;

    @FXML
    private Button placeOrderButton1;

    @FXML
    private HBox processingIndicator;

    @FXML
    private Label processingLabel;

    @FXML
    private CheckBox savePaymentInfoCheckbox;

    @FXML
    private Label shippingLabel;

    @FXML
    private Label summarySubtotalLabel;

    private AppServiceManager appServiceManager = Main.appServiceManager;
    private Customer currentUser = (Customer)Main.currentUser;
    private List<CartItem> cartItems = currentUser.getCart().getItems();
    


    @FXML
    public void initialize() {
        int shippingFee = 2;
        float subTotalPrice = (float) cartItems.stream()
        .mapToDouble(item -> appServiceManager.getProductManager().getProductById(item.getProductId()).getSellingPrice() * item.getQuantity())
        .sum();
        summarySubtotalLabel.setText(String.format("%.2f", subTotalPrice ) + " $");
        float totalPrice = subTotalPrice + shippingFee;
        orderTotalLabel.setText(String.format("%.2f", totalPrice ) + " $");
    }

    @FXML
    public void handlePlaceOrder(){
        String currentAddress = addressField.getText();
        String currentName = nameField.getText();
        String currentPhone = phoneField.getText();
        String currentPaymentMethod = "";
       


       
        Toggle selectedToggle = paymentGroup.getSelectedToggle();
        
        if (selectedToggle != null){
            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
        currentPaymentMethod = selectedRadioButton.getText();
        }
        else{
            showAlert("Lỗi", "Vui lòng chọn phương thức thanh toán");
            return;
        }


        
        if(currentAddress.isEmpty() || currentName.isEmpty() || currentPhone.isEmpty() || currentPaymentMethod.isEmpty()){
            showAlert("Lỗi", "Vui lòng nhập đầy đủ thông tin");
            return;
        }
        appServiceManager.getOrderManager().placeOrder(currentUser.getCart(), currentUser, currentAddress, currentPaymentMethod, currentPhone, currentUser);

        System.out.println(appServiceManager.getOrderManager().getAllOrders(currentUser));
        showAlert("Thành công", "Đơn hàng đã được đặt thành công");
        currentUser.getCart().clear();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    public void handleBackToCart(ActionEvent event){
       FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/Store/SeeCart.fxml"));
       try {
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) backToCartButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        Stage currentStage = (Stage) backToCartButton.getScene().getWindow();
        currentStage.close();
       } catch (IOException e) {
        e.printStackTrace();    
    }

  
}
}