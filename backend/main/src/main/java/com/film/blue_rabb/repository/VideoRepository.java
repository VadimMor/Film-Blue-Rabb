package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findFirstByFullName(String fullName);
}
