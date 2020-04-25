/*
 * @author: Laszlo Szlatki
 * @date: 18/Apr/2020
 * */
package pokerfx;

import java.util.Scanner;

/**
 * ConsoleGame class contains main method to start playing the game Game class creates
 * a Deck, Dealer and a Player.> Deals 5 cards each> player decides if he or
 * dealer opens the game (game can be opened only if one has a pair of jacks or
 * higher) If game opened, 1 token from each to be put to the pot allows player
 * to change up to 4 cards, and manages dealers actions player can bet 0-3
 * tokens after seeing new cards dealer can decide to let you win or 'see' your
 * bet best hand wins, taking all tokens from the pot game ends, when player
 * wants to, or one lost all their tokens Then announces the winner
 */
public class ConsoleGame {

	// Game class creates a Deck
	private static Deck deck = new Deck(5);
	// Game class created Dealer and a Player with 5 cards and 10 tokens each
	private static Player player = new Player(deck, 10);
	private static Dealer dealer = new Dealer(deck, 10);

	// set up static counters etc.
	private static boolean playAgain = true;
	private static int gameCounter = 0;

	/**
	 * main method to drive the poker game
	 * 
	 * @param args possible arguments passed in (not used here)
	 */
	public static void main(String[] args) {

		do {
			setupNewGame();
			// prompt player to open or pass and store it in userOpen variable
			char userOpen = openGame();
			// check if user wants to open and has the min required to open, put in 1 token
			// each to the pot,
			// and change cards
			if (userOpen == 'Y' && player.canOpen(player.getHand())) { // _____________________________player opens
				player.spendToken(1);
				dealer.spendToken(1);
				System.out.println(player.getHand().highestCard);
				System.out.println(player.getHand().handStrength(player.getCards()));
				System.out.println("Player opens game");

				// player opens and changes cards
				playerChangeCards();

				// player can raise the bet
				int playersBet = player.spendToken(raiseBet());

				// dealer changes cards
				dealerChangeCards();

				// dealer can decide to match bet or fold
				if (dealer.matchBet(playersBet)) {
					dealer.spendToken(playersBet);
				} else {
					// fold
//					playAgain();
//					continue;
				}
				// compare hands and announce winner of this round
				findWinner(dealer, player);

			} else if (dealer.canOpen(dealer.getHand())) { // _________________________________________computer opens
				// if player cannot or don't want to open, but dealer can, open game
				dealer.spendToken(1);
				player.spendToken(1);
				System.out.println(
						(player.getHand().handStrength(player.getCards())) + " of " + (player.getHand().highestCard));
				System.out.println(
						(dealer.getHand().handStrength(dealer.getCards())) + " of " + (dealer.getHand().highestCard));
				System.out.println("You cannot open, but dealer can");

				// dealer changes cards
				dealerChangeCards();

				// player changes cards
				playerChangeCards();

				// player can raise the bet
				int playersBet = player.spendToken(raiseBet());

				// dealer can decide to match bet or fold
				if (dealer.matchBet(playersBet)) {
					dealer.spendToken(playersBet);
				} else {
					// fold
				}

				// compare hands and announce winner of this round
				findWinner(dealer, player);

			} else { // ___________________________________________________________________none opens
				System.out.println(
						(player.getHand().handStrength(player.getCards())) + " of " + (player.getHand().highestCard));
				System.out.println(
						(dealer.getHand().handStrength(dealer.getCards())) + " of " + (dealer.getHand().highestCard));
				// none opens, new game starts
			}
			System.out.println("a new game is starting: " + (playAgain && Rules.hasMoreToken(dealer, player)));
		} while (playAgain && Rules.hasMoreToken(dealer, player));
		// Announce winner of the game
		// check if player has more tokens than dealer
		Participant winner = player.getToken() > dealer.getToken() ? player : dealer;
		System.out.println("The game has finished now\nThe overall winner is: " + winner);
	}

	/**
	 * clears off reminding of old game and sets up new game
	 */
	private static void setupNewGame() {
		// get organised for next game
		playAgain = true;
		gameCounter++;
		deck.reset();
		deck.shuffle();
		// if hands are not empty, throw cards away and deal 5 more
		if (!player.getCards().isEmpty()) {
			player.throwAllCards();
			dealer.throwAllCards();
			player.swapCards(deck, 5);
			dealer.swapCards(deck, 5);
		}
		// sort dealt cards in hands for strength check
		player.getHand().sortHand(player.getCards());
		dealer.getHand().sortHand(dealer.getCards());

		// display player's and dealer's hands
		System.out.println("Player has: " + player.getCards());
		System.out.println("Dealer has: " + dealer.getCards());
	}

	/**
	 * method to help player change cards
	 */
	private static void playerChangeCards() {
		System.out.println("Select cards to be replaced!");
		int playerReplaced = player.toBeReplaced();
		player.swapCards(deck, playerReplaced);
		player.newStrength(player.getHand());
		System.out.println("After change, player has: " + player.getCards());
		System.out
				.println((player.getHand().handStrength(player.getCards())) + " of " + (player.getHand().highestCard));
	}

	/**
	 * method to help dealer change cards
	 */
	private static void dealerChangeCards() {
		System.out.println("Dealers turn to change cards!");
		System.out.println("Dealers hand sterngte is: " + dealer.getHand().handStrength(dealer.getCards()));
		int dealerReplaced = dealer.toBeReplaced();
		System.out.println("Dealer replaced " + dealerReplaced + " cards");
		dealer.swapCards(deck, dealerReplaced);
		dealer.newStrength(dealer.getHand());
		System.out.println("After change, dealer has: " + dealer.getCards());
		System.out
				.println((dealer.getHand().handStrength(dealer.getCards())) + " of " + (dealer.getHand().highestCard));

	}

	/**
	 * process user choice for opening game
	 * @return the character what the user entered
	 */
	private static char openGame() {
		System.out.println("Would you like to open? (Y/N) \n(only if you have at least a pair of jacks)");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		char open = '\0';
		if (s.length() == 0) {
			open = 'N';
		} else {
			open = s.toUpperCase().charAt(0);
		}
		return open;
	}
	
	/**
	 * method to help player raise bet
	 * @return amount of tokens to be raised by
	 */
	private static int raiseBet() {
		int bet = 0;
		if (Rules.hasMoreToken(player) == false) {
			System.out.println("Cannot raise, no more tokens");
			return 0;
		}

		System.out.println("You have " + player.getToken() + " tokens left");
		System.out.println("You can bet an additional (0-3) tokens now, or max as many as you or your opponent has");
		System.out.println("Please enter the number of tokens you wish to bet: ");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		if (s.length() == 0) {
			bet = 0;
		} else {
			try {
				bet = Integer.parseInt(s);
				// if player enters negative number, bet defaults to 0
				if (bet < 0) {
					bet = 0;
				}
				// if player enters number greater than 3, bet defaults to 3
				if (bet > 3) {
					bet = 3;
				}
				// if more tokens entered than player has left, bet all reminding tokens
				if (bet > player.getToken()) {
					bet = player.getToken();
				}
				// if more tokens are bet than the dealer has left, bet as many as dealer can match
				if (bet > dealer.getToken()) {
					bet = dealer.getToken();
				}
			} catch (NumberFormatException e) {
				bet = 0;
			}
		}
		System.out.println("You have bet: " + bet + " tokens");

		return bet;
	}

	/**
	 * method to decide and announce who won the round
	 * @param dealer
	 * @param player
	 */
	private static void findWinner(Participant dealer, Participant player) {

		Hand winner = Rules.checkWinner(dealer.getHand(), player.getHand());
		if (winner == player.getHand()) {
			player.winToken();
			player.addRoundsWon();
			System.out.println("Player wins this round!");
		} else {
			dealer.winToken();
			dealer.addRoundsWon();
			System.out.println("Dealer wins this round!");
		}
		System.out.println("Player has: " + player.getToken() + " tokens");
		System.out.println("Dealer has: " + dealer.getToken() + " tokens");
		playAgain();
	}

	/**
	 * ask user if they want to play again or end game
	 * @return character representation of user choice
	 */
	private static char playAgain() {
		System.out.println("\nWould you like to play again? (Y/N)");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		char again = '\0';
		if (s.length() == 0) {
			again = 'N';
		} else {
			again = s.toUpperCase().charAt(0);
		}
		if (again != 'Y') {
			playAgain = false;
		}
		return again;
	}
}