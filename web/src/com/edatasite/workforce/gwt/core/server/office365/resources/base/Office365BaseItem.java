package com.edatasite.workforce.gwt.core.server.office365.resources.base;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.apache.commons.lang.math.NumberUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by umakarimov on 9/30/15.
 */
public abstract class Office365BaseItem implements IsSerializable {
    private final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    protected final static FieldMapper<String> stringMapper = new FieldMapper<String>() {
        @Override
        public String map(Object item) {
            return Office365BaseItem.tryCastToString(item);
        }
    };

    private static String tryCastToString(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    protected String getString(JSONObject data, String key) {
        return Office365BaseItem.tryCastToString(data.get(key));
    }

    protected Boolean getBoolen(JSONObject data, String key) {
        return Boolean.valueOf(this.getString(data, key));
    }

    protected Integer getInteger(JSONObject data, String key) {
        return NumberUtils.toInt(this.getString(data, key));
    }

    protected Long getLong(JSONObject data, String key) {
        return NumberUtils.toLong(this.getString(data, key));
    }

    protected Double getDouble(JSONObject data, String key) {
        return NumberUtils.toDouble(this.getString(data, key));
    }

    protected Date getDate(String dateStr) {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    protected Date getDate(Long dateTime) {
        try {
            return new Date(dateTime * 1000L);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected String formatDate(Date date) {
        if (date == null) {
            return null;
        }

        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        try {
            return formatter.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    protected ArrayList getArrayList(JSONObject data, String key, FieldMapper mapper) {
        JSONArray items = (JSONArray) data.get(key);
        ArrayList list = new ArrayList();

        if (items != null) {
            for (Object item : items) {
                list.add(mapper.map(item));
            }
        }

        return list;
    }

    protected JSONArray getJSONArray(final ArrayList list) {
        return this.getJSONArray(list, new FieldMapper() {
            @Override
            public Object map(Object item) {
                if (item instanceof Office365BaseItem) {
                    return ((Office365BaseItem) item).toJSON();
                }

                return item;
            }
        });
    }

    protected JSONArray getJSONArray(final ArrayList list, final FieldMapper mapper) {
        if (list == null) {
            return null;
        }

        return new JSONArray() {{
            for (Object item : list) {
                this.add(mapper.map(item));
            }
        }};
    }

    public abstract JSONObject toJSON();

    public static abstract class FieldMapper<T> {
        public abstract T map(Object item);
    }
}
