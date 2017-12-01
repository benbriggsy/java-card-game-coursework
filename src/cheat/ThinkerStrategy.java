package cheat;

import java.util.Random;

/**
 * An automatic implementation of Strategy where the player stores what cards
 * they have played previously and use this information to its advantage.
 */
public class ThinkerStrategy implements Strategy{    
    //a store of all the cards this player has played since cheat was called
    Hand cardsPreviouslyPlayed = new Hand();
    
    @Override
    public boolean cheat(Bid b, Hand h) {
        Random rand = new Random();        
        boolean cheat;
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
            int randRank = rand.nextInt(2);
            cardToPlay = chooseCardToCheat(h);
            //randomly choose rank from legal options
            //or play the rank of previous bid if
            //discard pile is empty
            if(randRank == 0 || b.getHand().size() == 0){                
              rankToPlay = b.getRank();
            }else{
              rankToPlay = b.getRank().getNext();
            }
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
        int bidSize = b.getHand().size();//amount of cards in bid
        //amount of cards in hand of same rank as rank given in the last bid
        int amountInHand = h.countRank(b.getRank());
        int amountInPrev = cardsPreviouslyPlayed.countRank(b.getRank());
        //if the sum is bigger than 4 the other player must be cheating
        int knownCards = amountInHand + amountInPrev;
        if(bidSize + knownCards > 4){
            callCheat = true;
        }else{
            callCheat = callCheat((bidSize + knownCards)/8);
        }
        return callCheat;
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
     * A method to choose the card to play when cheating, with a 0.6 
     * probability of trying to choose a Card above SEVEN.
     * @param h The players Hand
     * @return The card to be played
     */
    public cheat.Card chooseCardToCheat(Hand h) {
        Random rand = new Random();
        Card cardToPlay;
        int randIndex = rand.nextInt(h.size());
        Card tempCard = null;
        int playHighCard = rand.nextInt(10);
        boolean containsAboveSeven = false;
        if(playHighCard > 3){// 0.6 chance of trying to play a high card.
            for(Card c: h){
                if(c.getRank().ordinal() > 7){
                    containsAboveSeven = true;
                    tempCard = c;
                }
            }
            if(containsAboveSeven){
                cardToPlay = tempCard;
            }else{
                cardToPlay = h.getCards().get(randIndex);                    
            }
        }else{
            cardToPlay = h.getCards().get(randIndex);
        }
        return cardToPlay;
    }

    /**
     * This method clears the memory Hands.
     */
    public void clearPrevious(){
        cardsPreviouslyPlayed.clearCards();
    }
    
}
