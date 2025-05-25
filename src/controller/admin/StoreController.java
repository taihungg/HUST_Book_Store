    package controller.admin;

  
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableColumn.CellEditEvent; // Quan trọng: Import CellEditEvent
    import javafx.scene.control.TableView;
    import javafx.scene.control.TextField;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.control.cell.TextFieldTableCell; // Quan trọng: Import TextFieldTableCell
    import javafx.stage.Stage;

import javafx.util.converter.DoubleStringConverter; // Import converter cho Double
    import javafx.util.converter.IntegerStringConverter;
    public class StoreController {

        @FXML
        private TableColumn<Book, String> authorCol;

        @FXML
        private TableView<Book> bookTableView;

        @FXML
        private TableColumn<Book, String> categoryCol;

        @FXML
        private Button deleteButton;

        @FXML
        private Button displayButton;

        @FXML
        private Button editButton;

        @FXML
        private Button exitButton;

        @FXML
        private TableColumn<Book, String> nameCol; 

        @FXML
        private TableColumn<Book, Double> priceCol;

        @FXML
        private TableColumn<Book, Integer> quantityCol;

        @FXML
        private Button searchButton;

        @FXML
        private TextField searchField;

        @FXML
        private TableColumn<Book, Double> totalPriceCol;

        @FXML
        private Button viewButton;

        private ObservableList<Book> bookList;

        
    @FXML 
public void initialize() {
    bookList = FXCollections.observableArrayList(
        new Book(
            "Harry Potter và Hòn Đá Phù Thủy",    // title (Tên sách)
            "J.K. Rowling",                       // authors (Tác giả)
            150000.0,                             // price (Giá)
            25,                                   // quantity (Số lượng)
            "Fantasy"                             // category (Thể loại)
        ),
        new Book(
            "Đắc Nhân Tâm",                       // title (Tên sách)
            "Dale Carnegie",                      // authors (Tác giả)
            120000.0,                             // price (Giá)
            30,                                   // quantity (Số lượng)
            "Self-help"                           // category (Thể loại)
        )
    );
    bookTableView.setEditable(true);
    nameCol.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
    nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    nameCol.setOnEditCommit((CellEditEvent<Book,String> event) -> {
        Book book = event.getRowValue();
        book.setTitle(event.getNewValue());
    });
    authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("authors"));
    authorCol.setCellFactory (TextFieldTableCell.forTableColumn());
    authorCol.setOnEditCommit((CellEditEvent<Book,String> event)-> {
        Book book = event.getRowValue();
        book.setAuthors(event.getNewValue());
    });

    priceCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("price"));
    priceCol.setCellFactory (TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
    priceCol.setOnEditCommit((CellEditEvent<Book,Double>e) ->{
        Book book = e.getRowValue();
        book.setPrice(e.getNewValue());
    });

    quantityCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("quantity"));
    quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
    quantityCol.setOnEditCommit((CellEditEvent<Book,Integer> e ) -> {
        Book book = e.getRowValue();
        book.setQuantity(e.getNewValue());
       
    }); // THÊM CHO QUANTITY

    categoryCol.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
    categoryCol.setCellFactory(TextFieldTableCell.forTableColumn());
    categoryCol.setOnEditCommit((CellEditEvent<Book,String> e) -> {
        Book book = e.getRowValue();
        book.setCategory(e.getNewValue());
    });
    totalPriceCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("totalPrice"));
    
     // SỬA ĐỂ DÙNG GETTER totalPrice

    bookTableView.setItems(bookList);

    // In ra để kiểm tra
    System.out.println("Danh sách sách được nạp: " + bookList.size());
    if (!bookList.isEmpty()) {
        System.out.println("Sách đầu tiên: " + bookList.get(0).getTitle() + " - SL: " + bookList.get(0).getQuantity());
    }
}

        @FXML
        void handleDeleteAction(ActionEvent event) {
            Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
            bookList.remove(selectedBook);

        }

     
       

       

        @FXML
        void handleExitAction(ActionEvent event) {
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();

        }

        @FXML
        void handleSearchAction(ActionEvent event) {
            String searchText = searchField.getText();
            ObservableList<Book>filteredList = FXCollections.observableArrayList();
            for (Book book : bookList){
                if(book.getTitle().equals(searchText)){
                    filteredList.add(book);
                    bookTableView.setItems(filteredList);
                    break;
                }
            }
            if (filteredList.isEmpty()){
                bookTableView.setItems(bookList);
            }
                
            
        }

        

    }
