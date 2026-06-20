package com.example.qrgenerator;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.qrgenerator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void attachBaseContext(Context newBase) {
        // Menerapkan bahasa pilihan setiap kali Activity dimulai
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_generate) {
                loadFragment(new HomeFragment());
                return true;
            } else if (itemId == R.id.nav_saved) {
                loadFragment(new SavedFragment());
                return true;
            } else if (itemId == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
                return true;
            }
            return false;
        });
    }

    public void navigateToSettings() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_settings);
    }

    public void navigateToHome() {
        binding.bottomNavigation.setSelectedItemId(R.id.nav_generate);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
