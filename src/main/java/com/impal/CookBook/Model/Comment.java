package com.impal.CookBook.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import jakarta.validation.constraints.NotBlank;

@Document(collection = "comment")
public class Comment {
    @Id
    private ObjectId id;
    @DocumentReference
    @NotBlank
    private User author;
    private String body;

    public Comment() {
    }

    public Comment(ObjectId id, User author, String body) {
        this.id = id;
        this.author = author;
        this.body = body;
    }

    public Comment(User author, String body) {
        this.author = author;
        this.body = body;
    }

    public Comment(String body) {
        this.body = body;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    
    
}
