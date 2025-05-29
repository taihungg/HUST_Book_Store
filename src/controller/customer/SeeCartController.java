package controller.customer;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.product.Product;
import model.user.User;
import model.user.cart.CartItem;
import model.user.customer.Customer;
import model.manager.AppServiceManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Main;

public class SeeCartController implements Initializable {

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, Product> itemColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> totalPriceColumn;
    @FXML private Label totalLabel;
    @FXML private Button continueShoppingButton;
    @FXML private Button removeCartButton;
    @FXML private Button clearCartButton;
    @FXML private Button checkoutButton;

    private AppServiceManager appServiceManager = Main.appServiceManager;
    private Customer currentUser = (Customer)Main.currentUser;
    private ObservableList<CartItem> cartItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupEventHandlers();
        setCartData(currentUser.getCart().getItems());
    }

    

    public void setCartData(ObservableList<CartItem> items) {
        if (items != null) {
            this.cartItems = items;
            cartTable.setItems(cartItems);
        } else {
            this.cartItems.clear();
        }
        updateTotalLabel();
    }

    private void setupTableColumns() {
        if (itemColumn != null) {
            itemColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
            itemColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Product item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getTitle());
                }
            });
        } else {
            System.err.println("itemColumn is null, check FXML configuration.");
        }

        if (priceColumn != null) {
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            priceColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty || price == null ? null : String.format("%.2f USD", price));
                }
            });
         
        } else {
            System.err.println("priceColumn is null, check FXML configuration.");
        }

        if (quantityColumn != null) {
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            quantityColumn.setOnEditCommit(e -> {
                CartItem item = e.getRowValue();
                int oldQuantity = item.getQuantity();
                int newQuantity = e.getNewValue();
                if (newQuantity > 0) {
                    item.setQuantity(newQuantity);
                } else {
                    item.setQuantity(oldQuantity);
                    showAlert("Lỗi", "Số lượng phải lớn hơn 0!");
                }
                updateTotalLabel();
                if (cartTable != null) cartTable.refresh();
            });
        } else {
            System.err.println("quantityColumn is null, check FXML configuration.");
        }

        if (totalPriceColumn != null) {
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            totalPriceColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Double total, boolean empty) {
                    super.updateItem(total, empty);
                    setText(empty || total == null ? null : String.format("%.2f USD", total));
                }
            });
        } else {
            System.err.println("totalPriceColumn is null, check FXML configuration.");
        }
    }

    private void setupEventHandlers() {
        continueShoppingButton.setOnAction(e -> {
			try {
				handleContinueShopping(e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        removeCartButton.setOnAction(e -> handleRemoveCart());
        clearCartButton.setOnAction(e -> handleClearCart());
        checkoutButton.setOnAction(e -> handleCheckout());
    }

    public void handleContinueShopping(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/HomePage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
    

    public void handleRemoveCart() {
        CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            updateTotalLabel();
            showAlert("Thông báo", "Đã xóa mặt hàng khỏi giỏ hàng!");
        } else {
            showAlert("Lỗi", "Vui lòng chọn một mặt hàng để xóa!");
        }
    }

    public void handleClearCart() {
        cartItems.clear();
        updateTotalLabel();
        showAlert("Thông báo", "Đã xóa toàn bộ giỏ hàng!");
    }

    public void handleCheckout() {
        if (cartItems.isEmpty()) {
            showAlert("Lỗi", "Giỏ hàng trống, không thể thanh toán!");
        } else {
            showAlert("Thành công", "Thanh toán thành công!");
            cartItems.clear();
            updateTotalLabel();
        }
    }

    private void updateTotalLabel() {
        double total = cartItems.stream()
            .mapToDouble(item -> appServiceManager.getProductManager().getProductById(item.getProductId()).getSellingPrice() * item.getQuantity())
            .sum();
        totalLabel.setText(String.format("%.2f USD", total));
        cartTable.refresh();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}