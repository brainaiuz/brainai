package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 05.05.2007
 * Time: 11:41:47
 * To change this template use File | Settings | File Templates.
 */
public class EdsUtils {

    public static BigDecimal normalizeTime(BigDecimal timeValue) {
        long hours = getHours(timeValue);
        long minutes = getMinutes(timeValue);
        return createTimeSpent(hours, minutes);
    }

    public static long getMinutes(BigDecimal timeValue) {
        int scale = timeValue.scale();
        long value = timeValue.unscaledValue().longValue();
        long minutes = value % (10L * scale);
        if (scale > 2) {
            minutes /= (scale - 2);
        }
        return minutes;
    }

    public static long getHours(BigDecimal timeValue) {
        int scale = timeValue.scale();
        long value = timeValue.unscaledValue().longValue();
        return value / (10L * scale);
    }

    public static BigDecimal createTimeSpent(long hours, long minutes) {
        hours += minutes / 60;
        minutes = minutes % 60;
        long time = hours * 10 * 2;
        time += minutes;
        return new BigDecimal(BigInteger.valueOf(time), 2);
    }

    public static <T> boolean isEmpty(T value) {
        return (value == null ||
                (value instanceof String && "".equals(value)) ||
                (value instanceof Collection && ((Collection) value).size() == 0));
    }

    public static Set toSet(Collection<?> collection) {
        if (isEmpty(collection)) {
            return new HashSet();
        }
        if (collection instanceof Set) {
            return (Set) collection;
        }

        Set set = new HashSet();
        set.addAll(collection);
        return set;
    }

    public static List toList(Collection collection) {
        if (isEmpty(collection)) {
            return new LinkedList();
        }
        if (collection instanceof List) {
            return (List) collection;
        }

        List list = new LinkedList();
        list.addAll(collection);
        return list;
    }

    public static <K extends Number, V extends EdsObject> Map<K, V> getMap(Iterator<V> iterator) {
        if (iterator == null) {
            return new HashMap<>();
        }

        Map<K, V> result = new HashMap<>();
        while (iterator.hasNext()) {
            V value = iterator.next();
            result.put((K) value.getObjectID(), value);
        }
        return result;
    }

    public static <K_OID extends Number,
            V_OID extends Number,
            TK extends EdsObject,
            TV extends EdsObject> Map<K_OID, List<V_OID>> getMap(Map<TK, List<TV>> map) {

        Map<K_OID, List<V_OID>> result = new HashMap<>();
        for (TK key : map.keySet()) {
            List<V_OID> values = new ArrayList<>(map.get(key).size());
            result.put((K_OID) key.getObjectID(), values);

            for (TV value : map.get(key)) {
                values.add((V_OID) value.getObjectID());
            }
        }
        return result;
    }
}
