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

    @Autowired
    private UserService userService;

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
    public String findByIngredient(@RequestParam(defaultValue = "") String find, Model model) {
        List<RecipeCardResponse> allRecipesResponse = new ArrayList<RecipeCardResponse>();
        try {
            List<Recipe> allRes = service.getAllRecipe();
            for (Recipe re : allRes) {
                allRecipesResponse.add(service.convertToResponseCard(re));
            }

            if (find.isEmpty()) {
                model.addAttribute("find", "");
            }else {
                model.addAttribute("find", find);
            }
            model.addAttribute("data", allRecipesResponse);
            return "ingredients";
        }catch (Exception e) {
            model.addAttribute("data", null);
            return "ingredients";
        }
    }

    @PostMapping("/findRecipe")
    public String postMethodName(String find) {
        if (find.isEmpty() || find == null) {
            return "redirect:/recipes/findRecipe";
        }
        return "redirect:/recipes/findRecipe?find="+find;
    }
    

    @GetMapping("/{imdbId}")
    public String getRecipeFromId(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie,
                                Model model) {
        try {
            Recipe rp = service.findRecipeByImdbId(imdbId);
            RecipeResponse response = service.convertToResponse(rp);

            if (cookie.equals("Guest")) {
                model.addAttribute("isLoggedIn", false);
                model.addAttribute("hasBookmarked", false);
            }else {
                User user = userService.findUserByImdbId(cookie);
                boolean cek = false;
                for (Recipe recipe : user.getBookmarks()) {
                    if (recipe.getImdbId().matches(imdbId)) {
                        model.addAttribute("hasBookmarked", true);
                        cek = true;
                        break;
                    }
                }
                if (!cek) {
                    model.addAttribute("hasBookmarked", false);
                }

                model.addAttribute("isLoggedIn", true);
            }

            if (rp.getRating().containsKey(cookie)) {
                model.addAttribute("userRating", rp.getRating().get(cookie));
            }else{
                model.addAttribute("userRating", 0);
            }



            model.addAttribute("data", response);
            return "recipe";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/{imdbId}/addBookmark")
    public String addToBookmark(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            service.addToBookmark(imdbId, cookie);
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/{imdbId}/removeBookmark")
    public String removeToBookmark(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            service.removeBookmark(imdbId, cookie);
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @PostMapping("/{imdbId}/addRating")
    public String addRating(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, int rating) {
        try {
            service.addRating(imdbId, cookie, rating);
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/createRecipe")
    public String getCreateRecipe(@CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        if (cookie.equals("Guest")) {
            return "redirect:/login";
        }else {
            return "addRecipe";
        }
    }
    
    @PostMapping("/createRecipe")
    public String postCreateRecipe(CreateRecipeRequest request, 
            @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            if (cookie.equals("Guest")) {
                return "redirect:/";
            }else {
                String id = service.createRecipe(request, cookie);
                return "redirect:/recipes/" + id;
            }
        }catch (Exception e) {
            request.setTittle(e.getMessage());
            return "redirect:/";
        }
    }
}
