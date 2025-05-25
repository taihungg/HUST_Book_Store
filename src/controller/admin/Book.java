package controller.admin;


public class Book {
    private String title;       // Sẽ được dùng cho cột "Name"
    private String authors;     // Sẽ được dùng cho cột "Author"
    private double price;       // Sẽ được dùng cho cột "Price"
    private int quantity;      // Sẽ được dùng cho cột "Quantity"
    private String category;    // Sẽ được dùng cho cột "Category"

    // Constructor để tạo đối tượng Book với các thuộc tính này
    public Book(String title, String authors, double price, int quantity, String category) {
        this.title = title;
        this.authors = authors;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    // --- GETTERS cho các thuộc tính cần hiển thị trong TableView ---
    public String getTitle() {
        return title;
    }

    public String getAuthors() {
        return authors;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    // Phương thức tính toán TotalPrice (Tổng giá)
    // TableView sẽ gọi phương thức này nếu bạn dùng PropertyValueFactory với tên "totalPrice"
    public double getTotalPrice() {
        return this.price * this.quantity;
    }

    // --- SETTERS (Nếu bạn cần khả năng thay đổi giá trị các thuộc tính sau khi đối tượng được tạo) ---
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
