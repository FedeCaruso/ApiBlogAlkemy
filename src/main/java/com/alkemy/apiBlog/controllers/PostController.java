/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alkemy.apiBlog.controllers;


import com.alkemy.apiBlog.enums.Category;
import com.alkemy.apiBlog.exceptions.WebException;
import com.alkemy.apiBlog.models.Post;
import com.alkemy.apiBlog.services.PostService;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Fede
 */
@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public String listarPosts(Model model, @RequestParam(required = false) String q) {
        if (q != null) {
            try {
                model.addAttribute("posts", postService.listAllByQ(q));
            } catch (WebException ex) {
                Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                model.addAttribute("posts", postService.findAll());
            } catch (WebException ex) {
                Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "post-list";
    }

    @GetMapping("/form")
    public String crearPost(Model model, @RequestParam(required = false) String id, @RequestParam(required = false) String action) {
        if (id != null) {
            try {
                Optional<Post> optional = postService.findById(id);
                if (optional.isPresent()) {
                    model.addAttribute("post", optional.get());
                    model.addAttribute("action", action);
                } else {
                    return "post-list";
                }
            } catch (WebException ex) {
                Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            model.addAttribute("post", new Post());
            model.addAttribute("action", action);
        }
        model.addAttribute("categories", Arrays.asList(Category.values()));
        return "post-form";
    }

    @GetMapping("/details")
    public String DetallesPost(Model model, @RequestParam(required = false) String id, @RequestParam(required = false) String action) {
        if (id != null) {
            try {
                Optional<Post> optional = postService.findById(id);
                if (optional.isPresent()) {
                    model.addAttribute("post", optional.get());

                } else {
                    return "redirect:/";
                }
            } catch (WebException ex) {
                Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            model.addAttribute("post", new Post());

        }
        return "post-details";
    }

    @GetMapping("/delete")
    public String eliminarPost(@RequestParam(required = true) String id) {
        try {
            postService.deleteById(id);
            return "redirect:/post/list";
        } catch (WebException ex) {
            Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "redirect:/post/list";
    }

    @PostMapping("/save")
    public String guardarPost(Model model, @RequestParam(required = false) MultipartFile file, RedirectAttributes redirectAttributes,
            @ModelAttribute Post post, @RequestParam(required = true) String action, @RequestParam(required = true) String id) {
        try {
            if (action.equals("edit")) {
               Post foundPost = postService.findById(id).get();
                postService.modifyPost(file, foundPost, post);
                redirectAttributes.addFlashAttribute("success", "Post modificado con éxito.");
                return "redirect:/post/list";
            } else {
                postService.save(post, file);
                redirectAttributes.addFlashAttribute("success", "Post guardado con éxito.");
            }

        } catch (WebException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("post", post);
            return "post-form";
        }
        return "redirect:/post/list";
    }
}
