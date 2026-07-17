package com.edatasite.workforce.gwt.team.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 14, 2008
 * Time: 8:13:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewTeam implements IsSerializable {

    private String departmentCode;
    private NumberData numberData;
    private String name;
    private String email;
    private String description;
    private String shortDescription;
    private HashMap<String,String> descriptionLocale;
    private HashMap<String,String> shortDescriptionLocale;
    private Date startDate;
    private Integer[] members;
    private Integer leader;
    private Integer leader2;
    private Integer leader3;
    private Integer leader4;
    private Integer leader5;
    private SelectItem parent;

    private Boolean active;
    private HashSet<Integer> teamMembers;
    private ReferenceLocale referenceLocale;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private SelectItem location;

    private SelectItem departmentName;

    private Integer departmentfId;

    public Integer getLeader() {
        return leader;
    }

    public void setLeader(Integer leader) {
        this.leader = leader;
    }

    public Integer getLeader2() {
        return leader2;
    }

    public void setLeader2(Integer leader2) {
        this.leader2 = leader2;
    }

    public Integer getLeader3() {
        return leader3;
    }

    public void setLeader3(Integer leader3) {
        this.leader3 = leader3;
    }

    public Integer getLeader4() {
        return leader4;
    }

    public void setLeader4(Integer leader4) {
        this.leader4 = leader4;
    }

    public Integer getLeader5() {
        return leader5;
    }

    public void setLeader5(Integer leader5) {
        this.leader5 = leader5;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer[] getMembers() {
        return members;
    }

    public void setMembers(Integer[] members) {
        this.members = members;
    }

    public SelectItem getParent() {
        return parent;
    }

    public void setParent(SelectItem parent) {
        this.parent = parent;
    }

    public HashSet<Integer> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(HashSet<Integer> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public ReferenceLocale getReferenceLocale() {
        return referenceLocale;
    }

    public void setReferenceLocale(ReferenceLocale referenceLocale) {
        this.referenceLocale = referenceLocale;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(SelectItem departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getDepartmentfId() {
        return departmentfId;
    }

    public void setDepartmentfId(Integer departmentfId) {
        this.departmentfId = departmentfId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public HashMap<String, String> getDescriptionLocale() {
        return descriptionLocale;
    }

    public void setDescriptionLocale(HashMap<String, String> descriptionLocale) {
        this.descriptionLocale = descriptionLocale;
    }

    public HashMap<String, String> getShortDescriptionLocale() {
        return shortDescriptionLocale;
    }

    public void setShortDescriptionLocale(HashMap<String, String> shortDescriptionLocale) {
        this.shortDescriptionLocale = shortDescriptionLocale;
    }
}
