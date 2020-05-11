package com.natura.web.server.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@DiscriminatorValue("INSECT")
public class Insect extends Entry {

    public Insect() { super(); }

    public Insect(String name, Date date, String description, String location) {

        super(name, date, description, location);
    }
}
