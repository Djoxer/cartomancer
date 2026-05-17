package dev.djoxer.cartomancer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import dev.djoxer.cartomancer.util.Card;
import dev.djoxer.cartomancer.util.Cartomancer;

import java.util.ArrayList;
import java.util.List;

public class RandomCardFragment extends Fragment {

    private TextView textViewCardTitle;
    private ImageView imageViewCardIllustration;
    private ConstraintLayout wrapper;
    private LinearLayout cardBase;
    private List<Card> history;
    private List<Card> list = Cartomancer.buildDeck().list();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_random_card, container, false);

        history = new ArrayList<>();
        history.add(Cartomancer.buildTrumps().list().get(0));

        textViewCardTitle = v.findViewById(R.id.text_card_name);
        imageViewCardIllustration = v.findViewById(R.id.image_card_illustration);
        cardBase = v.findViewById(R.id.card_base);
        wrapper = v.findViewById(R.id.wrapper);

        cardBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRandomCard(Cartomancer.rng(0, 77));
            }
        });

        cardBase.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            @Override
            public void onSwipeLeft() {
                updateRandomCard(Cartomancer.rng(0, 77));
            }

            @Override
            public void onSwipeRight() {
                Card card = history.get(history.size() - 2);
                replaceCard(card);
            }

            @Override
            public void onSwipeBottom() {
                wrapper.setVisibility(View.INVISIBLE);
                cardBase.setBackgroundResource(R.drawable.cardback);
            }

            @Override
            public void onSwipeTop() {
                wrapper.setVisibility(View.VISIBLE);
                cardBase.setBackgroundResource(0);
            }

            @Override
            public void onDownTouch() {
                //Toast.makeText(getContext(), ((MainActivity)getActivity()).getValueName(history.get(history.size() - 1)), Toast.LENGTH_SHORT).show();
            }
        });

        updateRandomCard(Cartomancer.rng(0, 77));

        return v;
    }

    public void updateRandomCard(int rnd) {
        Card card = list.get(rnd);
        history.add(card);
        replaceCard(card);
    }

    private void replaceCard(Card card) {
        Toast.makeText(getContext(), ((MainActivity)getActivity()).getValueName(card), Toast.LENGTH_SHORT).show();
        imageViewCardIllustration.setImageResource(((MainActivity)getActivity()).getImageSrc(card));
        textViewCardTitle.setText(((MainActivity)getActivity()).getCardName(card).toUpperCase());
    }
}