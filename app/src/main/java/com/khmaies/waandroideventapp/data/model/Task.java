package com.khmaies.waandroideventapp.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */

public class Task {
    private int id;
    private String title;
    private String date;
    @SerializedName("complete")
    private boolean completed;

    public Task(int id, String title, String date, boolean completed) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }

}

