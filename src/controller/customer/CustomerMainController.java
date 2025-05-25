package controller.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

import java.net.URL;

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
        // Giả lập trạng thái đã đăng nhập ban đầu
        sessionManager.login(); // Đặt trạng thái đăng nhập
        updateButtonsVisibility(sessionManager.isLoggedIn());
        // Tải BrowseProducts.fxml làm giao diện mặc định
        loadContentView("/view/customer/Store/BrowseProducts.fxml");
    }

    /**
     * Cập nhật hiển thị của loginButton, logoutButton và menuBar dựa trên trạng thái đăng nhập
     */
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
        // Khi bấm Login, chuyển giao diện sang Login.fxml
        loadContentView("/view/Login.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            sessionManager.logout(); // Cập nhật trạng thái session
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
                fxmlPath = "/view/customer/Store/Cart.fxml";
                break;
            default:
                System.err.println("MenuItem không được hỗ trợ: " + menuItem.getId());
                return;
        }

        Object subController = loadContentView(fxmlPath);
        if (subController instanceof SubController) {
            ((SubController) subController).setMainController(this);
        }
    }

    /**
     * Tải nội dung FXML vào contentArea và trả về controller của FXML
     */
    private Object loadContentView(String fxmlPath) {
        try {
            URL location = getClass().getResource(fxmlPath);
            if (location == null) {
                System.err.println("KHÔNG TÌM THẤY FXML TẠI: " + fxmlPath);
                return null;
            }

            FXMLLoader loader = new FXMLLoader(location);
            Parent content = loader.load();
            Object controller = loader.getController();

            // Liên kết controller con với mainController
            if (controller instanceof SubController) {
                ((SubController) controller).setMainController(this);
            } else if (controller == null) {
                System.err.println("Controller không được chỉ định cho " + fxmlPath);
            }

            contentArea.getChildren().clear();
            contentArea.getChildren().add(content);

            return controller;
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình tải " + fxmlPath + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Được gọi từ LoginController khi đăng nhập thành công
     */
    public void onLoginSuccess() {
        sessionManager.login();
        updateButtonsVisibility(true);
        loadContentView("/view/customer/Store/BrowseProducts.fxml");
    }

    /**
     * Kiểm tra trạng thái đăng nhập
     */
    public boolean isUserLoggedIn() {
        return sessionManager.isLoggedIn();
    }

    // Quản lý trạng thái đăng nhập
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

interface SubController {
    void setMainController(CustomerMainController mainController);
}