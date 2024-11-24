package com.impal.CookBook.Model;

import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "image")
public class Image {
    @Id
    private ObjectId id;
    private String tittle;
    private Binary image;
}
