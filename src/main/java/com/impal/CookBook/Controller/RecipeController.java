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
@RequestMapping("/recipes")
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
    public String findByIngredient(@RequestParam String find, Model model) {
        List<RecipeCardResponse> allRecipesResponse = new ArrayList<RecipeCardResponse>();
        try {
            if (find.isEmpty()) {
                List<Recipe> allRes = service.getAllRecipe();
                for (Recipe re : allRes) {
                    allRecipesResponse.add(service.convertToResponseCard(re));
                }
            }else {
                List<Recipe> findRes = service.findByText(find);
                for (Recipe re : findRes) {
                    allRecipesResponse.add(service.convertToResponseCard(re));
                }
            }
            model.addAttribute("data", allRecipesResponse);
            return "ingredients";
        }catch (Exception e) {
            model.addAttribute("data", null);
            return "ingredients";
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
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/homepage";
        }
    }

    @GetMapping("/{imdbId}/removeBookmark")
    public String removeToBookmark(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            service.removeBookmark(imdbId, cookie);
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/homepage";
        }
    }

    @PostMapping("/{imdbId}/addRating")
    public String addRating(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, int rating) {
        try {
            service.addRating(imdbId, cookie, rating);
            return "redirect:/"+imdbId;
        } catch (Exception e) {
            return "redirect:/homepage";
        }
    }

    @GetMapping("/createRecipe")
    public String getCreateRecipe() {
        return "addRecipe";
    }
    
    
}
