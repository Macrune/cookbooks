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
    
    //Instantiate userRepository
    @Autowired
    private UserRepository repository;

    //Passowrd encoder untuk melakukan enkripsi passowrd
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

    //Function utuk melakukan find user berdasarkan id
    public User findUserByImdbId(String imdbId) throws Exception {
        Optional<User> user = repository.findUserByImdbId(imdbId);

        if (!user.isPresent()) {
            throw new Exception("findUserByImdbId.User doesn't exist");
        }
        return user.get();
    }

    //Function untuk melakukan konversi User menjadi UserInfoResponse
    public UserInfoResponse convertToResponse(User user) {
        return new UserInfoResponse(user.getImdbId(), user.getUsername(), user.getProfilePic());
    }

    //Functuin untuk melakukan autentikasi user berdasarkan username dan password
    public User authenticateUser(String username, String password) throws Exception {
        //Melkukan find user berdasarkan username
        Optional<User> user = repository.findUserByUsername(username);

        //Jika user tidak ada maka akan throw exception
        if (!user.isPresent()) {
            throw new Exception("authenticateUser.User doesn't exist");
        }

        //Cek apakah password benar
        if (passwordEncoder.matches(password, user.get().getPassword())) {
            return user.get();
        }else {
            throw new Exception("authenticateUser.Wrong password");
        }
    }

    //Function untuk melakukan regiter user baru
    public void registerUser(String username, String email, String password) throws Exception {
        //Cek apkah email sudah pernah digunakan
        if (repository.existsByEmail(email)) {
            throw new Exception("registerUser.Email already in used");
        }

        //Cek apakah username penrah digunakan
        if (repository.existsByUsername(username)) {
            throw new Exception("registerUser.Username already used");
        }

        //Create imdbid dari username
        String imdbId = username.replaceAll(" ", "").toLowerCase();

        //Crate new user dan menambahkan ke database
        User newUser = new User(imdbId, username, email, passwordEncoder.encode(password));
        repository.insert(newUser);
    }
}
