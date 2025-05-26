package controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

// Import các lớp model của bạn
import model.product.Product;
import model.product.book.Book;
import model.product.book.Ebook;
import model.product.book.Audiobook;
// Import service quản lý dữ liệu


public class BookController {

    //<editor-fold defaultstate="collapsed" desc="FXML Fields">
    @FXML private Button addProductButton;
    @FXML private TextField audioFormatField;
    @FXML private TextField audiobookDownloadUrlField;
    @FXML private CheckBox audiobookHasDrmCheckBox;
    @FXML private GridPane audiobookSpecificFieldsPane;
    @FXML private TextField authorsField; // Cho nhiều tác giả, cách nhau bằng dấu phẩy
    @FXML private ComboBox<String> bookTypeComboBox;
    @FXML private Button cancelButton;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextArea demoAudioArea;
    @FXML private TextArea descriptionArea;
    @FXML private TextField deviceCompatibilityField;
    @FXML private TextField ebookDownloadUrlField;
    @FXML private TextField ebookFormatField;
    @FXML private CheckBox ebookHasDrmCheckBox;
    @FXML private GridPane ebookSpecificFieldsPane;
    @FXML private TextField fileSizeField;
    @FXML private TextField galleryUrlField;
    @FXML private TextField idField;
    @FXML private TextField isbnField;
    @FXML private ComboBox<String> languageComboBox;
    @FXML private CheckBox musicScoreCheckBox;
    @FXML private TextField narratorField;
    @FXML private TextField numberOfPagesField;
    @FXML private TextField priceField; // sellingPrice
    @FXML private TextField publicationYearField;
    @FXML private TextField publisherField;
    @FXML private TextField readOnlineUrlField;
    @FXML private CheckBox soundEffectsCheckBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField streamingUrlField;
    @FXML private TextField titleField;
    @FXML private TextField totalDurationField;
    //</editor-fold>

    private ProductDataService productDataService;

    @FXML
    public void initialize() {
        // Populate ComboBoxes
        languageComboBox.getItems().addAll("Tiếng Việt", "Tiếng Anh", "Tiếng Nhật", "Tiếng Pháp", "Song ngữ", "Khác");
        statusComboBox.getItems().addAll("Còn hàng", "Hết hàng", "Sắp phát hành", "Ngừng kinh doanh");
        bookTypeComboBox.getItems().addAll("Sách thường (Print Book)", "Sách điện tử (Ebook)", "Sách nói (Audiobook)");
        categoryComboBox.getItems().addAll("Văn học", "Kinh tế", "Tâm lý - Kỹ năng sống", "Thiếu nhi", "Tiểu sử - Hồi ký",
                                           "Giáo trình", "Khoa học - Kỹ thuật", "Ngoại ngữ", "Lịch sử", "Truyện tranh", "Khác");

        // Listener để hiển thị/ẩn các trường cụ thể cho Ebook/Audiobook
        bookTypeComboBox.valueProperty().addListener((obs, oldValue, newValue) -> {
            updateSpecificFieldsVisibility(newValue);
        });

        // Trạng thái ban đầu: ẩn cả hai panel
        ebookSpecificFieldsPane.setVisible(false);
        ebookSpecificFieldsPane.setManaged(false);
        audiobookSpecificFieldsPane.setVisible(false);
        audiobookSpecificFieldsPane.setManaged(false);

        productDataService = ProductDataService.getInstance(); // Lấy instance của service
    }

    private void updateSpecificFieldsVisibility(String bookType) {
        if ("Sách điện tử (Ebook)".equals(bookType)) {
            ebookSpecificFieldsPane.setVisible(true);
            ebookSpecificFieldsPane.setManaged(true);
            audiobookSpecificFieldsPane.setVisible(false);
            audiobookSpecificFieldsPane.setManaged(false);
        } else if ("Sách nói (Audiobook)".equals(bookType)) {
            ebookSpecificFieldsPane.setVisible(false);
            ebookSpecificFieldsPane.setManaged(false);
            audiobookSpecificFieldsPane.setVisible(true);
            audiobookSpecificFieldsPane.setManaged(true);
        } else { // Sách thường hoặc không chọn
            ebookSpecificFieldsPane.setVisible(false);
            ebookSpecificFieldsPane.setManaged(false);
            audiobookSpecificFieldsPane.setVisible(false);
            audiobookSpecificFieldsPane.setManaged(false);
        }
    }

    @FXML
    void handleAddBook(ActionEvent event) {
        // 1. Lấy các giá trị chung từ form
        String id = idField.getText().trim();
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String galleryURL = galleryUrlField.getText().trim(); // Sửa tên biến cho nhất quán
        String priceText = priceField.getText().trim();
        String status = statusComboBox.getValue();
        String isbn = isbnField.getText().trim();
        String authors = authorsField.getText().trim();
        String publisher = publisherField.getText().trim();
        String category = categoryComboBox.getValue();
        String language = languageComboBox.getValue();
        String publicationYearText = publicationYearField.getText().trim();
        String selectedBookType = bookTypeComboBox.getValue();

        // 2. Validate dữ liệu chung cơ bản
        if (id.isEmpty() || title.isEmpty() || priceText.isEmpty() || status == null ||
            isbn.isEmpty() || authors.isEmpty() || publisher.isEmpty() || category == null ||
            language == null || publicationYearText.isEmpty() || selectedBookType == null) {
            showAlert(AlertType.ERROR, "Thiếu thông tin", "Vui lòng điền đầy đủ các trường thông tin chung bắt buộc của sách.");
            return;
        }

        double sellingPrice;
        int publicationYear;
        // Các giá trị mặc định cho các tham số của Product mà Book cần truyền cho super()
        // Bạn có thể thêm trường nhập liệu cho chúng nếu muốn người dùng tự nhập
        double purchasePriceDefault = 0.0;
        double averageRatingDefault = 0.0;
        int numberOfReviewsDefault = 0;
        // int quantityDefault = 0; // Nếu lớp Book của bạn có quantity

        try {
            sellingPrice = Double.parseDouble(priceText);
            if (sellingPrice <= 0) {
                showAlert(AlertType.ERROR, "Giá không hợp lệ", "Giá bán phải lớn hơn 0.");
                return;
            }
            publicationYear = Integer.parseInt(publicationYearText);
            // Thêm kiểm tra năm xuất bản hợp lệ nếu cần (ví dụ: không lớn hơn năm hiện tại)
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Định dạng không hợp lệ", "Giá hoặc Năm xuất bản phải là số hợp lệ.");
            return;
        }

        Product productToAdd = null; // Sử dụng kiểu Product cha

        // 3. Tạo đối tượng dựa trên loại sách được chọn
        if ("Sách điện tử (Ebook)".equals(selectedBookType)) {
            String numberOfPagesText = numberOfPagesField.getText().trim();
            String ebookDownloadURL = ebookDownloadUrlField.getText().trim();
            // Lấy các trường khác của Ebook
            String fileSizeText = fileSizeField.getText().trim();
            String ebookFormat = ebookFormatField.getText().trim();
            String readOnlineURL = readOnlineUrlField.getText().trim();
            String deviceCompatibility = deviceCompatibilityField.getText().trim();
            boolean ebookHasDRM = ebookHasDrmCheckBox.isSelected();


            if (numberOfPagesText.isEmpty() || ebookDownloadURL.isEmpty() /*|| các trường Ebook bắt buộc khác */) {
                 showAlert(AlertType.ERROR, "Thiếu thông tin Ebook", "Vui lòng điền số trang và URL tải về cho Ebook.");
                 return;
            }
            int numberOfPages;
            double fileSize = 0; // Giá trị mặc định nếu không bắt buộc
            try {
                numberOfPages = Integer.parseInt(numberOfPagesText);
                if (!fileSizeText.isEmpty()) {
                    fileSize = Double.parseDouble(fileSizeText);
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Định dạng Ebook không hợp lệ", "Số trang hoặc kích thước file của Ebook phải là số.");
                return;
            }

            // Giả sử constructor Ebook của bạn là:
            // Ebook(id, title, description, galleryURL, sellingPrice, purchasePrice, averageRating, numberOfReviews, status,
            //       isbn, authors, publisher, category, language, publicationYear,
            //       numberOfPages, downloadURL, fileSize, ebookFormat, readOnlineURL, deviceCompatibility, hasDRM)
            productToAdd = new Ebook(
                id,
                title,
                description,
                galleryURL,
                sellingPrice,
                purchasePriceDefault,    // purchasePrice
                averageRatingDefault,    // averageRating
                numberOfReviewsDefault,  // numberOfReviews
                status,
                isbn,
                authors,            // author (số ít theo constructor Ebook)
                publisher,
                category,
                language,
                // publicationYear,      // Constructor Ebook này không trực tiếp nhận publicationYear.
                                         // Nếu lớp Book (cha của Ebook) cần publicationYear,
                                         // thì constructor Book mà Ebook gọi super() đến phải xử lý việc này,
                                         // hoặc Ebook phải nhận publicationYear để truyền cho super().
                                         // Hiện tại, constructor Ebook bạn đưa không có, nên Book cũng không được truyền.
            
                numberOfPages,           // Tham số thứ 15 cho Ebook
                ebookDownloadURL    // Tham số thứ 16 cho Ebook
            );
            

        } else if ("Sách nói (Audiobook)".equals(selectedBookType)) {
            String audioFormat = audioFormatField.getText().trim();
            String audiobookDownloadURL = audiobookDownloadUrlField.getText().trim();
            String narrator = narratorField.getText().trim();
            String totalDurationText = totalDurationField.getText().trim();
            // Lấy các trường khác của Audiobook
            String streamingURL = streamingUrlField.getText().trim();
            // String demoAudioData = demoAudioArea.getText(); // Cần parse thành ArrayList<Audio>
            boolean audiobookHasDRM = audiobookHasDrmCheckBox.isSelected();
            boolean soundEffects = soundEffectsCheckBox.isSelected();
            boolean musicScore = musicScoreCheckBox.isSelected();

            if (audioFormat.isEmpty() || narrator.isEmpty() || totalDurationText.isEmpty() /*|| các trường Audiobook bắt buộc khác */) {
                 showAlert(AlertType.ERROR, "Thiếu thông tin Audiobook", "Vui lòng điền định dạng, người đọc và thời lượng cho Audiobook.");
                 return;
            }
            int totalDuration;
            try {
                totalDuration = Integer.parseInt(totalDurationText);
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Định dạng Audiobook không hợp lệ", "Thời lượng của Audiobook phải là số (phút).");
                return;
            }

            // Giả sử constructor Audiobook của bạn là:
            // Audiobook(id, title, description, galleryURL, sellingPrice, purchasePrice, averageRating, numberOfReviews, status,
            //           isbn, authors, publisher, category, language, publicationYear,
            //           audioFormat, downloadURL, streamingURL, narrator, totalDuration, hasDRM, soundEffects, musicScore /*, demoAudioList */)
            productToAdd = new Audiobook(id, title, description, galleryURL, sellingPrice, purchasePriceDefault,
            averageRatingDefault, numberOfReviewsDefault, status, isbn, authors, // authors ở đây
            publisher, category, language, streamingURL
            );

        } 
         else {
            showAlert(AlertType.WARNING, "Chưa chọn loại", "Vui lòng chọn loại sách cụ thể (Sách thường, Ebook, Audiobook).");
            return;
        }

        // 4. Thêm sản phẩm vào DataService
        if (productToAdd != null) {
            productDataService.addProduct(productToAdd);
            showAlert(AlertType.INFORMATION, "Thành Công", "Đã thêm sách '" + title + "' vào hệ thống.");
            clearFormFields(); // Xóa các trường sau khi thêm thành công
            // Bạn có thể đóng cửa sổ này nếu muốn
            Stage stage = (Stage) addProductButton.getScene().getWindow();
             stage.close();
        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/Manage/UpdateStore/ProductTypeSelectionView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Cập nhật kho");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
            
        }
        catch(Exception e){
            System.err.println("Lỗi khi đóng cửa sổ");
            e.printStackTrace();
        }
        
    }

    private void clearFormFields() {
        idField.clear();
        titleField.clear();
        descriptionArea.clear();
        galleryUrlField.clear();
        priceField.clear();
        statusComboBox.setValue(null); // Đặt lại giá trị ComboBox
        isbnField.clear();
        authorsField.clear();
        publisherField.clear();
        categoryComboBox.setValue(null);
        languageComboBox.setValue(null);
        publicationYearField.clear();
        bookTypeComboBox.setValue(null); // Việc này sẽ tự động ẩn các panel specific nhờ listener

        // Clear Ebook specific fields
        numberOfPagesField.clear();
        fileSizeField.clear();
        ebookFormatField.clear();
        ebookDownloadUrlField.clear();
        readOnlineUrlField.clear();
        deviceCompatibilityField.clear();
        ebookHasDrmCheckBox.setSelected(false);

        // Clear Audiobook specific fields
        audioFormatField.clear();
        audiobookDownloadUrlField.clear();
        streamingUrlField.clear();
        narratorField.clear();
        totalDurationField.clear();
        demoAudioArea.clear();
        audiobookHasDrmCheckBox.setSelected(false);
        soundEffectsCheckBox.setSelected(false);
        musicScoreCheckBox.setSelected(false);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // Không hiển thị header text phụ
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Nếu bạn cần parse demoAudioArea thành List<AudioObject> (ví dụ)
    // private ArrayList<Audio> parseDemoAudioToList(String text) { ... }
}