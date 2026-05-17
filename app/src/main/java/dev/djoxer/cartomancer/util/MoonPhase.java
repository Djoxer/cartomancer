package dev.djoxer.cartomancer.util;

import static android.provider.Settings.System.getString;

import androidx.annotation.NonNull;

import java.util.Date;

import dev.djoxer.cartomancer.R;

public class MoonPhase {
    public static double cyclePosition() {
        long knownNewMoon = 947182440000L;
        long now = System.currentTimeMillis();
        double synodicMonth = 29.530588853 * 24 * 60 * 60 * 1000;
        return ((now - knownNewMoon) % synodicMonth) / synodicMonth;
    }

    public static int percentage() {
        double pos = cyclePosition();
        // 0→0%, 0.5→100%, 1→0% — Sinuskurve
        return (int) Math.round((1 - Math.cos(pos * 2 * Math.PI)) / 2 * 100);
    }

    public static int index() {
        double pos = cyclePosition();
        // pos 0.0 = Neumond → 50, pos 0.5 = Vollmond → 100
        // pos 0.0-0.5 zunehmend: 50→100
        // pos 0.5-1.0 abnehmend: 100→50
        if (pos <= 0.5) {
            return 50 + (int) Math.round(pos * 100);
        } else {
            return 100 - (int) Math.round((pos - 0.5) * 100);
        }
    }
}
