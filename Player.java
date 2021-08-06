package Poker;

public class Player {
	//attributes
	private Mid mid;
	private Card[] hand;
	private int role;
	private int assets;
	private int bet;
	private boolean ai;
	private boolean fold;
	private boolean allIn;
	
	//constructor
	public Player(boolean ai, Mid mid) {
		setAi(ai);
		this.setMid(mid);
	}
	
	//what to do
	public int decide() {
		//bet
		//raise
		//falt
		//all in
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
	public boolean isAi() {
		return ai;
	}
	public void setAi(boolean ai) {
		this.ai = ai;
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
