package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsContainer;

import java.util.List;

public interface ContainerManager extends Manager<EdsContainer> {

    List<EdsContainer> getContainerBySorder(String moduleName);

    Integer getMaxSorderByModule(String moduleCode);
}