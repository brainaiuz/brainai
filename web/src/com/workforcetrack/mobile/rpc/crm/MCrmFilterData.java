package com.workforcetrack.mobile.rpc.crm;

import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 28.03.12
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MCrmFilterData {

    private List<MSelectItem> leadSource;
    private List<MSelectItem> status;
    private List<MSelectItem> country;
    private List<MSelectItem> assignee;
    private List<MSelectItem> campaign;

    public MCrmFilterData() {

    }

    public List<MSelectItem> getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(List<MSelectItem> leadSource) {
        this.leadSource = leadSource;
    }

    public List<MSelectItem> getStatus() {
        return status;
    }

    public void setStatus(List<MSelectItem> status) {
        this.status = status;
    }

    public List<MSelectItem> getCountry() {
        return country;
    }

    public void setCountry(List<MSelectItem> country) {
        this.country = country;
    }

    public List<MSelectItem> getAssignee() {
        return assignee;
    }

    public void setAssignee(List<MSelectItem> assignee) {
        this.assignee = assignee;
    }

    public List<MSelectItem> getCampaign() {
        return campaign;
    }

    public void setCampaign(List<MSelectItem> campaign) {
        this.campaign = campaign;
    }
}
