package com.natura.web.server.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("INSECT")
public class Insect extends Entry {

    public Insect() { super(); }

    public Insect(String name, String description, String location) {
        super(name, description, location);
    }
}
