package com.edatasite.workforce.gwt.core.client.ui;

import java.util.LinkedHashMap;

public class LoadingWidgets {
    private static LinkedHashMap<String, LoadingWidget> instance = new LinkedHashMap<>();

    public static LoadingWidget get(String code) {
        instance.computeIfAbsent(code, LoadingWidget::new);
        return instance.get(code);
    }
}
