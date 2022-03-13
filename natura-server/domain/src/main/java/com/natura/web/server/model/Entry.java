package com.natura.web.server.model;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Entry extends ValidableItem {

    private Long id;

    private String name;

    private User createdBy;

    private Image image;

    private String description;

    private String location;

    private Date date;

    public Entry() {
        super();
    }

    public Entry(String name) {
        this();
        this.name = name;
    }

    public Entry(String name, Date date, String description, String location) {
        this();
        this.name = name;
        this.date = date;
        this.description =description;
        this.location = location;
    }
}
