package ru.netology.geo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.entity.Country;

public class GeoServiceTest {

    private GeoService geoService;

    private final String RU_IP = "172.123.12.19";
    private final String US_IP = "96.42.167.129";

    @BeforeEach
    void setup() {
        geoService = new GeoServiceImpl();
    }

    @Test
    @DisplayName("Проверка определения ip России")
    void shouldReturnRussiaWithRuIP() {
        Assertions.assertEquals(Country.RUSSIA, geoService.byIp(RU_IP).getCountry());
    }

    @Test
    @DisplayName("Проверка определения ip США")
    void shouldReturnUSAWithUSIP() {
        Assertions.assertEquals(Country.USA, geoService.byIp(US_IP).getCountry());
    }
}
