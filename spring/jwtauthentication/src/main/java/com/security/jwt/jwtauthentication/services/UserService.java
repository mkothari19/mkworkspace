package com.security.jwt.jwtauthentication.services;

import com.security.jwt.jwtauthentication.entity.User;
import com.security.jwt.jwtauthentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
@Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

   public List<User> getUsers(){

       return userRepository.findAll();
   }

   public User createUser(User user){
       user.setUserId(UUID.randomUUID().toString());
       user.setPassword(passwordEncoder.encode(user.getPassword()));
       return userRepository.save(user);

   }


}
