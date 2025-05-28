package controller.admin;

import controller.Main;
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
import model.manager.AppServiceManager;
// Import các lớp model của bạn
import model.product.Product;
import model.product.book.Book;
import model.product.book.Ebook;
import model.product.book.Audiobook;
import model.product.book.Printbook;
// Import service quản lý dữ liệu


public class BookController {

    //<editor-fold defaultstate="collapsed" desc="FXML Fields">
    @FXML
    private Button addProductButton;

    @FXML
    private TextField audiobookDownloadUrlField;

    @FXML
    private GridPane audiobookSpecificFieldsPane;

    @FXML
    private TextField authorsField;

    @FXML
    private ComboBox<String> bookTypeComboBox;

    @FXML
    private Button cancelButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField ebookDownloadUrlField;

    @FXML
    private TextField ebookNumberOfPagesField;

    @FXML
    private GridPane ebookSpecificFieldsPane;

    @FXML
    private TextField galleryUrlField;

    @FXML
    private TextField isbnField;

    @FXML       
    private ComboBox<String> languageComboBox;

    @FXML
    private TextField printBookNumberOfPagesField;

    @FXML
    private GridPane printBookSpecificFieldsPane;

    @FXML
    private TextField printBookWeightField;

    @FXML
    private TextField publicationYearField;

    @FXML
    private TextField publisherField;

    @FXML
    private TextField purchasePriceField;

    @FXML
    private TextField sellingPriceField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextField titleField;

    //</editor-fold>

    private AppServiceManager appServiceManager = Main.appServiceManager;
    @FXML
    public void initialize() {
        // Populate ComboBoxes
        languageComboBox.getItems().addAll("Tiếng Việt", "Tiếng Anh", "Tiếng Nhật", "Tiếng Pháp", "Song ngữ", "Khác");
        statusComboBox.getItems().addAll("Còn hàng", "Hết hàng", "Sắp phát hành", "Ngừng kinh doanh");
        bookTypeComboBox.getItems().addAll("Print Book", "Ebook", "Audiobook");
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

    }

    private void updateSpecificFieldsVisibility(String bookType) {
        if ("Ebook".equals(bookType)) {
            ebookSpecificFieldsPane.setVisible(true);
            ebookSpecificFieldsPane.setManaged(true);
            audiobookSpecificFieldsPane.setVisible(false);
            audiobookSpecificFieldsPane.setManaged(false);
            printBookSpecificFieldsPane.setVisible(false);
            printBookSpecificFieldsPane.setManaged(false);
        } else if ("Audiobook".equals(bookType)) {
            ebookSpecificFieldsPane.setVisible(false);
            ebookSpecificFieldsPane.setManaged(false);
            audiobookSpecificFieldsPane.setVisible(true);
            audiobookSpecificFieldsPane.setManaged(true);
            printBookSpecificFieldsPane.setVisible(false);
            printBookSpecificFieldsPane.setManaged(false);
        } else { // Sách thường hoặc không chọn
            ebookSpecificFieldsPane.setVisible(false);
            ebookSpecificFieldsPane.setManaged(false);
            audiobookSpecificFieldsPane.setVisible(false);
            audiobookSpecificFieldsPane.setManaged(false);
            printBookSpecificFieldsPane.setVisible(true);
            printBookSpecificFieldsPane.setManaged(true);
        }
    }

    @FXML
    void handleAddBook(ActionEvent event) {
        // 1. Lấy các giá trị chung từ form
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String galleryURL = galleryUrlField.getText().trim(); // Sửa tên biến cho nhất quán
        double sellingPrice = Double.parseDouble(sellingPriceField.getText().trim());
        double purchasePrice = Double.parseDouble(purchasePriceField.getText().trim());
        String status = statusComboBox.getValue();
        String isbn = isbnField.getText().trim();
        String authors = authorsField.getText().trim();
        String publisher = publisherField.getText().trim();
        String category = categoryComboBox.getValue();
        String language = languageComboBox.getValue();
        String publicationYearText = publicationYearField.getText().trim();
        String selectedBookType = bookTypeComboBox.getValue();

        // 2. Validate dữ liệu chung cơ bản
        if ( title.isEmpty() || sellingPriceField.getText().isEmpty() || purchasePriceField.getText().isEmpty() || status == null ||
            isbn.isEmpty() || authors.isEmpty() || publisher.isEmpty() || category == null ||
            language == null || publicationYearText.isEmpty() || selectedBookType == null) {
            showAlert(AlertType.ERROR, "Thiếu thông tin", "Vui lòng điền đầy đủ các trường thông tin chung bắt buộc của sách.");
            return;
        }

        
        int publicationYear;
        // Các giá trị mặc định cho các tham số của Product mà Book cần truyền cho super()
        // Bạn có thể thêm trường nhập liệu cho chúng nếu muốn người dùng tự nhập
        double purchasePriceDefault = 0.0;
        double averageRatingDefault = 0.0;
        int numberOfReviewsDefault = 0;
        // int quantityDefault = 0; // Nếu lớp Book của bạn có quantity

        try {
            
            if (sellingPrice <= 0) {
                showAlert(AlertType.ERROR, "Giá không hợp lệ", "Giá bán phải lớn hơn 0.");
                return;
            }
            if (purchasePrice <= 0) {
                showAlert(AlertType.ERROR, "Giá không hợp lệ", "Giá mua phải lớn hơn 0.");
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
        if ("Ebook".equals(selectedBookType)) {
            String numberOfPagesText = ebookNumberOfPagesField.getText().trim();
            String ebookDownloadURL = ebookDownloadUrlField.getText().trim();
            // Lấy các trường khác của Ebook
            


            if (numberOfPagesText.isEmpty() || ebookDownloadURL.isEmpty() /*|| các trường Ebook bắt buộc khác */) {
                 showAlert(AlertType.ERROR, "Thiếu thông tin Ebook", "Vui lòng điền số trang và URL tải về cho Ebook.");
                 return;
            }
            int numberOfPages;
            try {
                numberOfPages = Integer.parseInt(numberOfPagesText);
                if (numberOfPages <= 0) {
                    showAlert(AlertType.ERROR, "Số trang không hợp lệ", "Số trang phải lớn hơn 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert(AlertType.ERROR, "Định dạng Ebook không hợp lệ", "Số trang hoặc kích thước file của Ebook phải là số.");
                return;
            }

            String id = "Ebook_" + System.currentTimeMillis();
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
                purchasePrice,    // purchasePrice
                averageRatingDefault,    // averageRating
                numberOfReviewsDefault,  // numberOfReviews
                status,
                isbn,
                authors,            // author (số ít theo constructor Ebook)
                publisher,
                category,
                language,
                numberOfPages,           
                ebookDownloadURL   
            );
            

        } else if ("Audiobook".equals(selectedBookType)) {
            String audiobookDownloadURL = audiobookDownloadUrlField.getText().trim();
            

            if (audiobookDownloadURL.isEmpty()  /*|| các trường Audiobook bắt buộc khác */) {
                 showAlert(AlertType.ERROR, "Thiếu thông tin Audiobook", "Vui lòng điền URL Audiobook.");
                 return;
            }
            
            String id = "Audiobook_" + System.currentTimeMillis();
            // Giả sử constructor Audiobook của bạn là:
            // Audiobook(id, title, description, galleryURL, sellingPrice, purchasePrice, averageRating, numberOfReviews, status,
            //           isbn, authors, publisher, category, language, publicationYear,
            //           audioFormat, downloadURL, streamingURL, narrator, totalDuration, hasDRM, soundEffects, musicScore /*, demoAudioList */)
            productToAdd = new Audiobook(id, title, description, galleryURL, sellingPrice, purchasePrice,
            averageRatingDefault, numberOfReviewsDefault, status, isbn, authors, // authors ở đây
            publisher, category, language, audiobookDownloadURL
            );

        } 
        else if ("Print Book".equals(selectedBookType)) {
            int numberOfPages = Integer.parseInt(printBookNumberOfPagesField.getText().trim());
            int weight = Integer.parseInt(printBookWeightField.getText().trim());
            String id = "PrintBook_" + System.currentTimeMillis();
            productToAdd = new Printbook(id, title, description, galleryURL, sellingPrice, purchasePrice,
            averageRatingDefault, numberOfReviewsDefault, status, isbn, authors, publisher, category, language, numberOfPages, weight);
        }

         else {
            showAlert(AlertType.WARNING, "Chưa chọn loại", "Vui lòng chọn loại sách cụ thể (Sách thường, Ebook, Audiobook).");
            return;
        }
        int quantityDefault = 10;

        // 4. Thêm sản phẩm vào DataService
        if (productToAdd != null) {
            appServiceManager.getProductManager().addProduct(productToAdd,quantityDefault,appServiceManager.getCurrentUser());
            showAlert(AlertType.INFORMATION, "Thành Công", "Đã thêm sách '" + title + "' vào hệ thống.");
            clearFormFields(); 
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
       
        titleField.clear();
        descriptionArea.clear();
        galleryUrlField.clear();
        sellingPriceField.clear();
        purchasePriceField.clear();
        statusComboBox.setValue(null); // Đặt lại giá trị ComboBox
        isbnField.clear();
        authorsField.clear();
        publisherField.clear();
        categoryComboBox.setValue(null);
        languageComboBox.setValue(null);
        publicationYearField.clear();
        bookTypeComboBox.setValue(null); // Việc này sẽ tự động ẩn các panel specific nhờ listener

        // Clear Ebook specific fields
        ebookNumberOfPagesField.clear();
        ebookDownloadUrlField.clear();

        // Clear Audiobook specific fields
        audiobookDownloadUrlField.clear();
        
        printBookNumberOfPagesField.clear();
        printBookWeightField.clear();   
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