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
 * Time: 11:02
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MLeads {

    private List<MSelectItem> lead;

    public MLeads() {

    }

    public MLeads(SelectItem[] leadItems) {
        lead = WebServiceUtils.getAsMSelectItemList(leadItems);
    }

    public List<MSelectItem> getLead() {
        return lead;
    }

    public void setLead(List<MSelectItem> lead) {
        this.lead = lead;
    }
}
