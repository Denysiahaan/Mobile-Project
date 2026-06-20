package com.example.readqrcodes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String result = getIntent().getStringExtra("SCAN_RESULT");
        TextView tvResult = findViewById(R.id.tv_result_content);
        tvResult.setText(result);

        findViewById(R.id.toolbar).setOnClickListener(v -> finish());

        findViewById(R.id.btn_copy).setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Scanned Content", result);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, getString(R.string.msg_copied), Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_share).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, result);
            startActivity(Intent.createChooser(intent, getString(R.string.label_share_via)));
        });

        findViewById(R.id.btn_open_link).setOnClickListener(v -> {
            if (result != null && (result.startsWith("http://") || result.startsWith("https://"))) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result));
                startActivity(intent);
            } else {
                Toast.makeText(this, getString(R.string.msg_invalid_url), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_scan_again).setOnClickListener(v -> finish());
    }
}
