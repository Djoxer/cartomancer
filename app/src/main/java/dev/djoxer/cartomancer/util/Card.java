package dev.djoxer.cartomancer.util;

import static dev.djoxer.cartomancer.util.Cartomancer.*;

public class Card implements Comparable<Card> {
    public final int value;
    public final Suit suit;

    public Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }

    @Override
    public int compareTo(Card card) {
        if (this.suit.getId() < card.suit.getId()) return -1;
        if (this.suit.getId() > card.suit.getId()) return 1;
        if (this.value < card.value) return -1;
        if (this.value > card.value) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Card{" + suit.getName() + '|' + (
                        isTrump(this) ? valueRoman(this) : value
                ) + '}').toString();
    }
}