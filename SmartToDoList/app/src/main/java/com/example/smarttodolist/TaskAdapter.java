package com.example.smarttodolist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private OnTaskClickListener listener;

    public interface OnTaskClickListener {
        void onCheckClick(Task task);
        void onOptionClick(Task task);
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskClickListener listener) {
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        
        // Menggunakan getDate() dari class Task (bukan model.Task)
        holder.tvDate.setText(task.getDate());
        
        holder.cbDone.setChecked(task.getIsDone() == 1);
        holder.tvPriority.setText(task.getPriority());

        // Mengatur warna badge prioritas
        int priorityColor;
        if (task.getIsDone() == 1) {
            priorityColor = ContextCompat.getColor(context, R.color.status_done);
            holder.tvPriority.setText(R.string.status_done);
        } else {
            switch (task.getPriority()) {
                case "Penting":
                    priorityColor = ContextCompat.getColor(context, R.color.priority_high);
                    break;
                case "Sedang":
                    priorityColor = ContextCompat.getColor(context, R.color.priority_medium);
                    break;
                default:
                    priorityColor = ContextCompat.getColor(context, R.color.priority_low);
                    break;
            }
        }
        
        holder.cardPriority.setCardBackgroundColor(priorityColor);
        holder.tvPriority.setTextColor(Color.WHITE);

        holder.cbDone.setOnClickListener(v -> listener.onCheckClick(task));
        holder.ivOption.setOnClickListener(v -> listener.onOptionClick(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbDone;
        TextView tvTitle, tvDate, tvPriority;
        MaterialCardView cardPriority;
        ImageView ivOption;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbDone = itemView.findViewById(R.id.cb_done);
            tvTitle = itemView.findViewById(R.id.tv_title);
            // ID disesuaikan dengan item_task.xml
            tvDate = itemView.findViewById(R.id.tv_deadline);
            tvPriority = itemView.findViewById(R.id.tv_priority_badge);
            cardPriority = itemView.findViewById(R.id.card_priority_badge);
            ivOption = itemView.findViewById(R.id.iv_option);
        }
    }
}
