package com.impal.CookBook.Controller;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.impal.CookBook.Model.Image;
import com.impal.CookBook.Model.ImageRepository;

@Service
public class ImageService {
    @Autowired
    private ImageRepository repository;

    public ObjectId addImage(String tittle, MultipartFile file) throws IOException {
        Image image = new Image(tittle);
        image.setImage(file.getBytes());
        image = repository.insert(image);
        return image.getId();
    }

    public Image getImage(ObjectId id) {
        return repository.findById(id).get();
    }
}
