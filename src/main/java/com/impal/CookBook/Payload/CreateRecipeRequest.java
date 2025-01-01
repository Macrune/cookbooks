package com.impal.CookBook.Payload;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

public class CreateRecipeRequest {
    private String tittle;
    private String Description;
    private int cookTime;
    private ArrayList<String> tags;
    private String prepCategory;
    private int servings;
    private MultipartFile mainImage;
    private ArrayList<String> ingredients;
    private ArrayList<String> stepInput;
    private ArrayList<MultipartFile> stepFile;

    public CreateRecipeRequest() {
    }

    public CreateRecipeRequest(String tittle, String Description, int cookTime, ArrayList<String> tags, String prepCategory, int servings, MultipartFile mainImage, ArrayList<String> ingredients, ArrayList<String> stepInput, ArrayList<MultipartFile> stepFile) {
        this.tittle = tittle;
        this.Description = Description;
        this.cookTime = cookTime;
        this.tags = tags;
        this.prepCategory = prepCategory;
        this.servings = servings;
        this.mainImage = mainImage;
        this.ingredients = ingredients;
        this.stepInput = stepInput;
        this.stepFile = stepFile;
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

    public MultipartFile getMainImage() {
        return mainImage;
    }

    public void setMainImage(MultipartFile mainImage) {
        this.mainImage = mainImage;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getStepInput() {
        return stepInput;
    }

    public void setStepInput(ArrayList<String> stepInput) {
        this.stepInput = stepInput;
    }

    public ArrayList<MultipartFile> getStepFile() {
        return stepFile;
    }

    public void setStepFile(ArrayList<MultipartFile> stepFile) {
        this.stepFile = stepFile;
    }

    
}