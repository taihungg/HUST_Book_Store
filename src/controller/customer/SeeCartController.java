package controller.customer;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.product.Product;
import model.product.Stationery;
import model.product.Toy;
import model.product.book.Book;
import model.user.User;
import model.user.cart.CartItem;
import model.user.customer.Customer;
import model.manager.AppServiceManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;

import controller.Main;

public class SeeCartController implements Initializable {

    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> itemColumn;
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
       // Corrected approach:
       if (itemColumn != null) {
        itemColumn.setCellValueFactory(cellDataFeatures -> {
            CartItem cartItem = cellDataFeatures.getValue();
            if (cartItem != null && appServiceManager != null && appServiceManager.getProductManager() != null) {
                Product product = appServiceManager.getProductManager().getProductById(cartItem.getProductId());
                if (product != null && product.getTitle() != null) {
                    // Return the title directly, wrapped in an ObservableValue
                    return new SimpleStringProperty(product.getTitle()); // MODIFIED
                } else if (product != null) {
                    return new SimpleStringProperty(""); // Product exists but no title
                }
            }
            return new SimpleStringProperty(null); // Or new SimpleStringProperty("") for an empty cell
        });

    if (priceColumn != null) {
        // This will now fetch the current selling price from the Product object
        priceColumn.setCellValueFactory(cellDataFeatures -> {
            CartItem cartItem = cellDataFeatures.getValue();
            if (cartItem != null && appServiceManager != null && appServiceManager.getProductManager() != null) {
                Product product = appServiceManager.getProductManager().getProductById(cartItem.getProductId());
                if (product != null) {
                    // Assuming Product class has a method like getSellingPrice() that returns a numeric type (e.g., double or Double)
                    return new javafx.beans.property.SimpleObjectProperty<>(product.getSellingPrice());
                }
            }
            return new javafx.beans.property.SimpleObjectProperty<>(null); // Return null if price can't be determined
        });

        if (quantityColumn != null) {
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        } 

        if (totalPriceColumn != null) {
            totalPriceColumn.setCellValueFactory(cellDataFeatures -> {
                CartItem cartItem = cellDataFeatures.getValue();
                if (cartItem != null && appServiceManager != null && appServiceManager.getProductManager() != null) {
                    Product product = appServiceManager.getProductManager().getProductById(cartItem.getProductId());
                    if (product != null) {
                        double itemTotalPrice = product.getSellingPrice() * cartItem.getQuantity();
                        return new SimpleObjectProperty<>(itemTotalPrice);
                    }
                }
                return new SimpleObjectProperty<>(null);
            });
        }
    }
    }
    }

    private void setupEventHandlers() {
        continueShoppingButton.setOnAction(e -> {
			try {
				handleContinueShopping(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
        removeCartButton.setOnAction(e -> handleRemoveCart());
        clearCartButton.setOnAction(e -> handleClearCart());
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
    @FXML
    public void handleCheckout(ActionEvent event) {
        try{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/customer/Store/PlaceOrder.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
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
    @FXML
    private void handleViewCart(ActionEvent event) throws IOException {
        CartItem selectedProduct = cartTable.getSelectionModel().getSelectedItem();
        Parent root = null;
    
        if(appServiceManager.getProductManager().getProductById(selectedProduct.getProductId()) instanceof Book){
            String fxmlPath = "/view/customer/Store/ViewDetails/ViewBookDetails.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
             root = loader.load();
            ViewBookDetailsController bookController = loader.getController();
            bookController.setAppServiceManager(appServiceManager); // Pass AppServiceManager
            bookController.updateUIforBook((Book) appServiceManager.getProductManager().getProductById(selectedProduct.getProductId())); // Pass the specific book
        }
        else if(appServiceManager.getProductManager().getProductById(selectedProduct.getProductId()) instanceof Toy){
            String fxmlPath = "/view/customer/Store/ViewDetails/ViewToyDetails.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
             root = loader.load();
            ViewToyDetailsController toyController = loader.getController();
            toyController.setAppServiceManager(appServiceManager); // Pass AppServiceManager
            toyController.updateUIforToy((Toy) appServiceManager.getProductManager().getProductById(selectedProduct.getProductId())); // Pass the specific book
        }
        else if(appServiceManager.getProductManager().getProductById(selectedProduct.getProductId()) instanceof Stationery){
            String fxmlPath = "/view/customer/Store/ViewDetails/ViewStationeryDetails.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
             root = loader.load();
            ViewStationeryDetailsController stationeryController = loader.getController();
            stationeryController.setAppServiceManager(appServiceManager); // Pass AppServiceManager
            stationeryController.updateUIforStationery((Stationery) appServiceManager.getProductManager().getProductById(selectedProduct.getProductId())); // Pass the specific book
        }
        Stage stage = new Stage();
        stage.setTitle(appServiceManager.getProductManager().getProductById(selectedProduct.getProductId()).getTitle() + " - Details"); // Use the actual product's title
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setScene(new Scene(root));
        stage.showAndWait(); 
}
}

