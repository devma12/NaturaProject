package com.natura.web.server.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("ENTRY")
public abstract class Entry extends ValidableItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User createdBy;

    @OneToOne
    @JoinColumn(name = "image_id")
    Image image;

    @Column(columnDefinition="TEXT")
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

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
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

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

}
