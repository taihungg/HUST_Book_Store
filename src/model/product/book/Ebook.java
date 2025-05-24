package model.product.book;

import model.product.interfaces.Readable;

public class Ebook extends Book implements Readable{
    private int numberOfPages; //số trang sách
    private double fileSize; //kích thước file
    private String ebookFormat; //định dạng file
    private String downloadURL; //đường dẫn tải file
    private String readOnlineURL; //đường dẫn đọc online
    private String deviceCompatibility; //tương thích với thiết bị
    private String demoPageURL; //đường dẫn trang demo
    private boolean hasDRM; //có DRM (cơ chế chống sao lưu) hay không

	public Ebook(String id, String title, String description, String galleryURL, double price, String status,
			String isbn, String authors, String publisher, String category, String language, int publicationYear,
			int numberOfPages, double fileSize, String ebookFormat, String downloadURL, String readOnlineURL,
			String deviceCompatibility, boolean hasDRM) {
		super(id, title, description, galleryURL, price, status, isbn, authors, publisher, category, language,
				publicationYear);
		this.numberOfPages = numberOfPages;
		this.fileSize = fileSize;
		this.ebookFormat = ebookFormat;
		this.downloadURL = downloadURL;
		this.readOnlineURL = readOnlineURL;
		this.deviceCompatibility = deviceCompatibility;
		this.hasDRM = hasDRM;
	}

	public Ebook(String title, String description, String galleryURL, double price, String status, String authors,
			String isbn, String publisher, String category, String language, int publicationYear, int numberOfPages,
			double fileSize, String ebookFormat, String downloadURL, String readOnlineURL, String deviceCompatibility,
			boolean hasDRM) {
		super(title, description, galleryURL, price, status, authors, isbn, publisher, category, language,
				publicationYear);
		this.numberOfPages = numberOfPages;
		this.fileSize = fileSize;
		this.ebookFormat = ebookFormat;
		this.downloadURL = downloadURL;
		this.readOnlineURL = readOnlineURL;
		this.deviceCompatibility = deviceCompatibility;
		this.hasDRM = hasDRM;
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public double getFileSize() {
		return fileSize;
	}
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
	public String getEbookFormat() {
		return ebookFormat;
	}
	public void setEbookFormat(String ebookFormat) {
		this.ebookFormat = ebookFormat;
	}
	public String getDownloadURL() {
		return downloadURL;
	}
	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}
	public String getReadOnlineURL() {
		return readOnlineURL;
	}
	public void setReadOnlineURL(String readOnlineURL) {
		this.readOnlineURL = readOnlineURL;
	}
	public String getDeviceCompatibility() {
		return deviceCompatibility;
	}
	public void setDeviceCompatibility(String deviceCompatibility) {
		this.deviceCompatibility = deviceCompatibility;
	}
	public boolean isHasDRM() {
		return hasDRM;
	}
	public void setHasDRM(boolean hasDRM) {
		this.hasDRM = hasDRM;
	}

	@Override
    public void read() {
        //giao diện các ảnh để đọc thử
        //hoặc có thể in ra nội dung của vài trang 
        System.out.println("Reading...");
    }

}
