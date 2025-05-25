package model.product;


public class Toy extends Product{
	private String material;   
    private String ageRecommendation;
    private String brand;
    private String type;

	public Toy(String title, String description, String galleryURL, double price, String status) {
		super(title, description, galleryURL, price, status);
	}

	public Toy(String id, String title, String description, String galleryURL, double price, String status,
			String material, String ageRecommendation, String brand, String type) {
		super(id, title, description, galleryURL, price, status);
		this.material = material;
		this.ageRecommendation = ageRecommendation;
		this.brand = brand;
		this.type = type;
	}
	
	public Toy(String title, String description, String galleryURL, double price, String status, String material,
			String ageRecommendation, String brand, String type) {
		super(title, description, galleryURL, price, status);
		this.material = material;
		this.ageRecommendation = ageRecommendation;
		this.brand = brand;
		this.type = type;
	}
	
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getAgeRecommendation() {
		return ageRecommendation;
	}
	public void setAgeRecommendation(String ageRecommendation) {
		this.ageRecommendation = ageRecommendation;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
