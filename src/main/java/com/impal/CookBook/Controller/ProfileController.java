package com.impal.CookBook.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.impal.CookBook.Model.User;
import com.impal.CookBook.Controller.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getProfilePage(@CookieValue(value = "userCookie", defaultValue = "Guest") String cookie, Model model) {
        // if ("Guest".equals(cookie)) {
        //     return "redirect:/login"; // Arahkan ke halaman login jika user belum login
        // }

        // try {
        //     User user = userService.findUserByImdbId(cookie); // Ambil data user berdasarkan cookie
        //     model.addAttribute("user", user);
        //     model.addAttribute("myRecipes", user.getMyrecipes()); // Resep yang dibuat oleh user
        //     model.addAttribute("savedRecipes", user.getBookmarks()); // Resep yang disimpan oleh user
        // } catch (Exception e) {
        //     model.addAttribute("error", "Failed to load user profile: " + e.getMessage());
        //     return "error";
        // }

        return "profile"; // Mengacu pada template `profile.html` di resources/templates/
    }
}
