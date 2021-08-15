package Poker;

import java.util.Arrays;
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
			int value;
			int choice = 0;
			Scanner sc = new Scanner(System.in);
			
			if(p != null) {
				//value for own bet
				if(!p.isAllIn()) {
					value = p.getBet();
				}else {
					value = getAssets();
				}
			}else {
				value = getBet();
			}
			
			boolean b = true;
			while(b) {
				
				if(ai == 0) {
						//print out choices
						System.out.println("+----------------------+");
						System.out.println("|Do you want to:       |");
						
						//there is no player before this player this round
						if(p == null) {
							if(mid.getRound() != 1) {
								//check
								System.out.println("|+ check	       |");
								
								//fold
								System.out.println("|+ fold		       |");
							}
							
							//bet
							System.out.println("|+ bet		       |");
	
							//all in
							System.out.println("|+ allIn	       |");
						}else {
							if(p.getBet() <= getAssets() && !mid.hasPot()) {
								//call
								System.out.println("|+ call	           |");
	
								//raise
								System.out.println("|+ raise	       |");
							}
	
							//fold
							System.out.println("|+ fold	       	   |");
	
							//all in
							System.out.println("|+ allIn	       |");
						}
						System.out.print("[" + name + "] chooses: ");
						String s = sc.next();
						System.out.println("+----------------------+");
						switch(s) {
							case "call": choice = 0;
							break;
							case "raise": choice = 1;
								System.out.println("raise by: ");
								int input = sc.nextInt();
								value = value + input;
							break;
							case "fold": choice = 2;
							break;
							case "allIn": choice = 3;
							break;
							case "check": choice = 4;
							break;
							case "bet": choice = 5;
								System.out.println("you bet: ");
								value = sc.nextInt();
							break;
							default:;
						}
					
					
				}
				//BOT
				else {
					double [] chance = getChance();
					int allIn = (int)(Math.random()*100);
					choice = (int)(Math.random()*5);
					switch(ai) {
						//level 1: random
						case 1:
							if(choice == 1) {//raise
								value = (int)(Math.random() * 25);
							}else if(choice == 5) {//bet
								value = (int)(Math.random() * 10);
							}
						break;
						//level 2: normal (when chance >= ...)
						case 2:
							//choice = choice * chance[0] * chance[1];
						break;
						//level 3: 
						case 3:
							
						break;
						//level 4: OP(sees every thing, can bluff)
						case 4:
							
						break;
					}
				}
				
				//get choice details catch false input
				switch(choice) {
					//call
					case 0:
						if(p != null && p.getBet() <= getAssets() && !mid.hasPot()) {
							if(call(value) != -1) {
								return 0;
							}
						}else {
							
						}
					break;
					//raise
					case 1:
						if(p != null && p.getBet() <= getAssets() && !mid.hasPot()) {
							if(raise(value) != -1) {
								return 1;
							}
						}
					break;
					//fold
					case 2:
						choice = 2;
						if(mid.getRound() != 1 || p != null) {
							System.out.println(" ! "+getName() + " decided to fold.");
							setFold(true);
							return 2;
						}
					//allIn
					case 3:
						return allIn();
					//check
					case 4:
						if(p == null && mid.getRound() != 1) {
							System.out.println(" ! "+getName() + " decided to check.");
							return 4;
						}
					break;
					//bet
					case 5:
						if(p == null) {
							if(value < 0) {}
							//all in
							else if(value > getAssets()) {
								return allIn();
							}
							//normal bet
							else {
								System.out.println(" ! "+getName() + " decided to bet: " + value);
								setAssets(getAssets() - value);
								setBet(value);
								return 5;
							}
						}
					break;
					default:;
				}
			}
			return -1;
		}
	//BEGIN help methods [decide]
	private int call(int value) {
		System.out.println(" ! "+getName() + " decided to call.");
		setAssets(getAssets() - (value - getBet()));
		setBet(value);
		return 0;
	}
	private int raise(int raiseBy) {
			//if raise is lower than the bet player has to choose again
			if(raiseBy < 0) {
				return -1;
			}
			//all in
			else if(raiseBy > getAssets()) {
				return allIn();
			}
			//normal raise
			else {
				System.out.println(" ! "+getName() + " decided to raise to: " + (getBet() + raiseBy));
				setBet( getBet() + raiseBy);
				setAssets(getAssets() - raiseBy);
				return 1;
			}

	}
	private int allIn() {
		System.out.println(" ! "+getName() + " decided to go all in.");
		setAssets(getBet() + getAssets());
		setBet(0);
		setAllIn(true);
		if(!mid.hasPot()) {
			Pot pot = new Pot(getAssets());
			pot.addPlayer(this);
			mid.addPot(pot);
			return 3;
		}else {
			int i = 0;
			while(mid.getPot(i) != null) {
				if(mid.getPot(i).getBetHight() > getAssets()) {
					mid.getPot(i).addPlayer(this);
				}else {
					mid.getPot(i).addPlayer(this);
					return 3;
				}
				i++;
			}
			if(getAssets() > 0) {
				Pot pot = new Pot(getAssets());
				pot.addPlayer(this);
				mid.addPot(pot);
			}
			return 3;
		}
	}
	//END help methods [decide]
	
	public long checkCombo(Mid middle) {

		/*
		 * Combinations (ranked from best to worst):
		 *
		 * Straight Flush (+Royal Flush) -	90.000.000.000 + [value of highest straight card] *100.000.000
		 * Four of a Kind (quad) - 		80.000.000.000 + [value of quad card] 		  *100.000.000 + [value of highest non-quad card]
		 * Full House - 			70.000.000.000 + [value of highest triple card]	  *100.000.000 + [value of highest non-triple pair card] *1.000.000
		 * Flush - 				60.000.000.000 + [value of highest flush card]	  *100.000.000 + [value of second highest flush card]	 *1.000.000 + ...
		 * Straight - 				50.000.000.000 + [value of highest straight card] *100.000.000
		 * Three of a Kind (triple) - 		40.000.000.000 + [value of triple card]		  *100.000.000 + [value of highest non-triple card]	 *1.000.000
		 * Double Pair - 			30.000.000.000 + [value of highest pair card] 	  *100.000.000 + [value of second highest pair card] 	 *1.000.000 + [value of highest non-pair card] *10.000
		 * Pair - 				20.000.000.000 + [value of pair card]		  *100.000.000 + [value of highest non-pair card]	 *1.000.000 + ... 					+ [value of third highest non-pair card]*100
		 * High Card - 				10.000.000.000 + [value of High Card]		  *100.000.000 + [value of second highest card]		 *1.000.000 + ...
		 *
		 */
		boolean straight_flush = false;
		boolean quad = false;
		boolean flush = false;
		boolean straight = false;
		boolean triple = false;
		boolean double_pair = false;
		boolean pair = false;


		/*
		 * Preparing input data
		 */
		int comboLength = middle.getCards().length + hand.length;	//Amount of cards which can be used to create a combination

		// If cards have not been turned yet (no combo possible)
		if(middle.getCards() == null) {
			return 0;
		}

		// Create Combo out of mid & hand cards and sorts them according to their value
		int index = 0;
		Card[] combo = new Card[comboLength];
		for(int i = 0; i<middle.getCards().length; i++) {
			combo[index] = middle.getCards()[i];
			index++;
		}
		for(int i = 0; i<hand.length; i++) {
			combo[index] = hand[i];
			index++;
		}
		Arrays.sort(combo);


		/*
		 * Checking for combinations
		 */
		int[][] color_card_indices = new int[4][comboLength];	//1-Dimension specifies the colors; 2-Dimension saves the indices of the accordingly colored cards
		int[] colors = new int[4];	//Saves the amount of cards belonging to a color
		int flush_color = 0;

		int[] straight_high_cards = new int[comboLength-4];   //Used to save the indices of high cards of possible straights; important to later check for straight flushs which high card is lower than the high card of the highest straight
		int index_straight_high_cards = 0;	//Used as index for straight_high_cards
		int straight_length = 1;	//Used to store the length of the array of consecutively lower (straight) cards

		int[] pair_indices = new int[comboLength/2];	//Used to store the indices of pair cards
		int pair_index = 0;		//Used as index for pair_indices
		int triple_index = 0;	//Used to store the index of a triple card; While two triples are possible in a deck of 7 cards, unlike with pairs, the highest triple is always required for the strongest combo (Three of a Kind & Full House)
		int quad_index = 0;		//Used to store the index of quad cards
		int same_kind_length = 1;	//Used to save the number of cards with the same value


		for(int i = 0; i<combo.length; i++) {
			//Gather information to check for (straight) flush
			color_card_indices[combo[i].getColor()][colors[combo[i].getColor()]] = i;
			colors[combo[i].getColor()]++;
			if(colors[combo[i].getColor()] >= 5 && !flush) {
				flush = true;
				flush_color = combo[i].getColor();
				if(comboLength == 6) {
					color_card_indices[flush_color][5] = -1;
				} else if(comboLength == 7) {
					color_card_indices[flush_color][6] = -1;
				}
			}

			/*
			 * Gather information to check for straight:
			 *
			 * [1] If the element at the next positions (if exists) has a value one lower than the current element
			 * OR if the last element is the card "2" and an ace exists (always at position 0 if existing),
			 * the straight length increases by one. If the straight length is equal to 5 a valid straight is found which high card's index
			 * (found 4 elements backwards) is saved in the array straight_high_cards leaving additional room for further straights.
			 *
			 * [2] If the element at the next position has the same value as the current element, the straight will neither be aborted nor prolonged.
			 *
			 * [3] If none of the above conditions are met, the straight length is reduced to 1.
			 *
			 * Information for Pairs, Triples and Quads:
			 *
			 * Condition 2 can also be used to determine the indices for the above named combinations. If the condition is met,
			 * same_kind_length increments and it is checked the condition for the combination is tested. Furthermore, condition 1 and 2
			 * must be used to reset same_kind_length to 1.
			 */
			try {
				if((combo[i].getValue() == (combo[i+1].getValue()+1) && (i+1) < combo.length )||(combo[i].getValue() == 0 && i == (combo.length-1) && combo[0].getValue() == 12)) {
					straight_length++;
					same_kind_length = 1;
					if(straight_length == 5) {
						straight = true;
						straight_high_cards[index_straight_high_cards] = i-3;
						index_straight_high_cards++;
						straight_length--;
					}
				} else if(combo[i].getValue() == combo[i+1].getValue() && (i+1) < combo.length) {
					same_kind_length++;
					if(same_kind_length == 2) {
						pair_indices[pair_index] = i;
						pair = true;
						if(pair_index == 1) {
							double_pair = true;
						}
						pair_index++;
					} else if(same_kind_length == 3) {
						triple_index = i;
						triple = true;
					} else if(same_kind_length == 4) {
						quad_index = i;
						quad = true;
					}
				}
				else {
					straight_length = 1;
					same_kind_length = 1;
				}
			}catch(ArrayIndexOutOfBoundsException e) {}
		}


		/*
		 * Ranking the combinations
		 */
		//Check for straight flush
		int straight_flush_card;
		if(flush && straight) {
			for(int i = 0; i<straight_high_cards.length; i++) {
				straight_flush = true;
				straight_flush_card = straight_high_cards[i];
				for(int j = 0; j<5; j++) {
					if(Arrays.binarySearch(color_card_indices[flush_color], straight_high_cards[i]+j) == -1) {
						straight_flush = false;
						break;
					}
				}
				if(straight_flush) {
					return 90000000000L + (long)(combo[straight_flush_card].getValue())*100000000L;
				}
			}
		}
		//Check for Quad
		if(quad) {
			for(int i = 0; i<combo.length; i++) {
				if(i != quad_index) {
					return 80000000000L + (long)(combo[quad_index].getValue())*100000000L + (long)(combo[i].getValue()*1000000L);
				}
			}
		}
		//Check for Full House
		if(triple && double_pair) {
			return 70000000000L + (long)(combo[triple_index].getValue())*100000000L + (long)(combo[pair_indices[1]].getValue()*1000000L);
		}
		//Flush
		if(flush) {
			return 60000000000L + (long)(combo[color_card_indices[flush_color][0]].getValue())*100000000L + (long)(combo[color_card_indices[flush_color][1]].getValue())*1000000L + (long)(combo[color_card_indices[flush_color][2]].getValue()*10000L) + (long)(combo[color_card_indices[flush_color][3]].getValue()*100L) + (long)(combo[color_card_indices[flush_color][4]].getValue()*1L);
		}
		//Straight
		if(straight) {
			return 50000000000L + (long)(combo[straight_high_cards[0]].getValue())*100000000L;
		}
		//Triple
		if(triple) {
			long value = 40000000000L + (long)(combo[triple_index].getValue())*100000000L;
			long multiplicator = 1000000L;
			for(int i = 0; i<comboLength; i++) {
				if(i == triple_index) {
					i += 2;
					continue;
				}
				value += (long)(combo[i].getValue())*multiplicator;
				multiplicator /= 100;
				if(multiplicator == 100L) {
					return value;
				}
			}
		}
		//Double-Pair
		if(double_pair) {
			long value =  30000000000L + (long)(combo[pair_indices[0]].getValue())*100000000L + (long)(combo[pair_indices[1]].getValue())*1000000L;
			long multiplicator = 1000000L;
			for(int i = 0; i<comboLength; i++) {
				if(i == pair_indices[0] || i == pair_indices[1]) {
					i += 1;
					continue;
				}
				return value + (long)(combo[i].getValue())*10000L;
			}
		}
		//Pair
		if(pair) {
			long value = 20000000000L + (long)(combo[pair_indices[0]].getValue())*100000000L;
			long multiplicator = 1000000L;
			for(int i = 0; i<comboLength; i++) {
				if(i == pair_indices[0]) {
					i += 1;
					continue;
				}
				value += (long)(combo[i].getValue())*multiplicator;
				multiplicator /= 100;
				if(multiplicator == 100L) {
					return value;
				}
			}
		}
		//High Card
		else {
			return 10000000000L + (long)(combo[0].getValue())*100000000L + (long)(combo[1].getValue())*1000000L + (long)(combo[2].getValue()*10000L) + (long)(combo[3].getValue()*100L) + (long)(combo[4].getValue()*1L);
		}

		//This case should not occurr
		return -1L;
	}

	//Outputs a double array containing the probability for win, tie or lose for an incomplete combination considering what the opponent might have
	public double[] getChance() {
		// !!! Note that this function assumes that the middle contains 5 community cards and that every player has 2 hand cards (Texas Hold'em) !!!
		double[] chance = new double[3];
		int[] wins_ties_loses = new int[3];
		int simulations = 10000;
		int players = 2;


		/*
		 * Prepare the simulation
		 */
		//Create a deck not including the hand and mid cards
		Card[] exceptions = new Card[hand.length+mid.numberOfVisibleCards()];
		int exception_index = 0;
		for(int i = 0; i<mid.numberOfVisibleCards(); i++) {
			exceptions[exception_index] = mid.getCards()[i];
			exception_index++;
		}
		for(int i = 0; i<hand.length; i++) {
			exceptions[exception_index] = hand[i];
			exception_index++;
		}
		Card[] deck = Game.createDeck(exceptions);

		//Create an array of the community cards (mid cards) leaving room for simulated cards from deck at the last indices of the array
		Card[] simulated_middle_cards = new Card[5];
		for(int i = 0; i<mid.numberOfVisibleCards(); i++) {
			simulated_middle_cards[i] = exceptions[i];
		}
		Mid simulated_mid = new Mid();
		simulated_mid.setCards(simulated_middle_cards);

		//Create an opponent with simualted hand
		Player simulated_opponent = new Player("bob",0,mid);
		Card[] opponents_cards = new Card[2];


		/*
		 * Starts the simulation
		 */
		int deck_index = 0;
		for(int i = 0; i<simulations; i++) {

			Game.shuffle(deck);
			//Check if players hand cards need to be filled
			if(hand == null) {
				hand = new Card[2];
				for(int j = 0; j<2; j++) {
					hand[j] = deck[deck_index];
					deck_index++;
				}
			}

			//Creates a possible hand for the opponent
			for(int j = 0; j<2; j++) {
				opponents_cards[j] = deck[deck_index];
				deck_index++;
			}
			simulated_opponent.setHand(opponents_cards);

			//Fills the simulated middle
			for(int j = mid.numberOfVisibleCards(); j<5; j++) {
				simulated_middle_cards[j] = deck[deck_index];
				deck_index++;
			}
			simulated_mid.setCards(simulated_middle_cards);

			//Uncomment for DEBUG
			/*
			System.out.println("Your cards: ");
			for(int j = 0; j<hand.length;j++) {
				System.out.println(hand[j]);
			}
			System.out.println("Opponents cards: ");
			for(int j = 0; j<simulated_opponent.getHand().length;j++) {
				System.out.println(simulated_opponent.getHand()[j]);
			}
			System.out.println("Mid cards: ");
			for(int j = 0; j<simulated_mid.getCards().length;j++) {
				System.out.println(simulated_mid.getCards()[j]);
			}
			System.out.println("You have a "+checkCombo(simulated_mid)+" & the opponent has a "+simulated_opponent.checkCombo(simulated_mid));
			*/

			//Compare both outcomes
			if(checkCombo(simulated_mid) > simulated_opponent.checkCombo(simulated_mid)) {
				wins_ties_loses[0]++;
			} else if(checkCombo(simulated_mid) == simulated_opponent.checkCombo(simulated_mid)) {
				wins_ties_loses[1]++;
			} else {
				wins_ties_loses[2]++;
			}

			//Reset variables
			deck_index = 0;
		}

		//Print out probabilities
		/*
		System.out.println((double)(6000)/10000);
		chance[0] = (double)(wins_ties_loses[0])/10000;
		chance[1] = (double)(wins_ties_loses[1])/10000;
		chance[2] = (double)(wins_ties_loses[2])/10000;
		System.out.println("probability of winning: "+chance[0]);
		System.out.println("probability of a tie: "+chance[1]);
		System.out.println("probability of losing: "+chance[2]);
		System.out.println("Sum: "+(chance[0]+chance[1]+chance[2]));
		*/

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
