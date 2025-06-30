package ru.netology.sender;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.i18n.LocalizationService;

import java.util.HashMap;
import java.util.Map;

import static ru.netology.geo.GeoServiceImpl.MOSCOW_IP;
import static ru.netology.geo.GeoServiceImpl.NEW_YORK_IP;

@ExtendWith(MockitoExtension.class)
public class MessageSenderTest {

    @Mock
    private GeoService geoService;
    @Mock
    private LocalizationService localizationService;

    private MessageSender messageSender;

    private Map<String, String> headers;

    private final String RU_TEXT = "Добро пожаловать";
    private final String EN_TEXT = "Welcome";

    private final String RU_IP = "172.123.12.19";
    private final String US_IP = "96.42.167.129";
    private final String LH_IP = "127.0.0.1";
    private final String UDF_IP = "152.145.1.201";

    @BeforeEach
    void setup() {
        Mockito.lenient().when(geoService.byIp(RU_IP))
                .thenReturn(new Location(null, Country.RUSSIA, null, 0));
        Mockito.lenient().when(geoService.byIp(MOSCOW_IP))
                .thenReturn(new Location(null, Country.RUSSIA, null, 0));
        Mockito.lenient().when(geoService.byIp(US_IP))
                .thenReturn(new Location(null, Country.USA, null, 0));
        Mockito.lenient().when(geoService.byIp(NEW_YORK_IP))
                .thenReturn(new Location(null, Country.USA, null, 0));
        Mockito.lenient().when(geoService.byIp(LH_IP))
                .thenReturn(new Location(null, null, null, 0));
        Mockito.lenient().when(geoService.byIp(UDF_IP))
                .thenReturn(null);

        Mockito.lenient().when(localizationService.locale(Country.RUSSIA)).thenReturn(RU_TEXT);
        Mockito.lenient().when(localizationService.locale(Country.USA)).thenReturn(EN_TEXT);
        Mockito.lenient().when(localizationService.locale(null)).thenThrow(new NullPointerException());

        messageSender = new MessageSenderImpl(geoService, localizationService);
        headers = new HashMap<String, String>();
    }

    @ParameterizedTest
    @DisplayName("Проверка MessageSenderImpl на отправку русского текста")
    @ValueSource(strings = {RU_IP, MOSCOW_IP})
    void shouldSendRussianText(String ip) {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        Assertions.assertEquals(RU_TEXT, messageSender.send(headers));
    }

    @ParameterizedTest
    @DisplayName("Проверка MessageSenderImpl на отправку английского текста")
    @ValueSource(strings = {US_IP, NEW_YORK_IP})
    void shouldSendEnglishText(String ip) {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        Assertions.assertEquals(EN_TEXT, messageSender.send(headers));
    }

    @ParameterizedTest
    @DisplayName("Проверка MessageSenderImpl на ошибку при неопределённых адресах")
    @ValueSource(strings = {LH_IP, UDF_IP})
    void shouldThrowNPE(String ip) {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, ip);

        try {
            messageSender.send(headers);
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.assertEquals("java.lang.NullPointerException", e.getClass().getName());
        }
    }
}
