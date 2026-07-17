package com.edatasite.workforce.gwt.core.client.ui;

import java.util.*;

public class LinkedMap {

    private List list;
    private Map map;

    public LinkedMap() {
        list = new ArrayList();
        map = new HashMap();
    }

    public void add(Object key, Object value) {
        if (!map.containsKey(key)) {
            list.add(key);
            map.put(key, value);
        }
    }

    public void insert(Object key, Object value, int index) {
        if (!map.containsKey(key)) {
            list.add(index, key);
            map.put(key, value);
        }
    }

    public Object[] get() {
        Object[] result = new Object[list.size()];

        Iterator iterator = list.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            result[i] = map.get(iterator.next());
            i++;
        }

        return result;
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public Object getKeyByIndex(int index) {
        return list.get(index);
    }

    public Object get(int index) {
        return map.get(list.get(index));
    }

    public void remove(Object key) {
        list.remove(key);
        map.remove(key);
    }

    public void clear() {
        map.clear();
        list.clear();
    }

    public boolean contains(Object key) {
        return map.containsKey(key);
    }

    public int indexOf(Object key) {
        return list.indexOf(key);
    }

    public int size() {
        return list.size();
    }

    public Map getMap() {
        return map;
    }
}
