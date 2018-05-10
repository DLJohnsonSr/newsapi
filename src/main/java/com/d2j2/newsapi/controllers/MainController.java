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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class MainController {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/")
    public String showIndex(Model model){
        RestTemplate restTemplate = new RestTemplate();
        NewsFeed newsFeed = restTemplate.getForObject("https://newsapi.org/v2/top-headlines?country=us&apiKey=???????????????????????", NewsFeed.class);
        model.addAttribute("newsFeed", newsFeed);
        System.out.println(newsFeed.getArticles().toString());
        return "index";
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
    @GetMapping("/login")
    public String login(){
        return "login";
    }
    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }
    @PostMapping("/savecategories")
    public String saveCategories(@Valid @ModelAttribute("user") AppUser user, BindingResult result){
        if(result.hasErrors()){
            return "addcategories";
        }
        userRepository.save(user);
        return "redirect:/usernewpage";
    }
    @GetMapping("/usernewspage")
    public String showUserPage(Model model, HttpServletRequest request, Principal p) {
        String username = p.getName();
        AppUser thisUser = userRepository.findByUsername(username);
        if (thisUser.getCategories().isEmpty()) {
            model.addAttribute("user", thisUser);
            return "redirect:addcategories";
        } else {
            model.addAttribute("user", thisUser);
            RestTemplate restTemplate = new RestTemplate();
            String categoeries = thisUser.getCategories();
            String categoriesURL = "https://newsapi.org/v2/everything?q=" + categoeries + "&apiKey=???????????????????";
            NewsFeed newsFeed = restTemplate.getForObject(categoriesURL, NewsFeed.class);
            model.addAttribute("newsFeed", newsFeed);
            System.out.println(newsFeed.getArticles().toString());
        return "usernewspage";
        }
    }
    @GetMapping("/addcategories")
    public String addCategories(Model model, HttpServletRequest request){
        String username = new String(request.getUserPrincipal().getName());
        model.addAttribute("user",userRepository.findByUsername(username));
        return "addcategories";
    }
    @PostMapping("/savecategories")
    public String saveCategories(AppUser appUser){
        userRepository.save(appUser);
        return "redirect:userneswpage"

    }
}
