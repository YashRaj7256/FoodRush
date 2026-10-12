package com.foodrush.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.foodrush.R;
import com.foodrush.activities.LoginActivity;
import com.foodrush.activities.SettingsActivity;
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * ProfileFragment — displays user profile details and provides logout option.
 */
public class ProfileFragment extends Fragment {

    private TextView tvName, tvEmail, tvPhone ;
    public View tvSetting;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 🔹 Find views
        tvName    = view.findViewById(R.id.tvName);
        tvEmail   = view.findViewById(R.id.tvEmail);
        tvPhone   = view.findViewById(R.id.tvPhone);
        tvSetting = view.findViewById(R.id.tvSetting);
        btnLogout = view.findViewById(R.id.btnLogout);

        // 🔹 Load user data
        loadUserProfile();

        // 🔹 Logout button
        btnLogout.setOnClickListener(v -> logout());

        // 🔥 SETTINGS CLICK (NEW FEATURE)
        tvSetting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        });
    }

    /** Loads user data from Firebase Realtime Database */
    private void loadUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        // Set email from Firebase Auth
        tvEmail.setText(currentUser.getEmail());

        // Load extra profile data
        FirebaseHelper.getInstance().getUsersRef()
                .child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!isAdded()) return;
                        User user = snapshot.getValue(User.class);

                        if (user != null) {
                            tvName.setText(user.getName());

                            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                                tvPhone.setText(user.getPhone());
                            } else {
                                tvPhone.setText("—");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        if (!isAdded()) return;
                        tvName.setText(getString(R.string.error_generic));
                    }
                });
    }

    /** Logs out user */
    private void logout() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}