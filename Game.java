package Poker;

public class Game {
	
	private Card[] deck;
	private Player[] player;
	private int round;
	private Mid mid;
	
	public Game(int numPlayer,int bots,int numMid,int numHand) {
		
		mid = new Mid();
		
		createPlayer(numPlayer, bots);
		
		deck = createDeck(null);
		
		shuffle(deck);
		
		deal(numMid, numHand);
		
		for(int i = 0; i < player.length; i++) {
			String s = "";
			Card[] mid = this.mid.getCards();
			for(int f = 0; f < this.mid.getCards().length; f++) {
				if(mid[f] == null) {
					s = s + null;
				}else {
					s = s + mid[f].toString() + " ; ";
				}
			}
			System.out.println("mid " + i + ": " + s);
		}
		
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
			System.out.println("Player " + i + ": " + s);
		}
		setRound(1);
		round(numMid);
		
		win();
	}
	
	
	//creates a deck
	public Card[] createDeck(Card[] exceptions) {
		Card[] deck;
		if(exceptions == null) {
			deck = new Card[52];
		} else {
			deck = new Card[52-exceptions.length];
		}
		
		int index = 0;
		for(int i = 0; i<13; i++) {
			for(int j = 0; j<4; j++) {
				deck[index] = new Card(i, j, i+"."+j);
				index++;
			}
		}		
		return deck;
	}
	
	
	//shuffles the cards of the deck
	public void shuffle(Card[] deck) {
		for(int i = 0; i<deck.length; i++) {
			int randomPosition = (int)(Math.random()*52);
			Card c1 = deck[i];
			Card c2 = deck[randomPosition];
			deck[i] = c2;
			deck[randomPosition] = c1;
		}
	}
	
	
	//
	private void createPlayer(int numPlayer, int bots) {
		//set random position for bots
		int[] botPosition = new int[bots];
		for(int i = 0; i < bots; i++) {
			botPosition[i] = (int) (Math.random()*(numPlayer-1));
		}
		
		player = new Player[numPlayer];
		
		//goes through player list
		for(int i = 0; i < numPlayer; i++) {
			
			//looks if position is taken by bot
			boolean b = false;
			for(int f = 0; f < bots; f++) {
				if(botPosition[f] == i) {
					b = true;
				}
			}
			
			//creates player
			player[i] = new Player(b,mid);
		}
	}
	
	
	//deal the cards
	private void deal(int numMid, int numHand) {
		Card[] mid = new Card[numMid];
		for(int i = 0; i < mid.length; i++) {
			mid[i] = getTopCard();
		}
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
	
	
	//
	private void round(int numMid){
		
		//recursion ends when...
		
		//...last man standing
		if(Oneleft()) {
			return;
		}
		
		//...all in
		if(isAllIn()) {
			round++;
			round(numMid);
		}
		
		//...all cards are open
		if(round == numMid) {
			//the person who last raisted has to show their cards 
			return;
		}
		
		//till all bets are the same height
		while(!sameBetHight() && !Oneleft()) {
			//goes to all player
			for(int i = 0; i < player.length; i++) {
				player[i].decide();
			}
		}
		
		//recursion
		round++;
		round(numMid);
	}
	
	//BEGIN help methods [round]
	//
	private boolean sameBetHight() {
		int max = playerWithHightestBet();
		for(int i = 0; i < player.length; i++) {
			if(player[i].getBet() < max && !player[i].hasFolded()) {
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
	
	private boolean Oneleft() {
		return false;
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
		return 0;
	}


	public int getRound() {
		return round;
	}


	public void setRound(int round) {
		this.round = round;
	}
}
