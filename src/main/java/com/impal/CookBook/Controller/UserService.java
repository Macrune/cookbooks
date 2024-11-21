package com.impal.CookBook.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.impal.CookBook.Model.User;
import com.impal.CookBook.Model.UserRepository;



@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public Optional<User> findUserByImdbId(String imdbId) {
        return repository.findUserByImdbId(imdbId);
    }

    public User authenticateUser(String username, String password) throws Exception {
        Optional<User> user = repository.findUserByUsername(username);

        if (!user.isPresent()) {
            throw new Exception("User doesn't exist");
        }

        if (user.get().getPassword().matches(password)) {
            return user.get();
        }else {
            throw new Exception("Wrong password");
        }
    }

    public void registerUser(String username, String email, String password) throws Exception {
        if (repository.existByEmail(email)) {
            throw new Exception("Email already in used");
        }

        if (repository.existByUsername(username)) {
            throw new Exception("Username already used");
        }

        String imdbId = username.replaceAll(" ", "").toLowerCase();
        User newUser = new User(imdbId, username, email, password);
        repository.insert(newUser);
    }
}
