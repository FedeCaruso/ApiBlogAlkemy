package com.alkemy.apiBlog.services;

import com.alkemy.apiBlog.exceptions.WebException;
import com.alkemy.apiBlog.models.Image;
import com.alkemy.apiBlog.models.Post;
import com.alkemy.apiBlog.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ImageService imageService;

    @Transactional
    public Post save(Post post, MultipartFile file) throws WebException {
        validatePost(post);

        Image image = imageService.save(file);
        post.setImage(image);
        return postRepository.save(post);
    }

    private void validatePost(Post post) throws WebException {

        if (post.getTitle().isEmpty()) {
            throw new WebException("Your post should have a title.");
        } else {
            post.setTitle(post.getTitle());
        }
        if (post.getCategory() == null) {
            throw new WebException("Your post should have a category.");
        } else {
            post.setCategory(post.getCategory());
        }
        if (post.getContent().isEmpty()) {
            throw new WebException("You're posting anything.");
        } else {
            post.setContent(post.getContent());
        }
        post.setCreationDate(new Date());
    }

    public List<Post> findAll() throws WebException {
        return postRepository.findAll();
    }

    public Optional<Post> findById(String id) throws WebException {
        return postRepository.findById(id);
    }

    public Post searchById(String id) throws WebException {
        return postRepository.searchById(id);
    }

    public List<Post> listAllByQ(String q) throws WebException {
        return postRepository.findAllByQ("%" + q + "%");
    }

    @Transactional
    public void deleteById(String id) throws WebException {
        Optional<Post> optional = postRepository.findById(id);
        if (optional.isPresent()) {
            imageService.delete(optional.get().getImage());
            postRepository.delete(optional.get());
        }
    }

    @Transactional
    public void modifyPost(MultipartFile file, Post foundPost, Post post) throws WebException {

        post.setId(foundPost.getId());
        post.setTitle(post.getTitle());
        post.setContent(post.getContent());


        String idImage = null;
        if (post.getImage() != null) {
            idImage = post.getImage().getId();
        }
        Image image = imageService.update(idImage, file);
        post.setImage(image);

        postRepository.save(post);

    }
}
