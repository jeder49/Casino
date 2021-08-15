package Poker;

import java.util.ArrayList;
import java.util.List;

public class Mid {
	private Card[] card;
	private boolean[] visible;
	private List<Pot> pot = new ArrayList<Pot>();
	private Game game;

	public Mid() {
	}

	public int numberOfVisibleCards() {
		int visible_cards = 0;
		for(int i = 0; i<visible.length; i++) {
			if(visible[i]) {
				visible_cards++;
			}
		}
		return visible_cards;
	}
	public Card[] getCards() {
		return card;
	}
	public void setCards(Card[] card) {
		this.card = card;
	}
	public boolean[] isVisible() {
		return visible;
	}
	public void setVisible(boolean[] visible) {
		this.visible = visible;
	}
	public void setVisible(int index, boolean visible) {
		this.visible[index] = visible;
	}
	public Pot getPot(int index) {
		return pot.get(index);
	}
	public void addPot(Pot pot) {
		this.pot.add(pot);
	}
	public boolean hasPot() {
		if(pot == null) {
			return false;
		}
		boolean hasPot = !pot.isEmpty();
		return hasPot;
	}
	public Player[] getPlayer() {
		return game.getPlayer();
	}

	public int getRound() {
		return game.getRound();
	}

	public void setRound(int round) {
		game.setRound(round);
	}
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
}
