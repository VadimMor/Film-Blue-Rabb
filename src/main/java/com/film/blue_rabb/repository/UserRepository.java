package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u " +
            "WHERE u.login = :login " +
            "OR u.email = :login")
    User getFirstByEmailOrLogin(String login);
}
