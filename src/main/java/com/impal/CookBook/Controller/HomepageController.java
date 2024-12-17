package com.impal.CookBook.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import com.impal.CookBook.Model.Recipe;
import com.impal.CookBook.Payload.RecipeCardResponse;


@Controller
@RequestMapping()
public class HomepageController {
    @Autowired
    RecipeService service;

    @Autowired
    UserService userService;

    @Autowired
    ImageController imageController;

    @GetMapping("/homepage")
    public String mainHomepage(@CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, Model model) {
        ResponseEntity<?> featured = getFeaturedRecipe();
        ResponseEntity<?> community = getCommunityRecipe();
        ResponseEntity<?> icon = imageController.getIcon();
        
        model.addAttribute("user", cookie);
        model.addAttribute("icon", icon.getBody());
        model.addAttribute("featured", featured.getBody());
        model.addAttribute("community", community.getBody());
        return "Home";
    }
    

    @GetMapping("/api/homepage/featured")
    public ResponseEntity<?> getFeaturedRecipe() {

        try {
            ArrayList<String> tag = new ArrayList<>();
            tag.add("featured");
            List<Recipe> recipes = service.findByTagsIn(tag);
            ArrayList<RecipeCardResponse> cardRecipes = new ArrayList<>();
            for (Recipe re: recipes) {
                cardRecipes.add(service.convertToResponseCard(re));
            }
            return new ResponseEntity<ArrayList<RecipeCardResponse>>(cardRecipes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/api/homepage/community")
    public ResponseEntity<?> getCommunityRecipe() {

        try {
            ArrayList<String> tag = new ArrayList<>();
            tag.add("community");
            List<Recipe> recipes = service.findByTagsIn(tag);
            ArrayList<RecipeCardResponse> cardRecipes = new ArrayList<>();
            for (Recipe re: recipes) {
                cardRecipes.add(service.convertToResponseCard(re));
            }
            return new ResponseEntity<ArrayList<RecipeCardResponse>>(cardRecipes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
}
