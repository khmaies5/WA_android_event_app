package com.khmaies.waandroideventapp.data.model;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */

public class Event {
    private int id;
    private String title;
    private String date;

    public Event(int id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
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
}

