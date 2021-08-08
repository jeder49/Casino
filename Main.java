/**
 * 
 */
package Poker;

/**
 * @author Frei
 *
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Person[] p = new Person[1];
		p[0] = new Person("bob");
		p[0].setAssets(2000);
		p[0].setBet(350);
		int[] dificulty = new int[1];
		dificulty[0] = 1;
		new Game(p,2,1,5,2,375,dificulty);
	}

}
