package com.natura.web.server.entities;

import jakarta.persistence.*;

import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType= DiscriminatorType.STRING)
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

    protected Entry() {
        super();
    }

    protected Entry(String name) {
        this();
        this.name = name;
    }

    protected Entry(String name, Date date, String description, String location) {
        this();
        this.name = name;
        this.date = date;
        this.description =description;
        this.location = location;
    }
}
