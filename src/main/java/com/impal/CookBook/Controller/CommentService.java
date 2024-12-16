package com.impal.CookBook.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.impal.CookBook.Model.Comment;
import com.impal.CookBook.Model.CommentRepository;
import com.impal.CookBook.Model.Recipe;
import com.impal.CookBook.Model.User;
import com.impal.CookBook.Payload.CommentResponse;

@Service
public class CommentService {
    @Autowired
    private CommentRepository repository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserService userServiceComment;

    public CommentResponse convertToResponse(Comment comment) {
        return new CommentResponse(userServiceComment.convertToResponse(comment.getAuthor()), comment.getBody());
    }

    public Comment createComment(String commentBody, String authorId, String recipeId) throws Exception {
        try {
            User author = userServiceComment.findUserByImdbId(authorId);
            Comment comment = new Comment(author, commentBody);
            repository.insert(comment);

            mongoTemplate.update(Recipe.class)
                .matching(Criteria.where("imdbId").is(recipeId))
                .apply(new Update().push("comments").value(comment.getId()))
                .first();
            
            return comment;
        }catch (Exception e) {
            throw new Exception("createComment.Author not found");
        }

        
    }
}
