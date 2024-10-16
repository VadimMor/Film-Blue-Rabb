package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.ContentImg;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentImgRepository extends MongoRepository<ContentImg, String> {
}
