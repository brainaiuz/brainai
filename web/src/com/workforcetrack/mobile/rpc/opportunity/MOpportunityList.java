package com.workforcetrack.mobile.rpc.opportunity;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/19/11
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MOpportunityList {

    private Integer totalCount;
    private List<MOpportunityListItem> opportunityListItem;

    public MOpportunityList() {

    }

    public MOpportunityList(OpportunityList opportunityList) {
        if (opportunityList != null) {
            this.opportunityListItem = new ArrayList<>();
            for (OpportunityListItem item : opportunityList.getOpportunityListItems()) {
                this.opportunityListItem.add(new MOpportunityListItem(item));
            }
            this.totalCount = opportunityList.getTotalCount();

        }
    }

    public MOpportunityList(ListResult<OpportunityListItem> opportunityList) {
        if (opportunityList != null && opportunityList.getList() != null && opportunityList.getList().size() > 0) {
            totalCount = opportunityList.getTotal();
            opportunityListItem = new ArrayList<>();
            for (OpportunityListItem item : opportunityList.getList()) {
                this.opportunityListItem.add(new MOpportunityListItem(item));
            }
        }
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<MOpportunityListItem> getOpportunityListItem() {
        return opportunityListItem;
    }

    public void setOpportunityListItem(List<MOpportunityListItem> opportunityListItem) {
        this.opportunityListItem = opportunityListItem;
    }
}
