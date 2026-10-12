package com.foodrush.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SignupActivity — registers a new user with Firebase Auth,
 * saves profile to Realtime Database, and animates smoothly on entry.
 */
public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private Button btnSignup;
    private TextView tvLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();

        etName            = findViewById(R.id.etName);
        etEmail           = findViewById(R.id.etEmail);
        etPhone           = findViewById(R.id.etPhone);
        etPassword        = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignup         = findViewById(R.id.btnSignup);
        tvLogin           = findViewById(R.id.tvLogin);
        progressBar       = findViewById(R.id.progressBar);

        animateEntrance();

        btnSignup.setOnClickListener(v -> attemptSignup());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }

    private void animateEntrance() {
        View headerLayout     = findViewById(R.id.headerLayout);
        View authCard         = findViewById(R.id.authCard);
        View tilName          = findViewById(R.id.tilName);
        View tilEmail         = findViewById(R.id.tilEmail);
        View tilPhone         = findViewById(R.id.tilPhone);
        View tilPassword      = findViewById(R.id.tilPassword);
        View tilConfirm       = findViewById(R.id.tilConfirmPassword);
        View footerLogin      = findViewById(R.id.footerLogin);

        if (headerLayout != null) {
            headerLayout.setTranslationY(-50f);
            headerLayout.setAlpha(0f);
            headerLayout.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(450)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        if (authCard != null) {
            authCard.setTranslationY(100f);
            authCard.setAlpha(0f);
            authCard.animate()
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(500)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        View[] fields = new View[]{tilName, tilEmail, tilPhone, tilPassword, tilConfirm};
        int delay = 150;
        for (View field : fields) {
            if (field != null) {
                field.setTranslationX(-30f);
                field.setAlpha(0f);
                field.animate()
                        .translationX(0f)
                        .alpha(1f)
                        .setStartDelay(delay)
                        .setDuration(350)
                        .start();
                delay += 50;
            }
        }

        if (btnSignup != null) {
            btnSignup.setScaleX(0.9f);
            btnSignup.setScaleY(0.9f);
            btnSignup.setAlpha(0f);
            btnSignup.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setStartDelay(delay + 50)
                    .setDuration(350)
                    .start();
        }

        if (footerLogin != null) {
            footerLogin.setAlpha(0f);
            footerLogin.animate()
                    .alpha(1f)
                    .setStartDelay(delay + 100)
                    .setDuration(350)
                    .start();
        }
    }

    private void attemptSignup() {
        String name     = etName.getText().toString().trim();
        String email    = etEmail.getText().toString().trim();
        String phone    = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm  = etConfirmPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name))     { etName.setError("Name required"); return; }
        if (TextUtils.isEmpty(email))    { etEmail.setError(getString(R.string.error_email_required)); return; }
        if (TextUtils.isEmpty(phone))    { etPhone.setError("Phone required"); return; }
        if (TextUtils.isEmpty(password)) { etPassword.setError(getString(R.string.error_password_required)); return; }
        if (password.length() < 6)      { etPassword.setError(getString(R.string.error_password_length)); return; }
        if (!password.equals(confirm))  { etConfirmPassword.setError("Passwords do not match"); return; }

        showProgress(true);

        // Create user in Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    if (firebaseUser != null) {
                        // Save profile to Realtime Database
                        User user = new User(firebaseUser.getUid(), name, email, phone, "");
                        FirebaseHelper.getInstance().saveUser(user);
                    }
                    showProgress(false);
                    Toast.makeText(this, "Account created! Welcome 🎉", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                    finish();
                } else {
                    showProgress(false);
                    Toast.makeText(this,
                            "Signup failed: " + (task.getException() != null
                                     ? task.getException().getMessage() : "Unknown error"),
                            Toast.LENGTH_LONG).show();
                }
            });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnSignup.setEnabled(!show);
    }
}
