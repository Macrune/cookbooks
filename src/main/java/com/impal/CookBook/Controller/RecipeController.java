package com.impal.CookBook.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.impal.CookBook.Model.*;
import com.impal.CookBook.Payload.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService service;

    @GetMapping("/allrecipe")    
    public ResponseEntity<List<RecipeResponse>> allRecipe() {
        List<Recipe> allRes = service.getAllRecipe();
        List<RecipeResponse> allRecipesResponse = new ArrayList<RecipeResponse>();
        for (Recipe re : allRes) {
            allRecipesResponse.add(service.convertToResponse(re));
        }
        
        return new ResponseEntity<List<RecipeResponse>>(allRecipesResponse, HttpStatus.OK);
    }
    
    @GetMapping("/findRecipe")    
    public ResponseEntity<?> findByIngredient(@RequestParam String find) {
        List<RecipeResponse> allRecipesResponse = new ArrayList<RecipeResponse>();
        try {
            if (find.isEmpty()) {
                List<Recipe> allRes = service.getAllRecipe();
                for (Recipe re : allRes) {
                    allRecipesResponse.add(service.convertToResponse(re));
                }
            }else {
                List<Recipe> findRes = service.findByText(find);
                for (Recipe re : findRes) {
                    allRecipesResponse.add(service.convertToResponse(re));
                }
            }
            return new ResponseEntity<List<RecipeResponse>>(allRecipesResponse, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping("/{imdbId}")
    public String getRecipeFromId(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie,
                                Model model) {
        try {
            Recipe rp = service.findRecipeByImdbId(imdbId);
            RecipeResponse response = service.convertToResponse(rp);
            if (rp.getRating().containsKey(cookie)) {
                model.addAttribute("rating", rp.getRating().get(cookie));
            }
            model.addAttribute("data", response);
            return "recipe";
        } catch (Exception e) {
            return "redirect:/homepage";
        }
    }

    @GetMapping("/{imdbId}/addBookmark")
    public String addToBookmark(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            service.addToBookmark(imdbId, cookie);
            return "redirect:/"+imdbId;
        } catch (Exception e) {
            return "redirect:/homepage";
        }
    }

    @PostMapping("/{imdbId}/addRating")
    public String postMethodName(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, int rating) {
        try {
            service.addRating(imdbId, cookie, rating);
            return "redirect:/"+imdbId;
        } catch (Exception e) {
            return "redirect:/homepage";
        }
    }
    
}
