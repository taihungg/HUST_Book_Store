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

    private final SessionManager sessionManager = SessionManager.getInstance();
    private final AppServiceManager appServiceManager = new AppServiceManager();

    @FXML
    public void initialize() {
        sessionManager.login(); // Temporarily logged in as Customer
        updateButtonsVisibility(sessionManager.isLoggedIn());
        loadContentView("/view/customer/Store/BrowseProducts.fxml");
    }

    private void updateButtonsVisibility(boolean isLoggedIn) {
        if (loginButton == null || logoutButton == null || menuBar == null) {
            throw new IllegalStateException("loginButton, logoutButton, or menuBar is not initialized");
        }
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
        menuBar.setVisible(true); // Always visible, but actions will check login status
    }

    @FXML
    private void handleLogin() {
        loadContentView("/view/Login.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            // Xử lý đăng xuất thông qua UserManager
            if (appServiceManager != null) {
                appServiceManager.getUserManager().logout();
            }
            sessionManager.logout();
            updateButtonsVisibility(false);
            loadContentView("/view/customer/Store/BrowseProducts.fxml");
            System.out.println("User logged out successfully");
        } catch (Exception e) {
            System.err.println("Error during logout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlPath;
        
        // Check login status for certain menu items
        if (!sessionManager.isLoggedIn() && !menuItem.getId().equals("browseProductsMenuItem")) {
            showAlert("Login Required", "Please log in to access this feature.");
            return;
        }
        
        switch (menuItem.getId()) {
            case "personalInfoMenuItem":
                fxmlPath = "/view/customer/Account/SeePersonalInformation.fxml";
                break;
            case "orderHistoryMenuItem":
                fxmlPath = "/view/customer/Account/OrderHistory.fxml";
                break;
            case "browseProductsMenuItem":
                fxmlPath = "/view/customer/Store/BrowseProducts.fxml";
                break;
            case "cartMenuItem":
                fxmlPath = "/view/customer/Store/SeeCart.fxml";
                break;
            default:
                System.err.println("Unsupported MenuItem: " + menuItem.getId());
                return;
        }
        
        Object subController = loadContentView(fxmlPath);
        if (subController instanceof SubController) {
            ((SubController) subController).setMainController(this);
        }
    }

    private Object loadContentView(String path) {
        return loadPageWithData(path, null);
    }

    public Object loadPageWithData(String fxmlPath, Object data) {
        try {
            URL location = getClass().getResource(fxmlPath);
            if (location == null) {
                System.err.println("Load Error: " + fxmlPath);
                return null;
            }
            FXMLLoader loader = new FXMLLoader(location);
            Parent content = loader.load();
            Object controller = loader.getController();
            
            if (controller == null) {
                System.err.println("Controller not found for " + fxmlPath);
                return null;
            }
            
            if (controller instanceof SubController) {
                ((SubController) controller).setMainController(this);
            }
            
            // Pass AppServiceManager to BrowseProductsController
            if (fxmlPath.contains("/view/customer/Store/BrowseProducts.fxml")) {
                if (controller instanceof BrowseProductsController) {
                    ((BrowseProductsController) controller).setAppServiceManager(appServiceManager);
                }
            }
            
            if (contentArea == null) {
                System.err.println("contentArea not initialized!");
                return null;
            }
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);
            return controller;
        } catch (IOException e) {
            System.err.println("Error loading " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void onLoginSuccess() {
        if (appServiceManager != null) {
            try {
                // Xử lý đăng nhập thông qua UserManager
                appServiceManager.getUserManager().login();
                sessionManager.login();
                updateButtonsVisibility(true);
                loadContentView("/view/customer/Store/BrowseProducts.fxml");
            } catch (Exception e) {
                System.err.println("Error during login: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    public AppServiceManager getAppServiceManager() {
        return appServiceManager;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class SessionManager {
        private static final SessionManager instance = new SessionManager();
        private boolean isLoggedIn;

        private SessionManager() {}

        public static SessionManager getInstance() {
            return instance;
        }

        public void login() {
            isLoggedIn = true;
        }

        public void logout() {
            isLoggedIn = false;
        }

        public boolean isLoggedIn() {
            return isLoggedIn;
        }
    }
}