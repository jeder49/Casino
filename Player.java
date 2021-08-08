package Poker;

import java.util.Scanner;

public class Player {
	//attributes
	private String name;
	private Mid mid;
	private Card[] hand;
	private int role;
	private int assets;
	private int bet;
	private int ai;
	private boolean fold;
	private boolean allIn;
	private boolean showCards;
	
	//constructor
	public Player(String name, int ai, Mid mid) {
		setAi(ai);
		setMid(mid);
		setName(name);
	}
	
	//what to do
	//get priviouse player to compare bet
	public int decide(Player p) {
		if(ai == 0) {
			Scanner sc = new Scanner(System.in);
			
			int value;
			
			if(p != null) {
				//value for own bet
				value = p.getBet();
			}else {
				value = getBet();
			}
			
			boolean b = true;
			while(b) {
				System.out.println("Do you want to: ");
				
				//there is no player before this player this round
				if(p == null) {
					//check
					System.out.println("check");
					
					//bet
					System.out.println("bet");
					
					//fold
					System.out.println("fold");
					
					//all in
					System.out.println("allIn");
				}else {
					if(p.getBet() <= getBet()+getAssets()) {
						//call
						System.out.println("call");
					
						//raise
						System.out.println("raise");
					}
					
					//fold
					System.out.println("fold");
					
					//all in
					System.out.println("allIn");
				}
				System.out.print("[" + name + "] chooses: ");
				String s = sc.next();
				
				switch(s) {
					case "call":
						if(p != null) {
							setBet(value);
							return 0;
						}else {
							System.out.println("Don't try to mess with the Casino!");
						}
					break;
					case "raise":
						if(p != null) {
							System.out.println("raise to: ");
							int input = sc.nextInt();
							//if raise is lower than the bet player has to choose again
							if(input < p.getBet()) {
								b = true;
							}
							//all in
							else if(input > getBet()+getAssets()) {
								value = getBet()+getAssets();
								setBet(value);
								return 3;
							}
							//normal raise
							else {
								setBet(input);
								return 1;
							}	
						}else {
							System.out.println("Don't try to mess with the Casino!");
						}
					break;
					case "fold":
						setFold(true);
						return 2;
					case "allIn":
						value = getBet()+getAssets();
						setBet(value);
						return 3;
					case "check":
						if(p == null) {
							setBet(value);
							return 4;
						}else {
							System.out.println("Don't try to mess with the Casino!");
						}
					break;
					case "bet":
						if(p == null) {
							System.out.println("you choose: ");
							value = sc.nextInt();
							setBet(value);
							return 5;
						}else {
							System.out.println("Don't try to mess with the Casino!");
						}
					break;
				}
			}
		}else {
			//level 1: random
			
			//level 2: normal
			
			//level3: OP(sees every thing, only plays when he wins)
			
			//level4: OP(sees every thing, can bluff)
			System.out.println("[Ai]: I choose you!");
		
		}
		return -1;
	}	

	public int getBest() {
		return 0;
	}
	
	public double[] getChance() {
		double[] chance = new double[3];
		return chance;
	}
	
	public boolean showedCards() {
		return showCards;
	}
	public boolean showCards() {
		System.out.print("Show cards? [Y/n]: ");
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		if(input.equals("n")) {
			showCards = false;
			return false;
		}
		showCards = true;
		return true;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


}
