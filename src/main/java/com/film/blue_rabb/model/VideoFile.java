package com.film.blue_rabb.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "video")
@NoArgsConstructor
public class VideoFile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Indexed(unique = true)
    @NotNull(message = "Name cannot be null")
    private String name;

    @Lob
    @NotNull(message = "Video content cannot be null")
    private byte[] video;

    private String contentType;

    private long size; // Размер файла в байтах

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;

    public VideoFile(String name, @NotNull(message = "Video content cannot be null") byte[] video, String contentType, long size, Date createdDate, Date updatedDate) {
        this.name = name;
        this.video = video;
        this.contentType = contentType;
        this.size = size;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
