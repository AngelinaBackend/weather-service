package com.example.demo.repository;

import com.example.demo.model.TemperatureRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TemperatureRepository extends JpaRepository<TemperatureRecord, Long> {
    List<TemperatureRecord> findByCityAndCountryAndTimestampBetween(String city, String country, LocalDateTime start, LocalDateTime end);
    Optional<TemperatureRecord> findTopByCityAndCountryOrderByTimestampDesc(String city, String country);

}


