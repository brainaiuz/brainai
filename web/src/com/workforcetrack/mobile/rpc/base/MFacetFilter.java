package com.workforcetrack.mobile.rpc.base;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 16.01.12
 * Time: 12:14
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MFacetFilter {

    private String type;
    //Task filters
    private List<Integer> projectID;
    private List<Integer> clientID;
    private List<String> taskStatus;
    private List<String> taskPriority;
    private List<Integer> assigneeID;

    //Case filters
    private List<Integer> statusID;
    private List<Integer> caseOriginID;
    private List<Integer> resolverID;

    //CRM LEAD filters leadSourceID, statusID, countryID, campaignID
    private List<Integer> leadSourceID;
    private List<Integer> countryID;
    private List<Integer> campaignID;

    //Project filters
    private List<Integer> managerID;


    public MFacetFilter() {

    }

    public static List<MSelectItem> getFacetItems(FacetFilterRpc facetFilterRpc, String solrFieldName) {
        SelectItem[] items = facetFilterRpc.getFacetContentMap().get(solrFieldName).getFacetItems();
        return WebServiceUtils.getAsMSelectItemList(items);
    }

    public static ArrayList<String> getColumnCodes(FacetContentType contentType) {
        ArrayList<String> columnCodes = new ArrayList<>(Arrays.asList(contentType.getContentCode()));
        return columnCodes;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getProjectID() {
        return projectID;
    }

    public void setProjectID(List<Integer> projectID) {
        this.projectID = projectID;
    }

    public List<Integer> getClientID() {
        return clientID;
    }

    public void setClientID(List<Integer> clientID) {
        this.clientID = clientID;
    }

    public List<String> getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(List<String> taskStatus) {
        this.taskStatus = taskStatus;
    }

    public List<String> getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(List<String> taskPriority) {
        this.taskPriority = taskPriority;
    }

    public List<Integer> getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(List<Integer> assigneeID) {
        this.assigneeID = assigneeID;
    }

    public List<Integer> getStatusID() {
        return statusID;
    }

    public void setStatusID(List<Integer> statusID) {
        this.statusID = statusID;
    }

    public List<Integer> getCaseOriginID() {
        return caseOriginID;
    }

    public void setCaseOriginID(List<Integer> caseOriginID) {
        this.caseOriginID = caseOriginID;
    }

    public List<Integer> getResolverID() {
        return resolverID;
    }

    public void setResolverID(List<Integer> resolverID) {
        this.resolverID = resolverID;
    }

    public List<Integer> getLeadSourceID() {
        return leadSourceID;
    }

    public void setLeadSourceID(List<Integer> leadSourceID) {
        this.leadSourceID = leadSourceID;
    }

    public List<Integer> getCountryID() {
        return countryID;
    }

    public void setCountryID(List<Integer> countryID) {
        this.countryID = countryID;
    }

    public List<Integer> getCampaignID() {
        return campaignID;
    }

    public void setCampaignID(List<Integer> campaignID) {
        this.campaignID = campaignID;
    }

    public List<Integer> getManagerID() {
        return managerID;
    }

    public void setManagerID(List<Integer> managerID) {
        this.managerID = managerID;
    }
}
