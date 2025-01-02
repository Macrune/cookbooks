package com.impal.CookBook.Model;

import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "user")
public class User {

    @Id
    private ObjectId id;
    @NotBlank
    private String imdbId;
    @NotBlank
    private String username;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private String profilePic;
    @DocumentReference
    private ArrayList<Recipe> bookmarks;
    @DocumentReference
    private ArrayList<Recipe> myrecipes;

    public User() {
    }

    public User(ObjectId id, String imdbId, String username, String email, String password, String profilePic, ArrayList<Recipe> bookmarks, ArrayList<Recipe> myrecipes) {
        this.id = id;
        this.imdbId = imdbId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = profilePic;
        this.bookmarks = bookmarks;
        this.myrecipes = myrecipes;
    }

    public User(String imdbId, String username, String email, String password, String profilePic) {
        this.imdbId = imdbId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = "/image/67435c44d45c2a3e6f39fa7f";
        this.bookmarks = new ArrayList<>();
        this.myrecipes = new ArrayList<>();
    }

    public User(String imdbId, String username, String email, String password) {
        this.imdbId = imdbId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePic = "/image/67435c44d45c2a3e6f39fa7f";
        this.bookmarks = new ArrayList<>();
        this.myrecipes = new ArrayList<>();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public ArrayList<Recipe> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<Recipe> bookmarks) {
        this.bookmarks = bookmarks;
    }

    public ArrayList<Recipe> getMyrecipes() {
        return myrecipes;
    }

    public void setMyrecipes(ArrayList<Recipe> myrecipes) {
        this.myrecipes = myrecipes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    
}
