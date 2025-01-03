package com.impal.CookBook.Controller;

import java.io.IOException;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.impal.CookBook.Model.Image;
import com.impal.CookBook.Model.ImageRepository;

@Service
public class ImageService {
    //Instansiasi Repository/database image
    @Autowired
    private ImageRepository repository;

    //Function untuk melakukan add image
    public ObjectId addImage(String tittle, MultipartFile file) throws IOException {
        //Create new image dengna tittle
        Image image = new Image(tittle);
        //Set image berdasarkan multiPartFile yang dikonversi ke bytes
        image.setImage(file.getBytes());
        //upload image ke repository
        image = repository.insert(image);
        //Return id dari image
        return image.getId();
    }

    //Function untuk mengambik image dari database
    public Image getImage(ObjectId id) {
        return repository.findById(id).get();
    }

    //Function untuk melakukan delete image
    public String deleteImage(ObjectId id){
        //Find image berdasarkan id dan melakukan delete
        Optional<Image> img = repository.deleteImageById(id);
        
        if (img.isPresent()) {
            return "Delete image successful";
        }else {
            return "Image not found";
        }
    }
}
