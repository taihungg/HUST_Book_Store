package model.product.book;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.product.Product;

public abstract class Book extends Product{
    private final StringProperty isbn; //mã isbn 13 số chuẩn quốc tế
	private final StringProperty author; //tác giả
    private final StringProperty publisher; //nhà xuất bản
    private final StringProperty category; //thể loại sách
    private final StringProperty language; // Ngôn ngữ của sách
    
	public Book(String id, String title, String description, String galleryURL, double sellingPrice, double purchasePrice,
			double averageRating, int numberOfReviews, String status, String isbn, String author,
			String publisher, String category, String language) {
		super(id, title, description, galleryURL, sellingPrice, purchasePrice, averageRating, numberOfReviews, status);
		this.isbn = new SimpleStringProperty(isbn);
		this.author = new SimpleStringProperty(author);
		this.publisher = new SimpleStringProperty(publisher);
		this.category = new SimpleStringProperty(category);
		this.language = new SimpleStringProperty(language);
	}

	public final StringProperty isbnProperty() {
		return this.isbn;
	}
	
	public final String getIsbn() {
		return this.isbnProperty().get();
	}

	public final void setIsbn(final String isbn) {
		this.isbnProperty().set(isbn);
	}
	
	public final StringProperty authorProperty() {
		return this.author;
	}
	
	public final String getAuthor() {
		return this.authorProperty().get();
	}
	
	public final void setAuthor(final String author) {
		this.authorProperty().set(author);
	}
	
	public final StringProperty publisherProperty() {
		return this.publisher;
	}
	
	public final String getPublisher() {
		return this.publisherProperty().get();
	}
	
	public final void setPublisher(final String publisher) {
		this.publisherProperty().set(publisher);
	}
	
	public final StringProperty categoryProperty() {
		return this.category;
	}
	
	public final String getCategory() {
		return this.categoryProperty().get();
	}
	
	public final void setCategory(final String category) {
		this.categoryProperty().set(category);
	}
	
	public final StringProperty languageProperty() {
		return this.language;
	}
	
	public final String getLanguage() {
		return this.languageProperty().get();
	}
	
	public final void setLanguage(final String language) {
		this.languageProperty().set(language);
	}
}
