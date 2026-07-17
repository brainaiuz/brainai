package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsContainerItem;
import com.edatasite.workforce.gwt.core.server.db.ContainerItemManager;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository("ContainerItemManager")
public class ContainerItemManagerImpl extends BaseManager<EdsContainerItem> implements ContainerItemManager {

    public ContainerItemManagerImpl() {
        super(EdsContainerItem.class);
    }


    @Override
    public List<EdsContainerItem> getItemsByContainer(Integer objectID, boolean fromSettings) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ci from EdsContainerItem ci left join ci.container c" +
                " where c.objectID=").append(objectID);
        if (!fromSettings) {
            sql.append(" and (ci.active is null or ci.active is true) and ci.module.active<>false");
        }
        sql.append(" order by ci.sorder");

        return slaveEntityManager.createQuery(sql.toString(), EdsContainerItem.class).getResultList();
    }


    @Override
    public List<EdsContainerItem> getItemsByProperty(Integer propertyID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ci from EdsContainerItem ci left join ci.property p" +
                " where p.objectID=").append(propertyID);

        return slaveEntityManager.createQuery(sql.toString(), EdsContainerItem.class).getResultList();
    }

    @Override
    public EdsContainerItem getItem(Integer propertyId, String moduleCode) {
        Query query = slaveEntityManager.createQuery("select ci from EdsContainerItem ci left join ci.property p where ci.moduleCode='" + moduleCode + "'" +
                " and p.objectID=" + propertyId, EdsContainerItem.class);
        return query.getResultList() != null && query.getResultList().size() > 0 ? (EdsContainerItem) query.getResultList().get(0) : null;
    }

    @Override
    public String getContainer(Integer propertyId, String moduleCode) {
        Query query = slaveEntityManager.createQuery("select ci.container.code from EdsContainerItem ci left join ci.property p where ci.moduleCode='" + moduleCode + "'" +
                " and p.objectID=" + propertyId, String.class);
        return query.getResultList() != null && query.getResultList().size() > 0 ? (String) query.getResultList().get(0) : null;
    }

    @Override
    public Integer getMaxSorderByContainer(String moduleCode, Integer containerId) {
        return slaveEntityManager.createQuery("select max(ci.sorder) from EdsContainerItem ci left join ci.container c where ci.moduleCode='" + moduleCode + "'" +
                " and c.objectID=" + containerId, Integer.class).getSingleResult();
    }

    @Override
    public void updateByProperty(Integer propertyId, Boolean status) {
        update("update EdsContainerItem set active = '" + status + "' where property = " + propertyId);

    }
}