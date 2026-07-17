package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsOrgBoardSettings;
import com.edatasite.workforce.gwt.core.server.db.OrgBoardSettingsManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository("orgBoardSettingsManager")
public class OrgBoardSettingsManagerImpl extends BaseManager<EdsOrgBoardSettings> implements OrgBoardSettingsManager {

    public OrgBoardSettingsManagerImpl() {
        super(EdsOrgBoardSettings.class);
    }

    @Override
    public EdsOrgBoardSettings findSettingsByEmployee(Integer employeeId) {
        Query queryObject = slaveEntityManager.createQuery("select s from EdsOrgBoardSettings s where s.employeeId = " + employeeId);
        queryObject.setMaxResults(1);
        try {
            return (EdsOrgBoardSettings) queryObject.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
