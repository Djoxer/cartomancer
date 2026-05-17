package dev.djoxer.cartomancer.util;

public enum Suit {
    TRUMPS(0),
    SWORDS(1),
    CUPS(2),
    WANDS(3),
    COINS(4);

    private int id;

    Suit(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        String name = name().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Suit{" + id + '|' + name() + '}').toString();
    }
}
