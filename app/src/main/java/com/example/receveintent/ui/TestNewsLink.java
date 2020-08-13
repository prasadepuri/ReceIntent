package com.example.receveintent.ui;

public class TestNewsLink {
    String link,title,imageUrl,category;

    public String getLink() {
        return link;
    }

    public TestNewsLink(String link, String title, String imageUrl, String category) {
        this.link = link;
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
