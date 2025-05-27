package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class ForgotPasswordController {

    @FXML
    private Button btnChangePassword;

    @FXML
    void btnChangePasswordclicked(ActionEvent event) {
    	try {
            // Chuyển về trang đăng nhập
            changePage(event, "view/Login.fxml"); // Thay đổi đường dẫn theo cấu trúc project của bạn
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi chuyển trang: " + e.getMessage());
        }
    }
    
    // Phương thức chung để chuyển trang
    private void changePage(ActionEvent event, String fxmlPath) throws IOException {
        Parent newPage = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene newScene = new Scene(newPage);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(newScene);
        window.show();
    }
}


