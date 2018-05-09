package com.d2j2.newsapi.entities;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AppUser {

    @Transient
    private PasswordEncoder passwordEncoder;

    @Transient
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    @NotNull
    private String username;
    @NotNull
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<AppRole>roles;

    public AppUser() {
        this.roles = new HashSet<>();
        passwordEncoder = passwordEncoder();
    }

    public AppUser(String username, String password, AppRole role){
        this.username = username;
        this.password = password;
        this.roles = new HashSet<>();
        addRoles(role);
        passwordEncoder = passwordEncoder();
        setPassword(password);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    public Set<AppRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<AppRole> roles) {
        this.roles = roles;
    }

    public void addRoles(AppRole appRoles){
        this.roles.add(appRoles);
    }

}
