package com.impal.CookBook.Payload;

import java.util.ArrayList;

public class RecipeResponse {

    private String imdbId;
    private UserInfoResponse author;
    private String tittle;
    private String Description;
    private int cookTime;
    private ArrayList<String> tags;
    private String prepCategory;
    private int servings;
    private double rating;
    private String mainImage;
    private ArrayList<String> ingredients;
    private ArrayList<String> body;
    private ArrayList<String> images;
    private ArrayList<CommentResponse> comments;

    public RecipeResponse(String imdbId, UserInfoResponse author, String tittle, String Description, int cookTime, ArrayList<String> tags, String prepCategory, int servings, double rating, String mainImage, ArrayList<String> ingredients, ArrayList<String> body, ArrayList<String> images, ArrayList<CommentResponse> comments) {
        this.imdbId = imdbId;
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

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public UserInfoResponse getAuthor() {
        return author;
    }

    public void setAuthor(UserInfoResponse author) {
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
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

    public ArrayList<CommentResponse> getComments() {
        return comments;
    }

    public void setComments(ArrayList<CommentResponse> comments) {
        this.comments = comments;
    }

    
}
