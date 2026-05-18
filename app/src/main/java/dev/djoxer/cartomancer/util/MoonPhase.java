package dev.djoxer.cartomancer.util;

import java.util.Date;

public class MoonPhase {

    // Bekannter Neumond als Referenzpunkt (2000-01-06 18:14 UTC)
    private static final long KNOWN_NEW_MOON = 947182440000L;
    // Synodischer Monat in Millisekunden
    private static final double SYNODIC_MONTH_MS = 29.530588853 * 24 * 60 * 60 * 1000;

    /**
     * Gibt die aktuelle Position im Mondzyklus zurück (0.0 = Neumond, 0.5 = Vollmond, 1.0 = nächster Neumond)
     */
    public static double cyclePosition() {
        long now = System.currentTimeMillis();
        return ((now - KNOWN_NEW_MOON) % SYNODIC_MONTH_MS) / SYNODIC_MONTH_MS;
    }

    /**
     * Beleuchtungsgrad in % (0 = Neumond, 100 = Vollmond)
     */
    public static int percentage() {
        double pos = cyclePosition();
        // Sinuskurve: 0→0%, 0.5→100%, 1→0%
        return (int) Math.round((1 - Math.cos(pos * 2 * Math.PI)) / 2 * 100);
    }

    /**
     * Index für Drawable-Mapping: 50 = Neumond, 100 = Vollmond
     */
    public static int index() {
        double pos = cyclePosition();
        if (pos <= 0.5) {
            return 50 + (int) Math.round(pos * 100);
        } else {
            return 100 - (int) Math.round((pos - 0.5) * 100);
        }
    }

    /**
     * Tage seit letztem Neumond (z.B. 1.3 = vor 1 Tag und ~7 Stunden)
     */
    public static double daysSinceNewMoon() {
        return cyclePosition() * 29.530588853;
    }

    /**
     * Tage bis zum nächsten Ereignis:
     * targetPos: 0.0 = Neumond, 0.25 = Erstes Viertel, 0.5 = Vollmond, 0.75 = Letztes Viertel
     */
    public static double daysUntil(double targetPos) {
        double current = cyclePosition();
        double diff = targetPos - current;
        if (diff <= 0) diff += 1.0; // bereits vorbei → nächsten Zyklus
        return diff * 29.530588853;
    }

    /**
     * Datum des nächsten Ereignisses als Date-Objekt
     */
    public static Date dateOfNext(double targetPos) {
        double daysUntil = daysUntil(targetPos);
        long ms = (long) (daysUntil * 24 * 60 * 60 * 1000);
        return new Date(System.currentTimeMillis() + ms);
    }
}
