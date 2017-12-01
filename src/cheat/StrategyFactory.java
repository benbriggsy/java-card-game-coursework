package cheat;

/**
 * A class to set the Strategy of a player by taking an enum describing which 
 * Strategy to use.
 */
public class StrategyFactory {
    
    public enum StrategyType {BASIC, HUMAN, THINKER, MY;}
    
    public static Strategy select(StrategyType s){
        if(s == StrategyType.BASIC){
            return new BasicStrategy();
        }else if(s == StrategyType.HUMAN){
            return new HumanStrategy();
        }else if(s == StrategyType.THINKER){
            return new ThinkerStrategy();
        }else if(s == StrategyType.MY){
            return new MyStrategy();
        }else{
            System.out.println("There was an error, Basic strategy chosen.");
            return new BasicStrategy();
        }
    }
}
