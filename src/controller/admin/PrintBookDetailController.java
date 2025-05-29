package controller.admin;

import java.io.InputStream;

import controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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

   
    private    AppServiceManager appServiceManager = Main.appServiceManager;


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

        String galleryURL = printbook.getGalleryURL();
        System.out.println("Attempting to load image for Printbook from: " + galleryURL);

        if (galleryURL != null && !galleryURL.isEmpty()) {
            try {
                Image image = null;
                if (galleryURL.startsWith("http://") || galleryURL.startsWith("https://") || galleryURL.startsWith("file:/")) {
                    image = new Image(galleryURL, true); // true để tải nền
                } else {
                    InputStream imageStream = getClass().getResourceAsStream(galleryURL);
                    if (imageStream != null) {
                        image = new Image(imageStream);
                    } else {
                        System.err.println("Không tìm thấy tài nguyên hình ảnh (trong resources): " + galleryURL);
                    }
                }

                if (image != null && !image.isError()) {
                    bookImage.setImage(image);
                } else {
                    if (image != null && image.getException() != null) {
                         image.getException().printStackTrace(); // In ra lỗi cụ thể của Image nếu có
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
            System.err.println("Gallery URL của Printbook bị null hoặc rỗng.");
            bookImage.setImage(getPlaceholderImage());
        }
        
        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(printbook.getId())));
        
        

    }
    @FXML
    void handleBackButton(ActionEvent event) {
        Stage stage = (Stage)backButton.getScene().getWindow();
        stage.close();
    }
    private Image createBlankImage() {
        // Tạo một ảnh 1x1 pixel màu xám nếu không có placeholder
        return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
    }

    // Phương thức này cần được tạo nếu notifyButton có onAction="#handleNotifyAction" trong FXML
   
    @FXML
    void handleNotifyAction(ActionEvent event) {
        // Logic cho nút Notify
        System.out.println("Notify button pressed for: " + (titleLabel.getText().equals("N/A") ? "Unknown Book" : titleLabel.getText()));
        showAlert("Thông báo", "Chức năng thông báo đang được phát triển!");
    }
    private Image getPlaceholderImage() {
        try {
            InputStream placeholderStream = getClass().getResourceAsStream("/images/placeholder.png"); // Đường dẫn tới ảnh placeholder
            if (placeholderStream != null) {
                return new Image(placeholderStream);
            } else {
                System.err.println("Không tìm thấy file /images/placeholder.png. Trả về ảnh trống.");
                return createBlankImage();
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh placeholder: " + e.getMessage());
            return createBlankImage();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // Hoặc Alert.AlertType.ERROR tùy ngữ cảnh
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

