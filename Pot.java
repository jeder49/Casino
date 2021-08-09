package Poker;

import java.util.ArrayList;
import java.util.List;

public class Pot {
	private int amountMoney;
	private int betHight;
	private List<Player> player = new ArrayList<Player>();
	public Pot(int betHight) {
		setBetHight(betHight);
	}
	public void addPlayer(Player player) {
		
		//
		this.player.add(player);
		
		//
		if(player.getAssets() - betHight < 0) {
			player.setAssets(0);
		}else {
			player.setAssets(player.getAssets() - betHight);
		}
	}
	public Player getPlayer(int i) {
		return player.get(i);
	}
	public int getAmountMoney() {
		return amountMoney;
	}
	public void setAmountMoney(int amountMoney) {
		this.amountMoney = amountMoney;
	}
	public int getBetHight() {
		return betHight;
	}
	public void setBetHight(int betHight) {
		this.betHight = betHight;
	}
}
