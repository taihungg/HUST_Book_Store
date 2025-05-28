package controller.customer;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import java.net.URL;
import java.util.ResourceBundle;

public class SeeCartController implements Initializable {

    @FXML private TableView<BrowseProductsController.CartItem> cartTable;
    @FXML private TableColumn<BrowseProductsController.CartItem, BrowseProductsController.Product> itemColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Double> priceColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Integer> quantityColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Double> totalPriceColumn;
    @FXML private Label totalLabel;
    @FXML private Button continueShoppingButton;
    @FXML private Button removeCartButton;
    @FXML private Button clearCartButton;
    @FXML private Button checkoutButton;

    private CustomerMainController mainController;
    private ObservableList<BrowseProductsController.CartItem> cartItems;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupEventHandlers();
    }

    @Override
    public void setMainController(CustomerMainController mainController) {
        this.mainController = mainController;
        this.cartItems = mainController.getCartItems();
        cartTable.setItems(cartItems);
        updateTotalLabel();
    }

    public void setCartData(ObservableList<BrowseProductsController.CartItem> items) {
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
                protected void updateItem(BrowseProductsController.Product item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getTitle());
                }
            });
        } else {
            System.err.println("itemColumn is null, check FXML configuration.");
        }

        if (priceColumn != null) {
            priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getProduct().getPrice()).asObject());
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
                BrowseProductsController.CartItem item = e.getRowValue();
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
        continueShoppingButton.setOnAction(e -> handleContinueShopping());
        removeCartButton.setOnAction(e -> handleRemoveCart());
        clearCartButton.setOnAction(e -> handleClearCart());
        checkoutButton.setOnAction(e -> handleCheckout());
    }

    public void handleContinueShopping() {
        if (mainController != null) {
            mainController.loadPageWithData("/view/customer/Store/BrowseProducts.fxml", null);
        }
    }

    public void handleRemoveCart() {
        BrowseProductsController.CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
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
            .mapToDouble(BrowseProductsController.CartItem::getTotalPrice)
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