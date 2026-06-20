package com.example.readqrcodes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(MainActivity.this, getString(R.string.msg_scan_cancelled), Toast.LENGTH_LONG).show();
                } else {
                    navigateToResult(result.getContents());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply window insets to the toolbar to prevent overlap with status bar
        if (findViewById(R.id.toolbar) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.toolbar), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(0, systemBars.top, 0, 0);
                return insets;
            });
        }

        // Logic for scanning
        Runnable startScan = () -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt(getString(R.string.scan_instruction));
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivityPortrait.class);
            barcodeLauncher.launch(options);
        };

        // Main Action: Scan QR Code via Button
        findViewById(R.id.btn_scan).setOnClickListener(v -> startScan.run());

        // Main Action: Scan QR Code via Central Image
        if (findViewById(R.id.card_main_image) != null) {
            findViewById(R.id.card_main_image).setOnClickListener(v -> startScan.run());
        }

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.nav_settings) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
                }
                return id == R.id.nav_home;
            });
        }
    }

    private void navigateToResult(String contents) {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra("SCAN_RESULT", contents);
        startActivity(intent);
    }
}
