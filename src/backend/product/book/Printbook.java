package backend.product.book;

import backend.product.interfaces.Readable;

public class Printbook extends Book implements Readable{
	private int numberOfPages; //số trang sách
	private int weight; //trọng lượng quyển sách
	private String coverType; //loại bìa
	private String paperType; //loại giấy
	private String bindingType; //loại gáy sách
	//khi import sách không cần đủ toàn bộ thuộc tính, cái nào không được nhập thì để là null
	//chỉ bắt buộc có các thuộc tính của lớp cha (Book)
	public Printbook(String id, String title, String description, String galleryURL, double price, String status,
			String isbn, String authors, String publisher, String category, String language, int publicationYear,
			int numberOfPages, int weight, String coverType, String paperType, String bindingType) {
		super(id, title, description, galleryURL, price, status, isbn, authors, publisher, category, language,
				publicationYear);
		this.numberOfPages = numberOfPages;
		this.weight = weight;
		this.coverType = coverType;
		this.paperType = paperType;
		this.bindingType = bindingType;
	}
	//đây là nhập từ UI nên không cần id	
	public Printbook(String title, String description, String galleryURL, double price, String status, String authors,
			String isbn, String publisher, String category, String language, int publicationYear, int numberOfPages,
			int weight, String coverType, String paperType, String bindingType) {
		super(title, description, galleryURL, price, status, authors, isbn, publisher, category, language,
				publicationYear);
		this.numberOfPages = numberOfPages;
		this.weight = weight;
		this.coverType = coverType;
		this.paperType = paperType;
		this.bindingType = bindingType;
	}
	
	public int getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getCoverType() {
		return coverType;
	}
	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}
	public String getPaperType() {
		return paperType;
	}
	public void setPaperType(String paperType) {
		this.paperType = paperType;
	}
	public String getBindingType() {
		return bindingType;
	}
	public void setBindingType(String bindingType) {
		this.bindingType = bindingType;
	}

	@Override
	public void read(){
		//giao diện các ảnh để đọc thử
		//hoặc có thể in ra nội dung của vài trang 
        System.out.println("Reading...");
	}
}
