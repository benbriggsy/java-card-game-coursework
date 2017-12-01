package cheat;

/**
 * A class that contains an exception for when a player cheats but 
 * says they wouldn't.
 */
public class CheatingWhenNotException extends Exception{
    public CheatingWhenNotException(String message) {
        super(message);
    }
}
