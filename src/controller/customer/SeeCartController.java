package controller.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import java.net.URL;
import java.util.ResourceBundle;

public class SeeCartController implements Initializable, SubController {

    @FXML private TableView<BrowseProductsController.CartItem> cartTable;
    @FXML private TableColumn<BrowseProductsController.CartItem, String> itemColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Double> priceColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Integer> quantityColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Double> totalPriceColumn;
    @FXML private Label totalLabel;
    @FXML private Button continueShoppingButton;
    @FXML private Button updateCartButton;
    @FXML private Button removeCartButton;
    @FXML private Button clearCartButton;
    @FXML private Button checkoutButton;

    private CustomerMainController mainController;
    private ObservableList<BrowseProductsController.CartItem> cartItems = FXCollections.observableArrayList();
    private boolean isCartUpdated = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupEventHandlers();
        cartTable.setItems(cartItems);
    }

    @Override
    public void setMainController(CustomerMainController mainController) {
        this.mainController = mainController;
    }

    public void setCartData(ObservableList<BrowseProductsController.CartItem> items) {
        cartItems.setAll(items);
        updateTotalLabel();
        isCartUpdated = true;
    }

    private void setupTableColumns() {
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        itemColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : getItem() != null ? getItem().toString() : "<no name>");
            }
        });

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("product"));
        priceColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                } else {
                    BrowseProductsController.CartItem cartItem = getTableRow().getItem();
                    setText(String.format("%.2f", cartItem.getProduct().getPrice()));
                }
            }
        });

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            BrowseProductsController.CartItem item = event.getRowValue();
            int newQuantity = event.getNewValue();
            if (newQuantity > 0) {
                item.setQuantity(newQuantity);
                cartTable.refresh();
                isCartUpdated = false;
            } else {
                showAlert("Lỗi", "Số lượng phải lớn hơn 0!");
                cartTable.refresh();
            }
        });

        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }

    private void setupEventHandlers() {
        continueShoppingButton.setOnAction(e -> handleContinueShopping());
        updateCartButton.setOnAction(e -> handleUpdateCart());
        removeCartButton.setOnAction(e -> handleRemoveCart());
        clearCartButton.setOnAction(e -> handleClearCart());
        checkoutButton.setOnAction(e -> handleCheckout());
    }

    private void handleContinueShopping() {
        if (mainController != null) {
            mainController.loadPageWithData("/view/customer/Store/BrowseProducts.fxml", null);
        }
    }

    private void handleUpdateCart() {
        updateTotalLabel();
        isCartUpdated = true;
        showAlert("Thông báo", "Giỏ hàng đã được cập nhật!");
    }

    private void handleRemoveCart() {
        BrowseProductsController.CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            cartItems.remove(selectedItem);
            updateTotalLabel();
            isCartUpdated = false;
            showAlert("Thông báo", "Đã xóa mặt hàng khỏi giỏ hàng!");
        } else {
            showAlert("Lỗi", "Vui lòng chọn một mặt hàng để xóa!");
        }
    }

    private void handleClearCart() {
        cartItems.clear();
        updateTotalLabel();
        isCartUpdated = false;
        showAlert("Thông báo", "Đã xóa toàn bộ giỏ hàng!");
    }

    private void handleCheckout() {
        if (cartItems.isEmpty()) {
            showAlert("Lỗi", "Giỏ hàng trống, không thể thanh toán!");
        } else if (!isCartUpdated) {
            showAlert("Thông báo", "Vui lòng cập nhật giỏ hàng trước khi thanh toán!");
        } else {
            showAlert("Thành công", "Thanh toán thành công!");
        }
    }

    private void updateTotalLabel() {
        double total = 0;
        for (BrowseProductsController.CartItem item : cartItems) {
            item.setQuantity(item.getQuantity()); // Updates totalPrice internally
            total += item.getTotalPrice();
        }
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