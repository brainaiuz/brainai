package com.edatasite.workforce.gwt.reportingsystem.client.enumerable;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 4, 2011
 * Time: 12:48:37 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ChartSizeType implements IsSerializable {
    CUSTOM("Custom") {
        @Override
        public Integer getHeight() {
            return 400;
        }

        @Override
        public Integer getWidth() {
            return 400;
        }
    },

    SMALL("Small") {
        @Override
        public Integer getHeight() {
            return 200;
        }

        @Override
        public Integer getWidth() {
            return 250;
        }
    },

    MIDDLE("Medium") {
        @Override
        public Integer getHeight() {
            return 250;
        }

        @Override
        public Integer getWidth() {
            return 300;
        }
    },

    LARGE("Large") {
        @Override
        public Integer getHeight() {
            return 300;
        }

        @Override
        public Integer getWidth() {
            return 350;
        }
    };


    ChartSizeType(String title) {
        this.title = title;
    }

    private String title;

    public String getTitle() {
        return title;
    }

    public abstract Integer getHeight();

    public abstract Integer getWidth();

    public static ChartSizeType getChartType(Integer height, Integer width) {
        if (height.equals(150) && width.equals(200)) {
            return SMALL;
        } else if (height.equals(200) && width.equals(250)) {
            return MIDDLE;
        } else if (height.equals(250) && width.equals(300)) {
            return LARGE;
        } else {
            return CUSTOM;
        }

    }

    public static SelectItem[] getChartSize() {
        SelectItem[] items = new SelectItem[4];
        int i = 0;
        for (ChartSizeType type : values()) {
            items[i++] = new SelectItem(i, type.getTitle(), type.name());
        }
        return items;
    }
}
