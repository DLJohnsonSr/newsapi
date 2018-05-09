package com.d2j2.newsapi.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String roleName;
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<AppUser>users;

    public AppRole() {
        this.users = new HashSet<>();
    }

    public AppRole(String roleName) {
        this.roleName = roleName;
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<AppUser> getUsers() {
        return users;
    }

    public void setUsers(Set<AppUser> users) {
        this.users = users;
    }

    public void AddUser(AppUser appUser){
        this.users.add(appUser);
    }
}
