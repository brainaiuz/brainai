package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: iskandar
 * Date: Jan 23, 2008
 * Time: 9:37:13 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class AbstractComparator<T extends Object> implements Comparator<T> {
    protected int internalCompare(Comparable c1, Comparable c2, int sortOrder) {
        int result;
        if (c1 == c2) {
            return 0;
        }
        // Null value is the biggest value whenever it goes!
        if (c1 == null && c2 != null) {
            return Integer.MAX_VALUE;
        }
        if (c2 == null && c1 != null) {
            return Integer.MIN_VALUE;
        }
        return switch (sortOrder) {
            case Constants.ASC -> c1.compareTo(c2);
            case Constants.DESC -> c2.compareTo(c1);
            default -> 0;
        };
    }
}
