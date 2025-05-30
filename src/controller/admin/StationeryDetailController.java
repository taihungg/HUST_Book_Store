package controller.admin;

import java.io.InputStream;

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
        sellingPriceLabel.setText(String.valueOf(newStationery.getSellingPrice()) + " $");
        statusLabel.setText(newStationery.getStatus());
        purchasePriceLabel.setText(String.valueOf(newStationery.getPurchasePrice()) + " $");
        descriptionArea.setText(newStationery.getDescription());
        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(newStationery.getId())));
        typeLabel.setText(newStationery.getType());
        brandLabel.setText(newStationery.getBrand());
        String galleryURL = newStationery.getGalleryURL();
        System.out.println("Attempting to load image for Stationery from: " + galleryURL);

        if (galleryURL != null && !galleryURL.isEmpty()) {
            try {
                Image image = null;
                if (galleryURL.startsWith("http://") || galleryURL.startsWith("https://") || galleryURL.startsWith("file:/")) {
                    image = new Image(galleryURL, true);
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
                    bookImage.setImage(image); // bookImage nên đổi tên thành itemImage hoặc stationeryImage trong FXML và controller
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
            System.err.println("Gallery URL của Stationery bị null hoặc rỗng.");
            bookImage.setImage(getPlaceholderImage());
        }
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
