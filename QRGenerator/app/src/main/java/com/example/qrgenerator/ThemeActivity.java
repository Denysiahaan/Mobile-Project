package com.example.qrgenerator;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Halaman ini dinonaktifkan sesuai permintaan pengguna.
 * Menghapus semua referensi ke View Binding untuk menghindari error kompilasi.
 */
public class ThemeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Langsung menutup activity jika tidak sengaja terpanggil
        finish();
    }
}
