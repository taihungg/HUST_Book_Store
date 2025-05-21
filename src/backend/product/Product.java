package backend.product;

public abstract class Product {
	private String id;
	private String title;
	private String description;
	private String galleryURL;
	private double price;
	private double averageRating;
	private int numberOfReviews;
	private String status;
	
	//phương thức khởi tạo này dùng khi import từ dtb (có cả id)
	public Product(String id, String title, String description, String galleryURL, double price, String status) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.galleryURL = galleryURL;
		this.price = price;
		this.averageRating = 0;
		this.numberOfReviews = 0;
		this.status = status;
	}
	//dùng khi tạo sản phẩm mới bằng cách nhập vào UI
	public Product(String title, String description, String galleryURL, double price, String status) {
		super();
		this.title = title;
		this.description = description;
		this.galleryURL = galleryURL;
		this.price = price;
		this.averageRating = 0;
		this.numberOfReviews = 0;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGalleryURL() {
		return galleryURL;
	}
	public void setGalleryURL(String galleryURL) {
		this.galleryURL = galleryURL;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	public int getNumberOfReviews() {
		return numberOfReviews;
	}
	public void setNumberOfReviews(int numberOfReviews) {
		this.numberOfReviews = numberOfReviews;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
