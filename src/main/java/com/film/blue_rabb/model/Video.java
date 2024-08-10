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

    @Column(name = "name_rus")
    String nameRus;

    @Column(name = "name_eng")
    String nameEng;

    @Column(name = "description")
    String description;

    @Column(name = "duration")
    String duration;

    @Column(name = "age")
    byte age;

    @Column(name = "creator")
    char creator;

    public Video(String nameRus, String nameEng, String description, String duration, byte age, char creator) {
        this.nameRus = nameRus;
        this.nameEng = nameEng;
        this.description = description;
        this.duration = duration;
        this.age = age;
        this.creator = creator;
    }
}
