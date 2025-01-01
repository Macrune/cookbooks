package com.impal.CookBook.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/recipes/{imdbId}/createComment")
public class CommentController {
    
    @Autowired
    private CommentService service;

    @PostMapping()
    public String createComment(String payload, @PathVariable String imdbId,
                                            @CookieValue(value = "userCookie", defaultValue = "Guest") String cookie) {
        try {
            if (cookie.equals("Guest")) {
                return "redirect:/recipes/" + imdbId;
            }else {
                service.createComment(payload, cookie, imdbId);
                return "redirect:/recipes/" + imdbId;
            }
        } catch (Exception e) {
            return "redirect:/";
        }
        
    }

    
    
}
