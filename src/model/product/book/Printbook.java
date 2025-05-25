package model.product.book;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.product.interfaces.PhysicalProduct;
import model.product.interfaces.Readable;

public class Printbook extends Book implements Readable, PhysicalProduct{
	private final IntegerProperty numberOfPages; //số trang sách
	private final IntegerProperty weight; //gram - trọng lượng quyển sách
	private final StringProperty[] demoPageURLs; //đường dẫn trang demo
	
	public Printbook(String id, String title, String description, String galleryURL, double sellingPrice,
			double purchasePrice, double averageRating, int numberOfReviews, String status, String isbn, String author,
			String publisher, String category, String language, int numberOfPages, int weight) {
		super(id, title, description, galleryURL, sellingPrice, purchasePrice, averageRating, numberOfReviews, status,
				isbn, author, publisher, category, language);
		this.numberOfPages = new SimpleIntegerProperty(numberOfPages);
		this.weight = new SimpleIntegerProperty(weight);
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
	
	public final IntegerProperty weightProperty() {
		return this.weight;
	}
	
	public final int getWeight() {
		return this.weightProperty().get();
	}
	
	public final void setWeight(final int weight) {
		this.weightProperty().set(weight);
	}
	
	@Override
    public void read() {
		//giao diện chỉ cần làm thông báo đang đọc demo
        System.out.println("Reading demo " + this.getTitle());
    }
}
