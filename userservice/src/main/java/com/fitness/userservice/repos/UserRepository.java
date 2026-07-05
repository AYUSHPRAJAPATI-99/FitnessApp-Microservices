package com.fitness.userservice.repos;

import com.fitness.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

import java.util.Optional;

// @Repository                                             // jpa already uderstand it is repository
public interface UserRepository extends JpaRepository<User,String> {
    Boolean existsByEmail(String email);        // method which belong to PK is already define in JPA
    Optional<User> findById(String s);
    Optional<User> findByEmail(String email);

}
