package com.natura.web.server.persistence.database.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Date;

@Entity(name = "flower")
@DiscriminatorValue("FLOWER")
public class FlowerEntity extends EntryEntity {

    public FlowerEntity() {
        super();
    }

    public FlowerEntity(String name, Date date, String description, String location) {
        super(name, date, description, location);
    }
}
