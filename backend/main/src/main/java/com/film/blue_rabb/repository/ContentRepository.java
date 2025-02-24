package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {
    Content findByNameRusOrNameEng(String nameRus, String nameEng);

    Content findFirstBySymbolicName(String symbolicName);
}
