package com.impal.CookBook.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.impal.CookBook.Model.*;
import com.impal.CookBook.Payload.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService service;

    @GetMapping("/test")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/allrecipe")    
    public ResponseEntity<List<RecipeResponse>> allRecipe() {
        List<Recipe> allRes = service.getAllRecipe();
        List<RecipeResponse> allRecipesResponse = new ArrayList<RecipeResponse>();
        for (Recipe re : allRes) {
            allRecipesResponse.add(service.convertToResponse(re));
        }
        
        return new ResponseEntity<List<RecipeResponse>>(allRecipesResponse, HttpStatus.OK);
    }
    

    @GetMapping("/recipes/{imdbId}")
    public ResponseEntity<?> getRecipeFromId(@PathVariable String imdbId) {
        try {
            return new ResponseEntity<RecipeResponse>(service.convertToResponse(service.findRecipeByImdbId(imdbId)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
