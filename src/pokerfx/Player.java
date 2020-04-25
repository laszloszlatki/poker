/*
 * @author: Laszlo Szlatki
 * @date: 02/Apr/2020
 * */
package pokerfx;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Player class holds player's hand
 */
public class Player extends Participant {
	// create player's hand using the constructor
	private Hand player;

	// number of cards to be replaced
	private int replace = 0;

	// store player's hand as list of cards;
	private List<Card> playerHand;

	/**
	 * Constructor to create a player with a hand of cards and tokens
	 *
	 * @param deck: the deck used in the Game
	 */
	public Player(Deck deck, int token) {
		super();
		setToken(token);
		player = new Hand(deck);
		playerHand = player.getHand();
		passHandToParticipant();
	}

	/**
	 * method to pass player's hand to participant for further processing
	 */
	public void passHandToParticipant() {
		super.setChildHandToParticipant(player);
	}

	/**
	 * Allow user to select cards to be replaced
	 * 
	 * @return indexes of cards to be replaced
	 */
	public int[] selectCards() {
		System.out.println("Which cards would you like to change? (Up to 4 cards can be changed)");
		System.out.println("Enter it's location (separate them with a space ' ')");
		System.out.println("i.e. 1 2 4 5");

		// read input from console
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();

		String[] indexString = s.split(" "); // Splits each spaced integer into a String array.
		int[] indexes = new int[indexString.length]; // Creates the integer array.
		if (s.length() == 0 || indexes.length > 4) {
			// create an array of length 0 instead of 'null' to be passed on to dropCards()
			indexes = new int[0];
		} else {
			for (int i = 0; i < indexes.length; i++) {
				try {
					indexes[i] = Integer.parseInt(indexString[i]) - 1; // Parses the integer for each string.
																	   // user input is from 1 to 5, cards in hand 0 - 4
					if (indexes[i] < 0 || indexes[i] >= 5) {
						indexes = new int[0];
						break;
					}
				} catch (NumberFormatException e) {
					indexes = new int[0];
				}
			}

		}
		// ascending order the array to be used in dropCards
		Arrays.sort(indexes);
		
		// ensure no duplicate elements in the array
		indexes = removeDuplicateElements(indexes);

		// printing array elements
		for (int i = 0; i < indexes.length; i++)
			System.out.print(indexes[i] + " ");
		return indexes;
	}

	/**
	 * remove duplicate elements from array (to be used with indexes[])
	 * 
	 * @param arr original array with possible duplicate indexes entered by user
	 * @return an array with distinct elements only
	 */
	public static int[] removeDuplicateElements(int arr[]) {
		int arrayLength = arr.length;
		if (arrayLength == 0 || arrayLength == 1) {
			return arr;
		}
		int[] temp = new int[arrayLength];
		int j = 0;
		for (int i = 0; i < arrayLength - 1; i++) {
			if (arr[i] != arr[i + 1]) {
				temp[j++] = arr[i];
			}
		}
		temp[j++] = arr[arrayLength - 1];

		// count the zero elements in temp array
		int nullCounter = 0;
		for (int i : temp) {
			if (temp[i] == 0 && i != 0) {
				nullCounter++;
			}
		}
		
		// copy elements from temp to shorter array
		int[] tempShort = new int[temp.length - nullCounter];
		for (int i = 0; i < tempShort.length; i++) {
			tempShort[i] = temp[i];
		}
		return tempShort;
	}

	/**
	 * drop cards, what user selected
	 * 
	 * @param indexesToBeDropped array of indexes to be dropped
	 */
	private int dropCards(int[] indexesToBeDropped) {
		// resetting counter (not to roll over from previous game)
		replace = 0;
		for (int i = indexesToBeDropped.length - 1; i >= 0; i--) {
			// drop card form the specified index - 1
			playerHand.remove(playerHand.get(indexesToBeDropped[i]));
			replace++;
		}
		return replace;
	}

	/**
	 * Adds the number of cards what the player selected to be replaced calls the
	 * dropCard method, to drop selectedCards from the hand
	 * 
	 * @return: number of cards to be replaced
	 */
	@Override
	public int toBeReplaced() {
		
		replace = dropCards(selectCards());
		System.out.println("Number of cards to be replaced: " + replace);
		return replace;
	}
}