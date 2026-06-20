package com.example.smarttodolist.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Class ThemeManager bertanggung jawab untuk mengelola pengaturan tema aplikasi (Light, Dark, System).
 * Menggunakan SharedPreferences untuk menyimpan preferensi pengguna secara permanen.
 */
public class ThemeManager {
    // Nama file SharedPreferences dan Key untuk menyimpan data tema
    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_THEME = "current_theme";

    // Konstanta untuk pilihan tema
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_SYSTEM = 2;

    /**
     * Menerapkan tema yang tersimpan saat aplikasi dimulai.
     * Biasanya dipanggil di onCreate() pada MainActivity atau di Class Application.
     */
    public static void applyTheme(Context context) {
        int theme = getSavedTheme(context);
        apply(theme);
    }

    /**
     * Menyimpan pilihan tema baru ke SharedPreferences dan langsung menerapkannya secara realtime.
     */
    public static void saveAndApplyTheme(Context context, int theme) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME, theme).apply();
        apply(theme);
    }

    /**
     * Mengambil nilai tema yang tersimpan. Default-nya adalah THEME_SYSTEM (Mengikuti sistem).
     */
    public static int getSavedTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME, THEME_SYSTEM);
    }

    /**
     * Logika internal untuk mengubah mode malam (Night Mode) menggunakan AppCompatDelegate.
     */
    private static void apply(int theme) {
        switch (theme) {
            case THEME_LIGHT:
                // Memaksa mode terang
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                // Memaksa mode gelap
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                // Mengikuti pengaturan tema global pada perangkat Android
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}
