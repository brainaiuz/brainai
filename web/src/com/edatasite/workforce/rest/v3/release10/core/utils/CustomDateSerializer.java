package com.edatasite.workforce.rest.v3.release10.core.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomDateSerializer extends JsonSerializer<Date> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void serialize(Date date, JsonGenerator generator, SerializerProvider provider) throws IOException {
        String formattedDate = dateFormat.format(date);
        generator.writeString(formattedDate);
    }
}
