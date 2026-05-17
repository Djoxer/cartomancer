package dev.djoxer.cartomancer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import dev.djoxer.cartomancer.R;
import dev.djoxer.cartomancer.util.MoonPhase;

public class StartpageFragment extends Fragment {

    private TextView textPhase, textPhasePer;
    private ImageView imagePhase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_startpage, container, false);

        textPhase = v.findViewById(R.id.text_phase);
        textPhase.setText(new MoonPhase().toString());

        imagePhase = v.findViewById(R.id.image_phase);
        imagePhase.setImageResource(getResources().getIdentifier("phase_" + MoonPhase.index(), "drawable", getActivity().getPackageName()));

        return v;
    }
}