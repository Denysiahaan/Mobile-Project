package com.example.readqrcodes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. Panggil installSplashScreen SEBELUM super.onCreate()
        // Ini akan menggantikan logo Android hijau dengan pengaturan di themes.xml Anda
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // 2. Aktifkan EdgeToEdge agar background memenuhi layar
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Jeda untuk menampilkan identitas aplikasi Anda
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            // Transisi fade yang mulus
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 2000);
    }
}
