package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInOutSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.InOutManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 27, 2010
 * Time: 10:01:42 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("inOutManager")
public class InOutManagerImpl extends BaseManager<EdsInOutSettings> implements InOutManager {

    public InOutManagerImpl() {
        super(EdsInOutSettings.class);
    }


    public EdsInOutSettings getCompanyInOutSettings() {
        EdsCompany company = getUser().getCompany();
        return (EdsInOutSettings) findSingle("from EdsInOutSettings where company=?", company);
    }

}
