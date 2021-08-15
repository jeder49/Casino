package Poker;

import java.util.Arrays;

public class Game {

	private Card[] deck;
	private Player[] player;
	private int round;
	private Mid mid;
	private int lastRaised;

	public Game(Person[] Player, int numPlayer, int bots, int numMid, int numHand, int assets, int dificulty[]) {
		//while(a nother game){
			//1. arrangements
			mid = new Mid();
			mid.setGame(this);

			createPlayer(Player, numPlayer, bots, assets, dificulty);
						
			deck = createDeck(null);

			shuffle(deck);

			deal(numMid, numHand);

			//2. shows the cards in the mid
			for(int i = 0; i < this.mid.getCards().length; i++) {
				Card[] mid = this.mid.getCards();
				System.out.println("mid " + i + ": " + mid[i].toString());
			}

			//shows all hands
			for(int i = 0; i < player.length; i++) {
				String s = "";
				Card[] hand = player[i].getHand();
				for(int f = 0; f < player[i].getHand().length; f++) {
					if(hand[f] == null) {
						s = s + null;
					}else {
						s = s + hand[f].toString() + " ; ";
					}
				}
				System.out.println("Player " + i + " : " + player[i].getName() + " : " + s);
			}

			System.out.println("######################| Game starts |######################");

			//3. starts game
			setRound(1);
			round(numMid);

			//4. show cards
			//the player who raised last
			String s = "";
			Card[] hand;
			int f = lastRaised;
			do {
				
				if(!player[lastRaised].hasFolded()) {
					hand = player[lastRaised].getHand();
				}else {
					hand = player[lastRaised+1].getHand();
				}
				
				if((f + 1) < player.length) {
					f++;
				}else {
					f = 0;
				}
			}while(f != lastRaised);

			for(f = 0; f < player[lastRaised].getHand().length; f++) {
				if(hand[f] == null) {
					s = s + null;
				}else {
					s = s + hand[f].toString() + " ; ";
				}
			}

			System.out.println("Player " + lastRaised + " : " + player[lastRaised].getName() + " : " + s);

			//the rest
			for(int i = 0; i < player.length; i++) {
				if(i != lastRaised) {
					if(player[i].showCards()) {
						s = "";
						hand = player[i].getHand();
						for(int f = 0; f < player[i].getHand().length; f++) {
							if(hand[f] == null) {
								s = s + null;
							}else {
								s = s + hand[f].toString() + " ; ";
							}
						}
						System.out.println("Player " + i + " : " + player[i].getName() + " : " + s);
					}
				}
			}

			//5. presents winner
			System.out.println("Player " + player[win()].getName() +" won the game!");
		//}
	}


	//creates a deck
		public static Card[] createDeck(Card[] exceptions) {
			//Creates the array to be filled
			Card[] deck;
			//Specifies the length according to the amount of exceptions(cards that should not be in the deck)
			if(exceptions == null) {
				deck = new Card[52];
			} else {
				deck = new Card[52-exceptions.length];
				Arrays.sort(exceptions);
			}

			//Fills the deck from highest to lowest value while checking for exceptions
			int index = 0;
			int exception_index = 0;

			for(int i = 12; i>=0; i--) {
				for(int j = 3; j>=0; j--) {
					try {
						if(i == exceptions[exception_index].getValue() && j == exceptions[exception_index].getColor()) {
							exception_index++;
						} else {
							deck[index] = new Card(i, j, i+"."+j);
							index++;
						}
					} catch(Exception e) {
						deck[index] = new Card(i, j, i+"."+j);
						index++;
					}
				}
			}
			return deck;
		}


	//shuffles the cards of the deck
	public static void shuffle(Card[] deck) {
		for(int i = 0; i<deck.length; i++) {
			int randomPosition = (int)(Math.random()*deck.length);
			Card c1 = deck[i];
			Card c2 = deck[randomPosition];
			deck[i] = c2;
			deck[randomPosition] = c1;
		}
	}


	//
	private void createPlayer(Person[] Player,int numPlayer, int bots, int assets, int dificulty[]) {
		//add bots to player list
		player = new Player[numPlayer];
		for(int i = 0; i < bots; i++) {
			player[i] = new Player(("AI"+i),dificulty[i],mid);
		}

		//add people to player list
		for(int i = 0; i < (numPlayer - bots); i++) {
			player[i+bots] = new Player(Player[i].getName(),0,mid);
			player[i+bots].setAssets(Player[i].getBet());
		}

		//set new random position for Player
		for(int i = 0; i < player.length; i++) {
			int randomPosition = (int)(Math.random() * (player.length - 1));
			Player p1 = player[i];
			Player p2 = player[randomPosition];
			player[i] = p2;
			player[randomPosition] = p1;
		}
	}


	//deal the cards
	private void deal(int numMid, int numHand) {
		Card[] mid = new Card[numMid];
		boolean[] visible = new boolean[numMid];
		for(int i = 0; i < mid.length; i++) {
			mid[i] = getTopCard();
			visible[i] = false;
		}
		this.mid.setVisible(visible);
		this.mid.setCards(mid);

		for(int i = 0; i < player.length; i++) {
			Card[] hand = new Card[numHand];

			//draw cards for the hand
			for(int f = 0; f < numHand; f++) {
				hand[f] = getTopCard();
			}

			player[i].setHand(hand);
		}
	}


	//get card top card of the deck and deletes it
	private Card getTopCard() {
		int i = deck.length-1;
		while(deck[i] == null) {
			i--;
		}
		Card card = deck[i];
		deck[i] = null;
		return card;
	}

	public void changePositionPlayer(){
		//to do
	}

	private void round(int numMid){
			
		//1. recursion ends when...
		
		//...all cards are open
		if(round == numMid) {
			//the person who last raisted has to show their cards
			return;
		}

		//...last man standing
		if(howManyleft() == 1) {
			round++;
			round(numMid);
			return;
		}

		//...all in
		if(isAllIn()) {
			round++;
			round(numMid);
			return;
		}
		
		//2. turn cards
		if(round == 2) {
			mid.setVisible(0, true);
			mid.setVisible(1, true);
			mid.setVisible(2, true);
		}if(round > 2) {
			mid.setVisible(round, true);
		}

		//3. show cards
		boolean[] visible = mid.isVisible();
		Card[] c = mid.getCards();
		for(int i = 0; i < mid.getCards().length; i++) {
			if(visible[i] == true) {
				System.out.println(" | " + c[i].toString() + " |");
			}else {
				System.out.println(" | ????-? |");
			}
		}

		//4. set role

		//get small blind if existing
		int index = getPlayerByRole(1);
		int smallBlind = 0;
		int bigBlind = 0;
			
		//if no round was played before new roles are made
		if(index == -1) {
			player[0].setRole(1);
			player[1].setRole(2);
			smallBlind = 0;
			bigBlind = 1;
		}else {
			for(int i = 1; i < 3; i++) {
				if((index+1) > player.length) {
					index++;
					player[index].setRole(i);
				}else {
					index = 0;
					player[index].setRole(i);
				}
				if(i == 1) {
					smallBlind = index;
				}else {
					bigBlind = index;
				}
			}

		}

		//5. first bets (first round)
		if(round == 1) {

			//bet/all in small blind
			player[smallBlind].decide(null);

			//raise/all in/call(if bet >= 2) big blind
			player[bigBlind].decide(player[smallBlind]);
		}

		//index to iterate
		int i;

		//get first player for the round
		if(round == 1) {
			if((bigBlind + 1) >= player.length) {
				i = 0;
			}else {
				i = bigBlind + 1;
			}
			lastRaised = bigBlind;
		}else {
			//index small blind
			i = smallBlind;
			lastRaised = i;
		}


		//till all bets are the same height or only one is left
		while(!sameBetHight() && howManyleft() != 1) {

			//if player is small blind
			if(player[i].getRole() == 1) {
				//6. make turn
				//exception for the first round
				if(round == 1) {
					if(player[i].decide(player[bigBlind]) == 1) {
						lastRaised = i;
					}else if(player[i].decide(player[bigBlind]) == -1) {
						System.out.println("Some thing went wrong!");
					}
				}else {
					if(player[i].decide(null) == 1) {
						lastRaised = i;
					}else if(player[i].decide(player[bigBlind]) == -1) {
						System.out.println("Some thing went wrong!");
					}
				}
				
			}else {
				//6. make turn
				//if index is out of range
				if(i-1 < 0) {
					if(player[i].decide(player[player.length-1]) == 1){
						lastRaised = i;
					}else if(player[i].decide(player[bigBlind]) == -1) {
						System.out.println("Some thing went wrong!");
					}
				}
				else {
					if(player[i].decide(player[i-1]) == 1) {
						lastRaised = i;
					}else if(player[i].decide(player[bigBlind]) == -1) {
						System.out.println("Some thing went wrong!");
					}
				}
				
			}
				
			if((i + 1) < player.length) {
				i++;
			}else {
				i = 0;
			}
		}
			
		System.out.println("------------------------------------------");
			
		//recursion
		round++;
		round(numMid);
	}

	//BEGIN help methods [round]
	//
	private int getPlayerByRole(int role){
		for(int i = 0; i < player.length; i++) {
			if(role == player[i].getRole()) {
				return i;
			}
		}
		return -1;
	}

	private boolean sameBetHight() {
		int max = playerWithHightestBet();
		for(int i = 0; i < player.length; i++) {
			//if the highest bet is not a all in it looks for the how high the bet of each player is. if the highest bet is an all in then it looks if some one doesn't have an all in
			if((!player[max].isAllIn() && player[i].getBet() != max  || (player[max].isAllIn() && !player[i].isAllIn())) && !player[i].hasFolded()) {
				return false;
			}
		}
		return true;
	}

	private int playerWithHightestBet() {
		int max = 0;
		for(int i = 0; i < player.length; i++) {
			if(player[max].getBet() < player[i].getBet() && !player[i].hasFolded() || player[i].isAllIn()) {
				max = i;
			}
		}
		return max;
	}

	private int howManyleft() {
		int num = 0;
		for(int i = 0; i < player.length; i++) {
			if(!player[i].hasFolded()) {
				num++;
			}
		}
		return num;
	}

	private boolean isAllIn() {
		for(int i = 0; i < player.length; i++) {
			if(player[i].isAllIn()) {
				return true;
			}
		}
		return false;
	}
	//END help methods [round]

	//returns winner
	private int win() {
		//get best combination of all player, who showed their cards
		int max = 0;
		for(int i = 0; i < player.length; i++) {
			if(player[i].showedCards() && !player[i].hasFolded() && player[i].checkCombo(mid) > player[max].checkCombo(mid)) {
				max = i;
			}
		}
		return max;
	}

	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public Player[] getPlayer() {
		return player;
	}
}
