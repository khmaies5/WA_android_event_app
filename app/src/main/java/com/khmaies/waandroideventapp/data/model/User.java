package com.khmaies.waandroideventapp.data.model;

/**
 * Created by Khmaies Hassen on 09,August,2023
 */

public class User {
    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

