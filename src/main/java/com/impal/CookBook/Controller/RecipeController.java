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



//Controller untuk semua recipe
@Controller
@RequestMapping("/recipes")
public class RecipeController {

    //Instantiate RecipeService
    @Autowired
    private RecipeService service;

    //Instantiate UserService
    @Autowired
    private UserService userService;

    //Mapping untuk get all recipe
    @GetMapping("/allrecipe")    
    public ResponseEntity<List<RecipeResponse>> allRecipe() {
        //Get all recipe menggunkan service
        List<Recipe> allRes = service.getAllRecipe();

        //Convert recipe menjadi response
        List<RecipeResponse> allRecipesResponse = new ArrayList<RecipeResponse>();
        for (Recipe re : allRes) {
            allRecipesResponse.add(service.convertToResponse(re));
        }
        
        //return response entitiy dengan semua recipe
        return new ResponseEntity<List<RecipeResponse>>(allRecipesResponse, HttpStatus.OK);
    }
    

    //Mapping untuk findRecipe
    @GetMapping("/findRecipe")    
    public String findByIngredient(@RequestParam(defaultValue = "") String find, Model model) {

        //Prepare list dari response card
        List<RecipeCardResponse> allRecipesResponse = new ArrayList<RecipeCardResponse>();
        try {
            //Get all recipe dan convert menjadi response card
            List<Recipe> allRes = service.getAllRecipe();
            for (Recipe re : allRes) {
                allRecipesResponse.add(service.convertToResponseCard(re));
            }

            //Ketika parameter find null maka parameter find akan menjadi ""
            if (find.isEmpty()) {
                model.addAttribute("find", "");
            }else {
                //Ketika find memiliki value maka model akan ditambahkan find dengna value
                model.addAttribute("find", find);
            }

            //Menyimpan semua recipe card ke attribute data
            model.addAttribute("data", allRecipesResponse);
            return "ingredients";
        }catch (Exception e) {
            model.addAttribute("data", null);
            return "ingredients";
        }
    }

    //Post mapping untuk find recipe ketika menggunakan seach bar
    @PostMapping("/findRecipe")
    public String postMethodName(String find) {
        //Ketika tidak ada value di find dari searachbar maka langsung rediredt ke mapping get
        if (find.isEmpty() || find == null) {
            return "redirect:/recipes/findRecipe";
        }
        //Ketika terdapat value find dari searachbar maka akan reciredct dengan parameter
        return "redirect:/recipes/findRecipe?find="+find;
    }
    
    //Mapping untuk membuka recipe dengan id
    @GetMapping("/{imdbId}")
    public String getRecipeFromId(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie,
                                Model model) {
        try {
            //Melakukan find recipe berdasarkan imdbId menggunakan service
            Recipe rp = service.findRecipeByImdbId(imdbId);
            //Convert recipe menjadi RecipeResponse
            RecipeResponse response = service.convertToResponse(rp);

            //Pengecekan apakah user merupakan "Guest" atau telah login
            if (cookie.equals("Guest")) {
                model.addAttribute("isLoggedIn", false);
                model.addAttribute("hasBookmarked", false);
            }else {
                //Jika user telah login maka akan dikalakukan pencarian berdasarkan id user
                User user = userService.findUserByImdbId(cookie);
                boolean cek = false;
                
                //Cek pada user apakah resep telah di bookmark oleh user
                //Jika user telag bookmark maka ditambahkan hasBookmarked dengan value true
                for (Recipe recipe : user.getBookmarks()) {
                    if (recipe.getImdbId().matches(imdbId)) {
                        model.addAttribute("hasBookmarked", true);
                        cek = true;
                        break;
                    }
                }
                //Jika user belum bookmark maka hasBookmarked menjadi false
                if (!cek) {
                    model.addAttribute("hasBookmarked", false);
                }

                //Set attribute isLoggedIn menjadi true
                model.addAttribute("isLoggedIn", true);
            }

            //Cek apakah user telah memberi rating ke recipe
            if (rp.getRating().containsKey(cookie)) {
                //Jika sudah maka nilai rating akan di set ke attribute userRating
                model.addAttribute("userRating", rp.getRating().get(cookie));
            }else{
                //Jika belum pernah rating maka userRating bernilai 0
                model.addAttribute("userRating", 0);
            }


            //Set data recipe yang telah dikonversi ke attribute data
            model.addAttribute("data", response);
            return "recipe";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    //Mapping untuk melakukan bookmark pada recipe berdasarkan id
    @GetMapping("/{imdbId}/addBookmark")
    public String addToBookmark(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            //Melakukan addBookmark menggunakan service
            service.addToBookmark(imdbId, cookie);
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    //Mapping remove bookmark berdasarkan id
    @GetMapping("/{imdbId}/removeBookmark")
    public String removeToBookmark(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            //Melakukan remove bookmark menggunakan imdbId dan session user pada service
            service.removeBookmark(imdbId, cookie);
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    //Mapping untuk menambahkan rating pada recipe
    @PostMapping("/{imdbId}/addRating")
    public String addRating(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, int rating) {
        try {
            //Melakukan addRating menggunakan id dan session user pada service
            service.addRating(imdbId, cookie, rating);
            return "redirect:/recipes/"+imdbId;
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    //Get Mapping untuk request recipe form
    @GetMapping("/createRecipe")
    public String getCreateRecipe(@CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        //Joka user belum login maka akan redirect ke login
        if (cookie.equals("Guest")) {
            return "redirect:/login";
        }else {
            return "addRecipe";
        }
    }
    
    //Post mapping untuk submit recipe form
    @PostMapping("/createRecipe")
    public String postCreateRecipe(CreateRecipeRequest request, 
            @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            //Jika user belum login maka redirect ke login
            if (cookie.equals("Guest")) {
                return "redirect:/login";
            }else {
                //Jika user usedah login maka akan melakukan create recipe mengugnakan service
                String id = service.createRecipe(request, cookie);
                return "redirect:/recipes/" + id;
            }
        }catch (Exception e) {
            request.setTittle(e.getMessage());
            return "redirect:/";
        }
    }
}
