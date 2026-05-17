package dev.djoxer.cartomancer.fragments;

import static dev.djoxer.cartomancer.MainActivity.calendar;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import dev.djoxer.cartomancer.MainActivity;
import dev.djoxer.cartomancer.R;
import dev.djoxer.cartomancer.util.tarot.Card;
import dev.djoxer.cartomancer.util.Cartomancer;

import java.util.Calendar;
import java.util.Date;

public class DayCardFragment extends Fragment {

    private TextView textCardName;
    private ImageView imageCardIllustration;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day_card, container, false);

        textCardName = v.findViewById(R.id.text_card_name);
        imageCardIllustration = v.findViewById(R.id.image_card_illustration);

        LinearLayout cardBase = v.findViewById(R.id.card_base);
        ConstraintLayout wrapper = v.findViewById(R.id.wrapper);

        cardBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wrapper.getVisibility() == View.VISIBLE) {
                    wrapper.setVisibility(View.INVISIBLE);
                    cardBase.setBackgroundResource(R.drawable.cardback);
                } else {
                    wrapper.setVisibility(View.VISIBLE);
                    cardBase.setBackgroundResource(0);
                }
            }
        });



        Date date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        Card card = Cartomancer.genDayCard(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));

        imageCardIllustration.setImageResource(((MainActivity)getActivity()).getImageSrc(card));
        textCardName.setText(((MainActivity)getActivity()).getCardName(card).toUpperCase());

        return v;
    }
}