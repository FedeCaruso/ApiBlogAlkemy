package com.alkemy.apiBlog.controllers;

import com.alkemy.apiBlog.exceptions.WebException;
import com.alkemy.apiBlog.models.Post;
import com.alkemy.apiBlog.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Level;

/**
 *
 * @author Fede
 */
@Controller
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private PostService postService;


    @GetMapping("/post")
    public ResponseEntity<byte[]> imagenPost(@RequestParam(required = true) String id) {

        try {
            Post post = postService.searchById(id);

            if (post.getImage() == null) {
                throw new WebException("El post no tiene una imagen asignada.");
            }

            byte[] image = post.getImage().getContent();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(image, headers, HttpStatus.OK);

        } catch (WebException ex) {
            java.util.logging.Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
