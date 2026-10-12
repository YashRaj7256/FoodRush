package com.foodrush.activities;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🌐 LOAD SAVED LANGUAGE
        loadSavedLanguage();

        setContentView(R.layout.activity_main);

        // 🔔 Create notification channel
        NotificationHelper.createNotificationChannel(this);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU
                && checkSelfPermission("android.permission.POST_NOTIFICATIONS")
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
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

    /** Replace fragment */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    /** 🛒 Modern Cart Badge Updater */
    public void updateCartBadge() {
        if (bottomNav == null) return;
        try {
            int count = com.foodrush.database.CartManager.getInstance().getTotalCount();
            com.google.android.material.badge.BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.nav_cart);
            if (count > 0) {
                badge.setVisible(true);
                badge.setNumber(count);
                badge.setBackgroundColor(getResources().getColor(R.color.primary));
                badge.setBadgeTextColor(getResources().getColor(R.color.white));
            } else {
                badge.setVisible(false);
            }
        } catch (Exception ignored) {}
    }

    /** Switch tab programmatically */
    public void switchToTab(int itemId) {
        bottomNav.setSelectedItemId(itemId);
    }
}
