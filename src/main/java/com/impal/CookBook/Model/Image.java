package com.impal.CookBook.Model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "image")
public class Image {
    @Id
    private ObjectId id;
    private String title;
    private byte[] image;

    public Image() {
    }

    public Image(ObjectId id, String title, byte[] image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public Image(String title, byte[] image) {
        this.title = title;
        this.image = image;
    }

    public Image(String title) {
        this.title = title;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tittle) {
        this.title = tittle;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
    
    
}
