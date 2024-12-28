package com.nuresemonovoleh.android_pzpi_23_2_semonov_oleh_labtask4;

public class Note {
    private String title;
    private String description;
    private int importance;
    private String dateTime;
    private String imageUri;

    public Note(String title, String description, int importance, String dateTime, String imageUri) {
        this.title = title;
        this.description = description;
        this.importance = importance;
        this.dateTime = dateTime;
        this.imageUri = imageUri;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getImportance() { return importance; }
    public void setImportance(int importance) { this.importance = importance; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}

