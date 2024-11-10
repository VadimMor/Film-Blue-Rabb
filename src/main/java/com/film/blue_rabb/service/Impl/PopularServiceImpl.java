package com.film.blue_rabb.service.Impl;

import com.film.blue_rabb.model.Popular;
import com.film.blue_rabb.repository.PopularRepository;
import com.film.blue_rabb.service.PopularService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PopularServiceImpl implements PopularService {
    private final PopularRepository popularRepository;

    @Override
    public Popular createPopularTable() {
        log.trace("PopularServiceImpl.createPopularTable");

        Popular popular = new Popular(
                0,
                0,
                0
        );

        popularRepository.saveAndFlush(popular);

        return popular;
    }
}
