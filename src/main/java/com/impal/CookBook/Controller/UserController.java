package com.impal.CookBook.Controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.impal.CookBook.Model.Recipe;
import com.impal.CookBook.Model.User;
import com.impal.CookBook.Payload.LoginRequest;
import com.impal.CookBook.Payload.RecipeCardResponse;
import com.impal.CookBook.Payload.SignupRequest;
import com.impal.CookBook.Payload.UserInfoResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;





@Controller
@RequestMapping("")
public class UserController {

    //Instantiate userSerice
    @Autowired
    private UserService service;

    //Instantiate recipeService
    @Autowired
    private RecipeService recipeService;


    //Get mappin untuk requst form login
    @GetMapping("/login")
    public String Login() {
        return "Loginform";
    }

    //Get mapping untuk melakukan logout
    @GetMapping("/logout")
    public String Logout(HttpServletResponse response) {
        //Set cookie user menjadi null
        Cookie cookie = new Cookie("userCookie", null);
        response.addCookie(cookie);
        return "redirect:/login";
    }

    //Get mapping untuk request form register
    @GetMapping("/register")
    public String signupForm() {
        return "sign";
    }
    
    //Post mapping untuk submit data login
    @PostMapping("/login")
    public String login(LoginRequest request,  HttpServletResponse response) {
        try {
            //Authenticate user dengan authenticateUser pada service
            User user = service.authenticateUser(request.getUsername(), request.getPassword());
            //Jika berhasil di authentikasi maka cookie akan diganti dengan id user
            Cookie cookie = new Cookie("userCookie", user.getImdbId());
            response.addCookie(cookie);
            return "redirect:/";
        }catch(Exception e) {
            return "/login";
        }
    }

    //Post mapping untuk submit data register
    @PostMapping("/register")
    public String register(SignupRequest request) {
        try {
            //Melakukan register menggunakan registerUser pada service
            service.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
            return "redirect:/login";
        }catch(Exception e) {
            return "redirect:/";
        }
    }
    

    //Versi API untuk login dan register
    @PostMapping("/api/account/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> payload) {
        try {
            User user = service.authenticateUser(payload.get("username"), payload.get("password"));
            UserInfoResponse userResponse = new UserInfoResponse(user.getImdbId(), user.getUsername(), user.getProfilePic());
            Cookie cookie = new Cookie("user_name", payload.get("username"));
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(userResponse);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/api/account/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
        try {
            service.registerUser(payload.get("username"), payload.get("email"), payload.get("password"));
            return new ResponseEntity<String>("register.User registered successfully!", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/account/logout")
    public ResponseEntity<?> logoutUser() {
        try {
            return new ResponseEntity<String>("logout.User logged out successfully!", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //Get mappin ketika ingin membuka profile
    @GetMapping("/profile")
    public String userProfile(@CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, Model model) {
        try {
            //Jika belum login maka  akan redirect ke login
            if (cookie.equals("Guest")) {
                return "redirect:/login";
            }else {
                //Jika sudah login maka akan dicek apakah user ada
                //Jika ada maka redirect ke profile page user
                @SuppressWarnings("unused")
                User user = service.findUserByImdbId(cookie);
                return "redirect:/account/" + cookie;
            }
        }catch (Exception e) {
            return "redirect:/";
        }
    }
    
    //Get mapping untuk membuka profile page berdasarkan id
    @GetMapping("/account/{imdbId}")
    public String findUser(@PathVariable String imdbId, @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie,
                            Model model) {
        try {
            //Convert User menjadi UserInfoResponse
            UserInfoResponse userResponse = service.convertToResponse(service.findUserByImdbId(imdbId));
    
            //find user by imdbId
            User user = service.findUserByImdbId(imdbId);

            //Get semua recipe dari myRecipes dan bookmarks
            ArrayList<RecipeCardResponse> myRecipes = new ArrayList<>();
            ArrayList<RecipeCardResponse> bookmarks = new ArrayList<>();
            for (Recipe rp : user.getMyrecipes()) {
                myRecipes.add(recipeService.convertToResponseCard(rp));
            }
            for (Recipe rp : user.getBookmarks()) {
                bookmarks.add(recipeService.convertToResponseCard(rp));
            }
            
            //JIka myrecipe kosong maka myrecipeNull is true
            if (myRecipes.isEmpty()) {
                model.addAttribute("myRecipeNull", true);
            }else {
                model.addAttribute("myRecipeNull", false);
            }

            //jika bookmarks kosong maka bookmarksNull is true
            if (bookmarks.isEmpty()) {
                model.addAttribute("bookmarksNull", true);
            }else {
                model.addAttribute("bookmarksNull", false);
            }

            //Jika profile dan session memiliki id sama maka
            // user sedang melihat profile sendiri
            if (user.getImdbId().matches(cookie)) {
                model.addAttribute("isUser", true);
            }else {
                model.addAttribute("isUser", false);
            }

            //add semua data ke attribute
            model.addAttribute("myrecipes", myRecipes);
            model.addAttribute("bookmarks", bookmarks);
            model.addAttribute("cookie", cookie);
            model.addAttribute("user", userResponse);
            return "profile";
            // return new ResponseEntity<Model>(model, HttpStatus.OK);
        }catch (Exception e) {
            return "redirect:/";
            // return new ResponseEntity<Model>(model, HttpStatus.OK);
        }
    }
    
    //Versi API open account 
    @GetMapping("/api/account/{imdbId}")
    public ResponseEntity<?> getMethodName(@PathVariable String imdbId) {
        try {
            User user = service.findUserByImdbId(imdbId);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
        }
    }
    
}
