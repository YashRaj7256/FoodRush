package com.foodrush.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    Button btnEnglish, btnHindi;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnEnglish = findViewById(R.id.btnEnglish);
        btnHindi   = findViewById(R.id.btnHindi);
        btnBack    = findViewById(R.id.btnBack);

        // 🔙 Back button
        btnBack.setOnClickListener(v -> finish());

        // 🌐 Language buttons
        btnEnglish.setOnClickListener(v -> setLanguage("en"));
        btnHindi.setOnClickListener(v -> setLanguage("hi"));
    }

    private void setLanguage(String langCode) {

        // 🔹 Save language
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        prefs.edit().putString("lang", langCode).apply();

        // 🔹 Apply language
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // 🔥 VERY IMPORTANT → Restart whole app
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}