package com.example.qrgenerator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qrgenerator.databinding.ActivityLanguageBinding;

public class LanguageActivity extends AppCompatActivity {
    private ActivityLanguageBinding binding;
    private String selectedLanguage = "en";

    @Override
    protected void attachBaseContext(Context newBase) {
        // Menerapkan bahasa saat activity dibuat
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ambil bahasa yang tersimpan saat ini
        selectedLanguage = LocaleHelper.getLanguage(this);
        updateSelection(selectedLanguage);

        binding.btnBack.setOnClickListener(v -> finish());

        // Listener untuk setiap pilihan bahasa
        binding.itemEnglish.setOnClickListener(v -> updateSelection("en"));
        binding.itemIndonesian.setOnClickListener(v -> updateSelection("in"));
        binding.itemArabic.setOnClickListener(v -> updateSelection("ar"));
        binding.itemChinese.setOnClickListener(v -> updateSelection("zh"));
        binding.itemJapanese.setOnClickListener(v -> updateSelection("ja"));
        binding.itemKorean.setOnClickListener(v -> updateSelection("ko"));
        binding.itemFrench.setOnClickListener(v -> updateSelection("fr"));
        binding.itemGerman.setOnClickListener(v -> updateSelection("de"));
        binding.itemSpanish.setOnClickListener(v -> updateSelection("es"));

        binding.btnSave.setOnClickListener(v -> {
            // Simpan bahasa secara permanen
            LocaleHelper.setLocale(this, selectedLanguage);

            // Restart MainActivity agar semua teks berubah ke bahasa yang baru
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void updateSelection(String langCode) {
        selectedLanguage = langCode;

        // Reset semua checkmark
        binding.ivEnglishCheck.setVisibility(View.GONE);
        binding.ivIndonesianCheck.setVisibility(View.GONE);
        binding.ivArabicCheck.setVisibility(View.GONE);
        binding.ivChineseCheck.setVisibility(View.GONE);
        binding.ivJapaneseCheck.setVisibility(View.GONE);
        binding.ivKoreanCheck.setVisibility(View.GONE);
        binding.ivFrenchCheck.setVisibility(View.GONE);
        binding.ivGermanCheck.setVisibility(View.GONE);
        binding.ivSpanishCheck.setVisibility(View.GONE);

        // Tampilkan centang sesuai pilihan
        switch (langCode) {
            case "en": binding.ivEnglishCheck.setVisibility(View.VISIBLE); break;
            case "in": binding.ivIndonesianCheck.setVisibility(View.VISIBLE); break;
            case "ar": binding.ivArabicCheck.setVisibility(View.VISIBLE); break;
            case "zh": binding.ivChineseCheck.setVisibility(View.VISIBLE); break;
            case "ja": binding.ivJapaneseCheck.setVisibility(View.VISIBLE); break;
            case "ko": binding.ivKoreanCheck.setVisibility(View.VISIBLE); break;
            case "fr": binding.ivFrenchCheck.setVisibility(View.VISIBLE); break;
            case "de": binding.ivGermanCheck.setVisibility(View.VISIBLE); break;
            case "es": binding.ivSpanishCheck.setVisibility(View.VISIBLE); break;
        }
    }
}
