package com.edatasite.workforce.gwt.core.server.utils;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.envers.tools.Pair;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 4/15/16
 * Time: 12:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionUtils {


    public static <T> T[] ar(T... ts) {
        return ts;
    }


    public static <T> List<T> list() {
        return new ArrayList<>();
    }


    public static <T> List<T> list(T... ts) {
        return list(Arrays.asList(ts));
    }


    public static <T> List<T> list(Collection<T> ts) {
        return new ArrayList<>(ts);
    }


    public static <T> List<T> listSlice(List<T> ts, int first, int last) {
        List<T> list = list();
        for (int n = first; n < last && n < ts.size(); ++n) {
            list.add(ts.get(n));
        }

        return list;
    }


    public static <T> Set<T> set() {
        return new HashSet<>();
    }


    public static <T> Set<T> set(T... ts) {
        return new HashSet<>(Arrays.asList(ts));
    }


    public static <T> Set<T> set(Collection<T> ts) {
        return new HashSet<>(ts);
    }


    public static <T> Set<T> set(Collection<T> ts, Collection<T> ts2) {
        Set<T> set = new HashSet<>(ts);
        set.addAll(ts2);
        return set;
    }

    /**
     * Create an instance of HashMap
     *
     * @param <K> key type
     * @param <V> value type
     * @return Map
     */

    public static <K, V> Map<K, V> map() {
        return new HashMap<>();
    }

    /**
     * Create an instance of HashMap from single key-value pair
     *
     * @param k   Single entry key
     * @param v   Single entry value
     * @param <K> key type
     * @param <V> value type
     * @return Map
     */

    public static <K, V> Map<K, V> map(K k, V v) {
        Map<K, V> map = new HashMap<>();
        map.put(k, v);
        return map;
    }


    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }


    public static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    /**
     * Create an instance of HashMap from existing map
     *
     * @param values Initial map data
     * @param <K>    key type
     * @param <V>    value type
     * @return Map
     */

    public static <K, V> Map<K, V> map(Map<K, V> values) {
        return new HashMap<>(values);
    }

    /**
     * Create an instance of TreeMap
     *
     * @param <K> key type
     * @param <V> value type
     * @return Map
     */

    public static <K, V> SortedMap<K, V> treeMap() {
        return new TreeMap<>();
    }


    public static <K, V> SortedMap<K, V> treeMap(K[] keys, V[] values) {
        SortedMap<K, V> map = treeMap();
        int n = 0;
        for (K k : keys) {
            V v = n < values.length ? values[n] : null;
            map.put(k, v);
            ++n;
        }

        return map;
    }


    public static <K, V> SortedMap<K, V> treeMap(Pair<K, V>... pairs) {
        SortedMap<K, V> map = treeMap();
        for (Pair<K, V> pair : pairs) {
            map.put(pair.getFirst(), pair.getSecond());
        }

        return map;
    }


    public static <K, V> Map<K, V> map(Pair<K, V>... pairs) {
        Map<K, V> map = map();
        for (Pair<K, V> pair : pairs) {
            map.put(pair.getFirst(), pair.getSecond());
        }

        return map;
    }


    public static <K, V> Map<K, V> map(K[] keys, V[] values) {
        Map<K, V> map = map();
        int n = 0;
        for (K k : keys) {
            V v = values != null && n < values.length ? values[n] : null;
            map.put(k, v);
            ++n;
        }

        return map;
    }

    /**
     * Helper interface that can get key for map by it's value
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    public interface KeyExtractor<K, V> {
        K key(V v);
    }


    public static <K, V> Map<K, V> map(Collection<V> values, KeyExtractor<K, V> extractor) {
        Map<K, V> map = map();
        for (V v : values) {
            map.put(extractor.key(v), v);
        }

        return map;
    }


    public static <T> SortedSet<T> treeSet() {
        return new TreeSet<>();
    }


    public static <T> SortedSet<T> treeSet(Collection<T> values) {
        return new TreeSet<>(values);
    }


    public static <T> SortedSet<T> treeSet(Collection<T> values, Comparator<T> comparator) {
        SortedSet<T> result = new TreeSet<>(comparator);
        result.addAll(values);
        return result;
    }

    /**
     * Check if maps values are equals by a specified set of keys
     *
     * @param keys Set of keys to check equality against
     * @param p1   First map
     * @param p2   Second map
     * @param <K>  Key parameter type
     * @param <V>  Value parameter type
     * @return <code>true</code> if maps has equals values for requested set of keys
     */
    public static <K, V> boolean isSame(Collection<K> keys, Map<K, V> p1, Map<K, V> p2) {
        EqualsBuilder equalsBuilder = new EqualsBuilder();
        for (K k : keys) {
            equalsBuilder.append(p1.get(k), p2.get(k));
        }

        return equalsBuilder.isEquals();
    }

    /**
     * Create ArrayStack
     *
     * @param objects Objects to put to stack
     * @return ArrayStack
     */
    public static ArrayStack arrayStack(Object... objects) {
        ArrayStack stack = new ArrayStack();
        for (Object o : objects) {
            stack.push(o);
        }

        return stack;
    }
}