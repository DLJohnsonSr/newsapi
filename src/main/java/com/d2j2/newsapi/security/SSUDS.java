package com.d2j2.newsapi.security;



import com.d2j2.newsapi.entities.AppRole;
import com.d2j2.newsapi.entities.AppUser;
import com.d2j2.newsapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUDS implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    public SSUDS(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AppUser thisUser = userRepository.findByUsername(username);
            if (thisUser == null) {
                return null;
            }
            return new org.springframework.security.core.userdetails.User(
                    thisUser.getUsername(),
                    thisUser.getPassword(),
                    getAuthorities(thisUser));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }
    private Set<GrantedAuthority>getAuthorities(AppUser thisUser){
        Set<GrantedAuthority>authorities = new HashSet<GrantedAuthority>();
        for(AppRole eachRole : thisUser.getRoles()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(eachRole.getRoleName());
            authorities.add(grantedAuthority);
        }
        return authorities;
    }
}
