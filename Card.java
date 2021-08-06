package Poker;

import java.awt.image.BufferedImage;

public class Card {

	
	private int value;
	private int color;
	private String image_path;
	private BufferedImage image;
	
	public Card(int value, int color, String image_path) {
		this.setValue(value);
		this.setColor(color);
		this.setImage_path(image_path);
		
		// TODO: Create BufferedImage
	}
	
	public String toString() {
		String name = "";
		switch(this.color) {
			case 0:
				name += "Herz-";
				break;
			case 1:
				name += "Kreuz-";
				break;
			case 2:
				name += "Karo-";
				break;
			case 3:
				name += "Pik-";
				break;
		}
		switch(this.value) {
			case 0:
				name += "2";
				break;
			case 1:
				name += "3";
				break;
			case 2:
				name += "4";
				break;
			case 3:
				name += "5";
				break;
			case 4:
				name += "6";
				break;
			case 5:
				name += "7";
				break;
			case 6:
				name += "8";
				break;
			case 7:
				name += "9";
				break;
			case 8:
				name += "10";
				break;
			case 9:
				name += "J";
				break;
			case 10:
				name += "Q";
				break;
			case 11:
				name += "K";
				break;
			case 12:
				name += "A";
				break;
		}
		return name;
	}
	
	/*
	 * Getter- & Setter-Methods
	 */
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public String getImage_path() {
		return image_path;
	}
	public void setImage_path(String image_path) {
		this.image_path = image_path;
	}

}