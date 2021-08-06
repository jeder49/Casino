package Poker;

public class Game {
	
	private Card[] deck;
	private Player[] player;
	private int round;
	private Mid mid;
	
	public Game(int numPlayer, int bots, int numMid, int numHand, int assets, int dificulty[]) {
		//while(a nother game){
			mid = new Mid();
		
			createPlayer(numPlayer, bots, assets, dificulty);
		
			deck = createDeck(null);
		
			shuffle(deck);
		
			deal(numMid, numHand);
		
			for(int i = 0; i < this.mid.getCards().length; i++) {
				Card[] mid = this.mid.getCards();
				System.out.println("mid " + i + ": " + mid[i].toString());
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
		
			//win();
		//}
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
	private void createPlayer(int numPlayer, int bots, int assets, int dificulty[]) {
		//set random position for bots
		int[] botPosition = new int[bots];
		for(int i = 0; i < bots; i++) {
			botPosition[i] = (int) (Math.random()*(numPlayer-1));
		}
		
		player = new Player[numPlayer];
		
		//goes through player list
		for(int i = 0; i < numPlayer; i++) {
			
			//looks if position is taken by bot
			int dificultyBot = 0;
			int indexBot = 0;
			for(int f = 0; f < bots; f++) {
				if(botPosition[f] == i) {
					dificultyBot = dificulty[indexBot];
					indexBot++;
				}
			}
			
			//creates player
			player[i] = new Player(dificultyBot,mid);
			player[i].setAssets(assets);
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
	
	//
	private void round(int numMid){
		
		//set role
		
		//get small blind
		int index = getPlayerByrole(1);
		
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
		
		//first bets
		if(round == 1) {
			
			//bet small blind
			player[getPlayerByrole(1)].setBet(1);
			
			//bet big blind
			player[getPlayerByrole(2)].setBet(2);
		}
		
		//recursion ends when...
		//...last man standing
		if(howManyleft() == 1) {
			round++;
			round(numMid);
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
		
		//turn cards
		if(round == 2) {
			mid.setVisible(0, true);
			mid.setVisible(1, true);
			mid.setVisible(2, true);
		}if(round > 2) {
			mid.setVisible(round, true);
		}
		System.out.println(sameBetHight());
		//till all bets are the same height
		while(!sameBetHight() && howManyleft() != 1) {
			
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
	private int getPlayerByrole(int role){
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
		return 0;
	}


	public int getRound() {
		return round;
	}


	public void setRound(int round) {
		this.round = round;
	}
}
