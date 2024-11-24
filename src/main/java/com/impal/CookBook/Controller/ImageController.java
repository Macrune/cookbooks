package com.impal.CookBook.Controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.impal.CookBook.Model.Image;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/image")
public class ImageController {
    @Autowired
    ImageService service;

    @PostMapping("/add")
    public ResponseEntity<String> addImage(@RequestParam("title") String title, 
    @RequestParam("image") MultipartFile image) throws IOException{
        ObjectId id = service.addImage(title, image);
        return new ResponseEntity<String>("image/" + id.toString(), HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        Image image = service.getImage(new ObjectId(id));
        // byte[] file = Base64.getDecoder().decode(image.getImage().toString());
        return new ResponseEntity<byte[]>(image.getImage(), HttpStatus.OK);
    }
    
    
}
