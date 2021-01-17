package com.mktech28.wsdownloader.models;

import android.net.Uri;

public class ModelStatuses {

    String full_path;
    String path;
    int type;

    public ModelStatuses(String full_path) {
        this.full_path = full_path;
    }

    public ModelStatuses(String full_path, String path) {
        this.full_path = full_path;
        this.path = path;
    }

    public ModelStatuses(String full_path, String path, int type) {
        this.full_path = full_path;
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFull_path() {
        return full_path;
    }

    public void setFull_path(String full_path) {
        this.full_path = full_path;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
