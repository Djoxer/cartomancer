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
//        int i = index();
//        // Mondphase als Prozentwert: 50=Neumond(0%), 100=Vollmond(100%)
//        // Unter 50: abnehmend, über 50: zunehmend — jeweils *2 für Prozentwert
//        if (i == 50) return 0;
//        return i > 50 ? (100 - i) * 2 : i * 2;

        int ret;

        switch (index()) {
            case 49: ret = 2; break;
            case 48: ret = 4; break;
            case 47: ret = 6; break;
            case 46: ret = 8; break;
            case 45: ret = 10; break;
            case 44: ret = 12; break;
            case 43: ret = 14; break;
            case 42: ret = 16; break;
            case 41: ret = 18; break;
            case 40: ret = 20; break;

            case 39: ret = 22; break;
            case 38: ret = 24; break;
            case 37: ret = 26; break;
            case 36: ret = 28; break;
            case 35: ret = 30; break;
            case 34: ret = 32; break;
            case 33: ret = 34; break;
            case 32: ret = 36; break;
            case 31: ret = 38; break;
            case 30: ret = 40; break;

            case 29: ret = 42; break;
            case 28: ret = 44; break;
            case 27: ret = 46; break;
            case 26: ret = 48; break;
            case 25: ret = 50; break;
            case 24: ret = 52; break;
            case 23: ret = 54; break;
            case 22: ret = 56; break;
            case 21: ret = 58; break;
            case 20: ret = 60; break;

            case 19: ret = 62; break;
            case 18: ret = 64; break;
            case 17: ret = 66; break;
            case 16: ret = 68; break;
            case 15: ret = 70; break;
            case 14: ret = 72; break;
            case 13: ret = 74; break;
            case 12: ret = 76; break;
            case 11: ret = 78; break;
            case 10: ret = 80; break;

            case 9: ret = 82; break;
            case 8: ret = 84; break;
            case 7: ret = 86; break;
            case 6: ret = 88; break;
            case 5: ret = 90; break;
            case 4: ret = 92; break;
            case 3: ret = 94; break;
            case 2: ret = 96; break;
            case 1: ret = 98; break;

            case 50: ret = 0; break;
            case 51: ret = 2; break;
            case 52: ret = 4; break;
            case 53: ret = 6; break;
            case 54: ret = 8; break;
            case 55: ret = 10; break;
            case 56: ret = 12; break;
            case 57: ret = 14; break;
            case 58: ret = 16; break;
            case 59: ret = 18; break;

            case 60: ret = 20; break;
            case 61: ret = 22; break;
            case 62: ret = 24; break;
            case 63: ret = 26; break;
            case 64: ret = 28; break;
            case 65: ret = 30; break;
            case 66: ret = 32; break;
            case 67: ret = 34; break;
            case 68: ret = 36; break;
            case 69: ret = 38; break;

            case 70: ret = 40; break;
            case 71: ret = 42; break;
            case 72: ret = 44; break;
            case 73: ret = 46; break;
            case 74: ret = 48; break;
            case 75: ret = 50; break;
            case 76: ret = 52; break;
            case 77: ret = 54; break;
            case 78: ret = 56; break;
            case 79: ret = 58; break;

            case 80: ret = 60; break;
            case 81: ret = 62; break;
            case 82: ret = 64; break;
            case 83: ret = 66; break;
            case 84: ret = 68; break;
            case 85: ret = 70; break;
            case 86: ret = 72; break;
            case 87: ret = 74; break;
            case 88: ret = 76; break;
            case 89: ret = 78; break;

            case 90: ret = 80; break;
            case 91: ret = 82; break;
            case 92: ret = 84; break;
            case 93: ret = 86; break;
            case 94: ret = 88; break;
            case 95: ret = 90; break;
            case 96: ret = 92; break;
            case 97: ret = 94; break;
            case 98: ret = 96; break;
            case 99: ret = 98; break;

            default: ret = 100;
        }

        return ret;
    }
}
