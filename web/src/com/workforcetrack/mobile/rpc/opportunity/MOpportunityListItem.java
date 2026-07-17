package com.workforcetrack.mobile.rpc.opportunity;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/19/11
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MOpportunityListItem {

    private Integer objectID;
    private String opportunityName;
    private MNumberData numberData;
    private Double amount;
    private Date closingDate;
    private Integer assigneeID;
    private String assignee;
    private Integer accountID;
    private String account;
    private Integer contactID;
    private String contact;
    private String stage;
    private Integer stageID;
    private List<MSelectItem> assignees;
    private List<MSelectItem> stages;
    private List<MOpportunityItem> items;

    private Float probability;
    private Double expectedRevenue;

    private Boolean isIncludeItems = true;
    private String leadSource;
    private Integer leadSouceID;
    private String type;
    private Integer typeID;
    private String nextStep;

    public MOpportunityListItem() {

    }

    public MOpportunityListItem(OpportunityListItem opportunityListItem) {
        this.objectID = opportunityListItem.getObjectId();
        this.opportunityName = opportunityListItem.getOpportunityName();
        this.accountID = opportunityListItem.getAccountId();
        this.account = opportunityListItem.getAccount();
        this.assigneeID = opportunityListItem.getAssigneeId();
        this.assignee = opportunityListItem.getAssignee();
        this.contactID = opportunityListItem.getContactId();
        this.contact = opportunityListItem.getContact();
        this.closingDate = opportunityListItem.getClosingDate();
        this.amount = opportunityListItem.getAmount();
        this.numberData = new MNumberData(opportunityListItem.getNumberData());
        this.stage = opportunityListItem.getStage() != null ? opportunityListItem.getStage().getName() : "";
        this.stageID = opportunityListItem.getStageId();
        this.probability = opportunityListItem.getProbability();
        this.expectedRevenue = opportunityListItem.getExpectedRevenue();
//        this.isIncludeItems = opportunityListItem.isIncludeItems();

        if (opportunityListItem.getItems() != null) {
            items = new ArrayList<>();
            for (OpportunityItem item : opportunityListItem.getItems()) {
                items.add(new MOpportunityItem(item));
            }
        }


        if (opportunityListItem.getAssignees() != null) {
            assignees = new ArrayList<>();
            for (SelectItem item : opportunityListItem.getAssignees()) {
                assignees.add(new MSelectItem(item));
            }
        }

        if (opportunityListItem.getStages() != null) {
            stages = new ArrayList<>();
            for (SelectItem item : opportunityListItem.getStages()) {
                stages.add(new MSelectItem(item));
            }
        }

        //FOR OUTLOOK
        this.leadSource = opportunityListItem.getLeadSource();
        this.nextStep = opportunityListItem.getNextStep();
        this.type = opportunityListItem.getType();

    }


    public OpportunityListItem convertToNewOpportunity(OpportunityListItem opportunityListItem) {
        if (opportunityListItem == null) {
            opportunityListItem = new OpportunityListItem();
        }
        if (this.objectID != null) {
            opportunityListItem.setObjectId(this.objectID);
        }

        opportunityListItem.setOpportunityName(this.opportunityName);
        opportunityListItem.setAccountId(this.accountID);
        opportunityListItem.setContactId(this.contactID);
        opportunityListItem.setAssigneeId(this.assigneeID);
        opportunityListItem.setStageId(this.stageID);
        opportunityListItem.setClosingDate(this.closingDate);
        opportunityListItem.setAmount(this.amount);
        opportunityListItem.setNumberData(numberData.convertToNumberData(null));
        opportunityListItem.setProbability(this.probability);
        opportunityListItem.setExpectedRevenue(this.expectedRevenue);
//        opportunityListItem.setIncludeItems(this.isIncludeItems);
        if (items != null) {
            List<OpportunityItem> newOpportunityItems = new ArrayList<>();
            for (MOpportunityItem item : items) {
                newOpportunityItems.add(item.convertToOpportunityItem(null));
            }
            opportunityListItem.setItems(newOpportunityItems.toArray(new OpportunityItem[0]));
        }
        return opportunityListItem;

    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public MNumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(MNumberData numberData) {
        this.numberData = numberData;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Integer getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(Integer assigneeID) {
        this.assigneeID = assigneeID;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getStageID() {
        return stageID;
    }

    public void setStageID(Integer stageID) {
        this.stageID = stageID;
    }

    public List<MSelectItem> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<MSelectItem> assignees) {
        this.assignees = assignees;
    }

    public List<MSelectItem> getStages() {
        return stages;
    }

    public void setStages(List<MSelectItem> stages) {
        this.stages = stages;
    }

    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public List<MOpportunityItem> getItems() {
        return items;
    }

    public void setItems(List<MOpportunityItem> items) {
        this.items = items;
    }

    public Boolean isIncludeItems() {
        return isIncludeItems;
    }

    public void setIncludeItems(Boolean includeItems) {
        isIncludeItems = includeItems;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public Integer getLeadSouceID() {
        return leadSouceID;
    }

    public void setLeadSouceID(Integer leadSouceID) {
        this.leadSouceID = leadSouceID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }
}
