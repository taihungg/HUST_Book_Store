package model.product.book;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import model.product.interfaces.DigitalProduct;
import model.product.interfaces.Readable;

public class Ebook extends Book implements Readable, DigitalProduct{
    private final IntegerProperty numberOfPages; //số trang sách
    private final StringProperty downloadURL; //đường dẫn tải file
    private final StringProperty[] demoPageURLs; //đường dẫn trang demo
    
	public Ebook(String id, String title, String description, String galleryURL, double sellingPrice,
			double purchasePrice, String status, String isbn, String author, String publisher, String category, String language, int numberOfPages, String downloadURL) {
		super(id, title, description, galleryURL, sellingPrice, purchasePrice, status, isbn, author, publisher, category, language);
		this.numberOfPages = new SimpleIntegerProperty(numberOfPages);
		this.downloadURL = new SimpleStringProperty(downloadURL);
		this.demoPageURLs = new SimpleStringProperty[5];
	}

	public final IntegerProperty numberOfPagesProperty() {
		return this.numberOfPages;
	}
	
	public final int getNumberOfPages() {
		return this.numberOfPagesProperty().get();
	}
	
	public final void setNumberOfPages(final int numberOfPages) {
		this.numberOfPagesProperty().set(numberOfPages);
	}
	
	public final StringProperty downloadURLProperty() {
		return this.downloadURL;
	}
	
	public final String getDownloadURL() {
		return this.downloadURLProperty().get();
	}
	
	public final void setDownloadURL(final String downloadURL) {
		this.downloadURLProperty().set(downloadURL);
	}
	
	@Override
    public void read() {
		//giao diện chỉ cần làm thông báo đang đọc demo
        System.out.println("Reading demo " + this.getTitle());
    }
}
