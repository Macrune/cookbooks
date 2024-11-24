package com.impal.CookBook.Model;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, ObjectId>{
    Optional<Recipe> findRecipeByImdbId(String imdbId);
}
