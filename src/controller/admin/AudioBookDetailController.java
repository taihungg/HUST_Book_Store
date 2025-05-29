package controller.admin;

import controller.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.product.book.Audiobook; // Đảm bảo import đúng lớp Audiobook
import model.manager.AppServiceManager;
import javafx.scene.image.Image;
import java.io.InputStream; // Thêm import này

public class AudioBookDetailController {

    @FXML private Label ISBNLabel;
    @FXML private Label authorLabel;
    @FXML private Button backButton;
    @FXML private ImageView bookImage; // ImageView để hiển thị ảnh
    @FXML private Label categoryLabel;
    @FXML private TextArea descriptionArea;
    @FXML private Label downloadURLLabel;
    @FXML private Label languageLabel;
    // @FXML private Button notifyButton; // Biến này không được sử dụng, có thể comment hoặc xóa nếu không cần
    @FXML private Button playButton;
    @FXML private Label publisherLabel;
    @FXML private Label purchasePriceLabel;
    @FXML private Label quantityLabel;
    @FXML private Label sellingPriceLabel;
    @FXML private Label statusLabel;
    @FXML private Label titleLabel;
    // @FXML private Label weightLabel1; // Biến này không được sử dụng, có thể comment hoặc xóa nếu không cần

    // Khởi tạo AppServiceManager trong initialize hoặc truyền vào qua constructor/setter nếu cần
    // private AppServiceManager appServiceManager = Main.appServiceManager; // Có thể gây NullPointerException nếu Main.appServiceManager chưa sẵn sàng
    private AppServiceManager appServiceManager;


    // Phương thức initialize (nếu controller này được dùng với FXML và cần khởi tạo)
    @FXML
    public void initialize() {
        // Lấy AppServiceManager một cách an toàn hơn
        if (Main.appServiceManager != null) {
            this.appServiceManager = Main.appServiceManager;
        } else {
            System.err.println("AudioBookDetailController: AppServiceManager is null from Main!");
            // Xử lý trường hợp appServiceManager là null, ví dụ: vô hiệu hóa các thành phần
        }
    }


    @FXML
    void handleBackButton(ActionEvent event) {
        if (backButton != null && backButton.getScene() != null) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
        }
    }

    @FXML
    void handlePlay(ActionEvent event) {
        // TODO: Thêm logic xử lý khi nhấn nút Play
        // Ví dụ: Mở URL download hoặc phát file audio
        if (downloadURLLabel != null && !downloadURLLabel.getText().isEmpty()) {
            System.out.println("Play button clicked. Download URL: " + downloadURLLabel.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Play Audio");
            alert.setHeaderText(null);
            alert.setContentText("Play button clicked. Download URL: " + downloadURLLabel.getText());
            alert.showAndWait();
            // Mở URL bằng trình duyệt mặc định (ví dụ)
            // try {
            //     Desktop.getDesktop().browse(new URI(downloadURLLabel.getText()));
            // } catch (IOException | URISyntaxException e) {
            //     e.printStackTrace();
            //     // Hiển thị lỗi cho người dùng
            // }
        } else {
            System.out.println("Play button clicked, but no download URL available.");
        }
    }

    public void setAudiobook(Audiobook audiobook) {
        if (audiobook == null) {
            System.err.println("Audiobook is null. Cannot set details.");
            // Có thể xóa thông tin trên UI hoặc hiển thị thông báo "Không có dữ liệu"
            clearDetails();
            return;
        }
        if (appServiceManager == null || appServiceManager.getProductManager() == null) {
            System.err.println("AppServiceManager or ProductManager is not initialized. Cannot fetch quantity.");
             // Có thể xóa thông tin trên UI hoặc hiển thị thông báo "Lỗi hệ thống"
            clearDetails();
            titleLabel.setText(audiobook.getTitle() != null ? audiobook.getTitle() : "N/A"); // Vẫn hiển thị title nếu có
            return;
        }


        // Sử dụng check null trước khi setText để tránh NullPointerException nếu audiobook có trường null
        titleLabel.setText(audiobook.getTitle() != null ? audiobook.getTitle() : "N/A");
        authorLabel.setText(audiobook.getAuthor() != null ? "Tác giả: " + audiobook.getAuthor() : "Tác giả: N/A");
        categoryLabel.setText(audiobook.getCategory() != null ? "Thể loại: " + audiobook.getCategory() : "Thể loại: N/A");
        publisherLabel.setText(audiobook.getPublisher() != null ? "Nhà xuất bản: " + audiobook.getPublisher() : "Nhà xuất bản: N/A");
        languageLabel.setText(audiobook.getLanguage() != null ? "Ngôn ngữ: " + audiobook.getLanguage() : "Ngôn ngữ: N/A");
        statusLabel.setText(audiobook.getStatus() != null ? "Trạng thái: " + audiobook.getStatus() : "Trạng thái: N/A");
        ISBNLabel.setText(audiobook.getIsbn() != null ? "ISBN: " + audiobook.getIsbn() : "ISBN: N/A");
        downloadURLLabel.setText(audiobook.getDownloadURL() != null ? audiobook.getDownloadURL() : "N/A");
        descriptionArea.setText(audiobook.getDescription() != null ? audiobook.getDescription() : "Không có mô tả.");

        // Xử lý tải hình ảnh an toàn hơn
        String galleryURL = audiobook.getGalleryURL();
        System.out.println("Attempting to load image from: " + galleryURL); // In URL ra để debug

        if (galleryURL != null && !galleryURL.isEmpty()) {
            try {
                // Giả sử galleryURL là đường dẫn tương đối trong resources (ví dụ: /images/covers/file.png)
                // Nếu galleryURL là một URL đầy đủ từ internet (http://...), thì không cần getClass().getResourceAsStream()
                // mà dùng thẳng new Image(galleryURL) nhưng vẫn cần try-catch.

                Image image = null;
                // Kiểm tra xem URL có phải là URL tuyệt đối (internet) hay không
                if (galleryURL.startsWith("http://") || galleryURL.startsWith("https://") || galleryURL.startsWith("file:/")) {
                    image = new Image(galleryURL, true); // true để tải nền và xử lý lỗi tốt hơn
                } else {
                    // Nếu là đường dẫn tương đối trong resources
                    InputStream imageStream = getClass().getResourceAsStream(galleryURL);
                    if (imageStream != null) {
                        image = new Image(imageStream);
                    } else {
                        System.err.println("Không tìm thấy tài nguyên hình ảnh tại (trong resources): " + galleryURL);
                    }
                }

                if (image != null && !image.isError()) {
                    bookImage.setImage(image);
                } else {
                    if (image != null && image.getException() != null) {
                         image.getException().printStackTrace();
                    }
                    System.err.println("Đối tượng Image báo lỗi hoặc null sau khi tạo từ: " + galleryURL);
                    bookImage.setImage(getPlaceholderImage()); // Đặt ảnh placeholder nếu có lỗi
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
            System.err.println("Gallery URL bị null hoặc rỗng.");
            bookImage.setImage(getPlaceholderImage()); // Đặt ảnh placeholder nếu URL rỗng
        }

        // Lấy số lượng và giá
        // Đảm bảo appServiceManager và productManager đã được khởi tạo
        if (appServiceManager != null && appServiceManager.getProductManager() != null && audiobook.getId() != null) {
            quantityLabel.setText("Số lượng: " + String.valueOf(appServiceManager.getProductManager().getProductQuantity(audiobook.getId())));
        } else {
            quantityLabel.setText("Số lượng: N/A");
        }
        sellingPriceLabel.setText("Giá bán: " + String.valueOf(audiobook.getSellingPrice()) + " VNĐ"); // Thêm đơn vị tiền tệ
        purchasePriceLabel.setText("Giá nhập: " + String.valueOf(audiobook.getPurchasePrice()) + " VNĐ");
    }

    private void clearDetails() {
        titleLabel.setText("N/A");
        authorLabel.setText("Tác giả: N/A");
        categoryLabel.setText("Thể loại: N/A");
        publisherLabel.setText("Nhà xuất bản: N/A");
        languageLabel.setText("Ngôn ngữ: N/A");
        statusLabel.setText("Trạng thái: N/A");
        ISBNLabel.setText("ISBN: N/A");
        downloadURLLabel.setText("N/A");
        descriptionArea.setText("Không có mô tả.");
        bookImage.setImage(getPlaceholderImage());
        quantityLabel.setText("Số lượng: N/A");
        sellingPriceLabel.setText("Giá bán: N/A");
        purchasePriceLabel.setText("Giá nhập: N/A");
    }

    // Phương thức để lấy ảnh placeholder
    private Image getPlaceholderImage() {
        try {
            // Đảm bảo bạn có file placeholder.png trong thư mục /images/ của resources
            InputStream placeholderStream = getClass().getResourceAsStream("/images/placeholder.png");
            if (placeholderStream != null) {
                return new Image(placeholderStream);
            } else {
                System.err.println("Không tìm thấy file /images/placeholder.png. Trả về ảnh trống.");
                // Tạo một ảnh 1x1 pixel màu xám nếu không có placeholder
                return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh placeholder: " + e.getMessage());
            return new Image("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII="); // Ảnh trống dự phòng cuối cùng
        }
    }
}