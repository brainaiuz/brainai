package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Umidbek on 31.01.2015.
 */
public class FacetFilterTO implements IsSerializable {

    Integer id;
    String name;

    Boolean isDefault;
    Boolean isPublic;
    Boolean isFavour;

    HashMap<String, ArrayList<FacetFilterItemTO>> facetContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Boolean getIsFavour() {
        return isFavour;
    }

    public void setIsFavour(Boolean isFavour) {
        this.isFavour = isFavour;
    }

    public HashMap<String, ArrayList<FacetFilterItemTO>> getFacetContent() {
        return facetContent;
    }

    public void setFacetContent(HashMap<String, ArrayList<FacetFilterItemTO>> facetContent) {
        this.facetContent = facetContent;
    }
}