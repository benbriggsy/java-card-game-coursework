package cheat;

import java.util.*;
/**
 * This is a class to run the game of cheat.
 * I changed the rules a little, so that the game doesn't get stuck if one 
 * player has all of the TWO's. This required me to make changes to Bid
 * so I have included that file.
 * 
 * In this game I have one player of each strategy and the rest are BASIC
 * It may take a long time to get to the end of the game with MY strategy
 * Player calling cheat whenever someone is on their last card.
 * I have commented out the lines of code that include HUMAN in the game
 * to see HUMAN in action uncomment lines 40 - 43.  
 */
public class BasicCheat implements CardGame {

    private Player[] players;
    private int nosPlayers;
    public static final int MINPLAYERS = 5;
    private int currentPlayer;
    private Hand discards;
    private Bid currentBid;

    public BasicCheat() {
        this(MINPLAYERS);
    }

    public BasicCheat(int n) {
        nosPlayers = n;
        players = new Player[nosPlayers];
        int showHuman = 2;  
        players[0] = new BasicPlayer(this);
        players[0].setStrategy(StrategyFactory.StrategyType.MY);        
        players[1] = new BasicPlayer(this);
        players[1].setStrategy(StrategyFactory.StrategyType.THINKER); 
////remove the comments below (40 - 43) to play game with HUMAN involved.        
//        players[2] = new BasicPlayer(this);
//        players[2].setStrategy(StrategyFactory.StrategyType.HUMAN);
//        showHuman = 3;        
        for (int i = showHuman; i < nosPlayers; i++) {
            players[i] = new BasicPlayer(this);
            players[i].setStrategy(StrategyFactory.StrategyType.BASIC);
        }
        currentBid = new Bid();
        currentBid.setRank(Card.Rank.TWO);
        currentPlayer = 0;
    }

    @Override
    public boolean playTurn() {
        //Ask player for a play,
        System.out.println("current bid = " + currentBid);
        currentBid = players[currentPlayer].playHand(currentBid);
        System.out.println("Player bid = " + currentBid);
        //Add hand played to discard pile
        discards.add(currentBid.getHand());
        //Offer all other players the chance to call cheat
        boolean cheatCalled = false;
        //changed this loop to ask the player after the current player if they
        //want to call cheat first
        for (int i = currentPlayer + 1; 
                i != currentPlayer && !cheatCalled; i++) {
            //go back to i = 0 if the end off the array is reached.
            if(i > players.length - 1){
                i = 0;
            }
            if (i != currentPlayer) {
                //set MY players record of current players hand size
                for (int j = 0; j < nosPlayers; j++) {
                        if (players[j].getStrategy() instanceof MyStrategy){
                            MyStrategy ms =
                                    (MyStrategy) players[j].getStrategy();
                            ms.setHandSizeOfCurrentPlayer(
                                    players[currentPlayer].cardsLeft());
                        }
                }
                System.out.println("asked player " + i);
                cheatCalled = players[i].callCheat(currentBid);
                if (cheatCalled) {
                    //let thinker and my players clear their memory
                    for (int j = 0; j < nosPlayers; j++) {
                        if(players[j].getStrategy() instanceof ThinkerStrategy){
                            ThinkerStrategy ts = 
                                    (ThinkerStrategy) players[j].getStrategy();
                            ts.clearPrevious();
                        }else if (players[j].getStrategy() 
                                instanceof MyStrategy){
                            MyStrategy ms =
                                    (MyStrategy) players[j].getStrategy();
                            ms.clearPrevious();
                        }
                    }
                    System.out.println("Player called cheat by Player " 
                            + (i + 1));
                    if (isCheat(currentBid)) {
//CHEAT CALLED CORRECTLY
//Give the discard pile of cards to currentPlayer who then has to play again                      
                        players[currentPlayer].addHand(discards);
                        System.out.println("Player cheats!");
                        System.out.println("Adding cards to player "
                                + (currentPlayer + 1) + players[currentPlayer]);

                    } else {
//CHEAT CALLED INCORRECTLY
//Give cards to caller i who is new currentPlayer
                        System.out.println("Player Honest");
                        currentPlayer = i;
                        players[currentPlayer].addHand(discards);
                        System.out.println("Adding cards to player "
                                + (currentPlayer + 1) + players[currentPlayer]);
                    }
//If cheat is called, current bid reset to an empty bid with rank two whatever 
//the outcome
                    currentBid = new Bid();
//Discards now reset to empty	
                    discards = new Hand();
                }
            }
            //If i is the current player make sure the loop is exited.
            if(i == currentPlayer){
                i = currentPlayer - 1;
            }
        }
        if (!cheatCalled) {
//Go to the next player       
            System.out.println("No Cheat Called");

            currentPlayer = (currentPlayer + 1) % nosPlayers;
        }
        return true;
    }

    @Override
    public int winner() {
        for (int i = 0; i < nosPlayers; i++) {
            if (players[i].cardsLeft() == 0) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void initialise() {
        //Create Deck of cards
        Deck d = new Deck();
        d.shuffle();
        //Deal cards to players
        int count = 0;
        while (d.size() > 0) {
            players[count % nosPlayers].addCard(d.deal());
            count++;
        }
        //Initialise Discards
        discards = new Hand();
        //Chose first player
        currentPlayer = 0;
        currentBid = new Bid();
        currentBid.setRank(Card.Rank.TWO);
    }

    public void playGame() {        
        initialise();
        int c = 0;
        Scanner in = new Scanner(System.in);
        boolean finished = false;
        while (!finished) {
            //Play a hand
            System.out.println(" Cheat turn for player " + (currentPlayer + 1));
            playTurn();
            System.out.println(" Current discards =\n" + discards);
            c++;
            System.out.println(" Turn " + c 
                    + " Complete. Press any key to "
                    + "continue or enter Q to quit>");
            String str = in.nextLine();
            if (str.equals("Q") || str.equals("q") || str.equals("quit")) {
                finished = true;
            }
            int w = winner();
            if (w >= 0) {
                System.out.println("The Winner is Player " + (w + 1));
                finished = true;
            }
        }
    }
    
    public static boolean isCheat(Bid b) {
        for (Card c : b.getHand()) {
            if (c.getRank() != b.r) {
                return true;
            }
        }
        return false;       
    }

    public static void main(String[] args) {
        BasicCheat cheat = new BasicCheat();
        cheat.playGame();
    }
}
