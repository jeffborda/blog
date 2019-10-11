package com.jeffborda.messenger.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class BlogPost {

    private String body;
    private String timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private ApplicationUser user;

    public BlogPost() {}

    public BlogPost(String body, ApplicationUser user) {
        this.body = body;
        this.timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        this.user = user;
    }


    public String getBody() {
        return body;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public long getId() {
        return id;
    }

    public ApplicationUser getUser() {
        return user;
    }

    @Override
    public String toString() {
        return String.format("Posted on: %s.  %s", this.timestamp, this.body);
    }
}
