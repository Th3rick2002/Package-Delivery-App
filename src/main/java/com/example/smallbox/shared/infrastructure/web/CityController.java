package com.example.smallbox.shared.infrastructure.web;

import com.example.smallbox.shared.application.GetCitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cities")
@RequiredArgsConstructor
@Slf4j
public class CityController {
    private final GetCitiesService getCitiesService;

    @GetMapping
    public Object getCities() {
        return getCitiesService.execute();
    }
}
