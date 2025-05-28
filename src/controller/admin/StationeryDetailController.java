package controller.admin;

import controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
import model.product.Stationery;

public class StationeryDetailController {

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
    private Label statusLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label typeLabel;

    @FXML
    void handleBackButton(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    private AppServiceManager appServiceManager = Main.appServiceManager;

    void setStationery(Stationery newStationery){
        if (newStationery == null){
            System.out.println("Stationery is null");
            return;
        }
        titleLabel.setText(newStationery.getTitle());
        sellingPriceLabel.setText(String.valueOf(newStationery.getSellingPrice()));
        statusLabel.setText(newStationery.getStatus());
        purchasePriceLabel.setText(String.valueOf(newStationery.getPurchasePrice()));
        descriptionArea.setText(newStationery.getDescription());
        bookImage.setImage(new Image(newStationery.getGalleryURL()));
        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(newStationery.getId())));
        typeLabel.setText(newStationery.getType());
        brandLabel.setText(newStationery.getBrand());
    }

}
