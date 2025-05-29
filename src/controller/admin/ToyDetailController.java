package controller.admin;

import java.io.InputStream;

import controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.manager.AppServiceManager;
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

    private AppServiceManager appServiceManager = Main.appServiceManager;

    
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
        quantityLabel.setText(String.valueOf(appServiceManager.getProductManager().getProductQuantity(toy.getId())));
        String galleryURL = toy.getGalleryURL();
        System.out.println("Attempting to load image for Toy from: " + galleryURL);

        if (toyImage == null) {
            System.err.println("toyImage ImageView is null. Check FXML fx:id.");
            return; // Không thể hiển thị ảnh nếu ImageView là null
        }

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
                    toyImage.setImage(image);
                } else {
                    if (image != null && image.getException() != null) {
                        image.getException().printStackTrace();
                    }
                    System.err.println("Đối tượng Image báo lỗi hoặc null sau khi tạo từ: " + galleryURL);
                    toyImage.setImage(getPlaceholderImage());
                }
            } catch (IllegalArgumentException e) {
                System.err.println("URL hình ảnh không hợp lệ: " + galleryURL + " - " + e.getMessage());
                toyImage.setImage(getPlaceholderImage());
            } catch (Exception e) {
                System.err.println("Lỗi không xác định khi tải hình ảnh: " + galleryURL);
                e.printStackTrace();
                toyImage.setImage(getPlaceholderImage());
            }
        } else {
            System.err.println("Gallery URL của Toy bị null hoặc rỗng.");
            toyImage.setImage(getPlaceholderImage());
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
