package com.edatasite.workforce.gwt.core.server;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date value = null;
        try {
            value = format.parse(text);
        } catch (ParseException e) {
            ;
        }
        setValue(value);
    }


}
