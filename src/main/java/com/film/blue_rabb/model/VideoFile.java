package com.film.blue_rabb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
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
@Getter
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
    @Column(name = "create_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "update_date")
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
