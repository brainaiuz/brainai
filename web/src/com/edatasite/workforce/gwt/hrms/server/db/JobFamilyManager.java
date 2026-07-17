package com.edatasite.workforce.gwt.hrms.server.db;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsJobFamily;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 24.11.2009
 * Time: 20:36:00
 * To change this template use File | Settings | File Templates.
 */
public interface JobFamilyManager extends Manager<EdsJobFamily> {
    List<EdsJobFamily> getJobFamilies(EdsCompany company);
}
