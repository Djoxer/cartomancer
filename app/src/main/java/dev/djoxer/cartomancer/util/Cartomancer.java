package dev.djoxer.cartomancer.util;

import static java.lang.String.join;
import static java.util.Collections.nCopies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Cartomancer {
    private Deck deck;
    private Hand[] hands;

    public Cartomancer() {
        this(buildDeck());
    }
    public Cartomancer(Deck deck) {
        this.deck = deck;
        Hand hand = new Hand(deck);
        hands = new Hand[]{hand};
    }

    public Deck getDeck() {
        return deck;
    }

    public static Stack<Card> merge(Stack<Card>[] stacks) {
        Stack<Card> stack = new Stack<>();
        for (Stack<Card> s : stacks) {
            while (!s.isEmpty()) {
                stack.push(s.pop());
            }
        }
        return stack;
    }

    public Stack<Card> shuffle() {
        return shuffle(deck.getDrawables());
    }
    public static Stack<Card> shuffle(Stack<Card> stack) {
        Collections.shuffle(stack);
        return stack;
    }

    public Stack<Card> sort() {
        return sort(deck.getDrawables());
    }
    public static Stack<Card> sort(Stack<Card> stack) {
        Collections.sort(stack);
        return stack;
    }

    public static Stack<Card> turn(Stack<Card> stack) {
        stack.sort(Collections.reverseOrder());
        return stack;
    }

    public static Stack<Card> stack(List<Card> list) {
        Stack<Card> stack = new Stack<>();
        stack.addAll(list);
        return turn(stack);
    }

    public static Card genDayCard(int day, int month, int year) {
        int num = sumDigits( year + month + day);
        if (num > 22) num = sumDigits(num);
        if (num == 22) return buildTrumps().list().get(0);
        return buildTrumps().list().get(num);
    }

    public static Card genSoulCard(int day, int month, int year) {
        int num = sumDigits( year + month + day);
        if (num >= 22) num = sumDigits(num);
        return buildTrumps().list().get(num);
    }

    public static Card getPersonalityCard(int year, int month, int day) {
        return genDayCard(day, month, year);
    }

    public static Card getCreativityCard(int year, int month, int day) {
        return buildTrumps().list().get(sumDigits(genSoulCard(day, month, year).value));
    }

    public static Card getSoulCard(int year, int month, int day) {
        return buildTrumps().list().get(sumDigits(getCreativityCard(year, month, day).value));
    }

    public static Card getGrowthCard(int month, int day) {
        int y = Calendar.getInstance().get(Calendar.YEAR);
        int year = month <= Calendar.getInstance().get(Calendar.MONTH) + 1 && day <= Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ? y : y - 1;
        return genDayCard(day, month, year);
    }

    public static String valueRoman(Card card) {
        return card.value == 0 ? "O" : join("", nCopies(card.value, "I"))
                .replace("IIIII", "V")
                .replace("IIII", "IV")
                .replace("VV", "X")
                .replace("VIV", "IX")
                .replace("XXXXX", "L")
                .replace("XXXX", "XL")
                .replace("LL", "C")
                .replace("LXL", "XC")
                .replace("CCCCC", "D")
                .replace("CCCC", "CD")
                .replace("DD", "M")
                .replace("DCD", "CM");
    }

    public static boolean isTrump(Card card) {
        return card.suit == Suit.TRUMPS;
    }

    public static int sumDigits(int num) {
        return num == 0 ? 0 : num % 10 + sumDigits(num / 10);
    }

    public static int rng(int min, int max) {
        return new Random().nextInt(max + min) + min;
    }

    private static List<Card> addToList(Suit suit, int count, List<Card> list, int offset) {
        for (int i = 0 + offset; i < count + offset; i++) {
            list.add(new Card(i, suit));
        }
        return list;
    }

    public static Deck buildDeck() {
        List<Card> list = new ArrayList<>();

        addToList(Suit.TRUMPS, 22, list, 0);
        addToList(Suit.SWORDS, 10, list, 1);
        addToList(Suit.SWORDS, 4, list, 11);
        addToList(Suit.CUPS, 10, list, 1);
        addToList(Suit.CUPS, 4, list, 11);
        addToList(Suit.WANDS, 10, list, 1);
        addToList(Suit.WANDS, 4, list, 11);
        addToList(Suit.COINS, 10, list, 1);
        addToList(Suit.COINS, 4, list, 11);

        return new Deck(list);
    }

    public static Deck buildTrumps() {
        List<Card> list = new ArrayList<>();
        addToList(Suit.TRUMPS, 22, list, 0);
        return new Deck(list);
    }

    public static Deck buildNumbered() {
        List<Card> list = new ArrayList<>();

        addToList(Suit.SWORDS, 10, list, 1);
        addToList(Suit.CUPS, 10, list, 1);
        addToList(Suit.WANDS, 10, list, 1);
        addToList(Suit.COINS, 10, list, 1);

        return new Deck(list);
    }

    public static Deck buildCourt() {
        List<Card> list = new ArrayList<>();

        addToList(Suit.SWORDS, 4, list, 11);
        addToList(Suit.CUPS, 4, list, 11);
        addToList(Suit.WANDS, 4, list, 11);
        addToList(Suit.COINS, 4, list, 11);

        return new Deck(list);
    }

    public static String printStack(Stack<Card> stack) {
        List<Card> list = new ArrayList<>(stack);
        StringBuilder s = new StringBuilder();
        s.append("Deck{\n");
        for (int i = 0; i < list.size(); i++) {
            s.append(i + 1 + ". " + list.get(i).toString() + '\n');
        }
        s.append('}');
        return s.toString();
    }
}
