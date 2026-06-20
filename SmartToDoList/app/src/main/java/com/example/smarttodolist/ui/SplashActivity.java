package com.example.smarttodolist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smarttodolist.R;

/**
 * SplashActivity menampilkan layar pembuka selama 2 detik.
 * Menggunakan animasi smooth dan transisi ke MainActivity.
 */
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000; // 2 detik

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inisialisasi View dan Animasi
        LinearLayout layoutLogo = findViewById(R.id.layout_logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        // Jalankan animasi secara bersamaan
        layoutLogo.startAnimation(fadeIn);
        layoutLogo.startAnimation(scaleUp);

        // Handler untuk berpindah ke MainActivity setelah durasi selesai
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            
            // Tambahkan transisi perpindahan activity yang smooth
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            
            finish(); // Selesai agar tidak bisa kembali ke splash saat menekan tombol back
        }, SPLASH_DURATION);
    }
}
