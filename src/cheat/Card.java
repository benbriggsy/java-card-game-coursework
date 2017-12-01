package cheat;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A class to model a standard playing card, each card consists of a a suit
 * and a rank. 
 */
public class Card implements Serializable, Comparable<Card>{
    static final long serialVersionUID = 100L;
    
    /**
     * Rank is the value of the card, of which there are 13. All royals have
     * equal value and ACE has a value of 11.
     */
    public enum Rank {TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
    EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11);
        final private int rankValue;
        /**
        * A constructor that gives the appropriate value to each Rank.
        */
        Rank(int x){
            rankValue=x;
        }
        
        /**
         * 
         * @return the value of the Rank
         */
        public int getIntValue(){return rankValue;}
        
        /**
        * A method to return the next highest ranked card
        * @return the next highest ranked card
        */
       public Rank getNext(){
           Rank[] values = Rank.values();
           int next = this.ordinal()+1;
           if(next > 12){
               next = 0;
           }
           return values[next];           
       }
    }
    
    /**
     * Suit contains the suit classification for a Card.
     */
    public enum Suit {CLUBS, DIAMONDS, HEARTS, SPADES}
    
    private final Rank rank;
    private final Suit suit;
    
    /**
     * A constructor for Card that gives it a Rank and Suit.
     * @param r The Rank of the Card to be created.
     * @param s The Suit of the Card to be created.
     */
    public Card(Rank r, Suit s){
        rank = r;
        suit = s;
    }

    /**
     *
     * @return The Rank of the Card.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     *
     * @return The Suit of the Card. 
     */
    public Suit getSuit() {
        return suit;
    }  
    
    /**
     * A method that finds the difference between the Card calling the method
     * and a Card given as a parameter.
     * @param c The Card to find the difference from.
     * @return The difference between the cards
     */
    public int difference(Card c){
        return this.getRank().ordinal() - c.getRank().ordinal();
    }
    
    /**
     * A method that finds the difference in value between the Card 
     * calling the method and a Card given as a parameter.
     * @param c The Card to find the difference in value from.
     * @return The difference in value between the cards
     */
    public int differenceValue(Card c){
        return this.rank.getIntValue() - c.getRank().getIntValue();           
    }
    
    @Override
    public int compareTo(Card c) {
        if(c.rank.ordinal() < this.rank.ordinal())
            return 1;
        else if(c.rank.ordinal() > this.rank.ordinal()){           
            return -1;
        }else{
            //if they are the same number sort by Suit.
            if (c.suit.ordinal() < this.suit.ordinal()){
                return 1;
            }else{
                return -1;
            }
        }            
    }    
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append(rank).append(" ");
        str.append(suit).append(" ");
        str.append(rank.getIntValue()).append("\n");
        return str.toString();
    }
    
    /**
     *A Comparator whose compare will put higher cards before lower cards
     * when comparing.
     */
    public static Comparator<Card> CompareDescending = new Comparator<Card>(){
        @Override
        public int compare(Card o1, Card o2) {  
            return (o1.getRank().compareTo(o2.getRank()))*(-1); //multiply by -1 to make descending       
        }        
    };
    
    /**
     *A Comparator to compare suits in the order CLUBS, DIAMONDS, HEARTS,
     * SPADES.
     */
    public static Comparator<Card> CompareSuit = new Comparator<Card>(){
        @Override
        public int compare(Card o1, Card o2) {
                return o1.suit.ordinal() - o2.suit.ordinal();
        }       
    };
    
    /**
     * A main method to demonstrate and test the methods in the Card class.
     * @param args
     */
    public static void main(String[] args) {
        Card card = new Card(Card.Rank.TEN, Card.Suit.SPADES);
        String nextRank = "" + card.getRank().getNext();
        System.out.println("next rank from TEN is: " 
                + nextRank + "\n");
               
        System.out.println(card.toString());
        
        Card card1 = new Card(Card.Rank.KING, Card.Suit.SPADES);
        System.out.println("difference in rank between TEN and KING: " 
                + card1.difference(card));
        System.out.println("difference in value TEN and KING: " 
                + card1.differenceValue(card));        
    }
}
