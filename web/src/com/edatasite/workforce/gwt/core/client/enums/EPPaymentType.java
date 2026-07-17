package com.edatasite.workforce.gwt.core.client.enums;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.HashMap;
import java.util.stream.Stream;

public enum EPPaymentType {
    RECURRING(1, "Recurring"),
    ADDITIONAL(2, "Additional");

    static HashMap<Integer, EPPaymentType> map = new HashMap<>();

    static {
        Stream.of(EPPaymentType.values()).forEach(t -> {
            map.put(t.id, t);
        });
    }

    int id;
    String title;

    EPPaymentType(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public static EPPaymentType findById(Integer id) {
        return map.get(id);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public SelectItem getAsSelectItem() {
        return new SelectItem(id, title);
    }
}
