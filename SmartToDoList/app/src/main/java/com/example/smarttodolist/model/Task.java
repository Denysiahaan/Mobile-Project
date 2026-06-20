package com.example.smarttodolist.model;

/**
 * Class Model untuk objek Task (Tugas).
 * Digunakan untuk menampung data yang diambil dari SQLite agar bisa diproses oleh UI.
 */
public class Task {
    private int id;                 // ID Unik (Primary Key)
    private String title;           // Judul Tugas
    private String priority;        // Prioritas (Penting, Sedang, Ringan)
    private int isDone;             // Status Selesai (0: Belum, 1: Selesai)
    private String createdDate;     // Tanggal tugas dibuat
    private String deadlineDate;    // Tanggal batas waktu (Deadline)
    private String statusTask;      // Deskripsi status (misal: "Terlambat", "2 Hari Lagi")

    // Constructor Kosong
    public Task() {}

    // Constructor dengan 6 parameter (tanpa statusTask)
    public Task(int id, String title, String priority, int isDone, String createdDate, String deadlineDate) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.isDone = isDone;
        this.createdDate = createdDate;
        this.deadlineDate = deadlineDate;
        this.statusTask = ""; // Inisialisasi default
    }

    // Constructor Lengkap untuk inisialisasi cepat
    public Task(int id, String title, String priority, int isDone, String createdDate, String deadlineDate, String statusTask) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.isDone = isDone;
        this.createdDate = createdDate;
        this.deadlineDate = deadlineDate;
        this.statusTask = statusTask;
    }

    // --- Getter & Setter ---
    // Berfungsi untuk akses data secara terbungkus (Encapsulation)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public int getIsDone() { return isDone; }
    public void setIsDone(int isDone) { this.isDone = isDone; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }

    public String getDeadlineDate() { return deadlineDate; }
    public void setDeadlineDate(String deadlineDate) { this.deadlineDate = deadlineDate; }

    public String getStatusTask() { return statusTask; }
    public void setStatusTask(String statusTask) { this.statusTask = statusTask; }
}
