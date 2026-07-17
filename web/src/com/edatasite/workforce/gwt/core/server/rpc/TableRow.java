package com.edatasite.workforce.gwt.core.server.rpc;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class TableRow {
    private static final Logger log = LoggerFactory.getLogger(TableRow.class);

    public static Map<String, Class> TYPE;

    static {
        TYPE = new HashMap<String, Class>();

        TYPE.put("INTEGER", Integer.class);
        TYPE.put("INT", Integer.class);
        TYPE.put("INT4", Integer.class);
        TYPE.put("TINYINT", Byte.class);
        TYPE.put("SMALLINT", Short.class);
        TYPE.put("BIGINT", Long.class);
        TYPE.put("REAL", Float.class);
        TYPE.put("FLOAT", Double.class);
        TYPE.put("DOUBLE", Double.class);
        TYPE.put("DECIMAL", BigDecimal.class);
        TYPE.put("NUMERIC", BigDecimal.class);
        TYPE.put("BOOLEAN", Boolean.class);
        TYPE.put("CHAR", String.class);
        TYPE.put("VARCHAR", String.class);
        TYPE.put("LONGVARCHAR", String.class);
        TYPE.put("TEXT", String.class);
        TYPE.put("DATE", Date.class);
        TYPE.put("TIME", Time.class);
        TYPE.put("TIMESTAMP", Timestamp.class);
        TYPE.put("SERIAL", Integer.class);
        TYPE.put("BIGSERIAL", Integer.class);
        TYPE.put("SMALLSERIAL", Integer.class);
        // ...
    }

    public List<Map.Entry<Object, Class>> data;

    public TableRow() {
        data = new ArrayList<Map.Entry<Object, Class>>();
    }

    public <T> void add(T data) {
        this.data.add(new AbstractMap.SimpleImmutableEntry<Object, Class>(data, data.getClass()));
    }

    public void addNull(Class castType) {
        this.data.add(new AbstractMap.SimpleImmutableEntry<Object, Class>(null, castType));
    }

    public void add(Object data, String sqlType) {
        Class castType = TableRow.TYPE.get(sqlType.toUpperCase());
        if (castType == null) {
            castType = String.class;
        }
        if (data == null) {
            addNull(castType);
        } else {
            try {
                add(castType.cast(data));
            } catch (NullPointerException e) {
                log.error(e.getMessage() + " Add the type " + sqlType + " to the TYPE hash map in the Row class.", e);
                throw e;
            }
        }
    }

    public List<Map.Entry<Object, Class>> getData() {
        return data;
    }

    public Object get(Integer index) {
        Map.Entry<Object, Class> column = data.get(index);
        if (column == null) {
            return null;
        }
        if (column.getValue() == null) {
            return null;
        }
        return column.getValue().cast(column.getKey());
    }

    public String getString(Integer index) {
        Object value = get(index);
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    public Integer getInt(Integer index) {
        Object value = get(index);
        if (value == null) {
            return null;
        }
        if (value instanceof String stringValue) {
            return Integer.valueOf(stringValue);
        } else {
            return (Integer) value;
        }
    }
}
