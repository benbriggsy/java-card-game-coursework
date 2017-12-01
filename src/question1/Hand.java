package question1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A class to model a Hand of playing Cards. It contains a list of Cards and 
 * has the ability to shuffle and deal the cards.
 */
public class Hand implements Serializable, Iterable<Card>{

    static final long serialVersionUID = 102L;
    
    private ArrayList<Card> cards;
    // initOrderCards stores the cards in the order they have been added.
    private ArrayList<Card> initOrderCards;
    private int[] suitCounts = new int[4];
    private int[] rankCounts = new int[13];
    private int totalValue;
    
    /**
     * Default constructor for Hand that creates and empty list of cards.
     */
    public Hand(){
        cards = new ArrayList();
        initOrderCards = new ArrayList();
    }
    
    /**
     * Constructor for Hand that adds an array of cards to the Hand.
     * @param cardsToAdd the array of cards to be added.
     */
    public Hand(Card[] cardsToAdd){
        for(Card c: cardsToAdd){
            this.cards.add(c);
            incrementCounts(c);
        }        
    }
    
    /**
     * Constructor for Hand that adds a Hand of cards to the Hand.
     * @param h the Hand of cards to be added.
     */
    public Hand(Hand h){
        this.add(h.getCards());
    }
    
    /**
     * Add a hand of cards to this Hand.
     * @param h the Hand to be added
     */
    public void add(Hand h){
        for(Card c: h.getCards()){
            this.add(c);
        }        
    }
    
    /**
     * Add a Collection of cards to this Hand.
     * @param c the Collection to be added
     */
    public void add(Collection<Card> c){
        for(Card i: c){
            this.add(i);
        }
    }
    
    /**
     * Adds a single Card to this Hand.
     * @param c the Card to be added
     */
    public void add(Card c){
        cards.add(c);
        initOrderCards.add(c);
        incrementCounts(c);
    }
    
    /**
     * Remove a hand of cards from this Hand.
     * @param h the Hand to be removed
     * @return True if the Hand was removed
     */
    public boolean remove(Hand h){
        boolean flag = false;
        ArrayList<Card> handToRemove = h.getCards();
        for(Card c: handToRemove){
            if(cards.contains(c)){
                this.remove(c);
                flag = true;
            }
        }
        return flag;
    }
    
    /**
     * Remove a single card from this Hand.
     * @param c the Card to be removed
     * @return returns true if the card was removed
     */
    public boolean remove(Card c){
        if(cards.contains(c)){
            cards.remove(c);
            initOrderCards.remove(c);
            decrementCounts(c);
            return true;
        }
        return false;
    }
    
    /**
     * Removes a Card at a certain index.
     * @param c the index of the Card to be removed
     * @return the card that was removed
     */
    public Card remove(int c){
        Card removedCard = cards.get(c);
        this.remove(removedCard);
        return removedCard;
    }
    
    /**
     * Removes all card from this Hand.
     */
    public void clearCards() {
        cards.clear();
        initOrderCards.clear();
    }
    
    /**
     * Counts the amount of Cards of a certain Suit in this Hand.
     * @param suit the Suit to be counted
     * @return the amount of the Suit in this Hand
     */
    public int countSuit(Card.Suit suit){
        return suitCounts[suit.ordinal()];       
    }
    
    /**
     * Counts the amount of Cards of a certain Rank in this Hand.
     * @param rank the Rank to be counted
     * @return the amount of the Rank in this Hand
     */
    public int countRank(Card.Rank rank){
        return rankCounts[rank.ordinal()];       
    }
    
    /**
     * @return the total value of this Hand
     */
    public int handValue(){
        return totalValue;       
    }
    
    /**
     * A method to determine whether the Hand is of all the same Suit.
     * @return true if the hand is all the same Suit
     */
    public boolean isFlush(){
        int types = 0;
        for(int i = 0; i < suitCounts.length; i++){
            if(suitCounts[i] > 0){
                types += 1;
            }
            if(types > 1){
                return false;
            }
        } 
        return true;
    }
    
    /**
     * A method to determine if the Cards in this Hand are consecutive.
     * @return true if the Hand is a straight
     */
    public boolean isStraight(){
        this.sortAscending();
        int temp = cards.get(0).getRank().ordinal()-1;        
        for(Card c: cards){
            if(c.getRank().ordinal() != temp + 1){
                return false;
            }else{
                temp = c.getRank().ordinal();
            }
        }
        return true;
    }
                
    /**
     * @return the Cards in this Hand
     */
    public ArrayList<Card> getCards(){
        return cards;
    }
    
    /**
     *A method to sort the cards into descending order.
     */
    public void sortDescending(){
        cards.sort(Card.CompareSuit);        
        cards.sort(Card.CompareDescending);
    }
    
    /**
     *A method to sort the cards into ascending order.
     */
    public void sortAscending(){        
        cards.sort(Card.CompareSuit); 
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        Card temp, temp2;   //holding variable

        while (flag){
                flag= false;    //set flag to false awaiting a possible swap
                for( j=0;  j < cards.size()-1;  j++ ){
                       if ( cards.get(j).compareTo(cards.get(j+1)) == 1 ){
                            temp = cards.get(j);   
                            temp2 = cards.get(j + 1);//swap elements
                            cards.remove(j);
                            cards.add(j, temp2);
                            cards.remove(j+1);
                            cards.add(j+1, temp);
                            flag = true;         //shows a swap occurred  
                      } 
                } 
        }
    }
    
    private void incrementCounts(Card c){
        rankCounts[c.getRank().ordinal()]++;
        suitCounts[c.getSuit().ordinal()]++;
        totalValue += c.getRank().getIntValue();
    }
    
    private void decrementCounts(Card c){
        rankCounts[c.getRank().ordinal()]--;
        suitCounts[c.getSuit().ordinal()]--;
        totalValue -= c.getRank().getIntValue();
    }
    
    @Override
    public String toString(){
        int i = 0;
        StringBuilder str = new StringBuilder("\n");
        str.append("__________________________________\n");
        for(Card c: cards){
            str.append(i).append(": ");
            str.append(c.toString());
            i++;
        }                  
        str.append("__________________________________\n");
        return str.toString();
    }
    
    @Override
    public Iterator<Card> iterator() {
        return new HandIterator();
    }

    /**
     * @return the size of the Hand
     */
    public int size() {
        return cards.size();
    }
    
    private class HandIterator implements Iterator<Card>{
        int pos = 0;
        @Override
        public boolean hasNext() {
            return pos < initOrderCards.size();
        }

        @Override
        public Card next() {
            return initOrderCards.get(pos++);
        }        
    }
    
    /**
     * A main method to demonstrate and test the methods in the Hand class.
     * @param args
     */
    public static void main(String[] args) {
        //example of a Hand
        Hand hand = new Hand();
        hand.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        hand.add(new Card(Card.Rank.EIGHT, Card.Suit.SPADES));
        hand.add(new Card(Card.Rank.EIGHT, Card.Suit.DIAMONDS));
        hand.add(new Card(Card.Rank.NINE, Card.Suit.DIAMONDS));
        hand.add(new Card(Card.Rank.TWO, Card.Suit.SPADES));
        System.out.println("Hand printout:" + hand);
        
        //examples of a sorting Hand           
        hand.sortDescending();        
        System.out.println("Hand printout after sort Descending:" + hand);
        
        hand.sortAscending();        
        System.out.println("Hand printout after sort Ascending:" + hand);
        
        //Flush and Straight examples
        hand = new Hand();
        hand.add(new Card(Card.Rank.SIX, Card.Suit.HEARTS));
        hand.add(new Card(Card.Rank.FIVE, Card.Suit.HEARTS));
        hand.add(new Card(Card.Rank.FOUR, Card.Suit.HEARTS));
        hand.add(new Card(Card.Rank.THREE, Card.Suit.HEARTS));
        hand.add(new Card(Card.Rank.TWO, Card.Suit.HEARTS));
        System.out.println("New hand for testing other functionality: " + hand);
        System.out.println("The hand is a straight: " + hand.isStraight());
        System.out.println("The hand is a flush: " + hand.isFlush());
        
        hand.add(new Card(Card.Rank.TEN, Card.Suit.SPADES));
        System.out.println("\nAfter adding TEN of SPADES: ");
        System.out.println("The hand is a straight: " + hand.isStraight());
        System.out.println("The hand is a flush: " + hand.isFlush());
        
        //countSuit example
        System.out.println("\nThere are " +
                hand.countSuit(Card.Suit.HEARTS) + " HEARTS in the Hand.");
        
        //countRank example
        hand.add(new Card(Card.Rank.TEN, Card.Suit.HEARTS));
        System.out.println("\nAfter adding TEN of HEARTS: ");
        System.out.println("There are " +
                hand.countRank(Card.Rank.TEN) + " TEN's in the Hand.");
        
    }    
}
