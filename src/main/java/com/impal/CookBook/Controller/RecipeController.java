package com.impal.CookBook.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.impal.CookBook.Model.Recipes;
import java.util.List;
import java.util.Optional;

@Controller
public class RecipeController {

    @Autowired
    RecipeService service;

    @GetMapping("/test")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/allrecipe")    
    public ResponseEntity<List<Recipes>> allRecipe() {
        return new ResponseEntity<List<Recipes>>(service.getAllRecipe(), HttpStatus.OK);
    }
    

    @GetMapping("/recipes/{imdbId}")
    public ResponseEntity<Optional<Recipes>> getRecipeFromId(@PathVariable String imdbId) {
        return new ResponseEntity<Optional<Recipes>>(service.findRecipeByImdbId(imdbId), HttpStatus.OK);
    }
    
    
}
