import java.util.ArrayList;

public class Deck {

    private  ArrayList<Card> deck = new ArrayList<>();
    private Card currentcard;
    public Deck(){
        String[] value = { "A", "02", "03", "04", "05", "06", "07", "08", "09", "10","J", "Q", "K"};
        String[] suit = {"hearts", "spades", "diamonds","clubs"};
        for (String v : value) {
            for (String s : suit) {
                Card x = new Card(s, v);
                deck.add(x);
            }
        }
    }
    public Card getRandomCard(){
        if (deck.size()!=0) {
            int r = (int) (Math.random() * (deck.size()));
            currentcard = deck.get(r);
            deck.remove(r);
            return currentcard;
        }
        else {
            return new Card();
        }
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

}