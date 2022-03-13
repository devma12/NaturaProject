package com.natura.web.server.ports.database.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity(name = "comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity commentedBy;

    private Date commentedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "entry_id", referencedColumnName = "entry_id"),
            @JoinColumn(name = "species_id", referencedColumnName = "species_id"),
    })
    @JsonBackReference
    private IdentificationEntity identification;

    public CommentEntity() {
    }

    public CommentEntity(String text, UserEntity user, Date date) {
        this();
        this.text = text;
        this.commentedBy = user;
        this.commentedDate = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UserEntity getCommentedBy() {
        return commentedBy;
    }

    public void setCommentedBy(UserEntity commentedBy) {
        this.commentedBy = commentedBy;
    }

    public Date getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(Date commentedDate) {
        this.commentedDate = commentedDate;
    }

    public IdentificationEntity getIdentification() {
        return identification;
    }

    public void setIdentification(IdentificationEntity identification) {
        this.identification = identification;
    }
}
