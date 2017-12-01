package cheat;

import java.util.Random;

/**
 * An automatic implementation of Strategy that never cheats unless it has to,
 * always plays the maximum number of cards of the lowest possible rank and
 * only calls cheat on other players if certain they are cheating.
 */
public class BasicStrategy implements Strategy {

    @Override
    public boolean cheat(Bid b, Hand h) {
            //cheat if the player has none of the 
            //legally playable card in their hand         
        return h.countRank(b.getRank()) < 1 
                    && h.countRank(b.getRank().getNext()) < 1;
    }
    
    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {
        Hand handToPlay = new Hand();
        Card cardToPlay;      
        Card.Rank rankToPlay;
        Random rand = new Random();
        if(cheat){//if player is cheating
            int randIndex = rand.nextInt(h.size());
            int randRank = rand.nextInt(2);
            cardToPlay = h.getCards().get(randIndex);            
            //randomly choose rank from legal options
            //or play the rank of previous bid if
            //discard pile is empty
            if(randRank == 0){                
              rankToPlay = b.getRank();
            }
            else{
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
        int bidSize = b.getHand().size();//amount of cards in bid
        //amount of cards in hand of same rank as the rank given in the last bid
        int amountInHand = h.countRank(b.getRank());
        //if the sum is bigger than 4 the other player must be cheating
        return bidSize + amountInHand > 4;
    }   
}