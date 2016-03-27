package com.example.shiv.fekc.item;

import android.graphics.drawable.Drawable;

/**
 * Created by shiv on 10/3/16.
 */
public class AppItem {

    private String appName;
    private Drawable appIcon;
    private boolean isSelected;
    private boolean isFavorite;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
