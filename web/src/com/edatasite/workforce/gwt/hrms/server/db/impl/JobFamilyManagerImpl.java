package com.edatasite.workforce.gwt.hrms.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsJobFamily;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.hrms.server.db.JobFamilyManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhom Lutfullaev
 * Date: 24.11.2009
 * Time: 20:37:51
 * To change this template use File | Settings | File Templates.
 */
@Repository("jobFamilyManager")
public class JobFamilyManagerImpl extends BaseManager<EdsJobFamily> implements JobFamilyManager {
    private UserManager userManager;

    public JobFamilyManagerImpl() {
        super(EdsJobFamily.class);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public List<EdsJobFamily> getJobFamilies(EdsCompany usercompany) {
        return find("select jf from EdsJobFamily jf ");
    }

}
