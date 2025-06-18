package com.epam.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CustomStringDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonToken token = jsonParser.getCurrentToken();

        if (token == JsonToken.VALUE_NUMBER_INT || token == JsonToken.VALUE_NUMBER_FLOAT) {
            throw new IllegalArgumentException("Expected string, but got numeric value: " + jsonParser.getText() + " at field " + jsonParser.currentName());
        }

        if (token == JsonToken.VALUE_TRUE || token == JsonToken.VALUE_FALSE) {
            throw new IllegalArgumentException("Expected string, but got boolean value: " + jsonParser.getText() + " at field " + jsonParser.currentName());
        }

        return jsonParser.getValueAsString();
    }
}
