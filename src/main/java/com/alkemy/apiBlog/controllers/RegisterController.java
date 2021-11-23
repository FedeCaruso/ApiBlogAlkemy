package com.alkemy.apiBlog.controllers;


import com.alkemy.apiBlog.exceptions.WebException;
import com.alkemy.apiBlog.services.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private BlogUserService blogUserService;



    @GetMapping("")
    public String registro() {
        return "registro";
    }

    @PostMapping("")
    public String registroSave(Model model, @RequestParam String username, 
            @RequestParam String clave, @RequestParam String clave2) {
        try {
            blogUserService.save(username, clave, clave2);
            return "redirect:/";
        } catch (WebException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("username", username);
        }
        return "registro";
    }
}
