package com.impal.CookBook.Model;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    Optional<User> findUserByImdbId(String imdbId);
    Optional<User> findUserByUsername(String username);
    Boolean existByUsername(String username);
    Boolean existByEmail(String email);
}
