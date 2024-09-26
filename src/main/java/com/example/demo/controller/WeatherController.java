package com.example.demo.controller;

import com.example.demo.model.TemperatureRecord;
import com.example.demo.repository.TemperatureRepository;
import com.example.demo.service.TemperatureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final TemperatureService temperatureService;
    private final TemperatureRepository temperatureRepository;

    public WeatherController(TemperatureService temperatureService, TemperatureRepository temperatureRepository) {
        this.temperatureService = temperatureService;
        this.temperatureRepository = temperatureRepository;
    }

    @GetMapping("/temperature")
    public double getTemperature(@RequestParam String city, @RequestParam String country) {
        try {
            temperatureService.updateTemperatureForCity(city, country);
            return temperatureRepository.findTopByCityAndCountryOrderByTimestampDesc(city, country)
                    .map(TemperatureRecord::getTemperature)
                    .orElseThrow(() -> new RuntimeException("Температура для города " + city + ", " + country + " не найдена"));
        } catch (Exception e) {
            System.err.println("Error retrieving temperature:" + e.getMessage());
            throw new RuntimeException("Failed to retrieve temperature for" + city + ", " + country);
        }
    }
}

