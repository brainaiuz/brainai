package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsUser;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmLeadsExcelHandler extends ContactListExcelHandler {
    @Override
    protected boolean isLeadExport() {
        return true;
    }

    @Override
    protected void setFileName() {
        EdsUser user = getUser();
        filename = user.getFirstName() + "_" + user.getLastName() + "_LeadList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/", "_");
        if (filename.length() > 31) {
            filename = filename.substring(0, 31);}
    }
}
