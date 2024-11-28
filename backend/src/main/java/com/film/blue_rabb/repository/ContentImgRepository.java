package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.ContentImg;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ContentImgRepository extends MongoRepository<ContentImg, String> {
    ContentImg findFirstByImgAndContentType(byte[] img, String contentType);
}
