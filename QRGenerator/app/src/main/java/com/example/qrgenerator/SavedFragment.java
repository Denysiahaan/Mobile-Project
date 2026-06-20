package com.example.qrgenerator;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.qrgenerator.databinding.FragmentSavedBinding;
import com.google.zxing.WriterException;
import java.util.ArrayList;
import java.util.List;

public class SavedFragment extends Fragment {
    private FragmentSavedBinding binding;
    private DatabaseHelper dbHelper;
    private SavedQRAdapter adapter;
    private List<SavedQRAdapter.QRItem> qrList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSavedBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(getContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Listener btnSettingsTop dihapus karena tombol sudah dihilangkan dari layout

        setupRecyclerView();
        loadSavedData();
    }

    private void setupRecyclerView() {
        qrList = new ArrayList<>();
        adapter = new SavedQRAdapter(qrList, new SavedQRAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int id) {
                dbHelper.deleteQR(id);
                loadSavedData();
                Toast.makeText(getContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onShareClick(String content) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, content);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.share_title)));
            }

            @Override
            public void onItemClick(SavedQRAdapter.QRItem item) {
                showQRDialog(item);
            }
        });

        binding.rvSaved.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvSaved.setAdapter(adapter);
    }

    private void showQRDialog(SavedQRAdapter.QRItem item) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_qr_view, null);
        ImageView ivFullQr = dialogView.findViewById(R.id.iv_full_qr);

        try {
            Bitmap bitmap = QRUtils.generateQRCode(item.getContent(), 800, 800);
            ivFullQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toast.makeText(getContext(), R.string.error_display_qr, Toast.LENGTH_SHORT).show();
        }

        new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton(R.string.close, null)
                .show();
    }

    private void loadSavedData() {
        qrList.clear();
        Cursor cursor = dbHelper.getAllSavedQR();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTENT));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                qrList.add(new SavedQRAdapter.QRItem(id, content, type, date));
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (qrList.isEmpty()) {
            binding.layoutEmpty.setVisibility(View.VISIBLE);
            binding.rvSaved.setVisibility(View.GONE);
        } else {
            binding.layoutEmpty.setVisibility(View.GONE);
            binding.rvSaved.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
