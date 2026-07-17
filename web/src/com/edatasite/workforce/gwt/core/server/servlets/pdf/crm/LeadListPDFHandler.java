package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 11.08.2009
 * Time: 14:54:47
 * To change this template use File | Settings | File Templates.
 */
public class LeadListPDFHandler extends CrmContactListPDFHandler {
    private CRMService crmService;

    public void setCrmService(CRMService crmService) {
        this.crmService = crmService;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_LeadList_" + dateFormat(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (fp.getPropertyCode().equals("leads")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("leadsList");
        } else if (fp.getPropertyCode().equals("contacts")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("contacts");
        }
        return null;
    }

    @Override
    protected ListResult<ContactListItem> getList(ListingFilterParameter filterParametrs) {
        return crmService.getNewLeads(filterParametrs);
    }
}
