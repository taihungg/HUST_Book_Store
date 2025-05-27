package controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import model.manager.AppServiceManager;
import model.user.User;
import model.user.customer.Customer;
import java.io.IOException;
import java.net.URL;

public class HomePageController {

    @FXML private Button loginButton;
    @FXML private Button logoutButton;
    @FXML private MenuBar menuBar;
    @FXML private MenuItem personalInfoMenuItem;
    @FXML private MenuItem orderHistoryMenuItem;
    @FXML private MenuItem browseProductsMenuItem;
    @FXML private MenuItem cartMenuItem;
    @FXML private StackPane contentArea;

    private AppServiceManager appServiceManager;
    private User currentUser;

    @FXML
    public void initialize() {
        appServiceManager = new AppServiceManager();
        // Khởi tạo với tài khoản khách
        currentUser = new Customer("Guest", "guest@example.com", "N/A", "guest", "guest", "N/A");
        updateButtonsVisibility(false);
        loadContentView("/view/customer/Store/BrowseProducts.fxml");
    }

    private void updateButtonsVisibility(boolean isLoggedIn) {
        if (loginButton == null || logoutButton == null || menuBar == null) {
            throw new IllegalStateException("loginButton, logoutButton, or menuBar is not initialized");
        }
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
        menuBar.setVisible(true); // Luôn hiển thị, nhưng các hành động sẽ kiểm tra trạng thái đăng nhập
    }

    @FXML
    private void handleLogin() {
        loadContentView("/view/Login.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            appServiceManager.logout();
            currentUser = new Customer("Guest", "guest@example.com", "N/A", "guest", "guest", "N/A");
            loadContentView("/view/customer/Store/BrowseProducts.fxml");
        } catch (Exception e) {
            showError("Logout failed", e.getMessage());
        }
    }

    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlPath;
        
        // Kiểm tra trạng thái đăng nhập cho các menu item cụ thể
        if (currentUser.getUsername().equals("guest") && !menuItem.getId().equals("browseProductsMenuItem")) {
            showAlert("Login Required", "Please log in to access this feature.");
            return;
        }
        
        // Xử lý các menu item
        switch (menuItem.getId()) {
            case "browseProductsMenuItem":
                fxmlPath = "/view/customer/Store/BrowseProducts.fxml";
                break;
            case "cartMenuItem":
                fxmlPath = "/view/customer/Cart/CartView.fxml";
                break;
            case "ordersMenuItem":
                fxmlPath = "/view/customer/Order/OrderHistory.fxml";
                break;
            case "profileMenuItem":
                fxmlPath = "/view/customer/Profile/ProfileView.fxml";
                break;
            default:
                return;
        }
        
        loadContentView(fxmlPath);
    }

    private void loadContentView(String fxmlPath) {
        try {
            URL url = getClass().getResource(fxmlPath);
            if (url == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }
            
            FXMLLoader loader = new FXMLLoader(url);
            Parent content = loader.load();
            
            // Set AppServiceManager for controllers that need it
            if (loader.getController() instanceof BrowseProductsController) {
                ((BrowseProductsController) loader.getController()).setAppServiceManager(appServiceManager);
            }
            
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
        } catch (IOException e) {
            showError("Error", "Failed to load view: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public User getCurrentUser() {
        return currentUser;
    }
}