package backend.product.book;

import backend.product.Product;

public abstract class Book extends Product{
    private String isbn; //mã isbn 13 số chuẩn quốc tế
	private String authors; //tác giả
    private String publisher; //nhà xuất bản
    private String category; //thể loại sách
    private String language; // Ngôn ngữ của sách
    private int publicationYear; // Năm xuất bản

	public Book(String id, String title, String description, String galleryURL, double price, String status,
			String isbn, String authors, String publisher, String category, String language, int publicationYear) {
		super(id, title, description, galleryURL, price, status);
        this.isbn = isbn;
		this.authors = authors;
		this.publisher = publisher;
		this.category = category;
		this.language = language;
		this.publicationYear = publicationYear;
	}

	public Book(String title, String description, String galleryURL, double price, String status, String authors,
			String isbn, String publisher, String category, String language, int publicationYear) {
		super(title, description, galleryURL, price, status);
        this.isbn = isbn;
		this.authors = authors;
		this.publisher = publisher;
		this.category = category;
		this.language = language;
		this.publicationYear = publicationYear;
	}
    
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
	public String getAuthors() {
		return authors;
	}
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getPublicationYear() {
		return publicationYear;
	}
	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}
    
}
