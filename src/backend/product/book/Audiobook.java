package backend.product.book;

import backend.product.interfaces.Playable;
import java.util.ArrayList;

public class Audiobook extends Book implements Playable{
    private String audioFormat;
	private String downloadURL;
	private String streamingURL;
	private String narrator;
	private int totalDuration;
	private boolean hasDRM;
	private boolean soundEffects;
	private boolean musicScore;
	private ArrayList<Audio> demoAudio;

	public Audiobook(String id, String title, String description, String galleryURL, double price, String status,
			String isbn, String authors, String publisher, String category, String language, int publicationYear,
			String audioFormat, String downloadURL, String streamingURL, String narrator, int totalDuration,
			boolean hasDRM, boolean soundEffects, boolean musicScore, ArrayList<Audio> demoAudio) {
		super(id, title, description, galleryURL, price, status, isbn, authors, publisher, category, language,
				publicationYear);
		this.audioFormat = audioFormat;
		this.downloadURL = downloadURL;
		this.streamingURL = streamingURL;
		this.narrator = narrator;
		this.totalDuration = totalDuration;
		this.hasDRM = hasDRM;
		this.soundEffects = soundEffects;
		this.musicScore = musicScore;
		this.demoAudio = demoAudio;
	}

	public Audiobook(String title, String description, String galleryURL, double price, String status, String authors,
			String isbn, String publisher, String category, String language, int publicationYear, String audioFormat,
			String downloadURL, String streamingURL, String narrator, int totalDuration, boolean hasDRM,
			boolean soundEffects, boolean musicScore, ArrayList<Audio> demoAudio) {
		super(title, description, galleryURL, price, status, authors, isbn, publisher, category, language,
				publicationYear);
		this.audioFormat = audioFormat;
		this.downloadURL = downloadURL;
		this.streamingURL = streamingURL;
		this.narrator = narrator;
		this.totalDuration = totalDuration;
		this.hasDRM = hasDRM;
		this.soundEffects = soundEffects;
		this.musicScore = musicScore;
		this.demoAudio = demoAudio;
	}
	
	public String getAudioFormat() {
		return audioFormat;
	}

	public void setAudioFormat(String audioFormat) {
		this.audioFormat = audioFormat;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getStreamingURL() {
		return streamingURL;
	}

	public void setStreamingURL(String streamingURL) {
		this.streamingURL = streamingURL;
	}

	public String getNarrator() {
		return narrator;
	}

	public void setNarrator(String narrator) {
		this.narrator = narrator;
	}

	public int getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(int totalDuration) {
		this.totalDuration = totalDuration;
	}

	public boolean isHasDRM() {
		return hasDRM;
	}

	public void setHasDRM(boolean hasDRM) {
		this.hasDRM = hasDRM;
	}

	public boolean isSoundEffects() {
		return soundEffects;
	}

	public void setSoundEffects(boolean soundEffects) {
		this.soundEffects = soundEffects;
	}

	public boolean isMusicScore() {
		return musicScore;
	}

	public void setMusicScore(boolean musicScore) {
		this.musicScore = musicScore;
	}

	public ArrayList<Audio> getDemoAudio() {
		return demoAudio;
	}

	public void setDemoAudio(ArrayList<Audio> demoAudio) {
		this.demoAudio = demoAudio;
	}

	public void play(){
		for(Audio demo: demoAudio) demo.play();
	}
}
