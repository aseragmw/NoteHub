package com.example.notehub.domain;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class NoteModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String content;
    private double timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public NoteModel(String title, String content, double timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }
}
