package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanyEmail;
import com.edatasite.workforce.gwt.core.server.db.CompanyEmailManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Azazello on 6/24/2017.
 */
@Repository("companyEmailManager")
public class CompanyEmailManagerImpl extends BaseManager<EdsCompanyEmail> implements CompanyEmailManager {
    public CompanyEmailManagerImpl() {
        super(EdsCompanyEmail.class);
    }

    @Override
    public String getCompanyEmail(Integer companyID) {
        return (String) findSingle("SELECT email from EdsCompanyEmail where company.objectID=?", companyID);
    }

    @Override
    public EdsCompanyEmail getByCompanyID(Integer companyID) {
        return (EdsCompanyEmail) findSingle("SELECT e from EdsCompanyEmail e where e.company.objectID=?", companyID);
    }
}
