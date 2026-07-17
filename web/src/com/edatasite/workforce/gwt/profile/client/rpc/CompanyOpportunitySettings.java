package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/12/11
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanyOpportunitySettings implements IsSerializable {
    private boolean isFillOpportunitItems;
    private boolean requireContractUpload;
    private boolean isJoinOpportunityToExpenseClaim;
    private boolean emailAutoLinking;
    private boolean generateCrmAccountNumber;
    private String opportunityNumberingSettings;
    private String prefix;
    private String importPreference;
    private String overwritePreference;
    private SelectItem contactType;
    private Integer contactTypeId;
    private Integer opportunityStageId;
    private Integer opportunitySourceId;
    private String convertsTo;
    private SelectItem stage;
    private SelectItem source;

    public CompanyOpportunitySettings() {
    }

    public boolean isFillOpportunitItems() {
        return isFillOpportunitItems;
    }

    public void setFillOpportunitItems(boolean fillOpportunitItems) {
        isFillOpportunitItems = fillOpportunitItems;
    }

    public boolean isRequireContractUpload() {
        return requireContractUpload;
    }

    public void setRequireContractUpload(boolean requireContractUpload) {
        this.requireContractUpload = requireContractUpload;
    }

    public boolean isJoinOpportunityToExpenseClaim() {
        return isJoinOpportunityToExpenseClaim;
    }

    public void setJoinOpportunityToExpenseClaim(boolean joinOpportunityToExpenseClaim) {
        isJoinOpportunityToExpenseClaim = joinOpportunityToExpenseClaim;
    }

    public boolean isEmailAutoLinking() {
        return emailAutoLinking;
    }

    public void setEmailAutoLinking(boolean emailAutoLinking) {
        this.emailAutoLinking = emailAutoLinking;
    }

    public String getOpportunityNumberingSettings() {
        return opportunityNumberingSettings;
    }

    public void setOpportunityNumberingSettings(String opportunityNumberingSettings) {
        this.opportunityNumberingSettings = opportunityNumberingSettings;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isGenerateCrmAccountNumber() {
        return this.generateCrmAccountNumber;
    }

    public void setGenerateCrmAccountNumber(final boolean generateCrmAccountNumber) {
        this.generateCrmAccountNumber = generateCrmAccountNumber;
    }

    public String getImportPreference() {
        return importPreference;
    }

    public void setImportPreference(String importPreference) {
        this.importPreference = importPreference;
    }

    public String getOverwritePreference() {
        return overwritePreference;
    }

    public void setOverwritePreference(String overwritePreference) {
        this.overwritePreference = overwritePreference;
    }

    public SelectItem getContactType() {
        return contactType;
    }

    public void setContactType(SelectItem contactType) {
        this.contactType = contactType;
    }

    public Integer getContactTypeId() {
        return contactTypeId;
    }

    public void setContactTypeId(Integer contactTypeId) {
        this.contactTypeId = contactTypeId;
    }

    public Integer getOpportunityStageId() {
        return opportunityStageId;
    }

    public void setOpportunityStageId(Integer opportunityStageId) {
        this.opportunityStageId = opportunityStageId;
    }

    public Integer getOpportunitySourceId() {
        return opportunitySourceId;
    }

    public void setOpportunitySourceId(Integer opportunitySourceId) {
        this.opportunitySourceId = opportunitySourceId;
    }

    public String getConvertsTo() {
        return convertsTo;
    }

    public void setConvertsTo(String convertsTo) {
        this.convertsTo = convertsTo;
    }

    public SelectItem getStage() {
        return stage;
    }

    public void setStage(SelectItem stage) {
        this.stage = stage;
    }

    public SelectItem getSource() {
        return source;
    }

    public void setSource(SelectItem source) {
        this.source = source;
    }
}
