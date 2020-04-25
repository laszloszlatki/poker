/*
 * @author: Laszlo Szlatki
 * @date: 14/Mar/2020
 * */
package pokerfx;

/*
 *  High card. If no combination can be made, then a player�s hand is valued at the highest single card. If two players have the same high card, then the second highest card would break the tie.
    
    One Pair. A pair is formed when you have two of any of the same ranks.
    
    Two Pairs. When more than one player has two pairs, the player with the highest pair wins.
    
    Three of a Kind. 3 of the same ranks.
  
    Straight. A straight is a five-card hand consisting of a running sequence of cards, regardless of suit. If two players have straights, the straight of the higher card wins.

    Flush. When all five cards in a hand are of the same suit, it is a flush. If two players have a flush, the person with the highest card in that suit wins.

    Full House. When a player has three-of-a-kind and a pair in the same hand, it is called a Full House.

    Four of a Kind. If you are lucky enough to have all four of a given number, then you have a very powerful hand.

    Straight Flush. Even rarer than four of a kind, a straight flush is made up of five consecutive cards, all from the same suit.
    
    Royal Flush. The best hand of them all is this famous combination, formed by a Straight Flush that runs to the Ace, making it unbeatable. Odds of being dealt this hand can be as high as 1 in 650,000 deals.
    */

public class Rules {

	/*
	 * method to decide on winner of the 2 hands according to rules above
	 * 
	 * @param: one of the hands to be compared
	 * 
	 * @param: the other hand to be compared
	 * 
	 * @return the winning hand
	 */
	public static Hand checkWinner(Hand myHand, Hand yourHand) {

		Hand winner = null;
		Strengths myStrength = myHand.handStrength(myHand.getHand());
		Strengths yourStrength = yourHand.handStrength(yourHand.getHand());

		// strength ranked higher in the enum wins - checked by the ordinal()
		if (myStrength.ordinal() > yourStrength.ordinal()) {
			winner = myHand;
		} else if (myStrength.ordinal() < yourStrength.ordinal()) {
			winner = yourHand;
			// if equal strengths, check highest card rank
		} else {
			if (myHand.highestCard.ordinal() > yourHand.highestCard.ordinal()) {
				winner = myHand;
			} else if (myHand.highestCard.ordinal() < yourHand.highestCard.ordinal()) {
				winner = yourHand;
				// if highestRank is equal too, check for second highest card rank
			} else {
				if (myHand.secondHighestCard.ordinal() > yourHand.secondHighestCard.ordinal()) {
					winner = myHand;
				} else if (myHand.secondHighestCard.ordinal() < yourHand.secondHighestCard.ordinal()) {
					winner = yourHand;
					// else, draw
				} else {
					winner = null;
				}
			}
		}
		return winner;
	}
	
	/**
	 * Check if all participants have enough token to continue game
	 * @param participants vararg holds all participants of the game
	 * @return false, if one player is out of tokens, true otherwise
	 */
	public static boolean hasMoreToken(Participant... participants) {
		boolean haveToken = true;
		if(Participant.getPot() >= 0) {
			for(Participant p : participants) {
				if (p.getToken() <= 0) {
					System.out.println(p + " out of token!");
					return false;
				}
			}
		}
		return haveToken;
	}
}
