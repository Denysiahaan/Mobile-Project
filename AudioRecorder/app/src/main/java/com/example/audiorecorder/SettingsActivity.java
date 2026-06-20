package com.example.audiorecorder;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private SwitchMaterial switchDarkMode;
    private RecyclerView rvHistory;
    private MaterialButton btnClearHistory;
    private SharedPreferences sharedPreferences;
    private HistoryAdapter adapter;
    private List<String> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        switchDarkMode = findViewById(R.id.switchDarkMode);
        rvHistory = findViewById(R.id.rvHistory);
        btnClearHistory = findViewById(R.id.btnClearHistory);

        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        switchDarkMode.setChecked(isDarkMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply();
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        btnClearHistory.setOnClickListener(v -> showClearHistoryDialog());

        loadHistory();
    }

    private void showClearHistoryDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Clear History")
                .setMessage("Are you sure you want to delete all recording history?")
                .setPositiveButton("Delete All", (dialog, which) -> clearHistory())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearHistory() {
        File dir = getExternalFilesDir(null);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".3gp"));
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        loadHistory();
        Toast.makeText(this, "History cleared", Toast.LENGTH_SHORT).show();
    }

    private void loadHistory() {
        File dir = getExternalFilesDir(null);
        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".3gp"));
        fileList = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                fileList.add(file.getName());
            }
        }

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter(fileList, this::playFile);
        rvHistory.setAdapter(adapter);
    }

    private void playFile(String fileName) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(getExternalFilesDir(null).getAbsolutePath() + "/" + fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
            Toast.makeText(this, "Playing: " + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing file", Toast.LENGTH_SHORT).show();
        }
    }
}