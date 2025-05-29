package model.product;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.product.interfaces.PhysicalProduct;

public class Stationery extends Product implements PhysicalProduct{
	private final StringProperty brand;
	private final StringProperty type;
	public Stationery(String id, String title, String description, String galleryURL, double sellingPrice,
			double purchasePrice, String status, String brand, String type) {
		super(id, title, description, galleryURL, sellingPrice, purchasePrice, status);
		this.brand = new SimpleStringProperty(brand);
		this.type = new SimpleStringProperty(type);
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
	
	public final StringProperty typeProperty() {
		return this.type;
	}
	
	public final String getType() {
		return this.typeProperty().get();
	}
	
	public final void setType(final String type) {
		this.typeProperty().set(type);
	}
}