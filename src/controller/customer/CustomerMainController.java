package controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import javafx.collections.ObservableList;

public class CustomerMainController {

    @FXML
    private Button loginButton;

    @FXML
    private Button logoutButton;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem personalInfoMenuItem;

    @FXML
    private MenuItem orderHistoryMenuItem;

    @FXML
    private MenuItem browseProductsMenuItem;

    @FXML
    private MenuItem cartMenuItem;

    @FXML
    private StackPane contentArea;

    private final SessionManager sessionManager = SessionManager.getInstance();

    @FXML
    public void initialize() {
        sessionManager.login();
        updateButtonsVisibility(sessionManager.isLoggedIn());
        loadContentView("/view/customer/Store/BrowseProducts.fxml");
    }

    private void updateButtonsVisibility(boolean isLoggedIn) {
        if (loginButton == null || logoutButton == null || menuBar == null) {
            throw new IllegalStateException("loginButton, logoutButton, or menuBar is not initialized");
        }
        loginButton.setVisible(!isLoggedIn);
        logoutButton.setVisible(isLoggedIn);
        menuBar.setVisible(isLoggedIn);
    }

    @FXML
    private void handleLogin() {
        loadContentView("/view/Login.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            sessionManager.logout();
            updateButtonsVisibility(false);
            loadContentView("/view/customer/Store/BrowseProducts.fxml");
            System.out.println("User logged out successfully");
        } catch (Exception e) {
            System.err.println("Lỗi khi đăng xuất: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuItemAction(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fxmlPath;

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
                System.err.println("MenuItem không được hỗ trợ: " + menuItem.getId());
                return;
        }

        if ("cartMenuItem".equals(menuItem.getId())) {
            // TODO: Truyền dữ liệu giỏ hàng thực sự từ nguồn chung (ví dụ: CartManager)
            System.out.println("Chưa có dữ liệu giỏ hàng để truyền từ CustomerMainController.");
            loadPageWithData(fxmlPath, null); // Giữ nguyên logic hiện tại
        } else {
            Object subController = loadContentView(fxmlPath);
            if (subController instanceof SubController) {
                ((SubController) subController).setMainController(this);
            }
        }
    }

    private Object loadContentView(String fxmlPath) {
        return loadPageWithData(fxmlPath, null);
    }

    public Object loadPageWithData(String fxmlPath, Object data) {
        try {
            URL location = getClass().getResource(fxmlPath);
            if (location == null) {
                System.err.println("KHÔNG TÌM THẤY FXML TẠI: " + fxmlPath);
                return null;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent content = loader.load();
            Object controller = loader.getController();

            if (controller == null) {
                System.err.println("Controller không được chỉ định cho " + fxmlPath);
                return null;
            }

            if (controller instanceof SubController) {
                ((SubController) controller).setMainController(this);
            }

            if (fxmlPath.contains("SeeCart.fxml")) {
                if (controller instanceof SeeCartController && data instanceof ObservableList) {
                    ((SeeCartController) controller).setCartData((ObservableList<BrowseProductsController.CartItem>) data);
                } else {
                    System.err.println("Dữ liệu không hợp lệ cho SeeCartController: " + data);
                }
            } else if (fxmlPath.contains("ViewBookDetails.fxml")) {
                if (controller instanceof ViewBookDetailsController && data instanceof BrowseProductsController.Product) {
                    ((ViewBookDetailsController) controller).setProductData((BrowseProductsController.Product) data);
                } else {
                    System.err.println("Dữ liệu không hợp lệ cho ViewBookDetailsController: " + data);
                }
            }

            if (contentArea == null) {
                System.err.println("contentArea chưa được khởi tạo!");
                return null;
            }
            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);

            return controller;
        } catch (IOException e) {
            System.err.println("Lỗi trong quá trình tải " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void onLoginSuccess() {
        sessionManager.login();
        updateButtonsVisibility(true);
        loadContentView("/view/customer/Store/BrowseProducts.fxml");
    }

    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
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