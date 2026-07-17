package com.edatasite.workforce.rest.v3.release10.core.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
/*
* Causes deserialization object issue
* */
@Deprecated
public class DateDeserializer extends StdDeserializer<Date> {
    public static final String[] DATE_PATTERNS = new String[]{
            "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d]",
            "yyyy['/']['-']['.']d[d]['/']['-']['.']M[M]",
            "d[d]['/']['-']['.']M[M]['/']['-']['.']yyyy",
            "M[M]['/']['-']['.']d[d]['/']['-']['.']yyyy"
    };
    public static final String[] TIMESTAMP_PATTERNS = new String[]{
            "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d]['T'][ ]HH:mm:ss[.SSS]['Z']",
            "yyyy['/']['-']['.']d[d]['/']['-']['.']M[M]['T'][ ]HH:mm:ss[.SSS]['Z']",
            "d[d]['/']['-']['.']M[M]['/']['-']['.']yyyy['T'][ ]HH:mm:ss[.SSS]['Z']",
            "M[M]['/']['-']['.']d[d]['/']['-']['.']yyyy['T'][ ]HH:mm:ss[.SSS]['Z']"
    };

    public DateDeserializer() {
        this(null);
    }

    protected DateDeserializer(Class<?> vc) {
        super(vc);
    }

    public static Date convertToDate(String strDate) {
        Date date = getDateFromString(strDate);

        if (date == null) {
            date = getTimestampFromString(strDate);
        }
        return date;
    }

    private static Date getDateFromString(String strDate) {
        for (String format : DATE_PATTERNS) {
            try {
                LocalDate ld = LocalDate.parse(strDate, DateTimeFormatter.ofPattern(format));
                return java.sql.Date.valueOf(ld);
            } catch (Exception e) {
            }
        }
        return null;
    }

    private static Date getTimestampFromString(String strTimestamp) {
        for (String format : TIMESTAMP_PATTERNS) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(strTimestamp, DateTimeFormatter.ofPattern(format));
                return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Date date = convertToDate(jsonParser.getText());

        if (date == null) {
            throw new RuntimeException("Cannot parse '" + jsonParser.getText() + "' to date");
        }
        return new Date(date.getTime());//todo for some reason(quick fix)
    }
}
