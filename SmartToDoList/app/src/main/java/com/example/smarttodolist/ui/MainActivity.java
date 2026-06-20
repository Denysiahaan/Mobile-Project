package com.example.smarttodolist.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarttodolist.R;
import com.example.smarttodolist.adapter.TaskAdapter;
import com.example.smarttodolist.database.DbHelper;
import com.example.smarttodolist.model.Task;
import com.example.smarttodolist.utils.ThemeManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * MainActivity dengan UI Modern & Premium.
 * Mengelola daftar tugas, pencarian, dan statistik.
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private List<Task> taskList = new ArrayList<>();
    private List<Task> filteredList = new ArrayList<>();
    private DbHelper dbHelper;
    private TextView tvCountAll, tvCountDone, tvCountPending, tvTodayDate;
    private EditText etSearch;
    private View layoutEmpty;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private SimpleDateFormat daySdf = new SimpleDateFormat("EEEE, dd MMM", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeManager.applyTheme(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);

        setupViews();
        setupRecyclerView();
        setupSearch();
        loadTasks();
    }

    private void setupViews() {
        rvTasks = findViewById(R.id.rv_tasks);
        tvCountAll = findViewById(R.id.tv_count_all);
        tvCountDone = findViewById(R.id.tv_count_done);
        tvCountPending = findViewById(R.id.tv_count_pending);
        tvTodayDate = findViewById(R.id.tv_today_date);
        etSearch = findViewById(R.id.et_search);
        layoutEmpty = findViewById(R.id.layout_empty);
        ExtendedFloatingActionButton fabAdd = findViewById(R.id.fab_add);
        View btnSettings = findViewById(R.id.btn_settings);

        // Set Tanggal Hari Ini
        tvTodayDate.setText(daySdf.format(new Date()));

        fabAdd.setOnClickListener(v -> showAddEditDialog(null));
        
        // Klik tombol settings di header
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            });
        }
    }

    private void setupRecyclerView() {
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, filteredList, new TaskAdapter.OnTaskClickListener() {
            @Override
            public void onCheckClick(Task task) {
                task.setIsDone(task.getIsDone() == 1 ? 0 : 1);
                dbHelper.updateTask(task);
                loadTasks();
            }

            @Override
            public void onOptionClick(Task task) {
                showActionMenu(task);
            }
        });
        rvTasks.setAdapter(adapter);
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String text) {
        filteredList.clear();
        for (Task task : taskList) {
            if (task.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(task);
            }
        }
        adapter.setTaskList(filteredList);
        layoutEmpty.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void loadTasks() {
        taskList = dbHelper.getAllTasks();
        
        // Update Stats
        int all = taskList.size();
        int done = 0;
        for (Task t : taskList) {
            if (t.getIsDone() == 1) done++;
        }
        int pending = all - done;

        tvCountAll.setText(String.valueOf(all));
        tvCountDone.setText(String.valueOf(done));
        tvCountPending.setText(String.valueOf(pending));

        // Re-apply search filter after loading
        filter(etSearch.getText().toString());
    }

    private void showActionMenu(Task task) {
        String[] options = {getString(R.string.edit_task), getString(R.string.delete_task)};
        new AlertDialog.Builder(this)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) showAddEditDialog(task);
                    else showDeleteConfirmDialog(task);
                }).show();
    }

    private void showAddEditDialog(Task taskToEdit) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_edit_task);
        
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        
        TextView tvDialogTitle = dialog.findViewById(R.id.tv_dialog_title);
        TextInputEditText etTitle = dialog.findViewById(R.id.et_task_title);
        MaterialButtonToggleGroup togglePriority = dialog.findViewById(R.id.toggle_priority);
        MaterialButton btnSetDeadline = dialog.findViewById(R.id.btn_set_deadline);
        TextView tvSelectedDeadline = dialog.findViewById(R.id.tv_selected_deadline);
        MaterialSwitch switchDone = dialog.findViewById(R.id.switch_done);
        View btnSave = dialog.findViewById(R.id.btn_save);
        View btnCancel = dialog.findViewById(R.id.btn_cancel);

        final Calendar calendar = Calendar.getInstance();

        if (taskToEdit != null) {
            tvDialogTitle.setText(R.string.edit_task);
            etTitle.setText(taskToEdit.getTitle());
            tvSelectedDeadline.setText(taskToEdit.getDeadlineDate());
            
            // Tampilkan toggle selesai hanya saat edit
            switchDone.setVisibility(View.VISIBLE);
            switchDone.setChecked(taskToEdit.getIsDone() == 1);

            if (taskToEdit.getPriority().equals("Penting")) togglePriority.check(R.id.btn_priority_high);
            else if (taskToEdit.getPriority().equals("Sedang")) togglePriority.check(R.id.btn_priority_medium);
            else togglePriority.check(R.id.btn_priority_low);
        } else {
            togglePriority.check(R.id.btn_priority_low);
            tvSelectedDeadline.setText(sdf.format(calendar.getTime()));
            switchDone.setVisibility(View.GONE);
        }

        btnSetDeadline.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                tvSelectedDeadline.setText(sdf.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty()) {
                etTitle.setError(getString(R.string.invalid_input));
                return;
            }

            int checkedId = togglePriority.getCheckedButtonId();
            String priority = "Ringan";
            if (checkedId == R.id.btn_priority_high) priority = "Penting";
            else if (checkedId == R.id.btn_priority_medium) priority = "Sedang";

            String createdDate = sdf.format(new Date());
            String deadlineDate = tvSelectedDeadline.getText().toString();

            if (taskToEdit != null) {
                taskToEdit.setTitle(title);
                taskToEdit.setPriority(priority);
                taskToEdit.setDeadlineDate(deadlineDate);
                // Set status selesai dari switch
                taskToEdit.setIsDone(switchDone.isChecked() ? 1 : 0);
                dbHelper.updateTask(taskToEdit);
                Toast.makeText(this, R.string.success_update, Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addTask(new Task(0, title, priority, 0, createdDate, deadlineDate));
                Toast.makeText(this, R.string.success_save, Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
            loadTasks();
        });

        dialog.show();
    }

    private void showDeleteConfirmDialog(Task task) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_task)
                .setMessage(R.string.delete_confirm)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    dbHelper.deleteTask(task.getId());
                    loadTasks();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }
}
