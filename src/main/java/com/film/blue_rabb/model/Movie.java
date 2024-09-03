package com.film.blue_rabb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "movie")
@NoArgsConstructor
public class Movie {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_rus")
    private String nameRus;

    @Column(name = "name_eng")
    private String nameEng;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "age")
    private byte age;

    @Column(name = "creator")
    private char creator;

    @Column(name = "average_duration")
    private Integer averageDuration;

    @OneToMany
    @Column(name = "movie_video_id")
    private List<MovieVideo> MovieVideos;

    public Movie(String nameRus, String nameEng, String description, Integer duration, byte age, char creator, Integer averageDuration, List<MovieVideo> movieVideos) {
        this.nameRus = nameRus;
        this.nameEng = nameEng;
        this.description = description;
        this.duration = duration;
        this.age = age;
        this.creator = creator;
        this.averageDuration = averageDuration;
        MovieVideos = movieVideos;
    }
}
