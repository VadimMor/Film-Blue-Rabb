package com.film.blue_rabb.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "image")
@NoArgsConstructor
@Getter
@Setter
public class ContentImg {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Indexed(unique = true)
    @NotNull(message = "Name cannot be null")
    private String name;

    @Lob
    @NotNull(message = "Video content cannot be null")
    private byte[] img;

    private String contentType;

    private long size; // Размер файла в байтах

    @CreatedDate
    private Date createdDate;

    @LastModifiedDate
    private Date updatedDate;

    public ContentImg(String name, @NotNull(message = "Video content cannot be null") byte[] img, String contentType, long size, Date createdDate, Date updatedDate) {
        this.name = name;
        this.img = img;
        this.contentType = contentType;
        this.size = size;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
