package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Mar 27, 2011
 * Time: 3:36:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingListItem implements IsSerializable {

    public static final String REPORTING = "reporting";
    public static final String REPORT = "report";
    public static final String ACTION = "action";
    public static final String TEMPLATE= "Template";



    private Integer reportId;
    private String reportName;
    private Integer templateId;
    private String templateName;
    private String templateBody;
    private String templateType;
    private Integer exceltemplateId;
    private Integer companyId;
    private Boolean library;
    private Boolean custom;
    private Integer order;
    private Integer maxexcelrowcount;


    public ReportingListItem() {

    }

    public ReportingListItem(Integer templateID, String templateName, String templateBody, String templateType) {
        this.templateId = templateID;
        this.templateName = templateName;
        this.templateBody = templateBody;
        this.templateType = templateType;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateBody() {
        return templateBody;
    }

    public void setTemplateBody(String templateBody) {
        this.templateBody = templateBody;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public Integer getExceltemplateId() {
        return exceltemplateId;
    }

    public void setExceltemplateId(Integer exceltemplateId) {
        this.exceltemplateId = exceltemplateId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public boolean isLibrary() {
        return library;
    }

    public void setLibrary(boolean library) {
        this.library = library;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean isCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public void setMaxExcelRowCount(Integer rowcount) {
        this.maxexcelrowcount = rowcount;
    }

    public Integer getMaxRowCount() {
        return this.maxexcelrowcount;

    }
}
