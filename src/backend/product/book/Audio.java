package backend.product.book;

import backend.product.interfaces.Playable;

public class Audio implements Playable{
	private String audioName;
    private int length;
    public void play(){
        System.out.println("Playing audio " + audioName + "\nAudio length: " + length);
    }
}
