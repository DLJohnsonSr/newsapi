package com.d2j2.newsapi.security;


import com.d2j2.newsapi.entities.AppRole;
import com.d2j2.newsapi.entities.AppUser;
import com.d2j2.newsapi.repositories.RoleRepository;
import com.d2j2.newsapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;


    @Override
    public void run(String... args) throws Exception {

        roleRepository.save( new AppRole("USER"));
        roleRepository.save( new AppRole("ADMIN"));

        AppUser aUser = new AppUser("testAdmin", "password", roleRepository.findByRoleName("ADMIN"));
        userRepository.save(aUser);

        aUser = new AppUser("testUser", "password", roleRepository.findByRoleName("USER"));
        userRepository.save(aUser);

    }
}
