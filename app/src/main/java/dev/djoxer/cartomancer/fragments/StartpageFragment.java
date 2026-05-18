package dev.djoxer.cartomancer.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import dev.djoxer.cartomancer.R;
import dev.djoxer.cartomancer.util.MoonPhase;

public class StartpageFragment extends Fragment {

    private TextView textPhase, textPhasePercent, textMoonSign, textSunSign, textKin;
    private TextView textClock;
    private ImageView imagePhase;
    private LinearLayout moonOverlay;

    // Handler für den tickenden Clock-Update
    private final Handler clockHandler = new Handler(Looper.getMainLooper());
    private final Runnable clockRunnable = new Runnable() {
        @Override
        public void run() {
            updateClock();
            // Jede Minute neu aufrufen
            clockHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_startpage, container, false);

        textPhase        = v.findViewById(R.id.text_phase);
        textMoonSign     = v.findViewById(R.id.text_moon_sign);
        textSunSign      = v.findViewById(R.id.text_sun_sign);
        textKin          = v.findViewById(R.id.text_kin);
        imagePhase       = v.findViewById(R.id.image_phase);
        textClock        = v.findViewById(R.id.text_clock);
        moonOverlay      = v.findViewById(R.id.moon_overlay);

        // Mondphase anzeigen
        textPhase.setText(getMoonPhaseName(MoonPhase.index()));
        imagePhase.setImageResource(
                getResources().getIdentifier(
                        "phase_" + MoonPhase.index(),
                        "drawable",
                        requireActivity().getPackageName()
                )
        );

        // Labels
        ((TextView) v.findViewById(R.id.label_moon)).setText("☽  " + getString(R.string.label_moon));
        ((TextView) v.findViewById(R.id.label_sun)).setText("☀  " + getString(R.string.label_sun));
        ((TextView) v.findViewById(R.id.label_kin)).setText("✦  " + getString(R.string.label_kin));

        // Sonnenzeichen
        Calendar cal = Calendar.getInstance();
        textSunSign.setText(getSunSign(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));

        // Mondzeichen + KIN folgen mit Maya-Integration
        textMoonSign.setText("—");
        textKin.setText("—");

        // Uhrzeit starten
        updateClock();
        clockHandler.post(clockRunnable); // sofort starten, kein Delay

        // Overlay-Daten befüllen
        setupMoonOverlay(v);

        // Mond-Tap → Overlay ein/ausblenden
        imagePhase.setOnClickListener(view -> {
            if (moonOverlay.getVisibility() == View.VISIBLE) {
                moonOverlay.setVisibility(View.GONE);
            } else {
                moonOverlay.setVisibility(View.VISIBLE);
            }
        });

        // Schließen-Button im Overlay
        v.findViewById(R.id.overlay_close).setOnClickListener(view ->
                moonOverlay.setVisibility(View.GONE)
        );

        return v;
    }

    // Format mit Sekunden + korrekte Timezone
    private void updateClock() {
        if (textClock == null) return;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault()); // System-Timezone explizit setzen
        textClock.setText(sdf.format(new Date()));
    }

    private void setupMoonOverlay(View v) {
        SimpleDateFormat dateFmt = new SimpleDateFormat("dd.MM. HH:mm", Locale.getDefault());

        // Beleuchtungsgrad — %1$d%% in strings.xml
        ((TextView) v.findViewById(R.id.overlay_illumination))
                .setText(getString(R.string.moon_overlay_illumination, MoonPhase.percentage()));

        // Tage seit letztem Neumond — %1$d T %2$d Std
        double sinceNew = MoonPhase.daysSinceNewMoon();
        int sinceDays  = (int) sinceNew;
        int sinceHours = (int) ((sinceNew - sinceDays) * 24);
        ((TextView) v.findViewById(R.id.overlay_last_new))
                .setText(getString(R.string.moon_overlay_last_new, sinceDays, sinceHours));

        // Nächster Vollmond — %1$s T (%2$s)
        String daysFullMoon = String.format(Locale.getDefault(), "%.1f", MoonPhase.daysUntil(0.5));
        String dateFullMoon = dateFmt.format(MoonPhase.dateOfNext(0.5));
        ((TextView) v.findViewById(R.id.overlay_next_full))
                .setText(getString(R.string.moon_overlay_next_full, daysFullMoon, dateFullMoon));

        // Erstes Viertel — %1$s T (%2$s)
        String daysFirstQ = String.format(Locale.getDefault(), "%.1f", MoonPhase.daysUntil(0.25));
        String dateFirstQ = dateFmt.format(MoonPhase.dateOfNext(0.25));
        ((TextView) v.findViewById(R.id.overlay_next_first_quarter))
                .setText(getString(R.string.moon_overlay_next_first_quarter, daysFirstQ, dateFirstQ));

        // Nächster Neumond — %1$s T (%2$s)
        String daysNextNew = String.format(Locale.getDefault(), "%.1f", MoonPhase.daysUntil(0.0));
        String dateNextNew = dateFmt.format(MoonPhase.dateOfNext(0.0));
        ((TextView) v.findViewById(R.id.overlay_next_new))
                .setText(getString(R.string.moon_overlay_next_new, daysNextNew, dateNextNew));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clock-Handler stoppen wenn Fragment zerstört wird — sonst Memory Leak
        clockHandler.removeCallbacks(clockRunnable);
    }

    private String getMoonPhaseName(int phase) {
        if (phase == 75)  return getString(R.string.moon_first_quarter);
        if (phase == 100) return getString(R.string.moon_full);
        if (phase == 25)  return getString(R.string.moon_last_quarter);
        if (phase == 50)  return getString(R.string.moon_new);
        if (phase < 25)   return getString(R.string.moon_waning_crescent);
        if (phase < 50)   return getString(R.string.moon_waning_gibbous);
        if (phase < 75)   return getString(R.string.moon_waxing_crescent);
        return getString(R.string.moon_waxing_gibbous);
    }

    private String getSunSign(int month, int day) {
        int resId;
        if      ((month == 3  && day >= 21) || (month == 4  && day <= 19)) resId = R.string.sign_aries;
        else if ((month == 4  && day >= 20) || (month == 5  && day <= 20)) resId = R.string.sign_taurus;
        else if ((month == 5  && day >= 21) || (month == 6  && day <= 20)) resId = R.string.sign_gemini;
        else if ((month == 6  && day >= 21) || (month == 7  && day <= 22)) resId = R.string.sign_cancer;
        else if ((month == 7  && day >= 23) || (month == 8  && day <= 22)) resId = R.string.sign_leo;
        else if ((month == 8  && day >= 23) || (month == 9  && day <= 22)) resId = R.string.sign_virgo;
        else if ((month == 9  && day >= 23) || (month == 10 && day <= 22)) resId = R.string.sign_libra;
        else if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) resId = R.string.sign_scorpio;
        else if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) resId = R.string.sign_sagittarius;
        else if ((month == 12 && day >= 22) || (month == 1  && day <= 19)) resId = R.string.sign_capricorn;
        else if ((month == 1  && day >= 20) || (month == 2  && day <= 18)) resId = R.string.sign_aquarius;
        else resId = R.string.sign_pisces;
        return getString(resId);
    }
}