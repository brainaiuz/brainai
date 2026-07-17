package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.PeriodItem;

import javax.persistence.CascadeType;
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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "labor_period_history")
public class EdsLaborPeriodHistory extends EdsSuperUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "creationDate")
    private Date creationDate;

    @Column(name = "text", length = 1000)
    private String text;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "labourPeriodId")
    private EdsLabourPeriod labourPeriod;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser user;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private Boolean deleted;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public EdsLabourPeriod getLabourPeriod() {
        return labourPeriod;
    }

    public void setLabourPeriod(EdsLabourPeriod labourPeriod) {
        this.labourPeriod = labourPeriod;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PeriodItem toRpc() {
        PeriodItem periodItem = new PeriodItem();
        periodItem.setObjectID(getObjectID());
        periodItem.setCreatedDate(getCreationDate());
        periodItem.setText(getText());
        periodItem.setPeriodID(getLabourPeriod() != null ? getLabourPeriod().getObjectID() : null);

        return periodItem;
    }
}
