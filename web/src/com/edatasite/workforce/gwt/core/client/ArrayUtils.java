package com.edatasite.workforce.gwt.core.client;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: kotabek
 * Date: 26.12.12 10:20
 */
public class ArrayUtils {
    /**
     * ToArrayList convertor For don't use List interface
     *
     * @param array
     * @return
     */
    public static <T> ArrayList<T> asList(T[] array) {
        ArrayList<T> result = new ArrayList<>();
        if (array != null && array.length != 0) {
            result = new ArrayList<>(array.length);
            result.addAll(Arrays.asList(array));
        }
        return result;
    }
}
