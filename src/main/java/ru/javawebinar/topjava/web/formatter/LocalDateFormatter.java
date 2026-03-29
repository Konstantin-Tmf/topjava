package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateFormatter implements Formatter<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return StringUtils.hasLength(text) ? LocalDate.parse(text, FORMATTER) : null;
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return object != null ? object.format(FORMATTER) : "";
    }
}
