package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.groupPayrun;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

public class GroupPayrunSubView extends GroupPayrunEditView {

    public GroupPayrunSubView(Integer id) {
        super("pending");
        setDescription(property.getSingular(wfmStrings.groupPayrun()));
        this.id = id;
    }

    @Override
    protected void addTopPanel() {
        //don't add top widgets
    }
    protected void addTotalTable() {
        //don't show total table
    }

    @Override
    protected void initButtons() {
    }

    protected ListingFilterParameter getFilterParameter() {
        filterParameter = super.getFilterParameter();
        filterParameter.setStatusCode(Constants.PAYRUN_STATUS_PENDING);
        return filterParameter;
    }

    @Override
    public String getPropertyCode() {
        return PAYSLIP_TABLE_LIST;
    }
}
