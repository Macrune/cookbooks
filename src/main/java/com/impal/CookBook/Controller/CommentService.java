package com.impal.CookBook.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.impal.CookBook.Model.Comment;
import com.impal.CookBook.Model.CommentRepository;
import com.impal.CookBook.Model.Recipes;
import com.impal.CookBook.Model.User;

@Service
public class CommentService {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserService userService;

    public Comment createComment(String commentBody, String authorId, String recipeId) {
        Optional<User> checkAuthor = userService.findUserByImdbId(authorId);
        User author;

        if (checkAuthor.isPresent()) {
            author = checkAuthor.get();
        }else {
            author = new User();
        }

        Comment comment = new Comment(author, commentBody);
        repository.insert(comment);

        mongoTemplate.update(Recipes.class)
            .matching(Criteria.where("imdbId").is(recipeId))
            .apply(new Update().push("comments").value(comment.getId()))
            .first();
        
        return comment;
    }
}
