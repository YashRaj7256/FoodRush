package com.foodrush.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.foodrush.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * LoginActivity — handles Firebase email/password login.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup, tvForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Bind UI views
        etEmail          = findViewById(R.id.etEmail);
        etPassword       = findViewById(R.id.etPassword);
        btnLogin         = findViewById(R.id.btnLogin);
        tvSignup         = findViewById(R.id.tvSignup);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        progressBar      = findViewById(R.id.progressBar);

        // Login button click
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Navigate to Signup
        tvSignup.setOnClickListener(v ->
            startActivity(new Intent(LoginActivity.this, SignupActivity.class))
        );

        // Forgot password
        tvForgotPassword.setOnClickListener(v -> sendPasswordReset());
    }

    /** Validates inputs and attempts Firebase sign-in */
    private void attemptLogin() {
        String email    = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_email_required));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_password_required));
            return;
        }
        if (password.length() < 6) {
            etPassword.setError(getString(R.string.error_password_length));
            return;
        }

        showProgress(true);

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(task -> {
                showProgress(false);
                if (task.isSuccessful()) {
                    // Login success → go to MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.error_login_failed) + ": "
                            + (task.getException() != null ? task.getException().getMessage() : ""),
                            Toast.LENGTH_LONG).show();
                }
            });
    }

    /** Sends password reset email */
    private void sendPasswordReset() {
        String email = etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_email_required));
            return;
        }
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, getString(R.string.reset_email_sent), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.error_generic), Toast.LENGTH_SHORT).show();
                }
            });
    }

    /** Shows or hides the loading spinner */
    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!show);
    }
}
