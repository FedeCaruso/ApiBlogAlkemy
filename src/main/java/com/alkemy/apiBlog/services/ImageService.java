package com.alkemy.apiBlog.services;

import com.alkemy.apiBlog.exceptions.WebException;
import com.alkemy.apiBlog.models.Image;
import com.alkemy.apiBlog.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public Image save(MultipartFile file) throws WebException {
        if (file != null) {
            try {
                Image image = new Image();
                image.setMime(file.getContentType());
                image.setName(file.getName());
                image.setContent(file.getBytes());
                return imageRepository.save(image);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public Image update(String idImg, MultipartFile file) {
        if (file != null) {
            try {
                Image image = new Image();

                if(idImg != null) {
                    Optional<Image> respuesta = imageRepository.findById(idImg);
                    if(respuesta.isPresent()) {
                        image = respuesta.get();
                    }
                }
                image.setMime(file.getContentType());
                image.setName(file.getName());
                image.setContent(file.getBytes());
                return imageRepository.save(image);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public void delete(Image image) {
        imageRepository.delete(image);
    }
}
