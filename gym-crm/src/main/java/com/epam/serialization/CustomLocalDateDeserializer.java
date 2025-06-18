package com.epam.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        JsonToken currentToken = p.currentToken();

        if (currentToken != JsonToken.VALUE_STRING) {
            throw new InvalidFormatException(p, "Expected date in 'yyyy-MM-dd' string format", p.getText(), LocalDate.class);
        }

        String date = p.getText().trim();
        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidFormatException(p, "Invalid date format. Expected 'yyyy-MM-dd'", date, LocalDate.class);
        }
    }

}
