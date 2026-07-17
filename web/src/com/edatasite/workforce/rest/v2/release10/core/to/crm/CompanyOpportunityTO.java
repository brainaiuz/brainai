package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;

/**
 * Created by Abdurakhmonov Farrukh on 03/06/2017.
 */
public class CompanyOpportunityTO extends OpportunityTO {
    private FlowSettingsTO status;

    public CompanyOpportunityTO() {
    }

    public FlowSettingsTO getStatus() {
        return status;
    }

    public void setStatus(FlowSettingsTO status) {
        this.status = status;
    }
}
