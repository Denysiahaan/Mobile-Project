package com.example.readqrcodes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.Size;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

/**
 * CaptureActivityPortrait - Aktivitas scanner yang mendukung lokalisasi bahasa.
 * Menggunakan AppCompatActivity agar sinkron dengan AppCompatDelegate (bahasa).
 */
public class CaptureActivityPortrait extends AppCompatActivity {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean isFlashlightOn = false;

    private final ActivityResultLauncher<String> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    processGalleryImage(uri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_scanner);

        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);

        if (barcodeScannerView != null && barcodeScannerView.getBarcodeView() != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int frameSize = (int) (Math.min(metrics.widthPixels, metrics.heightPixels) * 0.7);
            barcodeScannerView.getBarcodeView().setFramingRectSize(new Size(frameSize, frameSize));
        }

        capture = new CaptureManager(this, barcodeScannerView);
        capture.initializeFromIntent(getIntent(), savedInstanceState);
        capture.decode();

        initUI();
    }

    private void initUI() {
        // Tombol Close
        View btnClose = findViewById(R.id.btn_close_scanner);
        if (btnClose != null) {
            btnClose.setOnClickListener(v -> finish());
        }

        // Tombol Flashlight
        ImageButton btnFlash = findViewById(R.id.btn_toggle_flashlight);
        if (btnFlash != null) {
            if (!hasFlash()) {
                btnFlash.setVisibility(View.GONE);
            } else {
                btnFlash.setOnClickListener(v -> toggleFlash(btnFlash));
            }
        }

        // Tombol Import Galeri
        View btnImport = findViewById(R.id.btn_import);
        if (btnImport != null) {
            btnImport.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        }

        // Memastikan label tombol diperbarui sesuai bahasa yang dipilih
        TextView tvImportLabel = findViewById(R.id.tv_import_label);
        if (tvImportLabel != null) {
            tvImportLabel.setText(R.string.btn_import);
        }

        // Memperbarui instruksi lainnya (jika ada yang statis)
        TextView tvScanTitle = findViewById(R.id.tv_scan_title);
        if (tvScanTitle != null) tvScanTitle.setText(R.string.scan_title);

        TextView tvInstruction = findViewById(R.id.tv_instruction);
        if (tvInstruction != null) tvInstruction.setText(R.string.scan_instruction);

        TextView tvSubInstruction = findViewById(R.id.tv_sub_instruction);
        if (tvSubInstruction != null) tvSubInstruction.setText(R.string.scan_sub_instruction);
    }

    private void toggleFlash(ImageButton btnFlash) {
        if (barcodeScannerView != null) {
            if (isFlashlightOn) {
                barcodeScannerView.setTorchOff();
                btnFlash.setImageResource(R.drawable.ic_flash_on);
            } else {
                barcodeScannerView.setTorchOn();
                btnFlash.setImageResource(R.drawable.ic_flashlight);
            }
            isFlashlightOn = !isFlashlightOn;
        }
    }

    private void processGalleryImage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) throw new Exception();

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.recycle();

            LuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap, hints);

            handleResult(result.getText());

        } catch (Exception e) {
            Toast.makeText(this, R.string.msg_invalid_url, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleResult(String text) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SCAN_RESULT", text);
        startActivity(intent);
        finish();
    }

    private boolean hasFlash() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override protected void onResume() { super.onResume(); capture.onResume(); }
    @Override protected void onPause() { super.onPause(); capture.onPause(); }
    @Override protected void onDestroy() { super.onDestroy(); capture.onDestroy(); }
    @Override protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) {
        return (barcodeScannerView != null && barcodeScannerView.onKeyDown(keyCode, event)) || super.onKeyDown(keyCode, event);
    }
}
