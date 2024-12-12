package com.impal.CookBook.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.impal.CookBook.Model.User;
import com.impal.CookBook.Payload.UserInfoResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/api/account")
public class UserController {
    @Autowired
    private UserService service;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> payload) {
        try {
            User user = service.authenticateUser(payload.get("username"), payload.get("password"));
            UserInfoResponse userResponse = new UserInfoResponse(user.getImdbId(), user.getUsername(), user.getProfilePic());

            return new ResponseEntity<UserInfoResponse>(userResponse, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> payload) {
        try {
            service.registerUser(payload.get("username"), payload.get("email"), payload.get("password"));
            return new ResponseEntity<String>("register.User registered successfully!", HttpStatus.OK);
        }catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/{imdbId}")
    public ResponseEntity<?> findUser(@PathVariable String imdbId) {
        try {
            UserInfoResponse userResponse = service.convertToResponse(service.findUserByImdbId(imdbId));
            return new ResponseEntity<UserInfoResponse>(userResponse, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    
}
