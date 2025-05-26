package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.product.Toy;
public class ToyDetailController {

    @FXML
    private Button backButton;

    @FXML
    private Label imageCounterLabel;

    @FXML
    private Button nextImageButton;

    @FXML
    private Button prevImageButton;

    @FXML
    private Label toyAgeRecommendationLabel;

    @FXML
    private TextArea toyDescriptionArea;

    @FXML
    private ImageView toyImageView;

    @FXML
    private Label toyManufacturerLabel;

    @FXML
    private Label toyMaterialLabel;

    @FXML
    private Label toyNameHeaderLabel;

    @FXML
    private Label toyPriceLabel;

    @FXML
    private Label toyQuantityLabel;

    @FXML
    private Label toyStatusLabel;

    @FXML
    private Label toyTypeLabel;

    @FXML
    void handleBackButtonAction(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void setToy(Toy toy){
        if (toy == null){
            System.out.println("Toy is null");
            return;
        }
        toyNameHeaderLabel.setText(toy.getTitle());
        toyPriceLabel.setText(String.valueOf(toy.getSellingPrice()));
        toyStatusLabel.setText(toy.getStatus());
        //toyTypeLabel.setText(toy.getT
        toyAgeRecommendationLabel.setText(String.valueOf(toy.getSuitableAge()));
        toyManufacturerLabel.setText(toy.getBrand());
        //toyMaterialLabel.setText(toy.
        toyDescriptionArea.setText(toy.getDescription());
    }

}
