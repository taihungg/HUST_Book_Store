package model.product;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Product {
	private final StringProperty id;
	private final StringProperty title;
	private final StringProperty description;
	private final StringProperty galleryURL;
	private final DoubleProperty sellingPrice;
	private final DoubleProperty purchasePrice;
	private final DoubleProperty averageRating;
	private final IntegerProperty numberOfReviews;
	private final StringProperty status;
	
	public Product(String id, String title, String description, String galleryURL,
			double sellingPrice, double purchasePrice, String status) {
		super();
		this.id = new SimpleStringProperty(id);
		this.title = new SimpleStringProperty(title);
		this.description = new SimpleStringProperty(description);
		this.galleryURL = new SimpleStringProperty(galleryURL);
		this.sellingPrice = new SimpleDoubleProperty(sellingPrice);
		this.purchasePrice = new SimpleDoubleProperty(purchasePrice);
		this.averageRating = new SimpleDoubleProperty(0);
		this.numberOfReviews = new SimpleIntegerProperty(0);
		this.status = new SimpleStringProperty(status);
		System.out.println("Product " + getTitle() + " has been created.");
	}


	public final String getId() {
		return this.idProperty().get();
	}
	

	public final void setId(final String id) {
		this.idProperty().set(id);
	}
	
	public final StringProperty titleProperty() {
		return this.title;
	}
	
	public final String getTitle() {
		return this.titleProperty().get();
	}
	
	public final void setTitle(final String title) {
		this.titleProperty().set(title);
	}
	
	public final StringProperty descriptionProperty() {
		return this.description;
	}
	
	public final String getDescription() {
		return this.descriptionProperty().get();
	}
	
	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}
	
	public final StringProperty galleryURLProperty() {
		return this.galleryURL;
	}
	
	public final String getGalleryURL() {
		return this.galleryURLProperty().get();
	}
	
	public final void setGalleryURL(final String galleryURL) {
		this.galleryURLProperty().set(galleryURL);
	}
	
	public final DoubleProperty sellingPriceProperty() {
		return this.sellingPrice;
	}
	
	public final double getSellingPrice() {
		return this.sellingPriceProperty().get();
	}
	
	public final void setSellingPrice(final double sellingPrice) {
		this.sellingPriceProperty().set(sellingPrice);
	}
	
	public final DoubleProperty purchasePriceProperty() {
		return this.purchasePrice;
	}
	
	public final double getPurchasePrice() {
		return this.purchasePriceProperty().get();
	}
	
	public final void setPurchasePrice(final double purchasePrice) {
		this.purchasePriceProperty().set(purchasePrice);
	}
	
	public final DoubleProperty averageRatingProperty() {
		return this.averageRating;
	}
	
	public final double getAverageRating() {
		return this.averageRatingProperty().get();
	}
	
	public final void setAverageRating(final double averageRating) {
		this.averageRatingProperty().set(averageRating);
	}
	
	public final IntegerProperty numberOfReviewsProperty() {
		return this.numberOfReviews;
	}
	
	public final int getNumberOfReviews() {
		return this.numberOfReviewsProperty().get();
	}
	
	public final void setNumberOfReviews(final int numberOfReviews) {
		this.numberOfReviewsProperty().set(numberOfReviews);
	}
	
	public final StringProperty statusProperty() {
		return this.status;
	}
	
	public final String getStatus() {
		return this.statusProperty().get();
	}
	
	public final void setStatus(final String status) {
		this.statusProperty().set(status);
	}

	public final StringProperty idProperty() {
		return this.id;
	}
}
