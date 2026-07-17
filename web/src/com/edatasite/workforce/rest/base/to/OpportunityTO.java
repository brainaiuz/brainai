package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 31.11.2016.
 */
public class OpportunityTO implements IsSerializable {

    private Integer id;
    private String name;
    private Long closeDate;
    private String number;
    private Double amount;
    private CurrencyTO currency;

    private UserTO assignee;
    private UserTO backupAssignee;
    private SelectItemTO stage;
    private SelectItemTO type;
    private CrmAccountTO account;
    private ContactTO contact;
    private Float probability;
    private Double expectedRevenue;
    private SelectItemTO campaignSource;
    private SelectItemTO leadSource;
    private ArrayList<OpportunityItemTO> items;

    public OpportunityTO() {
    }

    public OpportunityTO(OpportunityListItem item) {
        this.id = item.getObjectId();
        this.name = item.getOpportunityName();
        this.amount = item.getAmount();
        this.closeDate = WrapUtils.dateToLong(item.getClosingDate());
        if (item.getStage() != null) {
            this.stage = new SelectItemTO(item.getStage().getId(), item.getStage().getName(), item.getStage().getCode(), item.getStage().getDescription());
        }
        this.currency = new CurrencyTO(item.getCurrencyId(), item.getCurrency());
    }

    public OpportunityTO(OpportunityListItem item, boolean isBriefly) {
        this(item);
        this.number = item.getNumberData() != null ? item.getNumberData().getNumberString() : null;
        this.assignee = new UserTO(item.getAssigneeId(), item.getAssignee());
        this.backupAssignee = new UserTO(item.getBackupAssigneeID(), item.getBaseCurrencyName());
        this.type = new SelectItemTO(item.getTypeId(), item.getType());
        this.account = item.getCrmAccountItem().getObjectId() != null ? new CrmAccountTO(item.getCrmAccountItem(), true) : null;
        if (item.getContactId() != null) {
            ContactTO contactTO = new ContactTO();
            contactTO.setId(item.getContactId());
            contactTO.setName(item.getContact());
            contactTO.setPrimaryEmail(item.getContactPrimaryEmail());
            contactTO.setPrimaryPhone(item.getContactPrimaryPhone());
            this.contact = contactTO;
        }
        this.probability = item.getProbability();
        this.expectedRevenue = item.getExpectedRevenue();
        this.campaignSource = new SelectItemTO(item.getCampaignId(), item.getCampaign());
        this.leadSource = new SelectItemTO(item.getLeadSourceId(), item.getLeadSource());
        if (item.getItems() != null && item.getItems().length > 0) {
            ArrayList<OpportunityItemTO> opportunityItems = new ArrayList<>();
            for (OpportunityItem opportunityItem : item.getItems()) {
                opportunityItems.add(new OpportunityItemTO(opportunityItem));
            }
            this.items = opportunityItems;
        }
    }

    public OpportunityListItem wrap(OpportunityTO opportunityTO) {
        OpportunityListItem item = new OpportunityListItem();
        item.setObjectId(opportunityTO.getId());
        item.setOpportunityName(opportunityTO.getName());
        item.setAmount(opportunityTO.getAmount());
        if (opportunityTO.getCurrency() != null) {
            item.setCurrencyId(opportunityTO.getCurrency().getId());
            item.setCurrency(opportunityTO.getCurrency().getName());
        }
        item.setClosingDate(WrapUtils.longToDate(opportunityTO.getCloseDate()));
        if (opportunityTO.getAccount() != null) {
            item.setAccountId(opportunityTO.getAccount().getId());
            item.setAccount(opportunityTO.getAccount().getName());
        }
        if (opportunityTO.getContact() != null) {
            item.setContactId(opportunityTO.getContact().getId());
            item.setContact(opportunityTO.getContact().getName());
        }
        if (opportunityTO.getStage() != null) {
            item.setStageId(opportunityTO.getStage().getId());
        }
        item.setProbability(opportunityTO.getProbability());
        if (opportunityTO.getType() != null) {
            item.setTypeId(opportunityTO.getType().getId());
        }
        item.setExpectedRevenue(opportunityTO.getExpectedRevenue());
        if (opportunityTO.getLeadSource() != null) {
            item.setLeadSourceId(opportunityTO.getLeadSource().getId());
        }
        if (opportunityTO.getCampaignSource() != null) {
            item.setCampaign(opportunityTO.getCampaignSource().getName());
            item.setCampaignId(opportunityTO.getCampaignSource().getId());
        }
        if (opportunityTO.getAssignee() != null) {
            item.setAssigneeId(opportunityTO.getAssignee().getId());
        }
        if (opportunityTO.getBackupAssignee() != null) {
            item.setBackupAssigneeID(opportunityTO.getBackupAssignee().getId());
        }
        if (opportunityTO.getItems() != null && !opportunityTO.getItems().isEmpty()) {
            ArrayList<OpportunityItem> items = new ArrayList<>();
            for (OpportunityItemTO opportunityItemTO : opportunityTO.getItems()) {
                OpportunityItem opportunityItem = opportunityItemTO.wrap(opportunityItemTO);
                if (opportunityItem != null) {
                    items.add(opportunityItem);
                }
            }
            item.setItems(items.toArray(new OpportunityItem[0]));
        }

        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Long closeDate) {
        this.closeDate = closeDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public UserTO getAssignee() {
        return assignee;
    }

    public void setAssignee(UserTO assignee) {
        this.assignee = assignee;
    }

    public UserTO getBackupAssignee() {
        return backupAssignee;
    }

    public void setBackupAssignee(UserTO backupAssignee) {
        this.backupAssignee = backupAssignee;
    }

    public CurrencyTO getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyTO currency) {
        this.currency = currency;
    }

    public SelectItemTO getStage() {
        return stage;
    }

    public void setStage(SelectItemTO stage) {
        this.stage = stage;
    }

    public SelectItemTO getType() {
        return type;
    }

    public void setType(SelectItemTO type) {
        this.type = type;
    }

    public CrmAccountTO getAccount() {
        return account;
    }

    public void setAccount(CrmAccountTO account) {
        this.account = account;
    }

    public ContactTO getContact() {
        return contact;
    }

    public void setContact(ContactTO contact) {
        this.contact = contact;
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

    public SelectItemTO getCampaignSource() {
        return campaignSource;
    }

    public void setCampaignSource(SelectItemTO campaignSource) {
        this.campaignSource = campaignSource;
    }

    public SelectItemTO getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(SelectItemTO leadSource) {
        this.leadSource = leadSource;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ArrayList<OpportunityItemTO> getItems() {
        return items;
    }

    public void setItems(ArrayList<OpportunityItemTO> items) {
        this.items = items;
    }
}
