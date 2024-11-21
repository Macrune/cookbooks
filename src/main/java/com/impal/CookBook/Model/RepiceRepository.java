package com.impal.CookBook.Model;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepiceRepository extends MongoRepository<Recipes, ObjectId>{
    Optional<Recipes> findRecipeByImdbId(String imdbId);
}
