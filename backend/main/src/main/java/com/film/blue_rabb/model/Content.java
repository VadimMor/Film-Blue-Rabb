package com.film.blue_rabb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "content")
@NoArgsConstructor
public class Content {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_rus")
    private String nameRus;

    @Column(name = "name_eng")
    private String nameEng;

    @Column(name = "symbolic_name")
    private String symbolicName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "age")
    private byte age;

    @Column(name = "creator")
    private String creator;

    @Column(name = "average_duration")
    private Integer averageDuration;

    @CreatedDate
    @Column(name = "create_date")
    private Date createdDate;

    @LastModifiedDate
    @Column(name = "update_date")
    private Date updatedDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "content_images", joinColumns = @JoinColumn(name = "content_id"))
    @Column(name = "image_id")
    private Set<String> imageSet;

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "content_id")
    private List<Video> videos;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "popular_id")
    private Popular popularTable;

    public Content(String nameRus, String nameEng, String symbolicName, String description, byte age, String creator, Integer averageDuration, Date createdDate, Date updatedDate, List<Video> videos, Popular popularTable) {
        this.nameRus = nameRus;
        this.nameEng = nameEng;
        this.symbolicName = symbolicName;
        this.description = description;
        this.age = age;
        this.creator = creator;
        this.averageDuration = averageDuration;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.videos = videos;
        this.popularTable = popularTable;
    }

    public void addImage(String image) {
        if (imageSet == null) imageSet = new HashSet<>();
        if (image != null) this.imageSet.add(image);
    }
}
