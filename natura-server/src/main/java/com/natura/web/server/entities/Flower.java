package com.natura.web.server.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("FLOWER")
public class Flower extends Entry {

    public Flower() { super(); }

    public Flower(String name, Date date, String description, String location) {
        super(name, date, description, location);
    }
}
