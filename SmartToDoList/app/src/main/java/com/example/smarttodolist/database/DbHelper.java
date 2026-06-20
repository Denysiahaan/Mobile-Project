package com.example.smarttodolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smarttodolist.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Class DbHelper mengelola pembuatan database SQLite dan operasi CRUD (Create, Read, Update, Delete).
 * Mewarisi dari SQLiteOpenHelper untuk manajemen versi database.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Nama file database dan versi
    private static final String DATABASE_NAME = "todolist_pro.db";
    private static final int DATABASE_VERSION = 2;

    // Nama tabel dan konstanta kolom-kolomnya
    public static final String TABLE_TASKS = "tb_tasks";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_PRIORITY = "priority";
    public static final String COLUMN_IS_DONE = "is_done";
    public static final String COLUMN_CREATED_DATE = "created_date";
    public static final String COLUMN_DEADLINE_DATE = "deadline_date";

    // Query SQL untuk membuat tabel
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_PRIORITY + " TEXT, " +
                    COLUMN_IS_DONE + " INTEGER, " +
                    COLUMN_CREATED_DATE + " TEXT, " +
                    COLUMN_DEADLINE_DATE + " TEXT);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Dipanggil saat database pertama kali dibuat.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    /**
     * Dipanggil saat DATABASE_VERSION ditingkatkan.
     * Berguna untuk melakukan migrasi skema tabel tanpa kehilangan data jika diatur dengan benar.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Untuk demo ini, kita hapus tabel lama dan buat baru
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            onCreate(db);
        }
    }

    /**
     * Menambahkan tugas baru ke dalam database.
     */
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_PRIORITY, task.getPriority());
        values.put(COLUMN_IS_DONE, task.getIsDone());
        values.put(COLUMN_CREATED_DATE, task.getCreatedDate());
        values.put(COLUMN_DEADLINE_DATE, task.getDeadlineDate());
        
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    /**
     * Memperbarui data tugas yang sudah ada.
     */
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_PRIORITY, task.getPriority());
        values.put(COLUMN_IS_DONE, task.getIsDone());
        values.put(COLUMN_DEADLINE_DATE, task.getDeadlineDate());
        
        // Memperbarui baris berdasarkan ID
        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
    }

    /**
     * Menghapus tugas berdasarkan ID.
     */
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * Mengambil semua daftar tugas dari database.
     * Diurutkan: yang belum selesai muncul di atas, lalu berdasarkan ID terbaru.
     */
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS + " ORDER BY " + COLUMN_IS_DONE + " ASC, " + COLUMN_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)));
                task.setPriority(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRIORITY)));
                task.setIsDone(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_DONE)));
                task.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_DATE)));
                task.setDeadlineDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEADLINE_DATE)));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }
}
