package com.edatasite.workforce.gwt.core.client.form.panel;

import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Abror Abdukadirov
 * Date: 14.08.2019 17:59
 */
public final class GridUIUtils {

    public static List<CustomizeFormItem> sort(List<CustomizeFormItem> list) {
        int maxWidth = Collections.max(list.stream()
                                           .map(item -> item.getX() + item.getWidth())
                                           .collect(Collectors.toList()));

        list.sort(new Comparator<CustomizeFormItem>() {
            @Override
            public int compare(CustomizeFormItem o1, CustomizeFormItem o2) {
                int leftOrder = getSorder(o1, maxWidth);
                int rightOrder = getSorder(o2, maxWidth);

                if (leftOrder == rightOrder) {
                    return 0;
                }
                return leftOrder > rightOrder ? 1 : -1;
            }
        });
        return list;
    }

    private static int getSorder(CustomizeFormItem o1, int width) {
        return o1.getX() + o1.getY() * width;
    }
}
