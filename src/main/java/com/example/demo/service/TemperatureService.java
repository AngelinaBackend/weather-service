package com.example.demo.service;

import com.example.demo.client.RESTClient;
import com.example.demo.client.SOAPClient;
import com.example.demo.repository.TemperatureRepository;
import com.example.demo.model.TemperatureRecord;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class TemperatureService {

    private final SOAPClient soapClient;
    private final RESTClient restClient;
    private final TemperatureRepository temperatureRepository;

    public TemperatureService(SOAPClient soapClient, RESTClient restClient, TemperatureRepository temperatureRepository) {
        this.soapClient = soapClient;
        this.restClient = restClient;
        this.temperatureRepository = temperatureRepository;
    }

    public void updateTemperatureForCity(String city, String country) {
        try {
            double soapTemp = soapClient.getTemperature(city, country);
            double restTemp = restClient.getTemperature(city, country);
            double avgTemp = (soapTemp + restTemp) / 2;

            TemperatureRecord record = new TemperatureRecord();
            record.setCity(city);
            record.setCountry(country);
            record.setTemperature(avgTemp);
            record.setTimestamp(LocalDateTime.now());

            temperatureRepository.save(record);
        } catch (Exception e) {

            System.err.println("Error updating temperature for city " + city + ", " + country + ": " + e.getMessage());
        }
    }
}
