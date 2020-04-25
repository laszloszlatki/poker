/*
 * @author: Laszlo Szlatki
 * @date: 27/Mar/2020
 * */
package pokerfx;

import java.util.List;

/**
 * Dealer class holds the computer's hand and logic for it's choices
 */
public class Dealer extends Participant {
	// create computer's hand using the constructor
	private Hand dealer;
	// store highest strength
	private Strengths dealerStrength;
	// store dealers hand as list of cards;
	private List<Card> dealerHand;

	/**
	 * Constructor to create a dealer with a hand of cards
	 *
	 * @param deck: the deck used in the Game
	 */
	public Dealer(Deck deck, int token) {
		super();
		setToken(token);
		dealer = new Hand(deck);
		dealerHand = dealer.getHand();
		dealerStrength = checkStrength(dealer);
		passHandToParticipant();
	}

	/**
	 * to pass hand to participant for further processing
	 */
	public void passHandToParticipant() {
		super.setChildHandToParticipant(dealer);
	}

	/**
	 * Adds the number of cards, dealer needs to replace
	 *
	 * @return number of cards to be replaced
	 */
	@Override
	public int toBeReplaced() {
		// dropping cards from back to front, to ensure indexes stay consistent and
		// avoid IndexOutOfBounds
		// number of cards to be replaced
		int replace = 0;
		dealerStrength = checkStrength(dealer);
		/** decide which card(s) to change */
		// nothing to be replaced, if FULL_HOUSE, STRAIGHT, FLUSH, STRAIGHT_FLUSH,
		// ROYAL_FLUSH

		// 1st or last to be removed if FOUR_OF_A_KIND
		if (dealerStrength.ordinal() == Strengths.FOUR_OF_A_KIND.ordinal()) {
			if ((dealerHand.get(1)).getRank() == (dealerHand.get(2)).getRank()
					&& (dealerHand.get(2)).getRank() == (dealerHand.get(3)).getRank()
					&& ((dealerHand.get(3)).getRank() == (dealerHand.get(4)).getRank())) {
				dealerHand.remove(dealerHand.get(0));
				replace = 1;
			} else {
				dealerHand.remove(dealerHand.get(4));
				replace = 1;
			}
			// 1st two or last two or first+last to be removed if THREE_OF_A_KIND
		} else if (dealerStrength.ordinal() == Strengths.THREE_OF_A_KIND.ordinal()) {
			if ((dealerHand.get(2)).getRank() == (dealerHand.get(4)).getRank()
					&& (dealerHand.get(1)).getRank() != (dealerHand.get(2)).getRank()
					&& (dealerHand.get(1)).getRank() != (dealerHand.get(0)).getRank()) {
				dealerHand.remove(dealerHand.get(1));
				dealerHand.remove(dealerHand.get(0));
				replace = 2;

			} else if ((dealerHand.get(0)).getRank() == (dealerHand.get(2)).getRank()
					&& (dealerHand.get(0)).getRank() != (dealerHand.get(3)).getRank()
					&& (dealerHand.get(3)).getRank() != (dealerHand.get(4)).getRank()) {
				dealerHand.remove(dealerHand.get(4));
				dealerHand.remove(dealerHand.get(3));
				replace = 2;

			} else if ((dealerHand.get(1)).getRank() == (dealerHand.get(3)).getRank()
					&& (dealerHand.get(0)).getRank() != (dealerHand.get(1)).getRank()
					&& (dealerHand.get(0)).getRank() != (dealerHand.get(4)).getRank()) {
				dealerHand.remove(dealerHand.get(4));
				dealerHand.remove(dealerHand.get(0));
				replace = 2;
			}
			// 1st or 3rd or 5th to be removed if TWO_PAIRS
		} else if (dealerStrength.ordinal() == Strengths.TWO_PAIRS.ordinal()) {
			if ((dealerHand.get(1)).getRank() == (dealerHand.get(2)).getRank()
					&& (dealerHand.get(3)).getRank() == (dealerHand.get(4)).getRank()
					&& (dealerHand.get(1)).getRank() != (dealerHand.get(3)).getRank()) {
				dealerHand.remove(dealerHand.get(0));
				replace = 1;

			} else if ((dealerHand.get(0)).getRank() == (dealerHand.get(1)).getRank()
					&& (dealerHand.get(3)).getRank() == (dealerHand.get(4)).getRank()
					&& (dealerHand.get(0)).getRank() != (dealerHand.get(3)).getRank()) {
				dealerHand.remove(dealerHand.get(2));
				replace = 1;

			} else if ((dealerHand.get(0)).getRank() == (dealerHand.get(1)).getRank()
					&& (dealerHand.get(2)).getRank() == (dealerHand.get(3)).getRank()
					&& (dealerHand.get(0)).getRank() != (dealerHand.get(2)).getRank()) {
				dealerHand.remove(dealerHand.get(4));
				replace = 1;
			}
			// 3 to be removed if PAIR
			// if highCard / secondHighest is > 10, only 2 to be removed
		} else if (dealerStrength.ordinal() == Strengths.PAIR.ordinal()) {
			if ((dealerHand.get(0)).getRank() == (dealerHand.get(1)).getRank()) {
				dealerHand.remove(dealerHand.get(3));
				dealerHand.remove(dealerHand.get(2));
				replace = 2;
				// adjust get() index and drop() index, as 2 cards were already removed
				if ((dealerHand.get(2)).getRank().ordinal() < Ranks.JACK.ordinal()) {
					dealerHand.remove(dealerHand.get(2));
					replace = 3;
				}
			} else if ((dealerHand.get(1)).getRank() == (dealerHand.get(2)).getRank()) {
				dealerHand.remove(dealerHand.get(3));
				dealerHand.remove(dealerHand.get(0));
				replace = 2;
				// adjust get() index, as 2 cards were already removed
				if ((dealerHand.get(2)).getRank().ordinal() < Ranks.JACK.ordinal()) {
					dealerHand.remove(dealerHand.get(2));
					replace = 3;
				}
			} else if ((dealerHand.get(2)).getRank() == (dealerHand.get(3)).getRank()) {
				dealerHand.remove(dealerHand.get(1));
				dealerHand.remove(dealerHand.get(0));
				replace = 2;
				// adjust get() index, as 2 cards were already removed
				if ((dealerHand.get(2)).getRank().ordinal() < Ranks.JACK.ordinal()) {
					dealerHand.remove(dealerHand.get(2));
					replace = 3;
				}
			} else if ((dealerHand.get(3)).getRank() == (dealerHand.get(4)).getRank()) {
				dealerHand.remove(dealerHand.get(1));
				dealerHand.remove(dealerHand.get(0));
				replace = 2;
				// adjust get() index, as 2 cards were already removed
				if ((dealerHand.get(0)).getRank().ordinal() < Ranks.JACK.ordinal()) {
					dealerHand.remove(dealerHand.get(0));
					replace = 3;
				}
			}
			// 4 to be removed if HIGH_CARD, only leave the highest value
		} else if (dealerStrength.ordinal() == Strengths.HIGH_CARD.ordinal()) {
			dealerHand.remove(dealerHand.get(3));
			dealerHand.remove(dealerHand.get(2));
			dealerHand.remove(dealerHand.get(1));
			dealerHand.remove(dealerHand.get(0));
			replace = 4;

		}
		return replace;
	}

	/**
	 * logic to decide if dealer wants to match current bet
	 * 
	 * @param currentBet
	 * @return true if matching, false otherwise
	 */
	public boolean matchBet(int currentBet) {
		boolean match = false;
		if (getToken() < currentBet) {
			System.out.println("Sorry, cannot match, not enough token left. Available tokens are: " + getToken());
			return false;
		}
		if (currentBet == 1 && dealer.handStrength(dealer.getHand()).ordinal() >= Strengths.HIGH_CARD.ordinal()
				&& dealer.highestCard.ordinal() > Ranks.JACK.ordinal()) {
			System.out.println("!!! Matching bet !!!");
			match = true;
		} else if (dealer.handStrength(dealer.getHand()).ordinal() >= Strengths.TWO_PAIRS.ordinal()) {
			match = true;
			System.out.println("!!! Matching bet !!!");
		} else {
			System.out.println("!!! Fold !!!");
		}
		return match;
	}
}