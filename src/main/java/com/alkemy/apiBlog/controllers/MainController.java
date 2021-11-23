package com.alkemy.apiBlog.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Fede
 */
@Controller
@RequestMapping("/")
public class MainController {
    
 @GetMapping("/index")
    public String index() {
        return "index";
    }
    

}
