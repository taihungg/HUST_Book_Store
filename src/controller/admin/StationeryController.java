package controller.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.product.Stationery;

public class StationeryController {
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
    private TextField brandField;
    
    @FXML
    private TextField typeField;

    @FXML
    private TextField colorField;
    
    @FXML
    private Button addStationeryButton;
    
    @FXML
    private Button cancelButton;

    @FXML 
    private TextField manufacturerField;

    @FXML
    public void initialize() {
        
        statusComboBox.getItems().addAll("Available", "Out of Stock", "Discontinued");
       

    }

    @FXML
    private void handleAddStationery() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String galleryUrl = galleryUrlField.getText();
        String material = materialField.getText();
        String type = typeField.getText();
        String color = colorField.getText();
        String manufacturer = manufacturerField.getText();
        String status = statusComboBox.getValue();
        Double price = Double.parseDouble(priceField.getText());
        if (title.isEmpty() || description.isEmpty() || galleryUrl.isEmpty() || material.isEmpty() || type.isEmpty() || color.isEmpty() || manufacturer.isEmpty() || price <= 0) {
            System.err.println("Please fill in all fields");
            return;
        }

        try{

            if (price<=0){
                System.err.println("Price must be greater than 0");
                return;
              }
        
        }
        catch (NumberFormatException e){
            System.err.println("Price must be a number");
            return;
        }
            
            
            

        closeWindow();
      
        
    }

    @FXML
    private void handleCancel() {

    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
} 