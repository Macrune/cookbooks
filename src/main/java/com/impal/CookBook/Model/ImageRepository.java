package com.impal.CookBook.Model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface ImageRepository extends MongoRepository<Image, ObjectId> {
    Optional<Image> findImageById(ObjectId id);
}
