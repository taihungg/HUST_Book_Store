package backend.product;

import java.util.ArrayList;

public class Stationery extends Product{
	private String type;
    private String color;               
    private String manufacturer;               
    private String material;
    
	public Stationery(String id, String title, String description, String galleryURL, double price, String status,
			String type, String color, String manufacturer, String material) {
		super(id, title, description, galleryURL, price, status);
		this.type = type;
		this.color = color;
		this.manufacturer = manufacturer;
		this.material = material;
	}
	
	public Stationery(String title, String description, String galleryURL, double price, String status, String type,
			String color, String manufacturer, String material) {
		super(title, description, galleryURL, price, status);
		this.type = type;
		this.color = color;
		this.manufacturer = manufacturer;
		this.material = material;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
}
