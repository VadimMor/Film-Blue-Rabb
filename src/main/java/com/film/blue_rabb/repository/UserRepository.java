package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findFirstByEmail(String email);

    @Query("SELECT COUNT(u) FROM Users u WHERE u.role = 'CLIENT'")
    int clientsCount();

    @Query("SELECT COUNT(u) FROM Users u WHERE u.role = 'ORGANIZATION'")
    int organizationsCount();
}


