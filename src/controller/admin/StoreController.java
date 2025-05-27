package controller.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import model.product.Product; // Đảm bảo bạn có lớp Product này
import model.product.Stationery;
import model.product.Toy;

import java.io.IOException;

import model.manager.product.ProductManager;
public class StoreController {

    // Các cột này sẽ khớp với FXML đã được sửa đổi
    @FXML
    private TableColumn<Product, String> titleCol; // Đổi từ nameCol cho nhất quán với Product

    @FXML
    private TableColumn<Product, String> descriptionCol; // Thêm cột này

    @FXML
    private TableColumn<Product, Double> priceCol;

    @FXML
    private TableColumn<Product, String> statusCol; // Thêm cột này

    // TableView sẽ chứa các đối tượng Product
    @FXML
    private TableView<Product> productTableView;


    // Các nút và trường tìm kiếm giữ nguyên fx:id
    @FXML
    private Button deleteButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchField;

    // Các nút Display, Edit, View có thể cần logic cụ thể hoặc bạn có thể tạm thời vô hiệu hóa/xóa chúng
    // nếu FXML mới không có chúng hoặc bạn chưa định nghĩa hành động.
     // Giữ lại nếu FXML có

    @FXML
    private Button viewButton;    // Giữ lại nếu FXML có


    private ProductDataService productDataService;

    @FXML
    public void initialize() {
        productDataService = ProductDataService.getInstance(); // Lấy instance của service


        productTableView.setEditable(true);

        // Thiết lập CellValueFactory cho các cột tương ứng với thuộc tính của Product
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit((CellEditEvent<Product, String> event) -> {
            Product product = event.getRowValue();
            product.setTitle(event.getNewValue()); // Product cần có setTitle()
        });

        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionCol.setOnEditCommit((CellEditEvent<Product, String> event) -> {
            Product product = event.getRowValue();
            product.setDescription(event.getNewValue()); // Product cần có setDescription()
        });

        priceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceCol.setOnEditCommit((CellEditEvent<Product, Double> event) -> {
            Product product = event.getRowValue();
            if (event.getNewValue() != null) {
                product.setSellingPrice(event.getNewValue()); // Product cần có setPrice()
            }
        });

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(TextFieldTableCell.forTableColumn());
        statusCol.setOnEditCommit((CellEditEvent<Product, String> event) -> {
            Product product = event.getRowValue();
            product.setStatus(event.getNewValue()); // Product cần có setStatus()
        });


        productTableView.setItems(productDataService.getProductList());
        System.out.println("StoreController: Khởi tạo hoàn tất. Hiển thị " + productDataService.getProductList().size() + " sản phẩm từ service.");

    }

    @FXML
    void handleDeleteAction(ActionEvent event) {
        productDataService.removeProduct( productTableView.getSelectionModel().getSelectedItem());
        
    }

    @FXML
    void handleExitAction(ActionEvent event) {

        try{
        FXMLLoader loader = new FXMLLoader (getClass().getResource("/view/admin/HomePageAdmin.fxml"));
        Parent root = loader.load();
        Stage currentstage = new Stage();
        currentstage.setTitle("Trang chủ");
        currentstage.setScene (new Scene(root));
        currentstage.show();
        
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    catch(IOException e){
        System.err.println("Lỗi khi mở trang chủ");
        e.printStackTrace();
    }
    }

    @FXML
    void handleSearchAction(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase().trim();
        if (searchText.isEmpty()) {
            productTableView.setItems(productDataService.getProductList()); // Hiển thị lại toàn bộ danh sách nếu ô tìm kiếm rỗng
            return;
        }

        ObservableList<Product> filteredList = FXCollections.observableArrayList();
        for (Product product : productDataService.getProductList()) {
            if (product.getTitle() != null && product.getTitle().equalsIgnoreCase(searchText)) {
                filteredList.add(product);
            }
            // Bạn có thể thêm tìm kiếm theo description hoặc status nếu muốn
        }
        productTableView.setItems(filteredList);

       
    }

    // Các phương thức handleDisplayAction, handleEditAction, handleViewAction
    // cần được định nghĩa lại hoặc bạn có thể comment out/xóa nếu FXML mới không có các nút này
    // hoặc nếu chúng chưa có chức năng.
   

    
    

    @FXML
    void handleViewAction(ActionEvent event) {
         Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct !=null){
            try{
            if (selectedProduct instanceof Toy){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/ToyDetail.fxml")); // Đường dẫn đến FXML chi tiết
                Parent detailRoot = loader.load();
                ToyDetailController toyDetailController = loader.getController();
                toyDetailController.setToy((Toy)selectedProduct);

                Stage detailStage = new Stage();
                detailStage.setTitle("Chi Tiết Sản Phẩm - " + selectedProduct.getTitle());
                detailStage.setScene(new Scene(detailRoot));
                detailStage.initModality(Modality.APPLICATION_MODAL); // Chặn tương tác với cửa sổ StoreController
                detailStage.showAndWait(); // Hiển thị và chờ cho đến khi cửa sổ này đóng

            }

        }
        catch (IOException e){
            System.err.println("Lỗi khi mở cửa sổ chi tiết sản phẩm:");

            e.printStackTrace();
        }
        }

        else {
            System.out.println("Selected product is null");
        }
        
    }

    
}