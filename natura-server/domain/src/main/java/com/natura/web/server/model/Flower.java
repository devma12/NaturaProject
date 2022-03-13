package com.natura.web.server.model;

import java.util.Date;

public class Flower extends Entry {

    public Flower() { super(); }

    public Flower(String name, Date date, String description, String location) {
        super(name, date, description, location);
    }
}
