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

    //Instantiate recipe repository
    @Autowired
    private RecipeRepository repository;

    //Instantiate userService
    @Autowired
    private UserService userService;

    //Instantiate commnetService
    @Autowired
    private CommentService commentService;

    //Instantiate ImageService
    @Autowired
    private ImageService imageService;

    //Instantiate mongoTemplate
    @Autowired
    private MongoTemplate mongoTemplate;

    //Function untuk convert recipe menjadi recipe response
    public RecipeResponse convertToResponse(Recipe re) {
        //Convert User menjadi UserInfoResponse
        UserInfoResponse author = userService.convertToResponse(re.getAuthor());
        //Get array rating
        Map<String, Integer> ratings = re.getRating();
        double rating = 0;

        //Convert comments pada recipe menjadi CommentResponse
        ArrayList<CommentResponse> comments = new ArrayList<>();
        for (Comment cm : re.getComments()) {
            comments.add(commentService.convertToResponse(cm));
        }

        //Convert rating menjadi double dengna mencari rata-rata rating
        if (ratings.size() > 0) {
            for (Integer val : ratings.values()) {
                rating += val;
            }
            rating = rating/ratings.size();
        }
        

        //Return RescipeResponse dengan membuat new RecipeResponse
        return new RecipeResponse(re.getImdbId(), author, re.getTittle(), re.getDescription(),
                        re.getCookTime(), re.getTags(), re.getPrepCategory(), re.getServings(), rating, re.getMainImage(),
                        re.getIngredients(), re.getBody(), re.getImages(), comments);
    }

    //Function untuk melakukan conversi Recipe menjadi RecipeCardResponse
    public RecipeCardResponse convertToResponseCard(Recipe re) {
        //Convert User menjadi UserInfoResponse
        UserInfoResponse author = userService.convertToResponse(re.getAuthor());
        //Get map rating
        Map<String, Integer> ratings = re.getRating();

        //Convert map rating menjadi double dengan mencari rata-rata
        double rating = 0;
        if (ratings.size() > 0) {
            for (Integer val : ratings.values()) {
                rating += val;
            }
            rating = rating/ratings.size();
        }

        //return new recipecard dengan membuat RecipeCardResponse baru
        return new RecipeCardResponse(re.getImdbId(), author, re.getTittle(), re.getDescription()
                , re.getCookTime(), re.getPrepCategory(), re.getServings(), rating, re.getMainImage(), re.getTags());
    }

    //Function untuk mengambil semua recipe dari repository
    public List<Recipe> getAllRecipe() {
        System.out.println(repository.findAll());
        return repository.findAll();
    }

    //Funtion untuk melakukan searching berdasarkan imdbId
    public Recipe findRecipeByImdbId(String imdbId) throws Exception {
        Optional<Recipe> recipe =  repository.findRecipeByImdbId(imdbId);

        if (!recipe.isPresent()) {
            throw new Exception("findRecipeByImdbId.Recipe is not found");
        }

        return recipe.get();
    }

    //Function untuk melakukan searching berdasarkan tags yang ada di recipe
    public List<Recipe> findByTagsIn(ArrayList<String> tags) throws Exception {
        List<Recipe> recipes = repository.findByTagsIn(tags);

        if (recipes.isEmpty()) {
            throw new Exception("findByIngredientsIn.Recipe is not found");
        }

        return recipes;
    }

    //Function untuk melakukan searching berdasarkan text
    public List<Recipe> findByText(String texts) throws Exception {
        List<Recipe> recipes = repository.findByIngredientsRegexOrTittleRegex(texts.toLowerCase(), texts);

        if (recipes.isEmpty()) {
            throw new Exception("findByIngredientsIn.Recipe is not found");
        }

        return recipes;
    }

    //Function untuk melakukan addBookmark
    public void addToBookmark(String imdbId, String cookie) throws Exception {
        try {
            //Mencari recipe berdasarkan imdbId
            Recipe rp = findRecipeByImdbId(imdbId);
            //Mencari user berdasarkan session
            User usr = userService.findUserByImdbId(cookie);
            List<Recipe> bookmarks = usr.getBookmarks();

            //Jika recipe sudah di bookmark maka akan throw exception
            for(Recipe recipe : bookmarks) {
                if (recipe.getImdbId().matches(rp.getImdbId())) {
                    throw new Exception("addToBookmark.Recipe already bookmarks");
                }
            }

            //Update data user dengan push id recipe ke bookamrks
            mongoTemplate.update(User.class)
            .matching(Criteria.where("imdbId").is(cookie))
            .apply(new Update().push("bookmarks").value(rp.getId()))
            .first();

        }catch (Exception e) {
            throw new Exception("addToBookmark." + e.getMessage());
        }
    }

    //Function untuk melakukan remove bookmark
    public void removeBookmark(String imdbId, String cookie) throws Exception {
        try {
            //Find recipe berdasarkan imdbId
            Recipe rp = findRecipeByImdbId(imdbId);

            //Update data user dengan pull id recipe dari bookmarks
            mongoTemplate.update(User.class)
                .matching(Criteria.where("imdbId").is(cookie))
                .apply(new Update().pull("bookmarks", rp.getId())).first();
        }catch (Exception e) {
            throw new Exception("removeBookmark.Failed to remove bookmark!!");
        }
    }

    //Function untuk melakukan add rating
    public void addRating(String imdbId, String cookie, int rating) throws Exception {
        try {
            //Find recipe berdasarkan imdbId
            Recipe rp = findRecipeByImdbId(imdbId);
            //Get rating dari recipe
            Map<String, Integer> rt = rp.getRating();
            //Menabahkan rating baru dengan key username ke map rating
            rt.put(cookie, rating);

            //Update recipe dengan mengubah rating dengan map baru
            mongoTemplate.update(Recipe.class)
                .matching(Criteria.where("imdbId").is(imdbId))
                .apply(new Update().set("rating", rt)).first();
        }catch (Exception e) {
            throw new Exception("addRating." + e.getMessage());
        }
    }


    //Fucntion untuk membuat resep baru
    public String createRecipe(CreateRecipeRequest request, String cookie) throws Exception {
        try {
            //Find author berdasarkan imdbId
            User author = userService.findUserByImdbId(cookie);
            ObjectId obId = new ObjectId();
            String mainImage = new String();
            ArrayList<String> ingredients = new ArrayList<>();
            ArrayList<String> body = new ArrayList<>();
            ArrayList<String> images = new ArrayList<>();

            //Add ingredients dari request ke ingredients
            ArrayList<String> requestIng = request.getIngredients();
            for (int i = 0; i < requestIng.size(); i++) {
                ingredients.add(requestIng.get(i));
            }

            //Add steps dan image ke body dan images
            ArrayList<String> stepinput = request.getStepInput();
            ArrayList<MultipartFile> stepFile = request.getStepFile();
            for (int i = 0; i < stepinput.size(); i++) {
                body.add(stepinput.get(i));

                //Jika tidak ada image maka array diisi kosong
                if (stepFile.get(i).isEmpty()) {
                    images.add("");
                }else {
                    //Jika terdapat image maka image akan disimpan di database
                    ObjectId imageId = imageService.addImage(obId.toString() + "_img" + i, stepFile.get(i));
                    //Image akan diisi dengan link reference dari image
                    images.add("/image/" + imageId.toString());
                }
            }

            //Add image ke database dan simpan reference ke mainImage
            MultipartFile requestMainImage = request.getMainImage();
            if (requestMainImage.isEmpty()) {
                mainImage = "";
            }else {
                ObjectId imageId = imageService.addImage(obId.toString() + "_main", requestMainImage);
                mainImage = "/image/" + imageId.toString();
            }

            //Create new recipe dengan data yang ada
            Recipe rcp = new Recipe(obId, author, request.getTittle(), request.getDescription(), request.getCookTime(), request.getTags(),
                                    request.getPrepCategory() + " Prep", request.getServings(), mainImage, ingredients,
                                    body, images);

            //Insert recipe baru ke database
            repository.insert(rcp);

            //Update data user dimana id recipe disimpan di myrecipes
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
