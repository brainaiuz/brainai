package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import com.google.gwt.user.client.rpc.IsSerializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365BaseList<T> extends ArrayList<T> implements IsSerializable {
    public Office365BaseList() {
    }

    public Office365BaseList(JSONObject data, Office365BaseItem.FieldMapper<T> mapper) {
        JSONArray value = (JSONArray) data.get("value");

        if (value != null) {
            for (Object item : value) {
                this.add(mapper.map(item));
            }
        }
    }
}
