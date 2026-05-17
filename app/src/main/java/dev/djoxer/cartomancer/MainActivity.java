package dev.djoxer.cartomancer;

import static android.os.Process.killProcess;
import static android.os.Process.myPid;
import static dev.djoxer.cartomancer.util.Cartomancer.buildDeck;

import android.app.DatePickerDialog;
import android.app.UiModeManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import dev.djoxer.cartomancer.fragments.AboutFragment;
import dev.djoxer.cartomancer.fragments.DayCardFragment;
import dev.djoxer.cartomancer.fragments.PersonalityCardsFragment;
import dev.djoxer.cartomancer.fragments.RandomCardFragment;
import dev.djoxer.cartomancer.fragments.SettingsFragment;
import dev.djoxer.cartomancer.fragments.SpreadsFragment;
import dev.djoxer.cartomancer.fragments.StartpageFragment;
import dev.djoxer.cartomancer.fragments.dialogs.SortingDialogFragment;
import dev.djoxer.cartomancer.fragments.sorting.SortingFragment;
import dev.djoxer.cartomancer.util.tarot.Card;
import dev.djoxer.cartomancer.util.Cartomancer;
import dev.djoxer.cartomancer.util.tarot.Suit;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener, SortingDialogFragment.BottomSheetListener {
    private DrawerLayout drawer;
    private SharedPreferences prefs;

    protected static Cartomancer cartomancer;
    public static Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Gespeicherte Sprache wiederherstellen
        prefs = getSharedPreferences("cartomancer_prefs", MODE_PRIVATE);
        String lang = prefs.getString("language", Locale.getDefault().getLanguage());
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Dark Mode (vor setContentView, keine View nötig)
        boolean darkMode = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                darkMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        setContentView(R.layout.activity_main);

        cartomancer = new Cartomancer(buildDeck());

        Date date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StartpageFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_startpage);
        }

        View header = navigationView.getHeaderView(0);
        AppCompatToggleButton toggleDarkmode = header.findViewById(R.id.toggle_darkmode);
        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        toggleDarkmode.setChecked(darkMode);
        // Initial-Icon entsprechend gespeichertem Wert setzen
        toggleDarkmode.setBackground(getResources().getDrawable(
                darkMode ? R.drawable.ic_mode_light : R.drawable.ic_mode_dark
        ));
        toggleDarkmode.setOnCheckedChangeListener((tgl, isChecked) -> {
            if (isChecked) {
                tgl.setBackground(getResources().getDrawable(R.drawable.ic_mode_dark));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                prefs.edit().putBoolean("dark_mode", true).apply();
            } else {
                tgl.setBackground(getResources().getDrawable(R.drawable.ic_mode_light));
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                prefs.edit().putBoolean("dark_mode", false).apply();
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        PersonalityCardsFragment fragment = PersonalityCardsFragment.newInstance(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_startpage) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new StartpageFragment()).commit();
        } else if (id == R.id.nav_day_card) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DayCardFragment()).commit();
        } else if (id == R.id.nav_personality_cards) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new PersonalityCardsFragment()).commit();
        } else if (id == R.id.nav_random_card) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new RandomCardFragment()).commit();
        } else if (id == R.id.nav_sorting) {
            SortingFragment fragment = SortingFragment.newInstance(false);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        } else if (id == R.id.nav_spreads) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SpreadsFragment()).commit();
        } else if (id == R.id.nav_share) {
            Toast.makeText(this, R.string.nav_share, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SettingsFragment()).commit();
            Toast.makeText(this, R.string.nav_settings, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_help) {
            Toast.makeText(this, R.string.nav_help, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new AboutFragment()).commit();
            Toast.makeText(this, R.string.nav_about, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_exit) {
            finishAffinity();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // killProcess nur wenn App wirklich beendet wird, nicht bei Recreation
        if (isFinishing()) {
            killProcess(myPid());
        }
    }

    @Override
    public void onStartButtonClicked() {
        SortingFragment fragment = SortingFragment.newInstance(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public String getCardName(Card card) {
        String postfix = Cartomancer.isTrump(card) ? "" : ' ' + getString(R.string.text_of) + ' ' + getSuitArray(card)[0];
        if (!Cartomancer.isTrump(card) && card.value > 1 && card.value < 11) postfix = "";
        return getSuitArray(card)[card.value] + (card.value > 10 ? "" : postfix);
    }

    public String getValueName(Card card) {
        String[] numbers = getResources().getStringArray(R.array.numbers);
        if (Cartomancer.isTrump(card)) return getString(R.string.text_trump) + ' ' + Cartomancer.valueRoman(card);
        String postfix = ' ' + getString(R.string.text_of) + ' ' + getSuitArray(card)[0];
        String prefix = card.value == 1 ? getSuitArray(card)[1] : card.value > 10 ? getSuitArray(card)[card.value] : numbers[card.value] + "";
        return prefix + postfix;
    }

    public String[] getSuitArray(Card card) {
        Resources r = getResources();
        String[] suit = r.getStringArray(R.array.trumps);
        if (card.suit == Suit.SWORDS) {
            suit = r.getStringArray(R.array.swords);
        } else if (card.suit == Suit.CUPS) {
            suit = r.getStringArray(R.array.cups);
        } else if (card.suit == Suit.WANDS) {
            suit = r.getStringArray(R.array.wands);
        } else if (card.suit == Suit.COINS) {
            suit = r.getStringArray(R.array.coins);
        }
        return suit;
    }

    public int getImageSrc(Card card) {
        return getResources().getIdentifier(card.suit.name().toLowerCase() + '_' + card.value, "drawable", getPackageName());
    }
}