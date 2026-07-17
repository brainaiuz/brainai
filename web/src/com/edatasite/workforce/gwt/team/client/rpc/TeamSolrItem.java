package com.edatasite.workforce.gwt.team.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class TeamSolrItem implements IsSerializable {

    private Integer objectId;
    private String number;
    private String name;
    private String nameUz;
    private String nameRu;
    private String nameEn;
    private String nameAr;
    private Date startDate;
    private SelectItem location;
    private SelectItem parentDepartment;
    private SelectItem leader;
    private Boolean leaderIsVacant;
    private String headCount;
    private Boolean statusName;
    private SelectItem createdBy;
    private Date createdDate;
    private SelectItem modifiedBy;
    private Date modifiedDate;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(SelectItem parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public SelectItem getLeader() {
        return leader;
    }

    public void setLeader(SelectItem leader) {
        this.leader = leader;
    }

    public Boolean getLeaderIsVacant() {
        return leaderIsVacant;
    }

    public void setLeaderIsVacant(Boolean leaderIsVacant) {
        this.leaderIsVacant = leaderIsVacant;
    }

    public String getHeadCount() {
        return headCount;
    }

    public void setHeadCount(String headCount) {
        this.headCount = headCount;
    }

    public Boolean getStatusName() {
        return statusName;
    }

    public void setStatusName(Boolean statusName) {
        this.statusName = statusName;
    }

    public SelectItem getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SelectItem createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public SelectItem getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(SelectItem modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
