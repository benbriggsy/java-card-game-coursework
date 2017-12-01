package cheat;

import java.util.Random;

public class Bid {
    Hand h;
    Card.Rank r;
    
    public Bid(){
        Random rand = new Random();
        h=new Hand();        
        Card.Rank[] values = Card.Rank.values(); 
        // The starting rank has been randomised to avoid the game getting 
        // stuck when one player has all of the TWO's
        r=values[rand.nextInt(13)]; 
    }
    
    public Bid(Hand hand,Card.Rank bidRank){
            h=hand;
            r=bidRank;
    }
    
    public void setHand(Hand hand){ h=hand;}
    
    public void setRank(Card.Rank rank){ r=rank;}

    public Hand getHand(){ return h;}
    
    public int getCount(){ return h.size();}
    
    public Card.Rank getRank(){ return r;}
    
    @Override
    public String toString(){
            return h.size()+" x "+r;
    }		
}	
