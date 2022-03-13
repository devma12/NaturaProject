package com.natura.web.server.ports.database.entities;

import jakarta.persistence.*;

@Entity(name = "image")
public class ImageEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "data", columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] data;

    public ImageEntity() {
        this.id = Long.valueOf(-1);
    }

    public ImageEntity(String name, String type, byte[] bytes) {
        this();
        this.name = name;
        this.type = type;
        this.data = bytes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
