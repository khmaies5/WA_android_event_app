package com.khmaies.waandroideventapp.data.model;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */

import java.util.List;

public class Meeting {
    private int id;
    private String title;
    private String date;
    private List<String> guests;

    public Meeting(int id, String title, String date, List<String> guests) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.guests = guests;
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

    public List<String> getGuests() {
        return guests;
    }
}

