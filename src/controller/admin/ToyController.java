package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.product.Toy;
public class ToyController {
    @FXML
    private TextField titleField;
    
    @FXML
    private TextArea descriptionArea;
    
    @FXML
    private TextField galleryUrlField;
    
    @FXML
    private TextField priceField;
    
    @FXML
    private ComboBox<String> statusComboBox;
    
    @FXML
    private TextField materialField;
    
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

    private ProductDataService productDataService;
   

    @FXML
    public void initialize() {
        // Khởi tạo các thành phần nếu cần
        statusComboBox.getItems().addAll("Available", "Out of Stock", "Discontinued");
        productDataService = ProductDataService.getInstance(); 
    }

    @FXML
    private void handleAddToy() {
       
     String title = titleField.getText();
    
    
    String description = descriptionArea.getText();
    
   String galleryUrl = galleryUrlField.getText();
    
    
    Double sellingPrice =Double.parseDouble(priceField.getText()) ;
     
    String status = statusComboBox.getValue();
    
    String material = materialField.getText();
    
    int suitableAge = Integer.parseInt(ageRecommendationField.getText());
    
    String brand =  brandField.getText();
    
   String type =  typeField.getText();

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

        if (title.isEmpty() || description.isEmpty() || galleryUrl.isEmpty() || brand.isEmpty() || type.isEmpty()){
            System.err.println("Please fill in all fields");
            return;
        }
    
   

   Toy newToy = new Toy( id,  title,  description,  galleryUrl,  sellingPrice,
   0, 0, 10,  status,
    brand,  suitableAge);

   productDataService.addProduct(newToy);
        
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
} 