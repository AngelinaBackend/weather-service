package com.example.demo.Client;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

@Service
public class SOAPClient {

    private final WebServiceTemplate webServiceTemplate;
    private final String soapUrl;

    public SOAPClient(@Value("${soap-service.url}") String soapUrl, WebServiceTemplate webServiceTemplate) {
        this.soapUrl = soapUrl;
        this.webServiceTemplate = webServiceTemplate;
    }

    public double getTemperature(String city, String country) {
        String requestPayload = String.format("<GetTemperatureRequest><city>%s</city><country>%s</country></GetTemperatureRequest>", city, country);

        String responsePayload = (String) webServiceTemplate
                .marshalSendAndReceive(soapUrl, requestPayload,
                        new SoapActionCallback("http://./GetTemperature"));

        return 22.00;
    }

    private double parseTemperatureFromResponse(String response) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(response.getBytes()));

            Element rootElement = document.getDocumentElement();
            String temperatureValue = rootElement.getElementsByTagName("temperature")
                    .item(0)
                    .getTextContent();

            return Double.parseDouble(temperatureValue);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге ответа SOAP: : " + e.getMessage(), e);
        }
    }
}
