package com.edatasite.workforce.gwt.reportingsystem.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * User: ${Dilsh0d}
 * Date: 19-Mar-2010
 * Time: 18:41:39
 */
public class FolderRpc implements IsSerializable {

    private Integer id;
    private String name;
    private String type;
    private String companyId;
    private String description;
    private ArrayList<SelectListRpc> reports;
    private RecurrenceJobItem recurrenceJobItem;
    private Integer recurrenceId;
    private String createdBy;
    private Date createdDate;
    private Integer categoryId;
    private String categoryName;
    private String icon;
    private Integer folderSorder;
    private String categoryCode;
    private String code;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<SelectListRpc> getReports() {
        if (reports == null) {
            reports = new ArrayList<>();
        }
        return reports;
    }

    public void setReports(ArrayList<SelectListRpc> reports) {
        this.reports = reports;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public Integer getRecurrenceId() {
        return recurrenceId;
    }

    public void setRecurrenceId(Integer recurrenceId) {
        this.recurrenceId = recurrenceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public Integer getFolderSorder() {
        return folderSorder == null ? 0 : folderSorder;
    }

    public void setFolderSorder(Integer folderSorder) {
        this.folderSorder = folderSorder;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
}
