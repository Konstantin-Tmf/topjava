package ru.javawebinar.topjava.web.formatter;

import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalTimeFormatter implements Formatter<LocalTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return StringUtils.hasLength(text) ? LocalTime.parse(text, FORMATTER) : null;
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object != null ? object.format(FORMATTER) : "";
    }
}
