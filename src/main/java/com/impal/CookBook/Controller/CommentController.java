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
            //Cek jika user merupakan Guest atau tidak
            if (cookie.equals("Guest")) {
                //Jika guest maka create comment tidak dilakukan
                return "redirect:/recipes/" + imdbId;
            }else {
                //Jika user telah login maka dilakukan create comment
                service.createComment(payload, cookie, imdbId);
                return "redirect:/recipes/" + imdbId;
            }
        } catch (Exception e) {
            //Kembali ke homepage jika terjadi exception
            return "redirect:/";
        }
        
    }

    
    
}
