package com.film.blue_rabb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document(collation = "video")
@NoArgsConstructor
public class VideoFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Indexed(unique = true)
    private String name;

    @Lob
    private byte[] video;

    public VideoFile(String name, byte[] video) {
        this.name = name;
        this.video = video;
    }
}
