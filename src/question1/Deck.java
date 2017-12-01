package question1;

import question1.*;
import question1.Card.Rank;
import question1.Card.Suit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

/**
 * A class to model a Deck of playing Cards. It contains a list of Cards and 
 * has the ability to shuffle and deal the cards.
 */
public class Deck implements Serializable, Iterable<Card>{

    static final long serialVersionUID = 101L;
    
    private ArrayList<Card> deck;
    
    /**
     * A default constructor that creates a new Deck of 52 Cards of all
     * possible combinations of Suit and Rank.
     */
    public Deck(){
        this.deck = new ArrayList<>();
        newDeck();   
    } 

    /** 
     * @return the Deck of this class
     */
    public ArrayList<Card> getDeck() {
        return deck;
    }

    /**
     * A method to randomise the order of the Cards in the Deck.
     */
    public void shuffle() {
        Random randGen = new Random();
        int k;
        for(int j = 0; j < deck.size(); j++){
            k = randGen.nextInt(deck.size());
            //swap the current index with a random index
            deck.set(j, deck.set(k, deck.get(j)));
        }
    }
    
    /**
     * A method to model the dealing of a single card from the top of the Deck.
     * @return The Card from the Top of the Deck
     */
    public Card deal(){
        Card topCard = deck.get(0);
        deck.remove(0);
        return topCard;
    }
    
    /**
     * @return The size of the Deck
     */
    public int size(){
        return deck.size();
    }
    
    /**
     * A method to remove any cards left in the Deck and fill it with 52 Cards.
     */
    public void newDeck(){
        if(!deck.isEmpty()){
            deck.clear();
        }
        Rank[] ranks = Card.Rank.values();
        Suit[] suits = Card.Suit.values();
        //fill deck with all possible cards
        for (int k = 0; k < suits.length; k++){
            for (int i = 0; i < ranks.length; i++){
                deck.add(new Card(ranks[i], suits[k]));
            }
        }
    }

    @Override
    public Iterator<Card> iterator() {
        return new DealIterator();
    }
    
    /**
     * An iterator that goes through all the odd cards in a deck first
     * an the even cards after it reaches the end.
     * @return 
     */
    public Iterator<Card> oddEvenIterator() {
        return new OddEvenIterator();
    }
    
    private class OddEvenIterator implements Iterator<Card>{
        int pos = 1;
        @Override
        public boolean hasNext() {
            return pos < deck.size();
        }

        @Override
        public Card next() {
            if(pos % 2 == 0){
                pos += 2;
                return deck.get(pos-2);
            }
            //if the end of the deck is reached go back and iterate over
            // even cards
            if(pos == deck.size()-1){
                pos = 0;
                return deck.get(deck.size()-1);                
            }
            pos += 2;
            return deck.get(pos-2);
        }        
    }
    
    private class DealIterator implements Iterator<Card>{
        int pos = 0;
        @Override
        public boolean hasNext() {
            return pos < deck.size();
        }

        @Override
        public Card next() {
            return deck.get(pos++);
        }  

        @Override
        public void remove() {
            deck.remove(pos);
        }       
    }
    /**
     * A method that writes the cards in the oddEven pattern.
     * @param os The output stream to write to
     * @throws IOException 
     */
    private void writeObject(java.io.ObjectOutputStream os) throws IOException{
        os.defaultWriteObject();
        Iterator<Card> iter = new question1.Deck.OddEvenIterator();
        while(iter.hasNext()){
            Card c = iter.next();
            os.writeObject(c);
        }
    }
    
    /**
     * A method to read an cards from an input stream into deck.
     * @param is The input stream to read the cards from
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private void readObject(ObjectInputStream is) 
            throws IOException, ClassNotFoundException{
        is.defaultReadObject();
        ArrayList<Card> cards = new ArrayList(); 
        Card card = (Card) is.readObject();
        
        while(card!=null){
            cards.add(card);
            try{
            card = (Card) is.readObject();
            }catch(IOException | ClassNotFoundException e){
                break;
            };
        }
        deck = cards;
    }
    
    /**
     * A method to load a serialised Deck object from a file.
     * @param filename The file the Deck was saved to
     * @return The loaded Deck
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static question1.Deck loadFromFile(String filename) 
            throws FileNotFoundException, IOException, ClassNotFoundException{
        FileInputStream fs = new FileInputStream(filename);
        ObjectInputStream os = new ObjectInputStream(fs);
        question1.Deck deck = (question1.Deck) os.readObject();
        os.close();        
        return deck;
    }
    
    /**
     * A method to serialise a Deck object to a file.
     * @param file The file to serialise to
     * @param deck The Deck object to serialise
     */
    public static void saveToFile(String file, question1.Deck deck){
        try{
            FileOutputStream fs = new FileOutputStream(file);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(deck);
            os.close();
        }
            catch(Exception e) {
            e.printStackTrace();
        }
    }   
    
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder("");
        str.append("__________________________________\n");
        for(Card c: deck){
            str.append(c);
        }                  
        str.append("__________________________________\n");
        return str.toString();
    }  
    
    /**
     * A main method to demonstrate and test the methods in the Deck class.
     * @param args
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        Deck deck = new Deck(); 

        System.out.println("full deck example:\n");   
        System.out.print(deck);
        
        deck.shuffle();
        System.out.println("shuffled deck example:\n");
        System.out.print(deck);

        Collections.sort(deck.getDeck(), Card.CompareDescending);
        Collections.sort(deck.getDeck(), Card.CompareSuit);
        
        System.out.println("sorted deck example:\n");
        System.out.print(deck);
        
        //Shows OddEvenIterator works
        System.out.println("OddEvenIterator example:\n");
        Iterator<Card> it = deck.oddEvenIterator();
        while(it.hasNext()){
            System.out.print(it.next());
        }
        
        //shows default iterator works
        System.out.println("DealIterator (default) example:\n");
        Iterator<Card> iter = deck.iterator();
        while(iter.hasNext()){
            System.out.print(iter.next());
        }
        
        //serialization
        System.out.println("\n");
        question1.Deck deck5 = new question1.Deck();
        saveToFile("deck.ser", deck5);
        question1.Deck deck6 = loadFromFile("deck.ser");
        System.out.println("____________________"
                + "_____________________");
        System.out.println("serialized deck");
         System.out.print("____________________"
                + "_____________________\n\n");
        //System.out.println(deck6);
        for(Card c: deck6.getDeck()){
            System.out.print(c);
        }
    }    
}

