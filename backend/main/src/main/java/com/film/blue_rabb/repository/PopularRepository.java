package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.Popular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularRepository extends JpaRepository<Popular, Long> {
}
