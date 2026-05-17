package dev.djoxer.cartomancer.util;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> list;
    private Deck deck;
    private int max;

    public Hand(Deck deck) {
        this(deck, 7);
    }

    public Hand(Deck deck, int max) {
        this.max = max;
        this.deck = deck;
        this.list = new ArrayList<>();
    }

    public List<Card> getList() {
        return list;
    }

    public void draw() {
        if (!deck.getDrawables().isEmpty() && count() < max) {
            list.add(deck.getDrawables().pop());
        }
    }

    public void discard(Card card) {
        if (list.remove(card)) deck.getDiscarded().push(card);
    }

    public void discardAll() {
        list.forEach(deck.getDiscarded()::push);
        list.clear();
    }

    public int count() {
        return list.size();
    }

    @Override
    public String toString() {
        if (list.isEmpty()) return "Hand{EMPTY}";
        StringBuilder s = new StringBuilder();
        s.append("Hand{\n");
        for (int i = 0; i < getList().size(); i++) {
            s.append(i + 1 + ". " + getList().get(i).toString() + '\n');
        }
        s.append('}');
        return s.toString();
    }
}
