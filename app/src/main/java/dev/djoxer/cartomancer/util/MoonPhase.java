package dev.djoxer.cartomancer.util;

import static android.provider.Settings.System.getString;

import androidx.annotation.NonNull;

import java.util.Date;

import dev.djoxer.cartomancer.R;

public class MoonPhase {

    public static int index() {
        double now, count, fullmoon;
        float p;

        now = (double) (new Date().getTime()) / 86400000;
        fullmoon = 1262286756000L / 86400000D;
        count = (now - fullmoon) / 29.530588;

        p = (float) Math.round(count * 100) / 100;

        int phase = (int) Math.floor((p - Math.floor(p)) * 100);
        return phase == 0 ? 100 : phase;
    }

    public static int percentage() {
        int i = index();
        // Mondphase als Prozentwert: 50=Neumond(0%), 100=Vollmond(100%)
        // Unter 50: abnehmend, über 50: zunehmend — jeweils *2 für Prozentwert
        if (i == 50) return 0;
        return i > 50 ? (100 - i) * 2 : i * 2;
    }
}
