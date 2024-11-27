package com.film.blue_rabb.controller;

import com.film.blue_rabb.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduledController {
    private final UserService userService;

    @Async
    @Scheduled(fixedRate = 3600000)
    public void UsersVerification() throws InterruptedException {
        log.info("ScheduledController.UsersVerification - 1 hours");
        userService.usersVerification();
    }
}
