package com.camuyen.quanlybug.model;

import android.graphics.Color;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class BugStatus {
    String status;
    String background;
    String text;
    List<BugStatus> bugStatusList = new ArrayList<>();

    public List<BugStatus> getBugStatusList() {
        bugStatusList.add(new BugStatus("New", "#fed0ca", "#8f202a"));
        bugStatusList.add(new BugStatus("Open", "#fed0ca", "#4f7398"));
        bugStatusList.add(new BugStatus("Fix", "#d3edbc", "#3f6c3f"));
        bugStatusList.add(new BugStatus("Pending", "#18a034", "#c0f8cf"));
        bugStatusList.add(new BugStatus("Reopen", "#f14747", "#ffe1e1"));
        bugStatusList.add(new BugStatus("Close", "#8d8586", "#f9f8f1"));
        bugStatusList.add(new BugStatus("Rejected", "#e6e6e6", "#645d63"));
        return bugStatusList;
    }

    public BugStatus(String status, String background, String text) {
        this.status = status;
        this.background = background;
        this.text = text;
    }

    public BugStatus() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return status;
    }
}
