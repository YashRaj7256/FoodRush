package com.foodrush.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.foodrush.R;
import com.foodrush.fragments.CartFragment;
import com.foodrush.fragments.HomeFragment;
import com.foodrush.fragments.OrdersFragment;
import com.foodrush.fragments.ProfileFragment;
import com.foodrush.utils.NotificationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;
import java.util.Random;

/**
 * MainActivity — root activity that hosts the BottomNavigationView
 * and swaps between Home, Cart, Orders, and Profile fragments.
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private Handler handler = new Handler();
    private Runnable notificationRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🌐 LOAD SAVED LANGUAGE
        loadSavedLanguage();

        setContentView(R.layout.activity_main);

        // 🔔 Create notification channel
        NotificationHelper.createNotificationChannel(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
        }

        bottomNav = findViewById(R.id.bottomNav);

        // Load Home fragment by default
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        // Bottom navigation logic
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            } else if (itemId == R.id.nav_orders) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else {
                return false;
            }

            loadFragment(selectedFragment);
            return true;
        });

        // 🔥 START RANDOM NOTIFICATIONS
        startRandomNotifications();
    }

    /** 🌐 Load saved language */
    private void loadSavedLanguage() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = prefs.getString("lang", "en");

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    /** 🔥 Random notification system */
    private void startRandomNotifications() {

        notificationRunnable = new Runnable() {
            @Override
            public void run() {

                NotificationHelper.showRandomNotification(MainActivity.this);

                // ✅ After first notification → fixed 10 minutes
                int delay = 10 * 60 * 1000; // 10 minutes

                handler.postDelayed(this, delay);
            }
        };

        // ✅ First notification after 20 seconds
        handler.postDelayed(notificationRunnable, 20000);
    }

    /** Replace fragment */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    /** Switch tab programmatically */
    public void switchToTab(int itemId) {
        bottomNav.setSelectedItemId(itemId);
    }

    /** 🛑 Stop notifications when activity destroyed */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && notificationRunnable != null) {
            handler.removeCallbacks(notificationRunnable);
        }
    }
}