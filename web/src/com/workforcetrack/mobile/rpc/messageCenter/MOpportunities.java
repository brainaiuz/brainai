package com.workforcetrack.mobile.rpc.messageCenter;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 14.09.11
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MOpportunities {

    private List<MSelectItem> opportunity;

    public MOpportunities() {
    }

    public MOpportunities(SelectItem[] opportunityItems) {
        opportunity = WebServiceUtils.getAsMSelectItemList(opportunityItems);
    }

    public List<MSelectItem> getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(List<MSelectItem> opportunity) {
        this.opportunity = opportunity;
    }
}
