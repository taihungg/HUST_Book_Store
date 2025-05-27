package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.product.book.Ebook;
import model.manager.AppServiceManager;
import javafx.scene.image.Image;
public class EBookDetailController {

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
    private Label numberOfPageLabel;

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
    private Label weightLabel;

    @FXML
    private Button readButton;

    private AppServiceManager appServiceManager = AppServiceManager.getInstance();
    @FXML
    void handleBackButton(ActionEvent event) {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();

    }

    public void setEbook(Ebook ebook){
        if(ebook == null){
            System.out.println("Ebook is null");
            return;
        }
        titleLabel.setText(ebook.getTitle());
        authorLabel.setText(ebook.getAuthor());
        categoryLabel.setText(ebook.getCategory());
        publisherLabel.setText(ebook.getPublisher());
        languageLabel.setText(ebook.getLanguage());
        statusLabel.setText(ebook.getStatus());
        ISBNLabel.setText(ebook.getIsbn());
        numberOfPageLabel.setText(String.valueOf(ebook.getNumberOfPages()));
        downloadURLLabel.setText(ebook.getDownloadURL());
        descriptionArea.setText(ebook.getDescription());
        bookImage.setImage(new Image(ebook.getGalleryURL()));
        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(ebook.getId())));
        sellingPriceLabel.setText(String.valueOf(ebook.getSellingPrice()));
        purchasePriceLabel.setText(String.valueOf(ebook.getPurchasePrice()));
    }

    @FXML
    void handleRead(ActionEvent event) {
        System.out.println("Read button clicked");
        
    }

}
