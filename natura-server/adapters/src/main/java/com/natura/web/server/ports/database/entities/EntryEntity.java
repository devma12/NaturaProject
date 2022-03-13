package com.natura.web.server.ports.database.entities;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "entry")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ENTRY")
public abstract class EntryEntity extends ValidableItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity createdBy;

    @OneToOne
    @JoinColumn(name = "image_id")
    ImageEntity image;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    private Date date;

    public EntryEntity() {
        super();
    }

    public EntryEntity(String name) {
        this();
        this.name = name;
    }

    public EntryEntity(String name, Date date, String description, String location) {
        this();
        this.name = name;
        this.date = date;
        this.description = description;
        this.location = location;
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

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public ImageEntity getImage() {
        return image;
    }

    public void setImage(ImageEntity image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
