package dev.djoxer.cartomancer.fragments;

import static dev.djoxer.cartomancer.MainActivity.calendar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dev.djoxer.cartomancer.fragments.dialogs.DatePickerDialogFragment;
import dev.djoxer.cartomancer.MainActivity;
import dev.djoxer.cartomancer.R;
import dev.djoxer.cartomancer.util.tarot.Card;
import dev.djoxer.cartomancer.util.Cartomancer;

import java.util.Calendar;

public class PersonalityCardsFragment extends Fragment {
    private static final String ARG_YEAR = "argYear";
    private static final String ARG_MONTH = "argMonth";
    private static final String ARG_DAY = "argDay";

    private TextView textCardName;
    private ImageView imageCardIllustration;
    private DatePickerDialogFragment datePicker;

    private int day, month, year;

    public static PersonalityCardsFragment newInstance(int year, int month, int day) {
        PersonalityCardsFragment fragment = new PersonalityCardsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_personality_cards, container, false);

        BottomNavigationView bottomNav = v.findViewById(R.id.navigation_bottom);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        textCardName = v.findViewById(R.id.text_card_name);
        imageCardIllustration = v.findViewById(R.id.image_card_illustration);
        datePicker = new DatePickerDialogFragment();
        CardView cardView = v.findViewById(R.id.card_view);
        cardView.setVisibility(View.INVISIBLE);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardView.setVisibility(View.INVISIBLE);
                datePicker.show(getParentFragmentManager(), "date picker");
            }
        });

        datePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                cardView.setVisibility(View.VISIBLE);
            }
        });

        if (getArguments() == null) {
            cardView.setVisibility(View.INVISIBLE);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            datePicker.show(getParentFragmentManager(), "date picker");
        } else {
            year = getArguments().getInt(ARG_YEAR);
            month = getArguments().getInt(ARG_MONTH) + 1;
            day = getArguments().getInt(ARG_DAY);
            cardView.setVisibility(View.VISIBLE);
            setResources();
        }

        return v;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Card card = Cartomancer.getPersonalityCard(year, month, day);

            int navId = item.getItemId();
            if (navId == R.id.nav_creativity_card) {
                card = Cartomancer.getCreativityCard(year, month, day);
                Toast.makeText(getActivity(), getString(R.string.text_creativity_card), Toast.LENGTH_SHORT).show();
            } else if (navId == R.id.nav_soul_card) {
                card = Cartomancer.getSoulCard(year, month, day);
                Toast.makeText(getActivity(), getString(R.string.text_soul_card), Toast.LENGTH_SHORT).show();
            } else if (navId == R.id.nav_growth_card) {
                card = Cartomancer.getGrowthCard(month, day);
                Toast.makeText(getActivity(), getString(R.string.text_growth_card), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.text_personality_card), Toast.LENGTH_SHORT).show();
            }

            setResources(card);

            return true;
        }
    };

    private void setResources() {
        Card card = Cartomancer.getPersonalityCard(year, month, day);
        setResources(card);
    }

    private void setResources(Card card) {
        imageCardIllustration.setImageResource(((MainActivity)getActivity()).getImageSrc(card));
        textCardName.setText(((MainActivity)getActivity()).getCardName(card).toUpperCase());
    }
}