package model.product.book;

import java.util.Objects;

import model.product.interfaces.DigitalProduct;
import model.product.interfaces.Playable;

public class Audio implements Playable, DigitalProduct{
	private String audioName;
    private int length;
    public void play(){
        System.out.println("Playing audio " + audioName + "\nAudio length: " + length);
    }
	public String getAudioName() {
		return audioName;
	}
	public void setAudioName(String audioName) {
		this.audioName = audioName;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audio audio = (Audio) o;
        return Objects.equals(audioName, audio.audioName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(audioName);
    }
}
