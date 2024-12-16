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
    ImageService service;

    @GetMapping("/add")
    public String addForm() {
        return "createRecipe";
    }
    

    @PostMapping("/add")
    public String addImage(String title, MultipartFile image) throws IOException{
        final ObjectId id = service.addImage(title, image);
        return "image/" + id.toString();
    }

    @GetMapping(value="/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        final Image image = service.getImage(new ObjectId(id));
        // byte[] file = Base64.getDecoder().decode(image.getImage().toString());
        return new ResponseEntity<byte[]>(image.getImage(), HttpStatus.OK);
    }
    
    @GetMapping(value="/svg/{id}")
    public ResponseEntity<byte[]> getSVG(@PathVariable String id) {
        final Image image = service.getImage(new ObjectId(id));
        final String header = "<svg x=\"0\" y=\"0 ... ...</svg>";

        return ResponseEntity.ok().header(header).body(image.getImage());
    }
    
    @GetMapping(value="/svg/icon")
    public ResponseEntity<byte[]> getIcon() {
        final Image image = service.getImage(new ObjectId("6745fa0443513141adebb498"));
        final String header = "<svg x=\"0\" y=\"0 ... ...</svg>";

        return ResponseEntity.ok().header(header).body(image.getImage());
    }
}
