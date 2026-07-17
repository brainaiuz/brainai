package com.edatasite.workforce.gwt.core.server.utils;

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: iskandar
 * Date: Jan 23, 2008
 * Time: 9:30:03 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ComparatorFactory<T extends Object> {
    Comparator<T> createComparator(int sortOrder);
}
