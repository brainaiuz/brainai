package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.recruitment.EdsRotation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface RotationManager extends Manager<EdsRotation> {
    Integer getRotationLastIntNumber();

    List<EdsRotation> getList(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);

    boolean isRotationNumberExist(String numberString, Integer objectID);
}
