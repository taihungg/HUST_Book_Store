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
import model.product.book.Book;
import model.product.book.Printbook;
import model.manager.AppServiceManager;
public class PrintBookDetailController {

    
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
    private Label languageLabel;

    @FXML
    private Button notifyButton;

    @FXML
    private Label numberOfPageLabel;

    @FXML
    private Label publicationYearLabel;

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

   
    private    AppServiceManager appServiceManager = AppServiceManager.getInstance();


    @FXML
    void handleBackButtonAction(ActionEvent event) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
    }


    
    @FXML
    public void initialize(){

        
    }


    public void setPrintBook(Printbook printbook){
        if(printbook ==null){
            System.out.println("Book is null");
            return;
        }
        titleLabel.setText(printbook.getTitle());
        authorLabel.setText(printbook.getAuthor());
        categoryLabel.setText(printbook.getCategory());
        publisherLabel.setText(printbook.getPublisher());
        languageLabel.setText(printbook.getLanguage());
        statusLabel.setText(printbook.getStatus());
        ISBNLabel.setText(printbook.getIsbn());
        numberOfPageLabel.setText(String.valueOf(printbook.getNumberOfPages()));
        weightLabel.setText(String.valueOf(printbook.getWeight()));
        sellingPriceLabel.setText(String.valueOf(printbook.getSellingPrice()));
        purchasePriceLabel.setText(String.valueOf(printbook.getPurchasePrice()));
        descriptionArea.setText(printbook.getDescription());
        bookImage.setImage(new Image(printbook.getGalleryURL()));
        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(printbook.getId())));
        
        

    }
    @FXML
    void handleBackButton(ActionEvent event) {
        Stage stage = (Stage)backButton.getScene().getWindow();
        stage.close();
    }
}

