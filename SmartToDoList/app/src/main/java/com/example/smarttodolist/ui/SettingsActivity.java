package com.example.smarttodolist.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttodolist.R;
import com.example.smarttodolist.utils.ThemeManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButtonToggleGroup;

/**
 * SettingsActivity mengelola preferensi pengguna, terutama pengaturan tema (Dark/Light).
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Terapkan tema yang sedang aktif
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_settings);

        // Inisialisasi Toolbar dengan tombol kembali (Back Button)
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi Toggle Group untuk pilihan tema
        MaterialButtonToggleGroup toggleTheme = findViewById(R.id.toggle_theme);
        
        // Atur posisi tombol sesuai dengan tema yang tersimpan di SharedPreferences
        int savedTheme = ThemeManager.getSavedTheme(this);
        if (savedTheme == ThemeManager.THEME_LIGHT) toggleTheme.check(R.id.btn_light);
        else if (savedTheme == ThemeManager.THEME_DARK) toggleTheme.check(R.id.btn_dark);
        else toggleTheme.check(R.id.btn_system);

        // Listener saat pengguna menekan salah satu tombol tema
        toggleTheme.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                int theme;
                if (checkedId == R.id.btn_light) theme = ThemeManager.THEME_LIGHT;
                else if (checkedId == R.id.btn_dark) theme = ThemeManager.THEME_DARK;
                else theme = ThemeManager.THEME_SYSTEM;
                
                // Simpan pilihan dan terapkan tema secara langsung
                ThemeManager.saveAndApplyTheme(SettingsActivity.this, theme);
            }
        });
    }

    /**
     * Menangani aksi klik pada tombol navigasi di Toolbar.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Kembali ke MainActivity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
