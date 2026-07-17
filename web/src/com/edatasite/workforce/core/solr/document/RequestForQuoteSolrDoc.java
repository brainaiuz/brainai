package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SolrDocument(collection = "requestForQuoteCore")
public class RequestForQuoteSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("rfqId")
    private Integer rfqId;

    @Field("rfqNumber")
    private String rfqNumber;

    @Field("clientId")
    @Indexed(name = "clientId", type = "pint")
    private Integer clientId;

    @Field("clientName")
    private String clientName;

    @Field("clientIdName")
    @Indexed(name = "clientIdName", type = "string", stored = false)
    private String clientIdName;

    @Field("countryId")
    @Indexed(name = "countryId", type = "pint", stored = false)
    private Integer countryId;

    @Field("countryName")
    private String countryName;

    @Field("countryIdName")
    @Indexed(name = "countryIdName", type = "string", stored = false)
    private String countryIdName;

    @Field("relatedProjectId")
    @Indexed(name = "relatedProjectId", type = "pint", stored = false)
    private Integer relatedProjectId;

    @Field("relatedProjectName")
    private String relatedProjectName;

    @Field("relatedProjectIdName")
    @Indexed(name = "relatedProjectIdName", type = "string", stored = false)
    private String relatedProjectIdName;

    @Field("relatedProjectNumber")
    private String relatedProjectNumber;

    @Field("multiProjectId")
    private List<Integer> multiProjectId = new ArrayList<>();

    @Field("multiProjectName")
    private List<String> multiProjectName = new ArrayList<>();

    @Field("multiProjectIdName")
    private List<String> multiProjectIdName = new ArrayList<>();

    @Field("multiProjectNumber")
    private List<String> multiProjectNumber = new ArrayList<>();

    @Field("multiProjectNumberName")
    private List<String> multiProjectNumberName = new ArrayList<>();


    @Field("statusId")
    @Indexed(name = "statusId", type = "pint", stored = false)
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("statusSorder")
    private Integer statusSorder;

    @Field("statusCode")
    private String statusCode;

    @Field("creatorId")
    @Indexed(name = "creatorId", type = "pint", stored = false)
    private Integer creatorId;

    @Field("creatorName")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("currentApproverId")
    @Indexed(name = "currentApproverId", type = "pint", stored = false)
    private Integer currentApproverId;

    @Field("currentApproverName")
    private String currentApproverName;

    @Field("currentApproverIdName")
    @Indexed(name = "currentApproverIdName", type = "string", stored = false)
    private String currentApproverIdName;

    @Field("itemId")
    private List<Integer> itemId = new ArrayList<>();

    @Field("dueDate")
    private Date dueDate;

    @Field("rfqDate")
    private Date rfqDate;

    @Field("creationDate")
    private Date creationDate;

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

    public Integer getRfqId() {
        return rfqId;
    }

    public void setRfqId(Integer rfqId) {
        this.rfqId = rfqId;
    }

    public String getRfqNumber() {
        return rfqNumber;
    }

    public void setRfqNumber(String rfqNumber) {
        this.rfqNumber = rfqNumber;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientIdName() {
        return clientIdName;
    }

    public void setClientIdName(String clientIdName) {
        this.clientIdName = clientIdName;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryIdName() {
        return countryIdName;
    }

    public void setCountryIdName(String countryIdName) {
        this.countryIdName = countryIdName;
    }

    public Integer getRelatedProjectId() {
        return relatedProjectId;
    }

    public void setRelatedProjectId(Integer relatedProjectId) {
        this.relatedProjectId = relatedProjectId;
    }

    public String getRelatedProjectName() {
        return relatedProjectName;
    }

    public void setRelatedProjectName(String relatedProjectName) {
        this.relatedProjectName = relatedProjectName;
    }

    public String getRelatedProjectIdName() {
        return relatedProjectIdName;
    }

    public void setRelatedProjectIdName(String relatedProjectIdName) {
        this.relatedProjectIdName = relatedProjectIdName;
    }

    public String getRelatedProjectNumber() {
        return relatedProjectNumber;
    }

    public void setRelatedProjectNumber(String relatedProjectNumber) {
        this.relatedProjectNumber = relatedProjectNumber;
    }

    public List<Integer> getMultiProjectId() {
        return multiProjectId;
    }

    public void setMultiProjectId(List<Integer> multiProjectId) {
        this.multiProjectId = multiProjectId;
    }

    public List<String> getMultiProjectName() {
        return multiProjectName;
    }

    public void setMultiProjectName(List<String> multiProjectName) {
        this.multiProjectName = multiProjectName;
    }

    public List<String> getMultiProjectIdName() {
        return multiProjectIdName;
    }

    public void setMultiProjectIdName(List<String> multiProjectIdName) {
        this.multiProjectIdName = multiProjectIdName;
    }

    public List<String> getMultiProjectNumber() {
        return multiProjectNumber;
    }

    public void setMultiProjectNumber(List<String> multiProjectNumber) {
        this.multiProjectNumber = multiProjectNumber;
    }

    public List<String> getMultiProjectNumberName() {
        return multiProjectNumberName;
    }

    public void setMultiProjectNumberName(List<String> multiProjectNumberName) {
        this.multiProjectNumberName = multiProjectNumberName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }

    public Integer getStatusSorder() {
        return statusSorder;
    }

    public void setStatusSorder(Integer statusSorder) {
        this.statusSorder = statusSorder;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorIdName() {
        return creatorIdName;
    }

    public void setCreatorIdName(String creatorIdName) {
        this.creatorIdName = creatorIdName;
    }

    public Integer getCurrentApproverId() {
        return currentApproverId;
    }

    public void setCurrentApproverId(Integer currentApproverId) {
        this.currentApproverId = currentApproverId;
    }

    public String getCurrentApproverName() {
        return currentApproverName;
    }

    public void setCurrentApproverName(String currentApproverName) {
        this.currentApproverName = currentApproverName;
    }

    public String getCurrentApproverIdName() {
        return currentApproverIdName;
    }

    public void setCurrentApproverIdName(String currentApproverIdName) {
        this.currentApproverIdName = currentApproverIdName;
    }

    public List<Integer> getItemId() {
        return itemId;
    }

    public void setItemId(List<Integer> itemId) {
        this.itemId = itemId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getRfqDate() {
        return rfqDate;
    }

    public void setRfqDate(Date rfqDate) {
        this.rfqDate = rfqDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
