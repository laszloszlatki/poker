/*
 * @author: Laszlo Szlatki
 * @date: 05/Mar/2020
 * */
package pokerfx;

import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class Deck {
/*
 * An object of the deck class represents all 52 cards in a
 * standard deck of playing cards. It can be shuffled or ordered.
 */
	// create a stack of cards
	private Stack<Card> deck = new Stack<Card>();

	// constructor for a deck of cards in order
	public Deck() {
		for (Suits suit : Suits.values()) {
			for (Ranks rank : Ranks.values()) {
				deck.add(new Card(suit, rank));
			}
		}
	}

	/*
	 * constructor for a deck of cards shuffled
	 * 
	 * @param: number of times the deck to be shuffled
	 */
	public Deck(int shuffle) {
		this();
		for (int i = 0; i < shuffle; i++) {
			Collections.shuffle(deck, new Random());
		}
	}

	/*
	 * shuffle deck
	 */
	public void shuffle() {
		Collections.shuffle(this.deck, new Random());
	}

	/**
	 * to reset used deck to a full one again creates and ordered deck, need to call
	 * the shuffle() on it after
	 */
	public void reset() {
		// empty current cards from deck
		deck.clear();
		// fill in again with all 52 cards
		for (Suits suit : Suits.values()) {
			for (Ranks rank : Ranks.values()) {
				deck.add(new Card(suit, rank));
			}
		}
	}

	/*
	 * draw top most card off the deck
	 * 
	 * @return: returns the top card off the deck (stack)
	 */
	public Card draw() {
		if (deck.size() <= 0) {

			throw new EmptyDeckException();
		}
		return deck.pop();
	}

	/*
	 * get deck size
	 * 
	 * @return: number of cards left in the deck
	 */
	public int size() {
		return deck.size();
	}

	// return the cards in the deck
	@Override
	public String toString() {
		String deckOfCards = "";
		for (Card c : deck) {
			deckOfCards += (c + "\n");
		}
		return deckOfCards;
	}

}