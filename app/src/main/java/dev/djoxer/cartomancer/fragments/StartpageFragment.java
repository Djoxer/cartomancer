package dev.djoxer.cartomancer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

import dev.djoxer.cartomancer.R;
import dev.djoxer.cartomancer.util.MoonPhase;

public class StartpageFragment extends Fragment {

    private TextView textPhase, textPhasePercent, textMoonSign, textSunSign, textKin;
    private ImageView imagePhase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_startpage, container, false);

        textPhase = v.findViewById(R.id.text_phase);
        textPhasePercent = v.findViewById(R.id.text_phase_percent);
        textMoonSign = v.findViewById(R.id.text_moon_sign);
        textSunSign = v.findViewById(R.id.text_sun_sign);
        textKin = v.findViewById(R.id.text_kin);
        imagePhase = v.findViewById(R.id.image_phase);

        // Mondphase
        textPhase.setText(new MoonPhase().toString());
        textPhasePercent.setText(MoonPhase.percentage() + "%");
        imagePhase.setImageResource(
                getResources().getIdentifier(
                        "phase_" + MoonPhase.index(),
                        "drawable",
                        getActivity().getPackageName()
                )
        );

        // Sonnenzeichen aus aktuellem Datum berechnen
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        textSunSign.setText(getSunSign(month, day));

        // Mondzeichen + KIN kommen später mit Maya-Integration
        textMoonSign.setText("—");
        textKin.setText("—");

        return v;
    }

    // Sonnenzeichen aus Monat/Tag berechnen
    private String getSunSign(int month, int day) {
        int resId;
        if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) resId = R.string.sign_aries;
        else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) resId = R.string.sign_taurus;
        else if ((month == 5 && day >= 21) || (month == 6 && day <= 20)) resId = R.string.sign_gemini;
        else if ((month == 6 && day >= 21) || (month == 7 && day <= 22)) resId = R.string.sign_cancer;
        else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) resId = R.string.sign_leo;
        else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) resId = R.string.sign_virgo;
        else if ((month == 9 && day >= 23) || (month == 10 && day <= 22)) resId = R.string.sign_libra;
        else if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) resId = R.string.sign_scorpio;
        else if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) resId = R.string.sign_sagittarius;
        else if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) resId = R.string.sign_capricorn;
        else if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) resId = R.string.sign_aquarius;
        else resId = R.string.sign_pisces;
        return getString(resId);
    }
}