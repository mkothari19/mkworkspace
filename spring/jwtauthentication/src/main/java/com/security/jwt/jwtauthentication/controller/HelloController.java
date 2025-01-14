package com.security.jwt.jwtauthentication.controller;

import com.security.jwt.jwtauthentication.entity.User;
import com.security.jwt.jwtauthentication.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/home")
public class HelloController {

    @Autowired
    UserService userService;
    Logger logger = LoggerFactory.getLogger(HelloController.class);
    //host : http://localhost:8181/home/user

    @GetMapping("/user")
    public List<User> getUser() {
        this.logger.warn("This is jwt user service");
        return this.userService.getUsers();
    }

    @GetMapping("/current-user")
    public String getLoginUser(Principal principal) {
        this.logger.warn("Login User is "+principal.getName());
        return principal.getName();
    }


}
