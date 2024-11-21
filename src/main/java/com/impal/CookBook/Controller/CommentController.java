package com.impal.CookBook.Controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import com.impal.CookBook.Model.Comment;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/recipes/{imdbId}/createComment")
public class CommentController {
    
    @Autowired
    private CommentService service;

    @PostMapping()
    public ResponseEntity<Comment> postMethodName(@RequestBody Map<String, String> payload, @PathVariable String imdbId) {
        return new ResponseEntity<Comment>(service.createComment(payload.get("commentBody"), payload.get("authorId"), imdbId), HttpStatus.CREATED);
    }

    
    
}
