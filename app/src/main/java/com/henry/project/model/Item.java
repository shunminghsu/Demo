package com.henry.project.model;

import com.google.gson.annotations.SerializedName;

public class Item {
    //Using gson to define our json format
    @SerializedName("image_path")
    private String imagePath;
    @SerializedName("description")
    private String description;
    @SerializedName("title")
    private String title;
    @SerializedName("just_image")
    private boolean justImage;

    public Item(String imagePath, String title, String description, boolean justImage) {
        this.imagePath = imagePath;
        this.description = description;
        this.title = title;
        this.justImage = justImage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public boolean isJustImage() {
        return justImage;
    }
}
