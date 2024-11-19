package com.film.blue_rabb.service;

import com.film.blue_rabb.model.Popular;
import org.springframework.stereotype.Service;

@Service
public interface PopularService {
    Popular createPopularTable();

    void updateViews(Popular popularTable);
}
