package com.example.oollan.androidexam.hero;

import android.graphics.Bitmap;

public class Hero {

    private String title;
    private String abilities;
    private Bitmap image;

    public Hero(String title, String abilities, Bitmap image) {
        this.title = title;
        this.abilities = abilities;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getAbilities() {
        return abilities;
    }

    public Bitmap getImage() {
        return image;
    }
}