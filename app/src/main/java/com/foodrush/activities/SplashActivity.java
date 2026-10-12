package com.foodrush.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;
import com.foodrush.utils.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SplashActivity — features entrance scale, bounce, and fade animations,
 * then seamlessly checks auth state and routes the user.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2200; // 2.2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Notification channel initialization
        NotificationHelper.createNotificationChannel(this);

        // Views to animate
        View logoCard = findViewById(R.id.splashLogoCard);
        View appName = findViewById(R.id.tvAppName);
        View tagline = findViewById(R.id.tvTagline);
        View progress = findViewById(R.id.splashProgress);
        View footer = findViewById(R.id.tvFooterBranding);

        // Initial animation setup
        if (logoCard != null) {
            logoCard.setScaleX(0.2f);
            logoCard.setScaleY(0.2f);
            logoCard.setAlpha(0f);
        }
        if (appName != null) {
            appName.setTranslationY(60f);
            appName.setAlpha(0f);
        }
        if (tagline != null) {
            tagline.setTranslationY(40f);
            tagline.setAlpha(0f);
        }
        if (progress != null) {
            progress.setAlpha(0f);
        }
        if (footer != null) {
            footer.setAlpha(0f);
        }

        // Trigger entrance animations
        if (logoCard != null) {
            logoCard.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .alpha(1.0f)
                    .setDuration(750)
                    .setInterpolator(new OvershootInterpolator(1.4f))
                    .start();
        }

        if (appName != null) {
            appName.animate()
                    .translationY(0f)
                    .alpha(1.0f)
                    .setStartDelay(250)
                    .setDuration(600)
                    .setInterpolator(new AccelerateDecelerateInterpolator())
                    .start();
        }

        if (tagline != null) {
            tagline.animate()
                    .translationY(0f)
                    .alpha(1.0f)
                    .setStartDelay(450)
                    .setDuration(600)
                    .start();
        }

        if (progress != null) {
            progress.animate()
                    .alpha(1.0f)
                    .setStartDelay(700)
                    .setDuration(500)
                    .start();
        }

        if (footer != null) {
            footer.animate()
                    .alpha(1.0f)
                    .setStartDelay(600)
                    .setDuration(600)
                    .start();
        }

        // Navigate after delay
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            Intent nextIntent;
            if (currentUser != null) {
                nextIntent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                nextIntent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(nextIntent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DELAY);
    }
}
