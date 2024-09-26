package com.example.demo.Client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RESTClient {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String restUrl;

    @Autowired
    public RESTClient(RestTemplateBuilder builder,
                      @Value("${rest-service.url}") String restUrl,
                      @Value("${rest-service.api-key}") String apiKey) {
        this.restTemplate = builder.build();
        this.restUrl = restUrl;
        this.apiKey = apiKey;
    }

    public double getTemperature(String city, String country) {
        String url = String.format("%s?q=%s,%s&appid=%s&units=metric", restUrl, city, country, apiKey);
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> main = (Map<String, Object>) response.getBody().get("main");
                if (main != null && main.get("temp") != null) {
                    return (double) main.get("temp");
                } else {
                    throw new RuntimeException("Ошибка извлечения температуры из ответа REST API");
                }
            } else {
                throw new RuntimeException("Некорректный ответ от REST API: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при запросе к REST API: " + e.getMessage());
        }
    }
}
