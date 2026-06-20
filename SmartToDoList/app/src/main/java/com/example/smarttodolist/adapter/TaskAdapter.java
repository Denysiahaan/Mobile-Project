package com.example.smarttodolist.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttodolist.R;
import com.example.smarttodolist.model.Task;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * TaskAdapter Modern & Premium.
 * Mengelola tampilan item tugas dengan animasi dan gaya minimalis.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;
    private OnTaskClickListener listener;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());

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

        // Set Data
        holder.tvTitle.setText(task.getTitle());
        holder.tvDeadline.setText(context.getString(R.string.deadline_at, task.getDeadlineDate()));
        holder.cbDone.setChecked(task.getIsDone() == 1);

        // Gaya Modern untuk Status Selesai
        if (task.getIsDone() == 1) {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvTitle.setTextColor(context.getColor(R.color.text_gray));
            holder.cardTask.setAlpha(0.6f);
        } else {
            holder.tvTitle.setPaintFlags(holder.tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvTitle.setTextColor(context.getColor(R.color.text_dark));
            holder.cardTask.setAlpha(1.0f);
        }

        // Warna Prioritas Premium
        int priorityColor;
        switch (task.getPriority()) {
            case "Penting": priorityColor = context.getColor(R.color.priority_high); break;
            case "Sedang": priorityColor = context.getColor(R.color.priority_medium); break;
            default: priorityColor = context.getColor(R.color.priority_low); break;
        }
        holder.tvPriorityBadge.setText(task.getPriority());
        holder.cardPriorityBadge.setCardBackgroundColor(priorityColor);

        // Handle Deadline UI
        handleDeadlineUI(holder, task);

        // Click Listeners
        holder.cbDone.setOnClickListener(v -> listener.onCheckClick(task));
        holder.ivOption.setOnClickListener(v -> listener.onOptionClick(task));

        // Animasi per item saat muncul
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
    }

    private void handleDeadlineUI(TaskViewHolder holder, Task task) {
        if (task.getIsDone() == 1) {
            holder.tvDeadline.setTextColor(context.getColor(R.color.text_gray));
            return;
        }

        try {
            Date deadlineDate = sdf.parse(task.getDeadlineDate());
            Date today = sdf.parse(sdf.format(new Date()));

            if (deadlineDate != null && today != null) {
                long diffInMs = deadlineDate.getTime() - today.getTime();
                long daysLeft = TimeUnit.MILLISECONDS.toDays(diffInMs);

                if (daysLeft < 0) {
                    holder.tvDeadline.setTextColor(context.getColor(R.color.error));
                    holder.tvDeadline.setText(R.string.status_overdue);
                } else if (daysLeft == 0) {
                    holder.tvDeadline.setTextColor(context.getColor(R.color.warning));
                    holder.tvDeadline.setText(R.string.status_today);
                } else {
                    holder.tvDeadline.setTextColor(context.getColor(R.color.text_gray));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        TextView tvTitle, tvDeadline, tvPriorityBadge;
        MaterialCardView cardTask, cardPriorityBadge;
        ImageView ivOption;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbDone = itemView.findViewById(R.id.cb_done);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDeadline = itemView.findViewById(R.id.tv_deadline);
            tvPriorityBadge = itemView.findViewById(R.id.tv_priority_badge);
            cardTask = itemView.findViewById(R.id.card_task);
            cardPriorityBadge = itemView.findViewById(R.id.card_priority_badge);
            ivOption = itemView.findViewById(R.id.iv_option);
        }
    }
}
