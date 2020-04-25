/*
 * @author: Laszlo Szlatki
 * @date: 27/Mar/2020
 * */
package pokerfx;

import java.util.List;

/**
 * abstract Participant class contains methods and attributes, that are used by
 * both dealer and player
 */
public abstract class Participant {

	private Hand participant;

	/**
	 * to receive player/dealer hand for further manipulation
	 * 
	 * @param palyer or dealer hand
	 */
	public void setChildHandToParticipant(Hand hand) {
		participant = hand;
	}

	private int token = 0;
	private static int pot = 0;
	private int roundsWon = 0;

	/**
	 * to get the amount of tokens left for each player
	 * 
	 * @return
	 */
	public int getToken() {
		return token;
	}

	/**
	 * to set the amount of tokens left for each player called form player and
	 * dealer constructors
	 * 
	 * @param amount of token given initially
	 */
	public void setToken(int token) {
		this.token = token;
	}

	/**
	 * to spend one or more tokens used for the initial entry 1 token and the
	 * betting (0-3 tokens) later
	 * 
	 * @param the amount of tokens to be spent
	 * @return the amount of tokens discounted
	 */
	public int spendToken(int token) {
		this.token -= token;
		addToPot(token);
		return token;
	}

	/**
	 * credits the contents of the pot to the participant's tokens
	 */
	public void winToken() {
		this.token += emptyPot();
	}

	/**
	 * to check the contents of the pot
	 * 
	 * @return the amount of tokens currently in the pot
	 */
	public static int getPot() {
		return pot;
	}

	/**
	 * method to empty pot when game is won
	 * 
	 * @return the initial content of the pot
	 */
	private static int emptyPot() {
		int temp = pot;
		pot = 0;
		return temp;
	}

	/**
	 * to add tokens to the pot used when game opened and when user bet placed /
	 * dealer bet matched
	 * 
	 * @param pot
	 */
	private static void addToPot(int pot) {
		Participant.pot += pot;
	}

	/**
	 * to increase the won games counter
	 */
	public void addRoundsWon() {
		roundsWon++;
	}

	/**
	 * to get the number of games won counter
	 * 
	 * @return current state of counter
	 */
	public int getRoundsWon() {
		return roundsWon;
	}

	/**
	 * Check dealer's hand strength and decide if dealer can open (to open, dealer
	 * has to have minimum a pair of Jacks)
	 * 
	 * @return canOpen: true if at least pair of Jacks, false otherwise
	 */
	public boolean canOpen(Hand hand) {
		boolean canOpen = false;
		Strengths handStrength = hand.handStrength(hand.getHand());
		if (handStrength.ordinal() > Strengths.PAIR.ordinal()) {
			canOpen = true;
		} else if (handStrength.ordinal() == Strengths.PAIR.ordinal()
				&& hand.highestCard.ordinal() >= Ranks.JACK.ordinal()) {
			canOpen = true;
		}
		return canOpen;
	}

	/**
	 * Helper method to get a new card from the deck
	 * 
	 * @param deck used for the game
	 * @return the new card drawn from the deck
	 */
	private Card getNewCard(Deck deck) {

		return deck.draw();
	}

	/**
	 * method to get hand strength
	 * 
	 * @param hand: hand of cards to be checked
	 * @return strength of given hand
	 */
	public Strengths checkStrength(Hand hand) {
		return hand.handStrength(hand.getHand());
	}

	/**
	 * method to check hand Strength after cards have been changed
	 * 
	 * @param hand
	 * @return
	 */
	public Strengths newStrength(Hand hand) {
		// sort hand with new cards using the helper method
		hand.sortHand(hand.getHand());
		// check for strength after changing some cards
		return checkStrength(participant);
	}

	/**
	 * method returning the participant's hand as a list of card objects
	 * 
	 * @return list of card objects in the participant's hand
	 */
	public List<Card> getCards() {
		return (participant.getHand());
	}

	/**
	 * returns a particular participant's hand
	 * 
	 * @return return hand of cards
	 */
	public Hand getHand() {
		return participant;
	}

	/**
	 * abstract method to count how many cards to be replaced
	 * 
	 * @return number of cards to be replaced
	 */
	public abstract int toBeReplaced();

	/**
	 * to empty all cards form hand
	 */
	public void throwAllCards() {
		for (int i = 0; i < 5; i++) {
			participant.getHand().remove(participant.getHand().get(0));
		}
	}

	/**
	 * logic to swap cards
	 * 
	 * @param deck:            deck used for the game
	 * @param numberToReplace: int number of cards to be replaced
	 */
	public void swapCards(Deck deck, int numberToReplace) {
		for (int i = 0; i < numberToReplace; i++) {

			// if deck is empty, create new one
			if (deck.size() == 0) {
				deck = new Deck(5);
			}
			// deal required number of extra cards
			participant.getHand().add(deck.draw());
		}
	}
}