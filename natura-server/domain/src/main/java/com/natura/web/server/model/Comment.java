package com.natura.web.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class Comment {

    private Long id;

    private String text;

    private User commentedBy;

    private Date commentedDate;

    private Identification identification;

    public Comment(String text, User user, Date date) {
        this.text = text;
        this.commentedBy = user;
        this.commentedDate = date;
    }

}
