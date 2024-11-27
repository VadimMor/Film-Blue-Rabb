package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("SELECT u FROM Users u " +
            "WHERE u.email = :email " +
            "OR u.login = :login")
    Users findFirstByEmailOrLogin(String email, String login);

    @Query("SELECT u FROM Users u " +
            "WHERE u.email = :email")
    Users findFirstByEmail(String email);

    @Query("SELECT u FROM Users u " +
            "WHERE u.status = 'INACTIVE' " +
            "AND u.dateCreation < :thresholdTime")
    List<Users> findAllByCreateDate(OffsetDateTime thresholdTime);
}
