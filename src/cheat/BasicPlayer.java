package cheat;

/**
 * An implementation of Player that is used for the players in BasicCheat.
 */
public class BasicPlayer implements Player{
    Hand hand;
    Strategy strategy;
    CardGame cardGame;
    
    public BasicPlayer(CardGame cg){
        cardGame = cg;
        hand = new Hand();
    }

    @Override
    public void addCard(Card c) {
        hand.add(c);
    }

    @Override
    public void addHand(Hand h) {
        hand.add(h);
    }

    @Override
    public int cardsLeft() {
        return hand.size();
    }

    @Override
    public void setGame(CardGame g) {
        cardGame = g;
    }

    @Override
    public void setStrategy(StrategyFactory.StrategyType s) {
        strategy = StrategyFactory.select(s);
    }    
    
    @Override
    public Bid playHand(Bid b) {
        return strategy.chooseBid(b, hand, strategy.cheat(b, hand));
    }

    @Override
    public boolean callCheat(Bid b) {
        return strategy.callCheat(hand, b);
    }

    @Override
    public Strategy getStrategy() {
        return strategy;
    }
    
}
