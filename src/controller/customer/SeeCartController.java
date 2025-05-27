package controller.customer;

import javafx.beans.property.SimpleIntegerProperty;
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
    @FXML private TableColumn<BrowseProductsController.CartItem, BrowseProductsController.Product> itemColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Integer> priceColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Integer> quantityColumn;
    @FXML private TableColumn<BrowseProductsController.CartItem, Integer> totalPriceColumn;
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
        if (cartTable != null) {
            cartTable.setItems(cartItems);
        } else {
            System.err.println("cartTable is null, check FXML configuration.");
        }
        updateTotalLabel();
    }

    public void setCartData(ObservableList<BrowseProductsController.CartItem> items) {
        if (items != null) {
            this.cartItems = items;
            if (cartTable != null) {
                cartTable.setItems(cartItems);
            } else {
                System.err.println("cartTable is null, check FXML configuration.");
            }
        } else {
            if (cartItems != null) {
                cartItems.clear();
            } else {
                System.err.println("cartItems is null, cannot clear.");
            }
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
        }

        if (priceColumn != null) {
            priceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProduct().getPrice()).asObject());
            priceColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Integer price, boolean empty) {
                    super.updateItem(price, empty);
                    setText(empty || price == null ? null : String.format("%,d VNĐ", price));
                }
            });
        }

        if (quantityColumn != null) {
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            quantityColumn.setOnEditCommit(event -> {
                BrowseProductsController.CartItem item = event.getRowValue();
                int oldQuantity = item.getQuantity();
                int newQuantity = event.getNewValue();
                if (newQuantity > 0) {
                    item.setQuantity(newQuantity);
                } else {
                    item.setQuantity(oldQuantity);
                    showAlert("Lỗi", "Số lượng phải lớn hơn 0!");
                }
                updateTotalLabel();
                if (cartTable != null) cartTable.refresh();
            });
        }

        if (totalPriceColumn != null) {
            totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
            totalPriceColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(Integer total, boolean empty) {
                    super.updateItem(total, empty);
                    setText(empty || total == null ? null : String.format("%,d VNĐ", total));
                }
            });
        }
    }

    private void setupEventHandlers() {
        if (continueShoppingButton != null) {
            continueShoppingButton.setOnAction(e -> handleContinueShopping());
        } else {
            System.err.println("continueShoppingButton is null, check FXML configuration.");
        }
        if (removeCartButton != null) {
            removeCartButton.setOnAction(e -> handleRemoveCart());
        } else {
            System.err.println("removeCartButton is null, check FXML configuration.");
        }
        if (clearCartButton != null) {
            clearCartButton.setOnAction(e -> handleClearCart());
        } else {
            System.err.println("clearCartButton is null, check FXML configuration.");
        }
        if (checkoutButton != null) {
            checkoutButton.setOnAction(e -> handleCheckout());
        } else {
            System.err.println("checkoutButton is null, check FXML configuration.");
        }
    }

    public void handleContinueShopping() {
        if (mainController != null) {
            mainController.loadPageWithData("/view/customer/Store/BrowseProducts.fxml", null);
        } else {
            System.err.println("mainController is null, cannot continue shopping.");
        }
    }

    public void handleRemoveCart() {
        if (cartTable != null) {
            BrowseProductsController.CartItem selectedItem = cartTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                cartItems.remove(selectedItem);
                updateTotalLabel();
                showAlert("Thông báo", "Đã xóa mặt hàng khỏi giỏ hàng!");
            } else {
                showAlert("Lỗi", "Vui lòng chọn một mặt hàng để xóa!");
            }
        } else {
            System.err.println("cartTable is null, check FXML configuration.");
        }
    }

    public void handleClearCart() {
        if (cartItems != null) {
            cartItems.clear();
            updateTotalLabel();
            showAlert("Thông báo", "Đã xóa toàn bộ giỏ hàng!");
        } else {
            System.err.println("cartItems is null, cannot clear cart.");
        }
    }

    public void handleCheckout() {
        if (cartItems != null && !cartItems.isEmpty()) {
            showAlert("Thành công", "Thanh toán thành công!");
            cartItems.clear();
            updateTotalLabel();
        } else {
            showAlert("Lỗi", "Giỏ hàng trống, không thể thanh toán!");
        }
    }

    private void updateTotalLabel() {
        long total = 0;
        if (cartItems != null) {
            total = cartItems.stream()
                .mapToLong(BrowseProductsController.CartItem::getTotalPrice)
                .sum();
        }
        if (totalLabel != null) {
            totalLabel.setText(String.format("%,d VNĐ", total));
        } else {
            System.err.println("totalLabel is null, check FXML configuration.");
        }
        if (cartTable != null) {
            cartTable.refresh();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}