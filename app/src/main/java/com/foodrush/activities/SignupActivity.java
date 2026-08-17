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
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * SignupActivity — registers a new user with Firebase Auth
 * and saves their profile to Firebase Realtime Database.
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

        btnSignup.setOnClickListener(v -> attemptSignup());
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
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
