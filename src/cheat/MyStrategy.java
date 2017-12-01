package cheat;

import java.util.Random;

/**
 * An automatic implementation of Strategy where the player stores what cards
 * they have played previously and use this information to its advantage.
 * It also stores information on other peoples bids.
 * When cheating it chooses what rank to cheat with based on how many bids
 * of that rank have been played since the last cheat called.
 * If a player is on their last Card this player calls cheat.
 */
public class MyStrategy implements Strategy{
    //a store of all the cards this player has played since cheat was called
    Hand cardsPreviouslyPlayed = new Hand();
    //a store of all the bids others have played since cheat was called
    Hand cardsBidByOthers = new Hand();    
    //a store of all the bids others have played since cheat was called
    int handSizeOfCurrentPlayer = 0;
    
    @Override
    public boolean cheat(Bid b, Hand h) {
        Random rand = new Random();        
        boolean cheat;
        //if player has to cheat
        if(h.size() == h.countRank(b.getRank()) 
                + h.countRank(b.getRank().getNext())){
            cheat = false;
        }else{
            if(h.countRank(b.getRank()) < 1 
                    && h.countRank(b.getRank().getNext()) < 1){
                //cheat if the player has none of the 
                //legally playable card in their hand 
                cheat = true;                
            }else{
                int randCheat = rand.nextInt(10);
                cheat = randCheat < 3;
            }
        }    
        return cheat;
    }
    
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Hand handToPlay = new Hand();
        Card cardToPlay;      
        Card.Rank rankToPlay;
        Random rand = new Random();

        if(cheat){//if player is cheating
            int randIndex = rand.nextInt(h.size());
            cardToPlay = h.getCards().get(randIndex); 
            rankToPlay = chooseRankForCheat(b);
            //add card to the hand that will be played
            handToPlay.add(cardToPlay);
        }else{//if player is not cheating
            for(Card c: h){//loop through cards in players hand 
                //if card rank equals rank of previous bid
                if(b.getRank() == c.getRank()){
                    //add card to the hand that will be played
                    handToPlay.add(c);
                }                
            }
            //if cards have been added to the handToPlay set the rankToPlay
            //to the same as the previous bid
            if(handToPlay.size() > 0){
              rankToPlay = b.getRank();
            }else{//hand must contain none of the same ranked cards as last bid 
                for(Card c: h){//loop through cards in players hand
                    //if card rank equals one more than rank of previous bid
                    if(b.getRank().getNext() == c.getRank()){
                        //add card to the hand that will be played
                       handToPlay.add(c);             
                    }
                }
                //set rankToPlay to one more than last bid
                rankToPlay = b.getRank().getNext();
            }           
        }
        //remove the hand being played from the players hand
        h.remove(handToPlay);
        //return a new bid that the player will put down
        return new Bid(handToPlay, rankToPlay);
    }

    @Override
    public boolean callCheat(Hand h, Bid b) {  
        boolean callCheat;
        addBidToMemory(b);
        int bidSize = b.getHand().size();//amount of cards in bid
        //amount of cards in hand of same rank as rank given in the last bid
        int amountPreviouslyBid = cardsBidByOthers.countRank(b.getRank());
        int amountInHand = h.countRank(b.getRank());
        int amountInPrev = cardsPreviouslyPlayed.countRank(b.getRank());
        //if the sum is bigger than 4 the other player must be cheating
        int knownCards = amountInHand + amountInPrev;
        if(handSizeOfCurrentPlayer <= b.getHand().size()){
            callCheat = true;
        }else if(bidSize + knownCards > 4){
            callCheat = true;
        }else{
            callCheat = callCheat((bidSize + knownCards
                    + amountPreviouslyBid)/8);
        }
        return callCheat;
    }
    
    /**
     * Adds a Bid to memory not worrying about 
     * the suit of the cards in the Bid.
     * @param b the bid to add to memory
     */
    public void addBidToMemory(Bid b){
        Card.Rank rankToAdd = b.getRank();
        int amountToAdd = b.getHand().size();
        for(int i = 0; i < amountToAdd; i++){
            //all cards added are SPADES since suit doesn't matter.
            cardsBidByOthers.add(new Card(rankToAdd, Card.Suit.SPADES));
        }
    }

    /**
     * A method to decide whether to call cheat based on a probability passed
     * in as a parameter
     * @param probability the probability of cheating
     * @return true if cheating
     */
    public boolean callCheat(double probability) {        
        Random rand = new Random(); 
        probability = probability * 100;
        return rand.nextInt(100) < probability;        
    }
    
    /**
     * A method to choose which rank to cheat with, based on what has
     * previously been bid.
     * @param b the Bid previously played
     * @return the Rank to play
     */
    public Card.Rank chooseRankForCheat(Bid b){  
        Card.Rank rankToPlay;
        int chooseRank = cardsBidByOthers.countRank(b.getRank());          
        //Play the rank of previous bid if
        //discard pile is empty
        if(chooseRank < 2 //if less than two cards of same rank bid previously
                || b.getHand().size() == 0){//or if bid is empty
            rankToPlay = b.getRank();
        }else{//otherwise play the next Rank
            rankToPlay = b.getRank().getNext();
        }
        return rankToPlay;
    }

    /**
     * This method clears the memory Hands.
     */
    public void clearPrevious(){
        cardsPreviouslyPlayed.clearCards();
        cardsBidByOthers.clearCards();
    }
    
    /**
     * Sets the size of the current players Hand.
     * @param s the value to set
     */
    public void setHandSizeOfCurrentPlayer(int s){
        handSizeOfCurrentPlayer = s;
    }
}
