package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
public class BookDetailController {

    @FXML
    private Label authorNameLabel;

    @FXML
    private Button backButton;

    @FXML
    private ImageView bookCoverImageView;

    @FXML
    private Label bookTitleHeaderLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label imageCounterLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label languageLabel;

    @FXML
    private Button nextImageButton;

    @FXML
    private Label pagesLabel;

    @FXML
    private Button prevImageButton;

    @FXML
    private Label priceLabel;

    @FXML
    private Label publicationYearLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label quantityLabel;

    @FXML
    private Label statusLabel;

    @FXML
    void handleBackButtonAction(ActionEvent event) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
    }


    
    @FXML
    public void initialize(){
        
    }
}

