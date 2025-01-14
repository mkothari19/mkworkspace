package com.security.jwt.jwtauthentication.repository;

import com.security.jwt.jwtauthentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    public Optional<User> findByEmail(String email);
}
