package com.edatasite.workforce.gwt.core.server.office365.resources;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.Map;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365HttpResponse extends JSONObject {
    private int statusCode;
    private JSONObject error;

    public Office365HttpResponse() {
    }

    public Office365HttpResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    public Office365HttpResponse(int statusCode, String jsonStr) {
        this.statusCode = statusCode;

        if (statusCode == 200 || statusCode == 201) { // OK || Created
            this.putAll(jsonStr);
            return;
        }

        if (statusCode == 202 || statusCode == 204) { // Accepted || No Content
            // noop
            return;
        }

        this.error = (JSONObject) this.tryParseJSON(jsonStr);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public JSONObject getError() {
        return error;
    }

    public Boolean hasError() {
        return error != null;
    }

    private void putAll(String jsonStr) {
        Object data = this.tryParseJSON(jsonStr);

        if (data != null) {
            this.putAll((Map) data);
        }
    }

    private Object tryParseJSON(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }

        JSONParser parser = new JSONParser();

        try {
            return parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

