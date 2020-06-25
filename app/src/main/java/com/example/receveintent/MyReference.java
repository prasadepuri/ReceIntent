package com.example.receveintent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "myreferenc_table")
public class MyReference {
    @NonNull
    @ColumnInfo(name="title")
    public String title;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="url")
    public String url;
    @ColumnInfo(name="description")
    public String description;
    @NonNull
    @ColumnInfo(name="timestamp")
    public long timestamp;

    public MyReference(@NonNull String title, @NonNull String url, String description, @NonNull long timestamp) {
        this.title = title;
        this.url = url;
        this.description = description;
        this.timestamp = timestamp;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull long timestamp) {
        this.timestamp = timestamp;
    }
}
