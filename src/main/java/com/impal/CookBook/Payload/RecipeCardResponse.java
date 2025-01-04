package com.impal.CookBook.Payload;

import java.util.ArrayList;

public class RecipeCardResponse {

    private String imdbId;
    private UserInfoResponse author;
    private String tittle;
    private String Description;
    private int cookTime;
    private String prepCategory;
    private int servings;
    private double rating;
    private String mainImage;
    private ArrayList<String> tags;

    public RecipeCardResponse(String imdbId, UserInfoResponse author, String tittle, String Description, int cookTime, String prepCategory, int servings, double rating, String mainImage, ArrayList<String> tags) {
        this.imdbId = imdbId;
        this.author = author;
        this.tittle = tittle;
        this.Description = Description;
        this.cookTime = cookTime;
        this.prepCategory = prepCategory;
        this.servings = servings;
        this.rating = rating;
        this.mainImage = mainImage;
        this.tags = tags;
    }

    public RecipeCardResponse() {
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
