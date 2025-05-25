package model.product.book;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.product.interfaces.DigitalProduct;

import model.product.interfaces.Playable;

public class Audiobook extends Book implements Playable, DigitalProduct{
	private final StringProperty downloadURL; //đường dẫn tải file
	private ObservableList<Audio> demoAudio; //các đoạn âm thanh demo
	
	public Audiobook(String id, String title, String description, String galleryURL, double sellingPrice, double purchasePrice,
			double averageRating, int numberOfReviews, String status, String isbn, String author, String publisher,
			String category, String language, String downloadURL) {
		super(id, title, description, galleryURL, sellingPrice, purchasePrice, averageRating, numberOfReviews, status, isbn, author,
				publisher, category, language);
		this.downloadURL = new SimpleStringProperty(downloadURL);
		this.demoAudio = FXCollections.observableArrayList();
	}

	public final StringProperty downloadURLProperty() {
		return this.downloadURL;
	}
	
	public final String getDownloadURL() {
		return this.downloadURLProperty().get();
	}
	
	public final void setDownloadURL(final String downloadURL) {
		this.downloadURLProperty().set(downloadURL);
	}
	
	public ObservableList<Audio> getDemoAudio(){
		return this.demoAudio;
	}
	
	public void addAudiok(Audio inputAudio){
        // Sử dụng phương thức contains() của List, nó sẽ gọi equals() của đối tượng Audio
        if(demoAudio.contains(inputAudio)){ 
            System.out.println("This audio is exist.");
        } else {
            demoAudio.add(inputAudio);
            System.out.println("Demo audio " + inputAudio.getAudioName() + " has been added to the Audiobook.");
        }
    }

    public void removeAudio(Audio outputAudio){
        // Sử dụng phương thức remove(Object o) của List, nó cũng sẽ gọi equals()
        if(demoAudio.remove(outputAudio)){ // remove() trả về true nếu đối tượng được tìm thấy và xóa
            System.out.println("Demo audio " + outputAudio.getAudioName() + " has been removed from the Audiobook.");
        } else {
            System.out.println("Demo audio " + outputAudio.getAudioName() + " is not exist in the Audiobook.");
        }
    }
	
	@Override
	public void play(){
		for(Audio demo: demoAudio) demo.play();
	}
}
