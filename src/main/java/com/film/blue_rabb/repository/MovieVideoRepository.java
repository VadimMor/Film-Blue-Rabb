package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.MovieVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieVideoRepository extends JpaRepository<MovieVideo, Long> {
}
