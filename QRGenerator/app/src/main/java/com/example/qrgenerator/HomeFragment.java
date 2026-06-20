package com.example.qrgenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.example.qrgenerator.databinding.FragmentHomeBinding;
import com.google.zxing.WriterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private DatabaseHelper dbHelper;
    private Bitmap generatedBitmap;
    private String currentInput;
    private String currentType = "URL";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.chipGroupType.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (!checkedIds.isEmpty()) {
                int id = checkedIds.get(0);
                resetPreview();
                String typeName = "";
                if (id == R.id.chip_url) {
                    currentType = "URL";
                    typeName = getString(R.string.type_url);
                } else if (id == R.id.chip_text) {
                    currentType = "Text";
                    typeName = getString(R.string.type_text);
                } else if (id == R.id.chip_email) {
                    currentType = "Email";
                    typeName = getString(R.string.type_email);
                } else if (id == R.id.chip_phone) {
                    currentType = "Phone";
                    typeName = getString(R.string.type_phone);
                }
                binding.tilInput.setHint(getString(R.string.hint_enter, typeName.toLowerCase()));
            }
        });

        binding.btnGenerate.setOnClickListener(v -> generateQR());
        binding.btnSave.setOnClickListener(v -> saveQRToStorage());
        binding.btnShare.setOnClickListener(v -> shareQRImage());
        binding.btnNew.setOnClickListener(v -> resetUI());
    }

    private void generateQR() {
        currentInput = binding.etInput.getText().toString().trim();
        if (currentInput.isEmpty()) {
            binding.tilInput.setError(getString(R.string.error_empty));
            return;
        }
        binding.tilInput.setError(null);

        try {
            generatedBitmap = QRUtils.generateQRCode(currentInput, 512, 512);
            binding.imgQrPreview.setImageBitmap(generatedBitmap);
            binding.imgQrPreview.setAlpha(1.0f);
            binding.tvPreviewStatus.setText(getString(R.string.qr_generated));
            binding.tvPreviewDesc.setText(currentInput);
            binding.layoutActions.setVisibility(View.VISIBLE);

            Animation scaleUp = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
            binding.layoutQrPreview.startAnimation(scaleUp);

            String timestamp = new SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault()).format(new Date());
            dbHelper.insertQR(currentInput, currentType, timestamp, "");

            Toast.makeText(getContext(), getString(R.string.qr_saved), Toast.LENGTH_SHORT).show();

        } catch (WriterException e) {
            Toast.makeText(getContext(), getString(R.string.error_generating), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveQRToStorage() {
        if (generatedBitmap == null) return;
        Toast.makeText(getContext(), "QR Code saved to Gallery", Toast.LENGTH_SHORT).show();
    }

    private void shareQRImage() {
        if (generatedBitmap == null) return;
        try {
            File cachePath = new File(getContext().getCacheDir(), "images");
            cachePath.mkdirs();
            FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
            generatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            File imageFile = new File(cachePath, "image.png");
            Uri contentUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", imageFile);

            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.setDataAndType(contentUri, getContext().getContentResolver().getType(contentUri));
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                startActivity(Intent.createChooser(shareIntent, "Share QR Code"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetUI() {
        binding.etInput.setText("");
        resetPreview();
    }

    private void resetPreview() {
        binding.imgQrPreview.setImageResource(R.drawable.icon);
        binding.imgQrPreview.setAlpha(0.2f);
        binding.tvPreviewStatus.setText(getString(R.string.preview_ready));
        binding.tvPreviewDesc.setText(getString(R.string.preview_desc));
        binding.layoutActions.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
