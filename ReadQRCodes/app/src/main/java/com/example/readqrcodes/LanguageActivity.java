package com.example.readqrcodes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.LocaleListCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private RecyclerView rvLanguage;
    private LanguageAdapter adapter;
    private List<Language> languageList;
    private int selectedPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        rvLanguage = findViewById(R.id.rvLanguage);
        rvLanguage.setLayoutManager(new LinearLayoutManager(this));

        prepareLanguages();

        // Deteksi bahasa yang sedang aktif saat ini
        String currentLang = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        if (currentLang.isEmpty()) {
            currentLang = Locale.getDefault().getLanguage();
        }

        for (int i = 0; i < languageList.size(); i++) {
            if (currentLang.startsWith(languageList.get(i).code)) {
                selectedPosition = i;
                break;
            }
        }

        adapter = new LanguageAdapter(languageList);
        rvLanguage.setAdapter(adapter);

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            Language selected = languageList.get(selectedPosition);

            // Terapkan perubahan bahasa secara sistemik (AndroidX)
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(selected.code);
            AppCompatDelegate.setApplicationLocales(appLocale);

            Toast.makeText(this, getString(R.string.msg_language_updated, selected.nativeName), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void prepareLanguages() {
        languageList = new ArrayList<>();
        languageList.add(new Language(getString(R.string.lang_en_title), getString(R.string.lang_en_sub), "en"));
        languageList.add(new Language(getString(R.string.lang_id_title), getString(R.string.lang_id_sub), "id"));
        languageList.add(new Language(getString(R.string.lang_ar_title), getString(R.string.lang_ar_sub), "ar"));
        languageList.add(new Language(getString(R.string.lang_zh_title), getString(R.string.lang_zh_sub), "zh"));
        languageList.add(new Language(getString(R.string.lang_es_title), getString(R.string.lang_es_sub), "es"));
        languageList.add(new Language(getString(R.string.lang_fr_title), getString(R.string.lang_fr_sub), "fr"));
        languageList.add(new Language(getString(R.string.lang_de_title), getString(R.string.lang_de_sub), "de"));
        languageList.add(new Language(getString(R.string.lang_ja_title), getString(R.string.lang_ja_sub), "ja"));
        languageList.add(new Language(getString(R.string.lang_ko_title), getString(R.string.lang_ko_sub), "ko"));
    }

    private static class Language {
        String displayName;
        String nativeName;
        String code;

        Language(String displayName, String nativeName, String code) {
            this.displayName = displayName;
            this.nativeName = nativeName;
            this.code = code;
        }
    }

    private class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {
        private final List<Language> languages;

        LanguageAdapter(List<Language> languages) {
            this.languages = languages;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Language language = languages.get(position);
            holder.tvEnglish.setText(language.displayName);
            holder.tvNative.setText(language.nativeName);
            holder.ivSelected.setVisibility(position == selectedPosition ? View.VISIBLE : View.GONE);

            holder.itemView.setOnClickListener(v -> {
                int previous = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previous);
                notifyItemChanged(selectedPosition);
            });
        }

        @Override
        public int getItemCount() {
            return languages.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvEnglish, tvNative;
            ImageView ivSelected;

            ViewHolder(View itemView) {
                super(itemView);
                tvEnglish = itemView.findViewById(R.id.tv_language_en);
                tvNative = itemView.findViewById(R.id.tv_language_native);
                ivSelected = itemView.findViewById(R.id.iv_selected);
            }
        }
    }
}
