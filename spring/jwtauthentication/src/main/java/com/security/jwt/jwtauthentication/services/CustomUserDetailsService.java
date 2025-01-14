package com.security.jwt.jwtauthentication.services;

import com.security.jwt.jwtauthentication.entity.User;
import com.security.jwt.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service


public class CustomUserDetailsService implements UserDetailsService {

    //@Autowired
   // private PasswordEncoder passwordEncoder;
    @Autowired
    private  UserRepository userRepository;

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //load user from database
      User user=  userRepository.findByEmail(username).orElseThrow(()->new RuntimeException("User Not Found"));
        return user;
    }

}
