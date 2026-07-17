package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsContainerItem;

import java.util.List;

public interface ContainerItemManager extends Manager<EdsContainerItem> {

    List<EdsContainerItem> getItemsByContainer(Integer objectID, boolean fromSettings);

    List<EdsContainerItem> getItemsByProperty(Integer propertyId);

    EdsContainerItem getItem(Integer objectID, String moduleCode);

    String getContainer(Integer objectID, String moduleCode);

    Integer getMaxSorderByContainer(String moduleCode, Integer objectID);

    void updateByProperty(Integer propertyId, Boolean status);
}