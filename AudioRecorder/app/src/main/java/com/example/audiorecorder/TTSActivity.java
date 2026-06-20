package com.example.audiorecorder;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Locale;

public class TTSActivity extends AppCompatActivity {

    private EditText etText;
    private Button btnTTS;
    private TextView tvDisplay;
    private MaterialCardView displayCard;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);

        etText = findViewById(R.id.etText);
        btnTTS = findViewById(R.id.btnTTS);
        tvDisplay = findViewById(R.id.tvDisplay);
        displayCard = findViewById(R.id.displayCard);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("id", "ID")); // Set to Indonesian
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts.setLanguage(Locale.US);
                }
            }
        });

        btnTTS.setOnClickListener(v -> {
            String text = etText.getText().toString();
            if (!text.isEmpty()) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                tvDisplay.setText(text);
                displayCard.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(TTSActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}