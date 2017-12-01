package cheat;

import java.util.Scanner;

/**
 * An implementation of Strategy where a user decides what cards to play and
 * whether to cheat or not.
 */
public class HumanStrategy implements Strategy{

    @Override
    public boolean cheat(Bid b, Hand h) {        
        Scanner in = new Scanner(System.in);
        boolean cheat;
        //if first bid/bid since empty discard
        //cheat if there are no twos in players hand 
        if(b.h.size() == 0 && h.countRank(Card.Rank.TWO) < 1){             
                System.out.println("Unfortunately you have to cheat...");
                cheat = true;
        }else{
            if(h.countRank(b.getRank()) < 1 
                    && h.countRank(b.getRank().getNext()) < 1){
                System.out.println("Unfortunately you have to cheat...");
                //cheat if the player has none of the 
                //legally playable card in their hand 
                cheat = true;                
            }else{//let the player choose whether to cheat or not        
                System.out.print("\nYour hand is: ");        
                System.out.println(h);
                System.out.println("would you like to cheat? [y/any key]: ");
                String str = in.nextLine();
                cheat = str.equals("y") || str.equals("Y");                
            }
        }
        return cheat;
    }

    @Override
    public Bid chooseBid(Bid b, Hand h, boolean cheat) {     
        Scanner in = new Scanner(System.in);
        Hand handToPlay = new Hand();
        Card.Rank rankToPlay;
        boolean playHand = false;
        int userInput;
        //sort for readability
        h.sortAscending();
        System.out.println(h);
        while(!playHand){     
            System.out.println("enter the number of a card to play" 
                    + " from your hand or [c] to continue:");
            String str = in.nextLine();//scan users input to variable str
            try{
                if(!"c".equals(str)){//if user doesn't want to continue
                    //check input was a valid integer
                    if(Integer.parseInt(str) > h.size() 
                            || Integer.parseInt(str) < 0){
                        System.out.println("you must enter an integer " 
                                + "displayed beside cards in your hand");                
                    }else{//parse integer and add relevant card from hand
                        userInput = Integer.parseInt(str);
                        handToPlay.add(h.getCards().get(userInput));
                    }
                }else{
                    if(handToPlay.size() < 1){//check that a card was added
                        System.out.println("You must add at least one card!");
                    }else{
                        //cheated is true if the player cheated
                        boolean cheated = handToPlay.getCards().get(0).getRank()
                                !=  b.getRank() 
                                && handToPlay.getCards().get(0).getRank()
                                !=  b.getRank().getNext();
                        //if they said the wouldn't cheat but they have
                        if(!cheat && cheated){
                            //remove the cards from hand to play
                            Hand handRemove = new Hand();
                            handRemove.add(handToPlay);
                            handToPlay.remove(handRemove);
                            throw new CheatingWhenNotException(
                                    "You said you wouldn't cheat!"
                                    + " looks like you'll have to enter"
                                    + " the cards again...");
                        }else{//otherwise exit loop
                            playHand = true;
                        }
                    } 
                }
            //if user entered something incorrect go back
            }catch(NumberFormatException | CheatingWhenNotException e){
                System.out.println(e + "\nsomething went wrong," 
                        + " lets try that again!:");
            }
        }
        if(cheat){ //Choose the Rank to say you are playing          
            System.out.println("to cheat with the same rank as" 
                    + " the previous bid enter [s],\n"
                    + "to cheat with the next rank from the" 
                    + " previous bid enter [any key]:");
            String str = in.nextLine();
            if("s".equals(str)){
                rankToPlay = b.getRank();
            }else{
                rankToPlay = b.getRank().getNext();
            }
        }else{
            rankToPlay = handToPlay.getCards().get(0).getRank();
        }  
        h.remove(handToPlay);
        return new Bid(handToPlay, rankToPlay);
    }

    @Override
    public boolean callCheat(Hand h, Bid b) {        
        Scanner in = new Scanner(System.in);
        boolean callCheat;
                
        System.out.print("\nYour hand is: ");         
        h.sortAscending();
        System.out.println(h);
        System.out.println("would you like to call cheat? [y/n]: ");
        String str = in.nextLine();
        callCheat = str.equals("y") || str.equals("Y") ||
                str.equals("yes")|| str.equals("Yes");
        return callCheat;
    }
    
}
