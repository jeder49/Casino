package Poker;

public class Person {
	private String name;
	private int assets;
	private int bet;
	public Person(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAssets() {
		return assets;
	}
	public void setAssets(int assets) {
		this.assets = assets;
	}
	public int getBet() {
		return bet;
	}
	public void setBet(int bet) {
		this.bet = bet;
	}

}
