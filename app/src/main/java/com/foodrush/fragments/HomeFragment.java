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
import android.view.Gravity;
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
import com.foodrush.database.AhmedabadDataSeeder;
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
    private TextView tvEmpty;

    // 🔥 LOCATION
    private TextView tvLocation;
    private FusedLocationProviderClient fusedLocationClient;

    private final List<Restaurant> allRestaurants = new ArrayList<>();
    private final List<Restaurant> featuredList = new ArrayList<>();
    private String selectedCategory = "All";

    private static class CategoryItem {
        final String name;
        final String emoji;
        CategoryItem(String name, String emoji) {
            this.name = name;
            this.emoji = emoji;
        }
    }

    private static final CategoryItem[] CATEGORIES = {
            new CategoryItem("All", "🔥"),
            new CategoryItem("Gujarati", "🍱"),
            new CategoryItem("Fast Food", "🍔"),
            new CategoryItem("Pizza", "🍕"),
            new CategoryItem("Street Food", "🥟"),
            new CategoryItem("North Indian", "🍛"),
            new CategoryItem("Biryani", "🍲"),
            new CategoryItem("Chinese", "🍜"),
            new CategoryItem("Desserts", "🍰"),
            new CategoryItem("Drinks", "🥤")
    };

    private void initDefaultRestaurantsIfEmpty() {
        if (allRestaurants.isEmpty()) {
            List<AhmedabadDataSeeder.AhmedabadRestaurantEntry> seederList = AhmedabadDataSeeder.getAhmedabadRestaurants();
            for (AhmedabadDataSeeder.AhmedabadRestaurantEntry entry : seederList) {
                allRestaurants.add(entry.restaurant);
            }

            featuredList.clear();
            for (int i = 0; i < allRestaurants.size() && i < 5; i++) {
                featuredList.add(allRestaurants.get(i));
            }
        }
    }

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
        tvEmpty = view.findViewById(R.id.tvEmpty);

        // 🔥 LOCATION VIEW
        tvLocation = view.findViewById(R.id.tvLocation);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        getUserLocation();

        initDefaultRestaurantsIfEmpty();

        // GRID LIST
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new RestaurantAdapter(getContext(), new ArrayList<>(allRestaurants));
        recyclerView.setAdapter(adapter);

        // FEATURED
        featuredRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        featuredAdapter = new RestaurantAdapter(getContext(), new ArrayList<>(featuredList));
        featuredRecycler.setAdapter(featuredAdapter);

        adapter.setOnItemClickListener(this::openRestaurant);
        featuredAdapter.setOnItemClickListener(this::openRestaurant);

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
                        tvLocation.setText(R.string.location_unavailable);
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
                tvLocation.setText(city != null ? city : getString(R.string.location_unavailable));
            }
        } catch (Exception e) {
            tvLocation.setText(R.string.location_unavailable);
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
        if (categoryContainer == null || getContext() == null) return;
        categoryContainer.removeAllViews();
        for (CategoryItem item : CATEGORIES) {
            TextView chip = new TextView(getContext());
            chip.setText(item.emoji + " " + item.name);
            chip.setPadding(36, 18, 36, 18);
            chip.setTextSize(13f);
            chip.setTypeface(null, android.graphics.Typeface.BOLD);
            chip.setGravity(Gravity.CENTER);
            chip.setTag(item.name);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd((int) (10 * getResources().getDisplayMetrics().density));
            chip.setLayoutParams(params);

            updateChipStyle(chip, item.name.equals(selectedCategory));
            chip.setOnClickListener(v -> {
                selectedCategory = item.name;
                for (int i = 0; i < categoryContainer.getChildCount(); i++) {
                    TextView categoryChip = (TextView) categoryContainer.getChildAt(i);
                    Object tag = categoryChip.getTag();
                    updateChipStyle(categoryChip, selectedCategory.equals(tag));
                }
                filterRestaurants(etSearch.getText().toString());
            });
            categoryContainer.addView(chip);
        }
    }

    private void updateChipStyle(TextView chip, boolean selected) {
        if (getContext() == null) return;
        chip.setBackgroundResource(selected ? R.drawable.chip_selected : R.drawable.chip_unselected);
        chip.setTextColor(getResources().getColor(selected ? R.color.white : R.color.text_primary));
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

                        if (!snapshot.exists() || snapshot.getChildrenCount() == 0) {
                            // Seed Firebase database in background
                            FirebaseHelper.getInstance().seedSampleRestaurantsAndFoods();

                            List<AhmedabadDataSeeder.AhmedabadRestaurantEntry> seederList = AhmedabadDataSeeder.getAhmedabadRestaurants();
                            for (AhmedabadDataSeeder.AhmedabadRestaurantEntry entry : seederList) {
                                allRestaurants.add(entry.restaurant);
                            }
                        } else {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Restaurant r = ds.getValue(Restaurant.class);

                                if (r != null) {
                                    r.setId(ds.getKey());
                                    allRestaurants.add(r);
                                }
                            }
                        }

                        for (int i = 0; i < allRestaurants.size() && i < 5; i++) {
                            featuredList.add(allRestaurants.get(i));
                        }

                        progressBar.setVisibility(View.GONE);

                        filterRestaurants(etSearch.getText().toString());
                        featuredAdapter.updateList(featuredList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        // Even on network/rule error, show fallback items so user can interact
                        if (allRestaurants.isEmpty()) {
                            List<AhmedabadDataSeeder.AhmedabadRestaurantEntry> seederList = AhmedabadDataSeeder.getAhmedabadRestaurants();
                            for (AhmedabadDataSeeder.AhmedabadRestaurantEntry entry : seederList) {
                                allRestaurants.add(entry.restaurant);
                            }

                            for (int i = 0; i < allRestaurants.size() && i < 5; i++) {
                                featuredList.add(allRestaurants.get(i));
                            }

                            adapter.updateList(allRestaurants);
                            featuredAdapter.updateList(featuredList);
                            tvEmpty.setVisibility(View.GONE);
                        } else {
                            tvEmpty.setText(R.string.error_loading_restaurants);
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }
                });

        adapter.setOnItemClickListener(this::openRestaurant);
        featuredAdapter.setOnItemClickListener(this::openRestaurant);
    }

    private void openRestaurant(Restaurant restaurant) {
        Intent intent = new Intent(getContext(), RestaurantDetailActivity.class);
        intent.putExtra("restaurantId", restaurant.getId());
        intent.putExtra("name", restaurant.getName());
        intent.putExtra("desc", restaurant.getDescription());
        startActivity(intent);
    }

    // SEARCH & CATEGORY FILTER
    private void filterRestaurants(String query) {
        List<Restaurant> filtered = new ArrayList<>();
        String trimmedQuery = query == null ? "" : query.trim().toLowerCase(Locale.getDefault());

        for (Restaurant r : allRestaurants) {
            String name = r.getName() == null ? "" : r.getName().toLowerCase(Locale.getDefault());
            String description = r.getDescription() == null ? "" : r.getDescription().toLowerCase(Locale.getDefault());
            String category = r.getCategory() == null ? "" : r.getCategory().toLowerCase(Locale.getDefault());

            boolean matchesQuery = trimmedQuery.isEmpty()
                    || name.contains(trimmedQuery)
                    || description.contains(trimmedQuery)
                    || category.contains(trimmedQuery);

            boolean matchesCategory = "All".equalsIgnoreCase(selectedCategory)
                    || category.equalsIgnoreCase(selectedCategory)
                    || description.contains(selectedCategory.toLowerCase(Locale.getDefault()))
                    || name.contains(selectedCategory.toLowerCase(Locale.getDefault()));

            if (matchesQuery && matchesCategory) {
                filtered.add(r);
            }
        }

        adapter.updateList(filtered);
        tvEmpty.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        if (filtered.isEmpty()) tvEmpty.setText(R.string.no_restaurants_found);
    }
}
