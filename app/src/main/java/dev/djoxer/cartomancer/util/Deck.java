package dev.djoxer.cartomancer.util;

import static dev.djoxer.cartomancer.util.Cartomancer.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Deck {
    private final Set<Card> set;
    private Stack<Card> drawPile;
    private Stack<Card> discardPile;

    public Deck(List<Card> list) {
        set = new HashSet<>(list);
        discardPile = new Stack<>();
        drawPile = new Stack<>();
        drawPile.addAll(list);
    }

    public Stack<Card> getDrawables() {
        return drawPile;
    }

    public Stack<Card> getDiscarded() {
        return discardPile;
    }

    public int size() {
        return set.size();
    }

    public List<Card> list() {
        List<Card> list = new ArrayList<>();
        Stack<Card> stack = new Stack<>();
        set.forEach(stack::push);
        turn(stack);
        list.addAll(sort(stack));
        return list;
    }

    public List<Card> discarded() {
        return new ArrayList<>(discardPile);
    }

    public List<Card> drawables() {
        return new ArrayList<>(drawPile);
    }

    public List<Card> circulating() {
        List<Card> list = new ArrayList<>();

        for (Card card : list()) {
            if (!discardPile.contains(card) && !drawPile.contains(card)) {
                list.add(card);
            }
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Deck{\n");
        for (int i = 0; i < list().size(); i++) {
            s.append(i + 1 + ". " + list().get(i).toString() + '\n');
        }
        s.append('}');
        return s.toString();
    }
}
