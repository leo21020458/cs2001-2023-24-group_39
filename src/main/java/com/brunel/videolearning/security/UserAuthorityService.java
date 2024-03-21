package com.brunel.videolearning.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.brunel.videolearning.model.User;
import com.brunel.videolearning.repository.UserRepository;

/**
 * Based on this user's role, specify the specific authorities they have.
 */
@Service
public class UserAuthorityService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> opUserInDB = repository.findUserByEmail(email);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (opUserInDB.isPresent()) {
            User userInDB = opUserInDB.get();
            //read user roleType from DB
            int currentUserRoleType = userInDB.getUserType();
            //Temporary user "TEST" for the specific authorities
            authorities.add(new SimpleGrantedAuthority("TEST"));
            //Combine the user's email, encrypted password, and their respective authorities, send these three parameters to the Spring Security framework, and have it generate a User under the Spring Security framework.
            return new org.springframework.security.core.userdetails.User(email, userInDB.getPassword(), true, true, true, true, authorities);
        } else {
            throw new UsernameNotFoundException("User not authorized.");
        }
    }

}
