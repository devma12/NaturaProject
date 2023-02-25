package com.natura.web.server.persistence.database.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity(name = "insect")
@DiscriminatorValue("INSECT")
public class InsectEntity extends EntryEntity {

    public InsectEntity() {
        super();
    }

    public InsectEntity(String name, Date date, String description, String location) {

        super(name, date, description, location);
    }
}
