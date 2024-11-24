package com.impal.CookBook.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impal.CookBook.Model.*;
import com.impal.CookBook.Payload.*;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    public RecipeResponse convertToResponse(Recipe re) {
        UserInfoResponse author = userService.convertToResponse(re.getAuthor());
        ArrayList<CommentResponse> comments = new ArrayList<>();
        for (Comment cm : re.getComments()) {
            comments.add(commentService.convertToResponse(cm));
        }
        return new RecipeResponse(re.getImdbId(), author, re.getTittle(), re.getDescription(),
                        re.getCookTime(), re.getTags(), re.getPrepCategory(), re.getServings(), re.getRating(), re.getMainImage(),
                        re.getIngredients(), re.getBody(), re.getImages(), comments);
    }

    public List<Recipe> getAllRecipe() {
        System.out.println(repository.findAll());
        return repository.findAll();
    }

    public Recipe findRecipeByImdbId(String imdbId) throws Exception {
        Optional<Recipe> recipe =  repository.findRecipeByImdbId(imdbId);

        if (!recipe.isPresent()) {
            throw new Exception("findRecipeByImdbId.Recipe is not found");
        }

        return recipe.get();
    }
}
