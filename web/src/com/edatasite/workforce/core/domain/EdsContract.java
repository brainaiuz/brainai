package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsContractCustomFields;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: faxriddin Taslimov Date: 27.08.2015
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contract")
public class EdsContract extends EdsObject implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "number")
    private String number;

    @Column(name = "allowancebyclient")
    private String allowanceByClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private EdsCrmAccount client;
    @Column(insertable = false, updatable = false)
    private Integer clientId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contractcustomfieldsid", unique = true)
    private EdsContractCustomFields contractCustomFields;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "contract")
    private List<EdsProjectPosition> projectPositions = new ArrayList<>();

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "isaccomodation")
    private Boolean isAccomodation = false;

    @Column(name = "isfood")
    private Boolean isFoot = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;
    @Column(insertable = false, updatable = false)
    private Integer projectId;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "dueDate")
    private Date dueDate;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAllowanceByClient() {
        return allowanceByClient;
    }

    public void setAllowanceByClient(String allowanceByClient) {
        this.allowanceByClient = allowanceByClient;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public List<EdsProjectPosition> getProjectPositions() {
        return projectPositions;
    }

    public void setProjectPositions(List<EdsProjectPosition> projectPositions) {
        this.projectPositions = projectPositions;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getIsAccomodation() {
        return isAccomodation;
    }

    public void setIsAccomodation(Boolean isAccomodation) {
        this.isAccomodation = isAccomodation;
    }

    public Boolean getIsFoot() {
        return isFoot;
    }

    public void setIsFoot(Boolean isFoot) {
        this.isFoot = isFoot;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public EdsContractCustomFields getContractCustomFields() {
        return contractCustomFields;
    }

    public void setContractCustomFields(EdsContractCustomFields contractCustomFields) {
        this.contractCustomFields = contractCustomFields;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
