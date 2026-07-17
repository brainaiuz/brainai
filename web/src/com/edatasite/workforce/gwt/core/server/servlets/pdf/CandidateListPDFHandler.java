package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.crm.CrmContactListPDFHandler;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * User: Babayev xushnud
 * Date: 7/25/12
 * Time: 5:09 PM
 */
public class CandidateListPDFHandler extends CrmContactListPDFHandler {

    private RecruitmentService recruitmentService;

    public void setRecruitmentService(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_CandidateListPDF_" + dateFormat(new Date()));
    }
    @Override
    protected String getTableName(Object dataClass) {
        EdsProperty property = propertManager.findByCode(Constants.CANDIDATE);
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("candidates");
    }

    @Override
    protected ListResult<ContactListItem> getList(ListingFilterParameter filterParametrs) {
        return recruitmentService.listCandidates(filterParametrs);
    }
}