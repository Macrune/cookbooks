package com.impal.CookBook.Model;

import java.util.ArrayList;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import jakarta.validation.constraints.NotBlank;

@Document(collection = "recipe")
public class Recipe {
    @Id
    private ObjectId id;
    private String imdbId;
    @DocumentReference
    @NotBlank
    private User author;
    private String tittle;
    private String Description;
    private int cookTime;
    private ArrayList<String> tags;
    private String prepCategory;
    private int servings;
    private Map<String, Integer> rating;
    private String mainImage;
    private ArrayList<String> ingredients;
    private ArrayList<String> body;
    private ArrayList<String> images;
    @DocumentReference
    private ArrayList<Comment> comments;

    public Recipe() {}

    public Recipe(ObjectId id, User author, String tittle, String Description, int cookTime, ArrayList<String> tags, String prepCategory, int servings, Map<String, Integer> rating, String mainImage, ArrayList<String> ingredients, ArrayList<String> body, ArrayList<String> images, ArrayList<Comment> comments) {
        this.id = id;
        this.imdbId = id.toString();
        this.author = author;
        this.tittle = tittle;
        this.Description = Description;
        this.cookTime = cookTime;
        this.tags = tags;
        this.prepCategory = prepCategory;
        this.servings = servings;
        this.rating = rating;
        this.mainImage = mainImage;
        this.ingredients = ingredients;
        this.body = body;
        this.images = images;
        this.comments = comments;
    }

    public Recipe(ObjectId id, User author, String tittle, String Description, int cookTime, ArrayList<String> tags, String prepCategory, int servings, Map<String, Integer> rating, String mainImage, ArrayList<String> ingredients, ArrayList<String> body, ArrayList<String> images) {
        this.id = id;
        this.imdbId = id.toString();
        this.author = author;
        this.tittle = tittle;
        this.Description = Description;
        this.cookTime = cookTime;
        this.tags = tags;
        this.prepCategory = prepCategory;
        this.servings = servings;
        this.rating = rating;
        this.mainImage = mainImage;
        this.ingredients = ingredients;
        this.body = body;
        this.images = images;
        this.comments = new ArrayList<>();
    }

    public Recipe(ObjectId id, String tittle, String Description, int cookTime, ArrayList<String> tags, String prepCategory, int servings, Map<String, Integer> rating, String mainImage, ArrayList<String> ingredients, ArrayList<String> body, ArrayList<String> images) {
        this.id = id;
        this.imdbId = id.toString();
        this.tittle = tittle;
        this.Description = Description;
        this.cookTime = cookTime;
        this.tags = tags;
        this.prepCategory = prepCategory;
        this.servings = servings;
        this.rating = rating;
        this.mainImage = mainImage;
        this.ingredients = ingredients;
        this.body = body;
        this.images = images;
        this.comments = new ArrayList<>();
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public String getPrepCategory() {
        return prepCategory;
    }

    public void setPrepCategory(String prepCategory) {
        this.prepCategory = prepCategory;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public Map<String, Integer> getRating() {
        return rating;
    }

    public void setRating(Map<String, Integer> rating) {
        this.rating = rating;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getBody() {
        return body;
    }

    public void setBody(ArrayList<String> body) {
        this.body = body;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    

}
