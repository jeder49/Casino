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
			Card[] hand = player[lastRaised].getHand();
			for(int f = 0; f < player[lastRaised].getHand().length; f++) {
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
				} catch(ArrayIndexOutOfBoundsException e) {
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
			int randomPosition = (int)(Math.random()*52);
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
			//turn cards
			if(round == 2) {
				mid.setVisible(0, true);
				mid.setVisible(1, true);
				mid.setVisible(2, true);
			}if(round > 2) {
				mid.setVisible(round, true);
			}

			//show cards
			boolean[] visible = mid.isVisible();
			Card[] c = mid.getCards();
			for(int i = 0; i < mid.getCards().length; i++) {
				if(visible[i] == true) {
					System.out.println(" | " + c[i].toString() + " |");
				}else {
					System.out.println(" | ????-? |");
				}
			}

			//set role

			//get small blind
			int index = getPlayerByRole(1);

			//if no round was played before and new roles has to be made
			if(index == -1) {

				player[0].setRole(1);
				player[1].setRole(2);

			}else {

				for(int i = 1; i < 3; i++) {

					if((index+1) > player.length) {

						index++;
						player[index].setRole(i);

					}else {

						index = 0;
						player[index].setRole(i);

					}

				}

			}

			//first bets (first round)
			if(round == 1) {

				//bet small blind
				player[getPlayerByRole(1)].setBet(1);

				//bet big blind
				player[getPlayerByRole(2)].setBet(2);
			}

			//recursion ends when...

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


			int i;

			//get first player for the round
			if(round == 1) {

				if((getPlayerByRole(2) + 1) >= player.length) {
					i = 0;
				}else {
					i = getPlayerByRole(2) + 1;
				}
				lastRaised = getPlayerByRole(2);

			}else {

				//index small blind
				i = getPlayerByRole(1);
				lastRaised = i;

			}


			//till all bets are the same height or only one is left
			while(!sameBetHight() && howManyleft() != 1) {

				//if player is small blind
				if(player[i].getRole() == 1) {
					if(player[i].decide(null) == 1) {
						lastRaised = i;
					}
				}else {
					//if index is out of range
					if(i-1 < 0) {
						if(player[i].decide(player[player.length-1]) == 1){
							lastRaised = i;
						}

					}
					else {
						if(player[i].decide(player[i-1]) == 1) {
							lastRaised = i;
						}

					}
				}
				if((i + 1) < player.length) {
					i++;
				}else {
					i = 0;
				}
				System.out.println("------------------------------------------");
			}

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
			if(player[i].getBet() != max && !player[i].hasFolded()) {
				return false;
			}
		}
		return true;
	}

	private int playerWithHightestBet() {
		int max = -1;
		int index = 0;
		for(int i = 0; i < player.length; i++) {
			if(max < player[i].getBet() && !player[i].hasFolded()) {
				max = player[i].getBet();
				index = i;
			}
		}
		return index;
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
		int max = lastRaised;
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
}
