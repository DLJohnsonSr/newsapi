package com.d2j2.newsapi.controllers;

import com.d2j2.newsapi.entities.AppUser;
import com.d2j2.newsapi.entities.NewsFeed;
import com.d2j2.newsapi.repositories.RoleRepository;
import com.d2j2.newsapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

@Controller
public class MainController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String showIndex(Model model){
        RestTemplate restTemplate = new RestTemplate();
        NewsFeed newsFeed = restTemplate.getForObject("https://newsapi.org/v2/top-headlines?country=us&apiKey=c965340100d449e081e3bedcd2c633c8", NewsFeed.class);
        model.addAttribute("newsFeed", newsFeed);
        System.out.println(newsFeed.getArticles().toString());
        return "index";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }
    @GetMapping("/signup")
    public String signUpNewUser(Model model){
        model.addAttribute("newUser", new AppUser());
        return "signup";
    }
    @PostMapping("/saveuser")
    public String saveNewUser(@Valid @ModelAttribute("newUser")AppUser appUser, BindingResult result){
        if (result.hasErrors()){
            return "signup";
        }
        appUser.addRoles(roleRepository.findByRoleName("USER"));
        userRepository.save(appUser);
        return "redirect:login";
    }


}
