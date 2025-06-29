package ru.netology.i18n;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.netology.entity.Country;

import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

public class LocalizationServiceTest {

    private LocalizationService localizationService;

    @BeforeEach
    void setup() {
        localizationService = new LocalizationServiceImpl();
    }

    @ParameterizedTest
    @DisplayName("Проверка возвращения русского текста")
    @EnumSource(names = {"RUSSIA"})
    void shouldReturnRuText(Country country) {
        Assertions.assertEquals("Добро пожаловать", localizationService.locale(country));
    }

    @ParameterizedTest
    @DisplayName("Проверка возвращения английского текста")
    @EnumSource(mode = EXCLUDE, names = {"RUSSIA"})
    void shouldReturnEnText(Country country) {
        Assertions.assertEquals("Welcome", localizationService.locale(country));
    }
}
