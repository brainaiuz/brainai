package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReferenceColor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceColorManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;

/**
 * User: Dilsh0d Madrahimov
 * Date: Jan 14, 2008 Time: 3:58:03 PM
 */

@Repository("referenceColorManager")
public class ReferenceColorManagerImpl extends BaseManager<EdsReferenceColor> implements ReferenceColorManager {

    public ReferenceColorManagerImpl() {
        super(EdsReferenceColor.class);
    }

    @Override
    public EdsReferenceColor getByHex(String colorHex) {
        try {
            return slaveEntityManager.createQuery("select rc from EdsReferenceColor rc where lower(hex)=:hexColor", EdsReferenceColor.class)
                    .setParameter("hexColor", colorHex.toLowerCase()).setMaxResults(1).getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return null;
        }
    }
}
