package com.edatasite.workforce.gwt.reportingsystem.server;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by Virus on 9/14/14.
 */
public class ReportItem implements IsSerializable {
    private Object[] item;
    private Integer reportId;
    private String reportName;
    private String reportCode;
    private String templateCode;
    private Boolean isLibrary;
    private Boolean isCustom;
    private String folderName;
    private Integer newcategoryId;
    private String newcategoryName;
    private Integer favouriteId;
    private String reportDescription;
    private Integer xmlTemplateId;
    private Integer categoryId;
    private String categoryName;
    private Boolean fakeReport;
    private String targetLink;
    private Integer folderId;
    private String folderDescription;
    private String templateName;
    private Integer orderNumber;
    private String folderType;
    private Integer folderSorder;
    private String createdBy;
    private Date creationDate;
    private String reportType;
    private String folderIcon;
    private String categoryCode;

    public ReportItem(Object... item) {
        this.item = item;
    }

    public Integer getReportId() {
        return reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public String getReportCode() {
        return reportCode;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public Boolean getIsLibrary() {
        return Boolean.TRUE.equals(isLibrary);
    }

    public Boolean getIsCustom() {
        return Boolean.TRUE.equals(isCustom);
    }

    public String getFolderName() {
        return folderName;
    }

    public Integer getNewcategoryId() {
        return newcategoryId;
    }

    public String getNewcategoryName() {
        return newcategoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public Integer getFavouriteId() {
        return favouriteId;
    }

    public String getReportDescription() {
        return reportDescription == null ? "" : reportDescription;
    }

    public Integer getXmlTemplateId() {
        return xmlTemplateId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getFolderIcon() {
        return folderIcon;
    }

    public void setFolderIcon(String folderIcon) {
        this.folderIcon = folderIcon;
    }

    public ReportItem invoke() {
        reportId = (Integer) item[0];
        reportName = (String) item[1];
        reportCode = (String) item[2];
        templateCode = (String) item[3];
        templateName = (String) item[4];
        isLibrary = (Boolean) item[5];
        isCustom = (Boolean) item[6];
        orderNumber = (Integer) item[7];
        folderId = (Integer) item[8];
        folderName = (String) item[9];
        newcategoryId = (Integer) item[10];
        newcategoryName = (String) item[11];
        favouriteId = (Integer) item[12];
        folderDescription = (String) item[13];
        reportDescription = (String) item[14];
        xmlTemplateId = (Integer) item[15];
        categoryId = (Integer) item[16];
        categoryName = (String) item[17];
        fakeReport = Boolean.TRUE.equals(item[18]);
        targetLink = (String) item[19];
        folderType = (String) item[20];
        createdBy = (String) item[21];
        creationDate = (Date) item[22];
        reportType = (String) item[23];
        folderSorder = (Integer) item[24];
        folderIcon = (String) item[27];
        categoryCode = (String) item[28];
        return this;
    }

    public ListItem toListItem() {
        ListItem item = new ListItem();
        item.setId(reportId);
        item.setName(reportName);
        item.setXmlTemplateId(xmlTemplateId);
        item.setViewCode(templateCode);
        item.setDescription(reportDescription);
        item.setFolderName(folderName);
        return item;
    }

    public SelectListRpc toSelectListRpc() {
        SelectListRpc item = new SelectListRpc();
        item.setId(reportId);
        item.setName(reportName);
//        item.setType(getTableType());
        item.setDescription(reportDescription != null ? reportDescription : "");
        item.setFakeReport(fakeReport);
        item.setTargetLink(targetLink);
        item.setCode(reportCode);
        item.setCategory(newcategoryName);
        item.setCategoryId(newcategoryId);
        item.setCreatedBy(createdBy);
        item.setCreatedDate(creationDate);
        item.setFavourited(getFavouriteId() != null);
        item.setFolder(folderName);
        item.setLibrary(isLibrary);
        return item;
    }

    public FolderRpc getFolder() {
        FolderRpc folder = new FolderRpc();
        folder.setId(folderId);
        folder.setName(folderName);
        folder.setDescription(folderDescription);
        folder.setType(folderType);
        folder.setIcon(folderIcon);
        folder.setFolderSorder(folderSorder);
        return folder;
    }

    public boolean getFakeReport() {
        return Boolean.TRUE.equals(fakeReport);
    }

    public String getTargetLink() {
        return targetLink;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public String getFolderDescription() {
        return folderDescription;
    }

    public String getFolderType() {
        return folderType;
    }

    public String getReportType() {
        return reportType;
    }

    public Integer getFolderSorder() {
        return folderSorder;
    }

    public void setFolderSorder(Integer folderSorder) {
        this.folderSorder = folderSorder;
    }
}