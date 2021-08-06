package Poker;

import java.util.Scanner;

public class Player {
	//attributes
	private Mid mid;
	private Card[] hand;
	private int role;
	private int assets;
	private int bet;
	private int ai;
	private boolean fold;
	private boolean allIn;
	
	//constructor
	public Player(int ai, Mid mid) {
		setAi(ai);
		this.setMid(mid);
	}
	
	//what to do
	public int decide() {
		if(ai == 0) {
			Scanner sc = new Scanner(System.in);
			System.out.println("Do you want to: ");
		
			//bet
			System.out.println("1. bet");
		
			//raise
			System.out.println("2. raise");
		
			//fold
			System.out.println("3. fold");
		
			//all in
			System.out.println("4. go all in");
		
			int i = sc.nextInt();
		
			System.out.println("you choose: " + i);
		}else {
			//
		}
		return 0;
	}
	
	/*
	 * Getter- & Setter-Methods
	 */
	
	public double[] getChance() {
		double[] chance = new double[3];
		return chance;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
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
	public int isAi() {
		return ai;
	}
	public void setAi(int ai2) {
		this.ai = ai2;
	}
	public Card[] getHand() {
		return hand;
	}
	public void setHand(Card[] hand) {
		this.hand = hand;
	}

	public boolean hasFolded() {
		return fold;
	}

	public void setFold(boolean fold) {
		this.fold = fold;
	}
	public boolean isAllIn() {
		return allIn;
	}
	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}
	public Mid getMid() {
		return mid;
	}
	public void setMid(Mid mid) {
		this.mid = mid;
	}

}
