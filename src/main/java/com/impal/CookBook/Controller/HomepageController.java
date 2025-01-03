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
@RequestMapping("/")
public class HomepageController {
    @Autowired
    private RecipeService service;

    //Mapping homepage atau index
    @GetMapping()
    public String mainHomepage(@CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, Model model) {
        //Mengambil semua recipe dengan tag featured dan community
        ResponseEntity<?> featured = getFeaturedRecipe();
        ResponseEntity<?> community = getCommunityRecipe();
        
        //Menambahkan attribute ke model untuk digunakan di html
        model.addAttribute("user", cookie);
        model.addAttribute("featured", featured.getBody());
        model.addAttribute("community", community.getBody());

        //Kirim Home.html dengan model kepada client
        return "Home";
    }
    

    //Mapping api untuk request recipe dengan tag featured
    @GetMapping("/api/homepage/featured")
    public ResponseEntity<?> getFeaturedRecipe() {

        try {
            //Buat tag yang berisi featured
            ArrayList<String> tag = new ArrayList<>();
            tag.add("featured");

            //Melakukan searching berdasarkan tag menggunakan service 
            List<Recipe> recipes = service.findByTagsIn(tag);

            //Conversi Recipe menjadi RecipeCard untuk digunakan dalam homepage
            ArrayList<RecipeCardResponse> cardRecipes = new ArrayList<>();
            for (Recipe re: recipes) {
                cardRecipes.add(service.convertToResponseCard(re));
            }

            //Return response dengan cardRecipes yang telah dibuat degnan httpstatus ok
            return new ResponseEntity<ArrayList<RecipeCardResponse>>(cardRecipes, HttpStatus.OK);
        } catch (Exception e) {
            //Return response dengan cardRecipes yang telah dibuat degnan httpstatus ok
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    //Mapping api untuk semua recipe dengan tag community
    @GetMapping("/api/homepage/community")
    public ResponseEntity<?> getCommunityRecipe() {

        try {
            //Buat tag yang berisi community
            ArrayList<String> tag = new ArrayList<>();
            tag.add("community");

            //Melakukan searching berdasarkan tag menggunakan service 
            List<Recipe> recipes = service.findByTagsIn(tag);

            //Conversi Recipe menjadi RecipeCard untuk digunakan dalam homepage
            ArrayList<RecipeCardResponse> cardRecipes = new ArrayList<>();
            for (Recipe re: recipes) {
                cardRecipes.add(service.convertToResponseCard(re));
            }

            //Return response dengan cardRecipes yang telah dibuat degnan httpstatus ok
            return new ResponseEntity<ArrayList<RecipeCardResponse>>(cardRecipes, HttpStatus.OK);
        } catch (Exception e) {
            //Return response dengan cardRecipes yang telah dibuat degnan httpstatus ok
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
}
