package com.foodrush.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;
import com.foodrush.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SplashActivity — shown for 2 seconds on launch.
 * Checks if the user is already logged in, then routes accordingly.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Create notification channel once here
        NotificationHelper.createNotificationChannel(this);

        // After delay, check auth state
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                // Already logged in → go to MainActivity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                // Not logged in → go to Login
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish(); // Remove splash from back stack
        }, SPLASH_DELAY);
    }
}
