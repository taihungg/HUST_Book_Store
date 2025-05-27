package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.product.Toy;
public class ToyDetailController {

    @FXML
    private Label ageLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Button backButton;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label brandLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button notifyButton;

    @FXML
    private Label purchasePriceLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label sellingPriceLabel;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private Label titleLabel;

    @FXML
    private Label statusLabel;

    @FXML
    void intialize(){
        statusComboBox.getItems().addAll("In Stock", "Out of Stock");
    }
    @FXML
    void handleBack(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    private ImageView toyImage;

    
    public void setToy(Toy toy){
        if (toy == null){
            System.out.println("Toy is null");
            return;
        }
        titleLabel.setText(toy.getTitle());
        sellingPriceLabel.setText(String.valueOf(toy.getSellingPrice()));
        statusLabel.setText(toy.getStatus());
        purchasePriceLabel.setText(String.valueOf(toy.getPurchasePrice()));
        //toyTypeLabel.setText(toy.getT
        ageLabel.setText(String.valueOf(toy.getSuitableAge()));
        //toyMaterialLabel.setText(toy.
        descriptionArea.setText(toy.getDescription());
        toyImage.setImage(new Image(toy.getGalleryURL()));

    }

}
