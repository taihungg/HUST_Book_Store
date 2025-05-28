package controller.admin;

import controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.product.Stationery;

public class StationeryController {
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private TextField galleryUrlField;
    
    @FXML
    private TextField sellingpriceField;

    @FXML
    private TextField purchasepriceField;
    
    @FXML
    private ComboBox<String> statusComboBox;
    
    @FXML
    private TextField brandField;
    
    @FXML
    private TextField typeField;

    @FXML
    private Button cancelButton;

    @FXML 
    private TextField manufacturerField;
    
    @FXML
    private TextField quantityField;
    


    @FXML
    public void initialize() {
        
        statusComboBox.getItems().addAll("In Stock", "Out of Stock");
       

    }
    private AppServiceManager appServiceManager = Main.appServiceManager;
    

    @FXML
    private void handleAddStationery() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String galleryUrl = galleryUrlField.getText();
        String type = typeField.getText();
        String status = statusComboBox.getValue();
        String brand = brandField.getText();
        String id = "STATIONERY_" + System.currentTimeMillis();
        Double sellingPrice = Double.parseDouble(sellingpriceField.getText());
        Double purchasePrice = Double.parseDouble(purchasepriceField.getText());
        int quantity = Integer.parseInt(quantityField.getText());
        if (title.isEmpty() || description.isEmpty() || galleryUrl.isEmpty()  || type.isEmpty() ) {
            System.err.println("Please fill in all fields");
            return;
        }

        try{

            if (sellingPrice<=0){
                System.err.println("Price must be greater than 0");
                return;
              }
        
        }
        catch (NumberFormatException e){
            System.err.println("Price must be a number");
            return;
        }

            Stationery newStationery = new Stationery(
                id,        // id
                title,              // title
                description,        // description
                galleryUrl,         // galleryURL
                sellingPrice,       // sellingPrice
                purchasePrice, // purchasePrice
                0, // averageRating
                0, // numberOfReviews
                status,             // status
                brand,
                type              // type
            );

            
            appServiceManager.getProductManager().addProduct(newStationery,quantity,appServiceManager.getCurrentUser());

            Alert alert = new Alert (Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Thêm đồ chơi thành công");
            alert.setContentText("Sản phẩm đã được thêm vào danh sách sản phẩm");
            alert.showAndWait();
        


            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/UpdateStore/ProductTypeSelectionView.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Cập nhật kho");
                stage.setScene(new Scene(root));
                stage.show();
                Stage currentStage = (Stage) cancelButton.getScene().getWindow();
                currentStage.close();
                }
                catch(Exception e){
                 System.err.println("Lỗi khi đóng cửa sổ");
                 e.printStackTrace();
                }
            

      
        
    }

    @FXML
    private void handleCancel() {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/UpdateStore/ProductTypeSelectionView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cập nhật kho");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
            
        }
        catch(Exception e){
            System.err.println("Lỗi khi đóng cửa sổ");
            e.printStackTrace();
        }


    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
} 