package Poker;

public class Mid {
	private Card[] cards;
	private boolean[] visible;
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
		return cards;
	}
	public void setCards(Card[] cards) {
		this.cards = cards;
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

}
