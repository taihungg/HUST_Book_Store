package controller.admin;

import java.io.InputStream;

import controller.Main;
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

    private AppServiceManager appServiceManager = Main.appServiceManager;
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

        String galleryURL = ebook.getGalleryURL();
        System.out.println("Attempting to load image for Ebook from: " + galleryURL);

        if (galleryURL != null && !galleryURL.isEmpty()) {
            try {
                Image image = null;
                if (galleryURL.startsWith("http://") || galleryURL.startsWith("https://") || galleryURL.startsWith("file:/")) {
                    image = new Image(galleryURL, true); // true để tải nền, false để tải ngay lập tức
                } else {
                    InputStream imageStream = getClass().getResourceAsStream(galleryURL);
                    if (imageStream != null) {
                        image = new Image(imageStream);
                        imageStream.close();
                    } else {
                        System.err.println("Không tìm thấy tài nguyên hình ảnh (trong resources): " + galleryURL);
                    }
                }

                if (image != null && !image.isError()) {
                    bookImage.setImage(image);
                } else {
                    if (image != null && image.getException() != null) {
                        image.getException().printStackTrace();
                    }
                    System.err.println("Đối tượng Image báo lỗi hoặc null sau khi tạo từ: " + galleryURL);
                    bookImage.setImage(getPlaceholderImage());
                }
            } catch (IllegalArgumentException e) {
                System.err.println("URL hình ảnh không hợp lệ: " + galleryURL + " - " + e.getMessage());
                bookImage.setImage(getPlaceholderImage());
            } catch (Exception e) {
                System.err.println("Lỗi không xác định khi tải hình ảnh: " + galleryURL);
                e.printStackTrace();
                bookImage.setImage(getPlaceholderImage());
            }
        } else {
            System.err.println("Gallery URL của Ebook bị null hoặc rỗng.");
            bookImage.setImage(getPlaceholderImage());
        }

        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(ebook.getId())));
        sellingPriceLabel.setText(String.valueOf(ebook.getSellingPrice()));
        purchasePriceLabel.setText(String.valueOf(ebook.getPurchasePrice()));
    }

    @FXML
    void handleRead(ActionEvent event) {
        System.out.println("Read button clicked");
        
    }
    private Image getPlaceholderImage() {
        try {
            InputStream placeholderStream = getClass().getResourceAsStream("/images/placeholder.png");
            if (placeholderStream != null) {
                Image img = new Image(placeholderStream);
                placeholderStream.close();
                return img;
            } else {
                System.err.println("Không tìm thấy file /images/placeholder.png. Trả về ảnh trống.");
                return createBlankImage();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh placeholder: " + e.getMessage());
            return createBlankImage();
        }
    }
    private Image createBlankImage() {
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
    }

}
