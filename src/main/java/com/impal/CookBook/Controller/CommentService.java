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
    //Instansiasi CommentRepository
    @Autowired
    private CommentRepository repository;

    //Instansiasi MongoTemplate
    @Autowired
    private MongoTemplate mongoTemplate;

    //Instansiasi UserService
    @Autowired
    private UserService userServiceComment;

    //Function untuk melakukan konversi dari Comment menjadi CommentResponse
    public CommentResponse convertToResponse(Comment comment) {
        return new CommentResponse(userServiceComment.convertToResponse(comment.getAuthor()), comment.getBody());
    }

    //Function membuat Comment
    public Comment createComment(String commentBody, String authorId, String recipeId) throws Exception {
        try {
            //Cari author berdasarkan authorId
            User author = userServiceComment.findUserByImdbId(authorId);
            //Create comment baru dengan author dan isi comment
            Comment comment = new Comment(author, commentBody);
            //Insert comment baru ke database comment
            repository.insert(comment);

            //Update recipe berdasarkan recipeId dimana comment ditambahkan ke array comments
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
