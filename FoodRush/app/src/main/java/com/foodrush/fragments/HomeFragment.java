package com.foodrush.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.foodrush.R;
import com.foodrush.activities.RestaurantDetailActivity;
import com.foodrush.adapters.RestaurantAdapter;
import com.foodrush.database.FirebaseHelper;
import com.foodrush.models.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView, featuredRecycler;
    private RestaurantAdapter adapter, featuredAdapter;

    private EditText etSearch;
    private LinearLayout categoryContainer;
    private View progressBar;

    // 🔥 LOCATION
    private TextView tvLocation;
    private FusedLocationProviderClient fusedLocationClient;

    private List<Restaurant> allRestaurants = new ArrayList<>();
    private List<Restaurant> featuredList = new ArrayList<>();

    private static final String[] CATEGORIES = {
            "All", "Fast Food", "Pizza", "Indian", "Drinks"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        featuredRecycler = view.findViewById(R.id.featuredRecycler);

        etSearch = view.findViewById(R.id.etSearch);
        categoryContainer = view.findViewById(R.id.categoryContainer);
        progressBar = view.findViewById(R.id.progressBar);

        // 🔥 LOCATION VIEW
        tvLocation = view.findViewById(R.id.tvLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getUserLocation();

        // GRID LIST
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new RestaurantAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // FEATURED
        featuredRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        featuredAdapter = new RestaurantAdapter(getContext(), new ArrayList<>());
        featuredRecycler.setAdapter(featuredAdapter);

        buildCategoryChips();
        loadRestaurants();

        // SEARCH
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRestaurants(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // 🔥 LOCATION LOGIC
    private void getUserLocation() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        getCityName(location);
                    } else {
                        tvLocation.setText("Location unavailable");
                    }
                });
    }

    private void getCityName(Location location) {
        try {
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            List<android.location.Address> addresses =
                    geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses != null && !addresses.isEmpty()) {
                String city = addresses.get(0).getLocality();
                tvLocation.setText(city != null ? city : "Unknown");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvLocation.setText("Error getting location");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getUserLocation();
        }
    }

    // CATEGORY
    private void buildCategoryChips() {
        for (String category : CATEGORIES) {
            TextView chip = new TextView(getContext());
            chip.setText(category);
            chip.setPadding(32, 16, 32, 16);
            chip.setTextSize(13f);

            chip.setOnClickListener(v -> filterRestaurants(etSearch.getText().toString()));
            categoryContainer.addView(chip);
        }
    }

    // LOAD FROM FIREBASE
    private void loadRestaurants() {
        progressBar.setVisibility(View.VISIBLE);

        FirebaseHelper.getInstance().getRestaurantsRef()
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        allRestaurants.clear();
                        featuredList.clear();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Restaurant r = ds.getValue(Restaurant.class);

                            if (r != null) {
                                r.setId(ds.getKey());
                                allRestaurants.add(r);
                            }
                        }

                        for (int i = 0; i < allRestaurants.size() && i < 2; i++) {
                            featuredList.add(allRestaurants.get(i));
                        }

                        progressBar.setVisibility(View.GONE);

                        adapter.updateList(allRestaurants);
                        featuredAdapter.updateList(featuredList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        adapter.setOnItemClickListener(restaurant -> {
            Intent intent = new Intent(getContext(), RestaurantDetailActivity.class);
            intent.putExtra("restaurantId", restaurant.getId());
            intent.putExtra("name", restaurant.getName());
            intent.putExtra("desc", restaurant.getDescription());
            startActivity(intent);
        });

        featuredAdapter.setOnItemClickListener(restaurant -> {
            Intent intent = new Intent(getContext(), RestaurantDetailActivity.class);
            intent.putExtra("restaurantId", restaurant.getId());
            intent.putExtra("name", restaurant.getName());
            intent.putExtra("desc", restaurant.getDescription());
            startActivity(intent);
        });
    }

    // SEARCH
    private void filterRestaurants(String query) {
        List<Restaurant> filtered = new ArrayList<>();

        for (Restaurant r : allRestaurants) {
            if (query.isEmpty() || r.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(r);
            }
        }

        adapter.updateList(filtered);
    }
}