package controller.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ProductTypeSelectionController {
    @FXML
    private Button addToyButton;
    
    @FXML
    private Button addStationeryButton;
    
    @FXML
    private Button addBookButton;
    
    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        // Khởi tạo các thành phần nếu cần
    }

    @FXML
    private void handleBackButton(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/ManagePageView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cập nhật kho");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) backButton.getScene().getWindow();
            currentStage.close();
        }
        catch(Exception e){
            System.err.println("Lỗi khi đóng cửa sổ");
            e.printStackTrace();
    }
    }

    @FXML
    private void handleAddToy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/UpdateStore/AddToy.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Thêm Đồ Chơi Mới");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) addBookButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddStationery() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/UpdateStore/AddStationery.fxml"));
            Parent root = loader.load();
           
            Stage stage = new Stage();
            stage.setTitle("Thêm Văn Phòng Phẩm Mới");
            stage.setScene(new Scene(root));
            stage.show();
            Stage currentStage = (Stage) addBookButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/UpdateStore/AddBook.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Thêm Sách Mới");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) addBookButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}       