package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08-Jul-2009
 * Time: 18:16:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "campaign")
public class EdsCampaign extends EdsObject {
    //TYPE
    public static final String _CAMPAIGN_TYPE = "_CAMPAIGN_TYPE";
    public static final String CONFERENCE = "CONFERENCE";
    public static final String WEBINAR = "WEBINAR";
    public static final String CT_TRADE_SHOW = "CT_TRADE_SHOW";
    public static final String CT_PUBLIC_RELATIONS = "CT_PUBLIC_RELATIONS";
    public static final String PARTNERS = "PARTNERS";
    public static final String REFERRAL_PROGRAM = "REFERRAL_PROGRAM";
    public static final String CT_ADVERTISEMENT = "CT_ADVERTISEMENT";
    public static final String BANNER_ADS = "BANNER_ADS";
    public static final String DIRECT_MAIL = "DIRECT_MAIL";
    public static final String CT_EMAIL = "CT_EMAIL";
    public static final String TELEMARKETING = "TELEMARKETING";
    public static final String OTHERS = "OTHERS";
    //STATUS
    public static final String _CAMPAIGN_STATUS = "_CAMPAIGN_STATUS";
    public static final String PLANNING = "PLANNING";
    public static final String CS_ACTIVE = "CS_ACTIVE";
    public static final String CS_INACTIVE = "CS_INACTIVE";
    public static final String COMPLATE = "COMPLATE";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaignOwner")
    private EdsUser assignee;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference status;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "expectedRevenue")
    private Double expectedRevenue;

    private Double budgetCost = 0.0;
    private Double actualCost = 0.0;

    @Column(name = "expectedResponse")
    private Double expectedResponse = 0.0;

    @Column(name = "numberSent")
    private String numberSent;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "entity_id")
    private Integer entityID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @Column(name = "createdDate")
    private Date createdDate;

    public CampaignItem getRPC(CampaignItem item, boolean brief) {
        if (item == null) {
            item = new CampaignItem();
        }
        item.setObjectId(getObjectID());
        item.setName(getName());
        item.setStartDate(getStartDate());
        item.setEndDate(getEndDate());
        if (getAssignee() != null) {
            item.setAssigneeId(getAssignee().getObjectID());
            item.setAssignee(getAssignee().getFullName());
        }
        if (getStatus() != null) {
            item.setStatusId(getStatus().getObjectID());
            item.setStatus(getStatus().getName());
            item.setStatusCode(getStatus().getCode());
        }
        if (getType() != null) {
            item.setTypeId(getType().getObjectID());
            item.setType(getType().getName());
            item.setTypeCode(getType().getCode());
        }
        if (brief) {
            item.setActualCost(getActualCost());
            item.setBudgetCost(getBudgetCost());
            item.setExpectedResponse(getExpectedResponse());
            item.setExpectedRevenue(getExpectedRevenue());
            item.setNumberSent(getNumberSent());
        }
        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getAssignee() {
        return assignee;
    }

    public void setAssignee(EdsUser assignee) {
        this.assignee = assignee;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public Double getBudgetCost() {
        return budgetCost;
    }

    public void setBudgetCost(Double budgetCost) {
        this.budgetCost = budgetCost;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public Double getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(Double expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public String getNumberSent() {
        return numberSent;
    }

    public void setNumberSent(String numberSent) {
        this.numberSent = numberSent;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
