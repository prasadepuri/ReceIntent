package com.example.receveintent;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "myreferenc_table",indices = @Index(value = {"title","imageUrl"},unique = true))
public class MyReference {
    @NonNull
    @ColumnInfo(name="title")
    public String title;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="url")
    public String url;
    @ColumnInfo(name="description")
    public String category;

    @NonNull
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NonNull String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    @ColumnInfo(name="imageUrl")
    public String imageUrl;
    @NonNull
    @ColumnInfo(name="timestamp")
    public long timestamp;

    public MyReference(@NonNull String title, @NonNull String url, String category, @NonNull String imageUrl, @NonNull long timestamp) {
        this.title = title;
        this.url = url;
        this.category = category;
        this.imageUrl=imageUrl;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @NonNull
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(@NonNull long timestamp) {
        this.timestamp = timestamp;
    }
}
