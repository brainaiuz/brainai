package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;

import java.util.Date;

/**
 * User: Babayev xushnud
 * Date: 7/25/12
 * Time: 5:02 PM
 */
public class ShortListPDFHandler extends CandidateListPDFHandler {

    private RecruitmentService recruitmentService;

    public void setRecruitmentService(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @Override
    protected String getTableName(Object dataClass) {
        EdsProperty property = propertManager.findByCode(Constants.SHORT_LIST);
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("shorts");
    }

    @Override
    protected ListResult<ContactListItem> getList(ListingFilterParameter filterParametrs) {
        filterParametrs.setShortList(true);
        return recruitmentService.listCandidates(filterParametrs);
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ShortList_" + dateFormat(new Date()));
    }
}