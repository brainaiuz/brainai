package com.edatasite.workforce.gwt.team.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.BaseListItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TeamListItem extends BaseListItem implements IsSerializable, ListingCustomFields {
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String LEADER_NAME = "leader";
    public static final String LEADER2_NAME = "leader2";
    public static final String LEADER3_NAME = "leader3";
    public static final String LEADER4_NAME = "leader4";
    public static final String LEADER5_NAME = "leader5";
    public static final String DESCRIPTION = "description";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String HEADCOUNT = "headCount";
    public static final String STATUS = "status";
    public static final String PARENT_DEPARTMENT = "parentDepartment";
    public static final String CODE = "code";
    public static final String LOCATION = "locationId";
    public static final String LOCATION_NAME = "locationName";

    public static final String CREATED_DATE = "createdDate";
    public static final String CREATED_BY = "createdBy";
    public static final String MODIFIED_DATE = "modifiedDate";
    public static final String MODIFIED_BY = "modifiedBy";

    private Integer objectID;
    private NumberData numberData;
    private String departmentCode;
    private String externalGUID;
    private String name;
    private String email;

    private Boolean active;
    private String leader;
    private Integer leaderId;
    private String leader2;
    private Integer leaderId2;
    private String leader3;
    private Integer leaderId3;
    private String leader4;
    private Integer leaderId4;
    private String leader5;
    private Integer leaderId5;
    private String description;
    private String shortDescription;
    private HashMap<String, String> descriptionLocale;
    private HashMap<String, String> shortDescriptonLocale;
    private Integer no;
    private Date startDate;
    private Date endDate;
    private SelectItem creator;
    private String headCount;
    private SelectItem[] memberList;
    private ArrayList<EmployeeListItem> membersList;
    private Integer[] members;
    private Integer companyDefaultDepartmentId;
    private SelectItem parentDepartment;
    private Boolean isDefault;
    private ArrayList<Integer> unSelectedEmployees;
    private ReferenceLocale localeItem;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;
    private SelectItem location;

    private Date createdDate;
    private Date modifiedDate;
    private String createdBy;
    private String modifiedBy;

    private Integer teamCid;
    private Integer locationCid;
    private Integer parentCid;
    private Integer numberCid;
    private Integer startDateCid;
    private Integer statusCid;

    private SelectItem departmentName;

    private Integer departmentfId;

    private Integer departmentNameid;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ArrayList<Integer> getUnSelectedEmployees() {
        return unSelectedEmployees;
    }

    public void setUnSelectedEmployees(ArrayList<Integer> unSelectedEmployees) {
        this.unSelectedEmployees = unSelectedEmployees;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
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

    public TeamListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeader2() {
        return leader2;
    }

    public void setLeader2(String leader2) {
        this.leader2 = leader2;
    }

    public Integer getLeaderId2() {
        return leaderId2;
    }

    public void setLeaderId2(Integer leaderId2) {
        this.leaderId2 = leaderId2;
    }

    public String getLeader3() {
        return leader3;
    }

    public void setLeader3(String leader3) {
        this.leader3 = leader3;
    }

    public Integer getLeaderId3() {
        return leaderId3;
    }

    public void setLeaderId3(Integer leaderId3) {
        this.leaderId3 = leaderId3;
    }

    public String getLeader4() {
        return leader4;
    }

    public void setLeader4(String leader4) {
        this.leader4 = leader4;
    }

    public Integer getLeaderId4() {
        return leaderId4;
    }

    public void setLeaderId4(Integer leaderId4) {
        this.leaderId4 = leaderId4;
    }

    public String getLeader5() {
        return leader5;
    }

    public void setLeader5(String leader5) {
        this.leader5 = leader5;
    }

    public Integer getLeaderId5() {
        return leaderId5;
    }

    public void setLeaderId5(Integer leaderId5) {
        this.leaderId5 = leaderId5;
    }

    public Integer getNo() {
        return no;
    }

    public Boolean isActive() {
        return active;
    }

    public Boolean setActive(Boolean active) {
        return this.active = active;
    }


    public void setNo(Integer no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public HashMap<String, String> getShortDescriptonLocale() {
        return shortDescriptonLocale;
    }

    public void setShortDescriptonLocale(HashMap<String, String> shortDescriptonLocale) {
        this.shortDescriptonLocale = shortDescriptonLocale;
    }

    public HashMap<String, String> getDescriptionLocale() {
        return descriptionLocale;
    }

    public void setDescriptionLocale(HashMap<String, String> descriptionLocale) {
        this.descriptionLocale = descriptionLocale;
    }

    public String getHeadCount() {
        return headCount;
    }

    public void setHeadCount(String headCount) {
        this.headCount = headCount;
    }

    public Integer getCompanyDefaultDepartmentId() {
        return companyDefaultDepartmentId;
    }

    public void setCompanyDefaultDepartmentId(Integer companyDefaultDepartment) {
        this.companyDefaultDepartmentId = companyDefaultDepartment;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public SelectItem[] getMemberList() {
        return memberList;
    }

    public void setMemberList(SelectItem[] memberList) {
        this.memberList = memberList;
    }

    public Integer[] getMembers() {
        return members;
    }

    public void setMembers(Integer[] members) {
        this.members = members;
    }

    public ArrayList<EmployeeListItem> getMembersList() {
        return membersList;
    }

    public void setMembersList(ArrayList<EmployeeListItem> membersList) {
        this.membersList = membersList;
    }

    public SelectItem getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(SelectItem parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    @Override
    public Integer getRelationID() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRelationType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRelationName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ReferenceLocale getLocaleItem() {
        return localeItem;
    }

    public void setLocaleItem(ReferenceLocale localeItem) {
        this.localeItem = localeItem;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String empCode) {
        this.departmentCode = empCode;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public Integer getTeamCid() {
        return teamCid;
    }

    public void setTeamCid(Integer teamCid) {
        this.teamCid = teamCid;
    }

    public Integer getLocationCid() {
        return locationCid;
    }

    public void setLocationCid(Integer locationCid) {
        this.locationCid = locationCid;
    }

    public Integer getParentCid() {
        return parentCid;
    }

    public void setParentCid(Integer parentCid) {
        this.parentCid = parentCid;
    }

    public Integer getNumberCid() {
        return numberCid;
    }

    public void setNumberCid(Integer numberCid) {
        this.numberCid = numberCid;
    }

    public Integer getStartDateCid() {
        return startDateCid;
    }

    public void setStartDateCid(Integer startDateCid) {
        this.startDateCid = startDateCid;
    }

    public Integer getStatusCid() {
        return statusCid;
    }

    public void setStatusCid(Integer statusCid) {
        this.statusCid = statusCid;
    }

    public Integer getDepartmentfId() {
        return departmentfId;
    }

    public void setDepartmentfId(Integer departmentfId) {
        this.departmentfId = departmentfId;
    }

    public SelectItem getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(SelectItem departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepartmentNameid() {
        return departmentNameid;
    }

    public void setDepartmentNameid(Integer departmentNameid) {
        this.departmentNameid = departmentNameid;
    }
}
