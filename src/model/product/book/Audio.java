package model.product.book;

import model.product.interfaces.Playable;

public class Audio implements Playable{
	private String audioName;
    private int length;
    public void play(){
        System.out.println("Playing audio " + audioName + "\nAudio length: " + length);
    }
}
