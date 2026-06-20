package com.example.qrgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qrgenerator.databinding.ItemSavedQrBinding;
import com.google.zxing.WriterException;
import java.util.List;

public class SavedQRAdapter extends RecyclerView.Adapter<SavedQRAdapter.ViewHolder> {
    private List<QRItem> qrList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int id);
        void onShareClick(String content);
        void onItemClick(QRItem item);
    }

    public SavedQRAdapter(List<QRItem> qrList, OnItemClickListener listener) {
        this.qrList = qrList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSavedQrBinding binding = ItemSavedQrBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        QRItem item = qrList.get(position);
        Context context = holder.itemView.getContext();
        holder.binding.tvContent.setText(item.getContent());

        // Menggunakan string resource untuk format tanggal dan tipe
        holder.binding.tvMeta.setText(context.getString(R.string.created_at, item.getDate(), item.getType()));

        try {
            Bitmap qrBitmap = QRUtils.generateQRCode(item.getContent(), 200, 200);
            holder.binding.ivThumbnail.setImageBitmap(qrBitmap);
            holder.binding.ivThumbnail.setPadding(0, 0, 0, 0);
        } catch (WriterException e) {
            holder.binding.ivThumbnail.setImageResource(R.drawable.ic_qr_placeholder);
        }

        holder.binding.btnDelete.setOnClickListener(v -> listener.onDeleteClick(item.getId()));
        holder.binding.btnShare.setOnClickListener(v -> listener.onShareClick(item.getContent()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return qrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemSavedQrBinding binding;
        public ViewHolder(ItemSavedQrBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class QRItem {
        private int id;
        private String content;
        private String type;
        private String date;

        public QRItem(int id, String content, String type, String date) {
            this.id = id;
            this.content = content;
            this.type = type;
            this.date = date;
        }

        public int getId() { return id; }
        public String getContent() { return content; }
        public String getType() { return type; }
        public String getDate() { return date; }
    }
}
