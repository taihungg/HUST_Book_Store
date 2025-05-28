package controller.admin;

import controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.product.book.Audiobook;
import model.manager.AppServiceManager;
import javafx.scene.image.Image;
public class AudioBookDetailController {

    @FXML
    private Label ISBNLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Button backButton;

    @FXML
    private ImageView bookImage;

    @FXML
    private Label categoryLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label downloadURLLabel;

    @FXML
    private Label languageLabel;

    @FXML
    private Button notifyButton;

   

    @FXML
    private Button playButton;

    @FXML
    private Label publisherLabel;

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
    private Label weightLabel1;

    private AppServiceManager appServiceManager = Main.appServiceManager;

    @FXML
    void handleBackButton(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void handlePlay(ActionEvent event) {
        System.out.println("Play button clicked");

    }

    public void setAudiobook(Audiobook audiobook){
        if(audiobook == null){
            System.out.println("Audiobook is null");
            return;
        }
        titleLabel.setText(audiobook.getTitle());
        authorLabel.setText(audiobook.getAuthor());
        categoryLabel.setText(audiobook.getCategory());
        publisherLabel.setText(audiobook.getPublisher());
        languageLabel.setText(audiobook.getLanguage());
        statusLabel.setText(audiobook.getStatus());
        ISBNLabel.setText(audiobook.getIsbn());
        downloadURLLabel.setText(audiobook.getDownloadURL());
        descriptionArea.setText(audiobook.getDescription());
        bookImage.setImage(new Image(audiobook.getGalleryURL()));
        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(audiobook.getId())));
        sellingPriceLabel.setText(String.valueOf(audiobook.getSellingPrice()));
        purchasePriceLabel.setText(String.valueOf(audiobook.getPurchasePrice()));
        
    }

}
