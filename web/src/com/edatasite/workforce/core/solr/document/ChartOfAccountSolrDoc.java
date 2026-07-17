package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:24.
 */
@SolrDocument(collection = "chartOfAccountCore")
public class ChartOfAccountSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("accountId")
    @Indexed(name = "accountId", type = "pint", required = true)
    private Integer accountId;

    @Field("code")
    private String code;

    @Field("key")
    private Integer chartOfAccountKey;

    @Field("name")
    private String name;

    @Field("lastUpdatedDate")
    private Date lastUpdatedDate;

    @Field("parentId")
    private Integer parentId;

    @Field("parentName")
    private String parentName;

    @Field("parentIdName")
    @Indexed(name = "parentIdName", type = "string", stored = false)
    private String parentIdName;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("typeId")
    private Integer typeId;

    @Field("typeName")
    private String typeName;

    @Field("typeCode")
    private String typeCode;

    @Field("typeCategory")
    private String typeCategory;

    @Field("typeIdName")
    @Indexed(name = "typeIdName", type = "string", stored = false)
    private String typeIdName;

    @Field("bankAccountActive")
    private Boolean bankAccountActive;

    @Field("showInExpense")
    private Boolean showInExpense;

    @Field("active")
    private Boolean active;

    @Field("enablePayments")
    private Boolean enablePayments;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentIdName() {
        return parentIdName;
    }

    public void setParentIdName(String parentIdName) {
        this.parentIdName = parentIdName;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyIdName() {
        return currencyIdName;
    }

    public void setCurrencyIdName(String currencyIdName) {
        this.currencyIdName = currencyIdName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeCategory() {
        return typeCategory;
    }

    public void setTypeCategory(String typeCategory) {
        this.typeCategory = typeCategory;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }

    public Boolean getBankAccountActive() {
        return bankAccountActive != null && bankAccountActive;
    }

    public void setBankAccountActive(Boolean bankAccountActive) {
        this.bankAccountActive = bankAccountActive;
    }

    public Boolean getShowInExpense() {
        return showInExpense != null && showInExpense;
    }

    public void setShowInExpense(Boolean showInExpense) {
        this.showInExpense = showInExpense;
    }

    public Boolean getEnablePayments() {
        return enablePayments != null && enablePayments;
    }

    public void setEnablePayments(Boolean enablePayments) {
        this.enablePayments = enablePayments;
    }

    public Integer getChartOfAccountKey() {
        return chartOfAccountKey;
    }

    public void setChartOfAccountKey(Integer chartOfAccountKey) {
        this.chartOfAccountKey = chartOfAccountKey;
    }

    public Boolean isActive() {
        return active != null && active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
