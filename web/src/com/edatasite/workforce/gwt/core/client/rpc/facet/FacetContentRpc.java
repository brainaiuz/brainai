package com.edatasite.workforce.gwt.core.client.rpc.facet;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 04-Aug-2010
 * Time: 16:37:39
 */
public class FacetContentRpc implements IsSerializable {
    private SelectItem[] facetItems;
    private HashMap<Integer, Integer> savedItems;

    public SelectItem[] getFacetItems() {
        if (facetItems == null) {
            facetItems = new SelectItem[0];
        }
        return facetItems;
    }

    public void setFacetItems(SelectItem[] facetItems) {
        this.facetItems = facetItems;
    }

    public HashMap<Integer, Integer> getSavedItems() {
        if (savedItems == null) {
            savedItems = new HashMap<>();
        }
        return savedItems;
    }

    public void setSavedItems(HashMap<Integer, Integer> savedItems) {
        this.savedItems = savedItems;
    }
}
