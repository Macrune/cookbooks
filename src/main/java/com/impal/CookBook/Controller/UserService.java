package com.impal.CookBook.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.impal.CookBook.Model.User;
import com.impal.CookBook.Model.UserRepository;
import com.impal.CookBook.Payload.UserInfoResponse;



@Service
public class UserService {
    

    @Autowired
    private UserRepository repository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

    public User findUserByImdbId(String imdbId) throws Exception {
        Optional<User> user = repository.findUserByImdbId(imdbId);

        if (!user.isPresent()) {
            throw new Exception("findUserByImdbId.User doesn't exist");
        }
        return user.get();
    }

    public UserInfoResponse convertToResponse(User user) {
        return new UserInfoResponse(user.getImdbId(), user.getUsername(), user.getProfilePic());
    }

    public User authenticateUser(String username, String password) throws Exception {
        Optional<User> user = repository.findUserByUsername(username);

        if (!user.isPresent()) {
            throw new Exception("authenticateUser.User doesn't exist");
        }

        if (passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }else {
            throw new Exception("authenticateUser.Wrong password");
        }
    }

    public void registerUser(String username, String email, String password) throws Exception {
        if (repository.existsByEmail(email)) {
            throw new Exception("registerUser.Email already in used");
        }

        if (repository.existsByUsername(username)) {
            throw new Exception("registerUser.Username already used");
        }

        String imdbId = username.replaceAll(" ", "").toLowerCase();
        User newUser = new User(imdbId, username, email, passwordEncoder.encode(password));
        repository.insert(newUser);
    }
}
