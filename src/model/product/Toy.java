package model.product;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.product.interfaces.PhysicalProduct;

public class Toy extends Product implements PhysicalProduct{
	private final StringProperty brand;  
	private final IntegerProperty suitableAge; // age for toy >= suitable age
	
	public Toy(String id, String title, String description, String galleryURL, double sellingPrice,
			double purchasePrice, String status, String brand, int suitableAge) {
		super(id, title, description, galleryURL, sellingPrice, purchasePrice, status);
		this.brand = new SimpleStringProperty(brand);
		this.suitableAge = new SimpleIntegerProperty(suitableAge);	
	}

	public final StringProperty brandProperty() {
		return this.brand;
	}
	
	public final String getBrand() {
		return this.brandProperty().get();
	}
	
	public final void setBrand(final String brand) {
		this.brandProperty().set(brand);
	}
	
	public final IntegerProperty suitableAgeProperty() {
		return this.suitableAge;
	}
	
	public final int getSuitableAge() {
		return this.suitableAgeProperty().get();
	}
	
	public final void setSuitableAge(final int suitableAge) {
		this.suitableAgeProperty().set(suitableAge);
	}
}
