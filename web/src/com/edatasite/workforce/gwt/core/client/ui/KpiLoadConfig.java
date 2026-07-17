package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.LinkedHashMap;
/**
 * User: dilshod madrahimov
 * Date: 7/26/12
 * Time: 2:56 AM
 */
public class KpiLoadConfig implements IsSerializable {
    /**
     * start specifies the current cursor.
     */
    public int start;

    /**
     * limit specifies the number of records being requested.
     */
    public int limit;

    /**
     * sortField specifies the field to sort by.
     */
    public String sortField;

    /**
     * sortDir specifies the requested sort direction.
     */
    public int sortDir;

    /**
     * dataMap contains application specific load properties
     *
     * @gwt.typeArgs <java.lang.String,com.google.gwt.user.client.rpc.IsSerializable>
     */
    protected LinkedHashMap dataMap;

    /**
     * Returns the application specific load property for the given name, or
     * <code>null</code> if it has not been set.
     *
     * @param key the name of the property
     * @return the value or <code>null</code> if it has not been set
     */
    public Object getData(String key) {
        if (dataMap == null) return null;
        return dataMap.get(key);
    }

    /**
     * Returns the config's data map.
     *
     * @return the data map
     */
    public LinkedHashMap getDataMap() {
        return dataMap;
    }

    /**
     * Sets the application specific load property with the given name.
     *
     * @param key the name of the property
     * @param config the new value for the property
     */
    public void setData(String key, Object config) {
        if (dataMap == null) dataMap = new LinkedHashMap();
        dataMap.put(key, config);
    }

    /**
     * Sets the config's data map.
     *
     * @param dataMap the data map
     */
    public void setDataMap(LinkedHashMap dataMap) {
        this.dataMap = dataMap;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("start=" + start);
        sb.append("&limit=" + limit);
        sb.append("&sortField=" + sortField);
        sb.append("&sortDir=" + sortDir);
        return sb.toString();
    }

}
