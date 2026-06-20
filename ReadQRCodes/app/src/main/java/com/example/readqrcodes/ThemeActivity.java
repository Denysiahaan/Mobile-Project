package com.example.readqrcodes;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        RadioGroup rgTheme = findViewById(R.id.rg_theme);

        // Simple logic to show selection based on current mode
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
            ((RadioButton)findViewById(R.id.rb_dark)).setChecked(true);
        } else {
            ((RadioButton)findViewById(R.id.rb_light)).setChecked(true);
        }

        rgTheme.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_light) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(this, getString(R.string.msg_theme_light), Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.rb_dark) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(this, getString(R.string.msg_theme_dark), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
