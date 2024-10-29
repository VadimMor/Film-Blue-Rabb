package com.film.blue_rabb.repository;

import com.film.blue_rabb.model.VideoFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface VideoFileRepository extends MongoRepository<VideoFile, UUID> {
    VideoFile findFirstByVideoAndContentType(byte[] bytes, String contentType);
}
