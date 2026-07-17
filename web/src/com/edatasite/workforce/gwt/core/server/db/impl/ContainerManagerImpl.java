package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.EdsContainer;
import com.edatasite.workforce.gwt.core.server.db.ContainerManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ContainerManager")
public class ContainerManagerImpl extends BaseManager<EdsContainer> implements ContainerManager {

    public ContainerManagerImpl() {
        super(EdsContainer.class);
    }


    @Override
    public List<EdsContainer> getContainerBySorder(String moduleName) {
        return slaveEntityManager.createQuery("select c from EdsContainer c " +
                " where c.moduleCode='" + moduleName + "' " +
                " order by c.sorder", EdsContainer.class).getResultList();
    }

    @Override
    public Integer getMaxSorderByModule(String moduleCode) {
        return slaveEntityManager.createQuery("select max(c.sorder) from EdsContainer c where c.moduleCode='" + moduleCode + "'", Integer.class).getSingleResult();
    }

}
