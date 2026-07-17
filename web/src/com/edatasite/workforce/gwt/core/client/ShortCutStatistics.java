package com.edatasite.workforce.gwt.core.client;

import gwt.material.design.client.ui.html.Span;

public interface ShortCutStatistics {

    default void initStatistics(Integer parentId, Span container) {
    }

    default String countFormat(Integer totalCount) {

        if (totalCount != null && totalCount.intValue() > 0) {
            String format = "";

            if (totalCount.compareTo(1000) < 0) {
                format = "" + totalCount;
            } else if (totalCount.compareTo(1000000) < 0) {
                format = (totalCount/1000) + "K+";
            } else {
                format = (totalCount/1000000) + "M+";
            }

            return format;
        }

        return null;
    }
}
