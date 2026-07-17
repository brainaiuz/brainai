package com.edatasite.workforce.gwt.core.server.app;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 12/13/11
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class MapEntry<K, V> {
    private K key;
    private V value;

    public MapEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public MapEntry() {
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
