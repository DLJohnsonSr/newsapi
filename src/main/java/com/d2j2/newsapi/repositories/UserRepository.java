package com.d2j2.newsapi.repositories;

import com.d2j2.newsapi.entities.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
