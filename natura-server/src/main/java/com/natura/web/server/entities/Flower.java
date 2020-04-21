package com.natura.web.server.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("FLOWER")
public class Flower extends Entry {

    public Flower() { super(); }

    public Flower(String name) {
        super(name);
    }
}
