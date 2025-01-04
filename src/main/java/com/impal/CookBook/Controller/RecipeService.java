package com.impal.CookBook.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


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
    private ImageService imageService;

    @Autowired
    private MongoTemplate mongoTemplate;

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
                , re.getCookTime(), re.getPrepCategory(), re.getServings(), rating, re.getMainImage(), re.getTags());
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
            User usr = userService.findUserByImdbId(cookie);
            List<Recipe> bookmarks = usr.getBookmarks();

            for(Recipe recipe : bookmarks) {
                if (recipe.getImdbId().matches(rp.getImdbId())) {
                    throw new Exception("addToBookmark.Recipe already bookmarks");
                }
            }

            mongoTemplate.update(User.class)
            .matching(Criteria.where("imdbId").is(cookie))
            .apply(new Update().push("bookmarks").value(rp.getId()))
            .first();

        }catch (Exception e) {
            throw new Exception("addToBookmark." + e.getMessage());
        }
    }

    public void removeBookmark(String imdbId, String cookie) throws Exception {
        try {
            Recipe rp = findRecipeByImdbId(imdbId);

            mongoTemplate.update(User.class)
                .matching(Criteria.where("imdbId").is(cookie))
                .apply(new Update().pull("bookmarks", rp.getId())).first();
        }catch (Exception e) {
            throw new Exception("removeBookmark.Failed to remove bookmark!!");
        }
    }

    public void addRating(String imdbId, String cookie, int rating) throws Exception {
        try {
            Recipe rp = findRecipeByImdbId(imdbId);
            Map<String, Integer> rt = rp.getRating();
            rt.put(cookie, rating);
            mongoTemplate.update(Recipe.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().set("rating", rt)).first();
        }catch (Exception e) {
            throw new Exception("addRating." + e.getMessage());
        }
    }

    public String createRecipe(CreateRecipeRequest request, String cookie) throws Exception {
        try {
            User author = userService.findUserByImdbId(cookie);
            ObjectId obId = new ObjectId();
            String mainImage = new String();
            ArrayList<String> ingredients = new ArrayList<>();
            ArrayList<String> body = new ArrayList<>();
            ArrayList<String> images = new ArrayList<>();

            ArrayList<String> requestIng = request.getIngredients();
            for (int i = 0; i < requestIng.size(); i++) {
                ingredients.add(requestIng.get(i));
            }

            ArrayList<String> stepinput = request.getStepInput();
            ArrayList<MultipartFile> stepFile = request.getStepFile();
            for (int i = 0; i < stepinput.size(); i++) {
                body.add(stepinput.get(i));

                if (stepFile.get(i).isEmpty()) {
                    images.add("");
                }else {
                    ObjectId imageId = imageService.addImage(obId.toString() + "_img" + i, stepFile.get(i));
                    images.add("/image/" + imageId.toString());
                }
            }

            MultipartFile requestMainImage = request.getMainImage();
            if (requestMainImage.isEmpty()) {
                mainImage = "";
            }else {
                ObjectId imageId = imageService.addImage(obId.toString() + "_main", requestMainImage);
                mainImage = "/image/" + imageId.toString();
            }

            Recipe rcp = new Recipe(obId, author, request.getTittle(), request.getDescription(), request.getCookTime(), request.getTags(),
                                    request.getPrepCategory() + " Prep", request.getServings(), mainImage, ingredients,
                                    body, images);

            repository.insert(rcp);

            mongoTemplate.update(User.class)
                .matching(Criteria.where("imdbId").is(author.getImdbId()))
                .apply(new Update().push("myrecipes").value(rcp.getId()))
                .first();
            
            return obId.toString();
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
