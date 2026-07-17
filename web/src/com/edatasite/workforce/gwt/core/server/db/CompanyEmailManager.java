package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompanyEmail;

/**
 * Created by Azazello on 6/24/2017.
 */
public interface CompanyEmailManager extends Manager<EdsCompanyEmail> {
    String getCompanyEmail(Integer companyID);

    EdsCompanyEmail getByCompanyID(Integer companyID);
}
