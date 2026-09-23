package com.hangang.web.activity;

import org.springframework.web.multipart.MultipartFile;

public class ContentBlockForm {

    private String type;
    private String text;
    private MultipartFile image;
    private String existingImagePath;
    private boolean removed;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getExistingImagePath() {
        return existingImagePath;
    }

    public void setExistingImagePath(String existingImagePath) {
        this.existingImagePath = existingImagePath;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }
}
