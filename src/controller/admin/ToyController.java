package controller.admin;

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
import model.product.Toy;
public class ToyController {
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private TextField galleryUrlField;
    
    @FXML
    private TextField sellingpriceField;
    
    @FXML
    private ComboBox<String> statusComboBox;
    
    @FXML
    private TextField quantityField;
    
    @FXML
    private TextField ageRecommendationField;
    
    @FXML
    private TextField brandField;
    
    @FXML
    private TextField typeField;
    
    @FXML
    private Button addToyButton;
    
    @FXML
    private Button cancelButton;

    @FXML
    private TextField purchasepriceField;

    private AppServiceManager   appServiceManager ;
   

    @FXML
    public void initialize() {
        // Khởi tạo các thành phần nếu cần
        statusComboBox.getItems().addAll("In Stock", "Out of Stock");
        appServiceManager = AppServiceManager.getInstance();
    }

    @FXML
    private void handleAddToy() {
       
     String title = titleField.getText();
    
    
    String description = descriptionArea.getText();
    
   String galleryUrl = galleryUrlField.getText();
    
    
    Double sellingPrice =Double.parseDouble(sellingpriceField.getText()) ;
    Double purchasePrice = Double.parseDouble(purchasepriceField.getText());
     
    String status = statusComboBox.getValue();
    
    int quantity = Integer.parseInt(quantityField.getText());
    
    int suitableAge = Integer.parseInt(ageRecommendationField.getText());
    
    String brand =  brandField.getText();
    

   String id = "TOY_" + System.currentTimeMillis();

    try{
        if (    sellingPrice < 0 ){
            System.err.println("Price must be greater than 0");
            return;
        }
    }catch (NumberFormatException e){
        System.err.println("Price must be a number");
        return;

        }

        if (title.isEmpty() || description.isEmpty() || galleryUrl.isEmpty() || brand.isEmpty() ){
            System.err.println("Please fill in all fields");
            return;
        }
    
   

   Toy newToy = new Toy( id,  title,  description,  galleryUrl,  sellingPrice,
   purchasePrice, 0, 10,  status,
    brand,  suitableAge);

   appServiceManager.getProductManager().addProduct(newToy, quantity,appServiceManager.getCurrentUser());

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
        }    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
} 