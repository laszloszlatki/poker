package pokerfx;

import java.util.*;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * class to coordinate javaFX game
 * @author Laszlo Szlatki
 *
 */
public class PokerGame extends Application {

	private static Text message = new Text();

	// to activate and de-activate some of the buttons
	private static SimpleBooleanProperty playable = new SimpleBooleanProperty(false);

	List<Button> playerCardButtonList = new ArrayList<>();
	List<Button> dealerCardButtonList = new ArrayList<>();

	private HBox dealerCardsHBox = new HBox(20);
	private HBox playerCardsHBox = new HBox(20);

	private static final int STARTING_TOKEN = 10;
	private Deck deck = new Deck(5);
	private Player player = new Player(deck, STARTING_TOKEN);
	private Dealer dealer = new Dealer(deck, STARTING_TOKEN);

	/**
	 * start method to initialise javaFX game
	 * @param primaryStage
	 */
	public void start(Stage primaryStage) {

		// sort dealt cards in hands for strength check
		//player.getHand().sortHand(player.getCards());
		//dealer.getHand().sortHand(dealer.getCards());

		// create UI
		Pane root = new Pane();
		root.setPrefSize(835, 560);

		Region background = new Region();
		background.setPrefSize(835, 560);
		background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

		HBox rootLayout = new HBox(5);
		rootLayout.setPadding(new Insets(5, 5, 5, 5));
		Rectangle leftBG = new Rectangle(600, 550);
		leftBG.setArcWidth(50);
		leftBG.setArcHeight(50);
		leftBG.setFill(Color.GREEN);
		Rectangle rightBG = new Rectangle(220, 550);
		rightBG.setArcWidth(50);
		rightBG.setArcHeight(50);
		rightBG.setFill(Color.ORANGE);

		// LEFT

		VBox leftVBox = new VBox(50);
		leftVBox.setAlignment(Pos.TOP_CENTER);

		Text dealerScore = new Text("Dealer: ");
		Text playerScore = new Text("Player: ");

		leftVBox.getChildren().addAll(dealerScore, dealerCardsHBox, message, playerCardsHBox, playerScore);

		// RIGHT

		VBox rightVBox = new VBox(20);
		rightVBox.setAlignment(Pos.CENTER);

		final TextField bet = new TextField("bet");
		bet.setDisable(false);
		bet.setMaxWidth(50);
		// player can raise the bet
		Text money = new Text("MONEY");

		// radio button for betting (check, 1, 2, 3 above the "BET button")
//		RadioButton bt1 = new RadioButton("0");
//		RadioButton bt2 = new RadioButton("1");
//		RadioButton bt3 = new RadioButton("2");
//		RadioButton bt4 = new RadioButton("3");
//		ToggleGroup betGroup = new ToggleGroup();
//		bt1.setToggleGroup(betGroup);
//		bt2.setToggleGroup(betGroup);
//		bt3.setToggleGroup(betGroup);
//		bt4.setToggleGroup(betGroup);	
//		HBox betBox = new HBox(15, bt1, bt2, bt3, bt4);	
//		betBox.setAlignment(Pos.CENTER);

		Button btnBet = new Button("BET");
		Button btnDisplay = new Button("DISPLAY CARDS");
		Button btnOpen = new Button("OPEN");
		Button btnPass = new Button("PASS");
		Button btnReplace = new Button("REPLACE CARDS");
		// Button btnHit = new Button("HIT");
		// Button btnStand = new Button("STAND");
		Button btnExit = new Button("EXIT");

		HBox openPassHBox = new HBox(15, btnOpen, btnPass);
		openPassHBox.setAlignment(Pos.CENTER);
		HBox betHBox = new HBox(15, bet, btnBet);
		betHBox.setAlignment(Pos.CENTER);

		// HBox hitStandHBox = new HBox(15, btnHit, btnStand);
		// hitStandHBox.setAlignment(Pos.CENTER);

		rightVBox.getChildren().addAll(betHBox, btnDisplay, openPassHBox, btnReplace, money,
				btnExit/* , hitStandHBox */);

		// ADD BOTH STACKS TO ROOT LAYOUT

		rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(rightBG, rightVBox));
		root.getChildren().addAll(background, rootLayout);

		// BIND PROPERTIES

		// bind buttons - some active, others are inactive
//		btnBet.disableProperty().bind(playable.not());
//		btnOpen.disableProperty().bind(playable);
//		btnPass.disableProperty().bind(playable);
//		btnReplace.disableProperty().bind(playable.not());


		// INIT CARD BUTTONS
		
		
		
		
		
		// INIT BUTTONS on right

		btnDisplay.setOnAction(event -> {
			startNewGame(dealerCardsHBox, playerCardsHBox);

			// displayCards();
			// message.setText("do you want to open?");
			// btnDisplay.setDisable(true);

		});
//				
		btnOpen.setOnAction(event -> {
			// check if player can open
			if (player.canOpen(player.getHand())) {
				message.setText("You have opened.\nPlease select cards to be changed");
				player.spendToken(1);
				dealer.spendToken(1);
				btnOpen.setDisable(true);
				btnPass.setDisable(true);
				playable.set(false);
				// check if dealer can open
			} else if (dealer.canOpen(dealer.getHand())) {
				message.setText("Dealer opened. Player didn't have qualifying hand");
				btnOpen.setDisable(true);
				btnPass.setDisable(true);
				playable.set(false);
			} else {
				playable.set(false);
				btnDisplay.setDisable(false);
				btnOpen.setDisable(true);
				btnPass.setDisable(true);
				message.setText("None can open, none has qualifying hand");
				startNewGame(dealerCardsHBox, playerCardsHBox);
				// displayCards();
			}
		});

		btnPass.setOnAction(event -> {
//			Button playerButton = new Button();
//			playerButton.setGraphic(new ImageView(new Image("images/back.jpg", 140, 100, true, true)));
//			playerCards.getChildren().add(playerButton);

			// check if dealer can open
			if (dealer.canOpen(dealer.getHand())) {
				playable.set(true);
			} else {
				startNewGame(dealerCardsHBox, playerCardsHBox);
				// displayCards();
				btnDisplay.setDisable(true);
			}
		});

//				btnBet.setOnAction(event -> {
//					Integer playersBet;
//					// action if textfield input
//		    		if (Rules.hasMoreToken(player) == false) {
//		    			System.out.println("Cannot raise, no more tokens");
//		    		}
//					try {
//						playersBet = Integer.valueOf(bet.getText()); // save textfield input
//						// if player enters negative number, bet defaults to 0
//						if (playersBet < 0) {
//							playersBet = 0;
//						}
//						// if greater number than 3 entered, default bet is 3
//						if (playersBet > 3) {
//							playersBet = 3;
//						}
//						// if more tokens entered than player has left, bet all reminding tokens
//		    				if (playersBet > player.getToken()) {
//		    					playersBet = player.getToken();
//		    				}
//					} catch (NumberFormatException e) {
//						playersBet = 0;
//					}
//					
//					// action if radiobutton input
//
//					
//				});

		btnExit.setOnAction(event -> {
			
			//remove cards from table
			dealerCardsHBox.getChildren().clear();
			playerCardsHBox.getChildren().clear();
			// close program
			System.exit(0);
		});

		// Process events

		// Create a scene and place it in the stage
		Scene scene = new Scene(root);
		primaryStage.setTitle("Poker Game"); // Set title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
	}

	private void startNewGame(HBox dealerCard, HBox playerCard) {
		playable.set(false);

		// message.setText("The game is starting now");

		// get organised for next game
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

		// clear HBox
		dealerCard.getChildren().clear();
		playerCard.getChildren().clear();

		// pass cards to HBox
		for (int i = 0; i < 5; i++) {

			/*
			 * Get string representation of cards and add them to the button list as labels
			 * 
			 * String dealerCards = dealer.getCards().get(i).toString(); // extract button
			 * text dealerCardButtonList.add(new Button(dealerCards)); // add button to HBox
			 * 
			 * String playerCards = player.getCards().get(i).toString(); // extract button
			 * text playerCardButtonList.add(new Button(playerCards)); // add button to HBox
			 * 
			 */

			// create button with image on and add it to the button list
			Button dealerButton = new Button();
			dealerButton.setGraphic(new ImageView(new Image(dealer.getCards().get(i).getPath(), 140, 100, true, true)));
			dealerCardButtonList.add(dealerButton);

			Button playerButton = new Button();
			playerButton.setGraphic(new ImageView(new Image(player.getCards().get(i).getPath(), 140, 100, true, true)));
			playerCardButtonList.add(playerButton);

			dealerCard.getChildren().add(dealerCardButtonList.get(i));
			playerCard.getChildren().add(playerCardButtonList.get(i));
		}

	}

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
//		playAgain();
	}
}
