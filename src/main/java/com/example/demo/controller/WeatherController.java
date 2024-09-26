package controller;

import com.example.demo.Client.SOAPClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private final SOAPClient soapClient;

    public WeatherController(SOAPClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping("/temperature")
    public double getTemperature(@RequestParam String city, @RequestParam String country) {
        try {
            return soapClient.getTemperature(city, country);
        } catch (Exception e) {
            System.err.println("Ошибка при получении температуры: " + e.getMessage());
            throw new RuntimeException("Не удалось получить температуру для " + city + ", " + country);
        }
    }
}