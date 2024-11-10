package com.film.blue_rabb.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "popular")
@NoArgsConstructor
public class Popular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_of_views")
    private Integer numberViews;

    @Column(name = "number_of_likes")
    private Integer numberLikes;

    @Column(name = "number_of_favorites")
    private Integer numberFavorites;

    public Popular(Integer numberViews, Integer numberLikes, Integer numberFavorites) {
        this.numberViews = numberViews;
        this.numberLikes = numberLikes;
        this.numberFavorites = numberFavorites;
    }
}
