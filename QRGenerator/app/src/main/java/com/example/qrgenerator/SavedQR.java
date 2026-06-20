package com.example.qrgenerator;

public class SavedQR {
    private int id;
    private String content;
    private String type;
    private String date;
    private String imagePath;

    public SavedQR(int id, String content, String type, String date, String imagePath) {
        this.id = id;
        this.content = content;
        this.type = type;
        this.date = date;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public String getType() { return type; }
    public String getDate() { return date; }
    public String getImagePath() { return imagePath; }
}
