package com.example.shiv.fekc.item;

import android.graphics.drawable.Drawable;

/**
 * Created by shiv on 10/3/16.
 */
public class AppItem {

    private String appName;
    private Drawable appIcon;
    private boolean isSelected;
    private String packageName;

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName=packageName;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
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
