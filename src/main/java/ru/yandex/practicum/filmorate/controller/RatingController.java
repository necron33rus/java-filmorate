package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public Collection<Rating> getAllRatings() {
        log.info("Получен GET-запрос к эндпоинту /mpa");
        return ratingService.getAllRatings();
    }

    @GetMapping("/{id}")
    public Rating getRatingById(@PathVariable Integer id) {
        log.info("Получен GET-запрос к эндпоинту /mpa/{}", id);
        return ratingService.getRatingById(id);
    }
}
