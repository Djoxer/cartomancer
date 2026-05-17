package dev.djoxer.cartomancer.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import dev.djoxer.cartomancer.R;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        RadioGroup radioGroup = v.findViewById(R.id.radio_group_language);
        RadioButton radioDe = v.findViewById(R.id.radio_lang_de);
        RadioButton radioEn = v.findViewById(R.id.radio_lang_en);

        // Aktuelle Sprache ermitteln und entsprechenden Button selektieren
        String currentLang = Locale.getDefault().getLanguage();
        if (currentLang.equals("de")) {
            radioDe.setChecked(true);
        } else {
            radioEn.setChecked(true);
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Locale locale;
            if (checkedId == R.id.radio_lang_de) {
                locale = Locale.GERMAN;
            } else {
                locale = Locale.ENGLISH;
            }
            setLocale(locale);
        });

        return v;
    }

    private void setLocale(Locale locale) {
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        requireContext().getResources().updateConfiguration(
                config,
                requireContext().getResources().getDisplayMetrics()
        );
        // Activity neu starten damit alle Strings neu geladen werden
        requireActivity().recreate();
    }
}
