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
import org.springframework.web.multipart.MultipartFile;

import com.impal.CookBook.Model.Image;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@Controller
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private ImageService service;

    @PostMapping("/add")
    public ResponseEntity<String> addImage(String title, MultipartFile image) throws IOException{
        final ObjectId id = service.addImage(title, image);

        return new ResponseEntity<String>("/image/" + id.toString(), HttpStatus.OK);
    }

    @GetMapping(value="/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        final Image image = service.getImage(new ObjectId(id));
        // byte[] file = Base64.getDecoder().decode(image.getImage().toString());
        return new ResponseEntity<byte[]>(image.getImage(), HttpStatus.OK);
    }
    
    @GetMapping(value="/logo", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getLogo() {
        final Image image = service.getImage(new ObjectId("6772c69326802f23feaaaa4a"));

        return new ResponseEntity<byte[]>(image.getImage(), HttpStatus.OK);
    }

    @GetMapping(value="/bowlIcon", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getBowlIcon() {
        final Image image = service.getImage(new ObjectId("6772c7de26802f23feaaaa4c"));

        return new ResponseEntity<byte[]>(image.getImage(), HttpStatus.OK);
    }

    @GetMapping(value="/starIcon", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getStarIcon() {
        final Image image = service.getImage(new ObjectId("6772c96f26802f23feaaaa50"));

        return new ResponseEntity<byte[]>(image.getImage(), HttpStatus.OK);
    }

    @GetMapping("/{id}/delete")
    public ResponseEntity<String> deleteImage(@PathVariable String id) {
        return new ResponseEntity<String>(service.deleteImage(new ObjectId(id)), HttpStatus.OK);
    }
    
}
