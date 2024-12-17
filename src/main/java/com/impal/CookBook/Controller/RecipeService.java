package com.impal.CookBook.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition.TextIndexDefinitionBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
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

    @Autowired
    private MongoTemplate mongoTemplate;

    Logger logger = LoggerFactory.getLogger(RecipeService.class);

    TextIndexDefinition textIndex = new TextIndexDefinitionBuilder()
    .onField("title")
    .onField("ingredients")
    .onField("tags")
    .build();

    public RecipeResponse convertToResponse(Recipe re) {
        UserInfoResponse author = userService.convertToResponse(re.getAuthor());
        Map<String, Integer> ratings = re.getRating();
        double rating = 0;
        ArrayList<CommentResponse> comments = new ArrayList<>();
        for (Comment cm : re.getComments()) {
            comments.add(commentService.convertToResponse(cm));
        }
        if (ratings.size() > 0) {
            for (Integer val : ratings.values()) {
                rating += val;
            }
            rating = rating/ratings.size();
        }
        

        return new RecipeResponse(re.getImdbId(), author, re.getTittle(), re.getDescription(),
                        re.getCookTime(), re.getTags(), re.getPrepCategory(), re.getServings(), rating, re.getMainImage(),
                        re.getIngredients(), re.getBody(), re.getImages(), comments);
    }

    public RecipeCardResponse convertToResponseCard(Recipe re) {
        UserInfoResponse author = userService.convertToResponse(re.getAuthor());
        Map<String, Integer> ratings = re.getRating();
        double rating = 0;
        if (ratings.size() > 0) {
            for (Integer val : ratings.values()) {
                rating += val;
            }
            rating = rating/ratings.size();
        }
        return new RecipeCardResponse(re.getImdbId(), author, re.getTittle(), re.getDescription()
                , re.getCookTime(), re.getPrepCategory(), re.getServings(), rating, re.getMainImage());
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

    public List<Recipe> findByTagsIn(ArrayList<String> tags) throws Exception {
        List<Recipe> recipes = repository.findByTagsIn(tags);

        if (recipes.isEmpty()) {
            throw new Exception("findByIngredientsIn.Recipe is not found");
        }

        return recipes;
    }

    public List<Recipe> findByText(String texts) throws Exception {
        List<Recipe> recipes = repository.findByIngredientsRegexOrTittleRegex(texts.toLowerCase(), texts);

        if (recipes.isEmpty()) {
            throw new Exception("findByIngredientsIn.Recipe is not found");
        }

        return recipes;
    }

    public void addToBookmark(String imdbId, String cookie) throws Exception {
        try {
            Recipe rp = findRecipeByImdbId(imdbId);

            mongoTemplate.update(User.class)
                .matching(Criteria.where("imdbId").is(cookie))
                .apply(new Update().push("bookmarks").value(rp.getId()))
                .first();
        }catch (Exception e) {
            throw new Exception("addToBookmark.Recipe not found!!");
        }
    }

    public void addRating(String imdbId, String cookie, int rating) throws Exception {
        try {
            Recipe rp = findRecipeByImdbId(imdbId);
            Map<String, Integer> rt = new HashMap<>();
            rt.put(cookie, rating);
            mongoTemplate.update(Recipe.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().push("rating").value(rt))
                .first();
        }catch (Exception e) {
            throw new Exception("addRating.Recipe not found!!");
        }
    }
}
