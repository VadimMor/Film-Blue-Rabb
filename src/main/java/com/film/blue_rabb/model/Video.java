package com.film.blue_rabb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "id_mongo")
    private String idMongo;

    public Video(String fullName, String shortName, String description, Integer durationMinutes, String idMongo) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.description = description;
        this.durationMinutes = durationMinutes;
        this.idMongo = idMongo;
    }
}
