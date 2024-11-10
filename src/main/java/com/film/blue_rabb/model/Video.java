package com.film.blue_rabb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Data
@Entity
@Table(name = "video")
@NoArgsConstructor
public class Video {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "id_mongo")
    private String idMongo;

    @CreatedDate
    @Column(name = "create_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private Date updatedDate;

    public Video(String fullName, String shortName, String description, Integer durationMinutes, String idMongo, Date createdDate, Date updatedDate) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.idMongo = idMongo;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
