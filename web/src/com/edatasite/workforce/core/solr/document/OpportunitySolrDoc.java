package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:29.
 */
@SolrDocument(collection = "opportunityCore")
public class OpportunitySolrDoc extends RelationBaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("opportunityId")
    @Indexed(name = "opportunityId", type = "pint", required = true)
    private Integer opportunityId;

    @Field("opportunityIdName")
    @Indexed(name = "opportunityIdName", type = "string", stored = false)
    private String opportunityIdName;

    @Field("opportunityName")
    private String opportunityName;

    @Field("opportunityIntNumber")
    private Integer opportunityIntNumber;

    @Field("opportunityNumber")
    private String opportunityNumber;

    @Field("opportunityStringNumber")
    private String opportunityStringNumber;

    @Field("ownerId")
    private Integer ownerId;

    @Field("ownerIdName")
    @Indexed(name = "ownerIdName", type = "string", stored = false)
    private String ownerIdName;

    @Field("ownerName")
    private String ownerName;

    @Field("assigneeId")
    private Integer assigneeId;

    @Field("assigneeIdName")
    @Indexed(name = "assigneeIdName", type = "string", stored = false)
    private String assigneeIdName;

    @Field("assigneeName")
    private String assigneeName;

    @Field("backupAssigneeId")
    private Integer backupAssigneeId;

    @Field("backupAssigneeIdName")
    @Indexed(name = "backupAssigneeIdName", type = "string", stored = false)
    private String backupAssigneeIdName;

    @Field("backupAssigneeName")
    private String backupAssigneeName;

    @Field("closingDate")
    private Date closingDate;

    @Field("creationDate")
    private Date creationDate;

    @Field("modificationDate")
    private Date modificationDate;

    @Field("creatorId")
    private Integer creatorId;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("creatorName")
    private String creatorName;

    @Field("crmAccountId")
    private Integer crmAccountId;

    @Field("crmAccountIdName")
    @Indexed(name = "crmAccountIdName", type = "string", stored = false)
    private String crmAccountIdName;

    @Field("crmAccountName")
    private String crmAccountName;

    @Field("crmAccountNumber")
    private String crmAccountNumber;

    @Field("crmContactId")
    private Integer crmContactId;

    @Field("crmContactIdName")
    @Indexed(name = "crmContactIdName", type = "string", stored = false)
    private String crmContactIdName;

    @Field("crmContactName")
    private String crmContactName;

    @Field("crmContactPrimaryEmail")
    private String crmContactPrimaryEmail;

    @Field("crmContactEmailAllowed")
    private Boolean crmContactEmailAllowed;

    @Field("crmContactPrimaryPhone")
    private String crmContactPrimaryPhone;

    @Field("opportunityStageId")
    private Integer opportunityStageId;

    @Field("opportunityStageName")
    private String opportunityStageName;

    @Field("opportunityStageCode")
    private String opportunityStageCode;

    @Field("stageUzName")
    private String stageUzName;
    @Field("stageEnName")
    private String stageEnName;

    @Field("stageRuName")
    private String stageRuName;

    @Field("stageArName")
    private String stageArName;


    @Field("opportunityStageIdCode")
    @Indexed(name = "opportunityStageIdCode", type = "string", stored = false)
    private String opportunityStageIdCode;

    @Field("opportunityStageIdCodeName")
    @Indexed(name = "opportunityStageIdCodeName", type = "string", stored = false)
    private String opportunityStageIdCodeName;

    @Field("opportunityStageSorder")
    @Indexed(name = "opportunityStageSorder", type = "pint", stored = false)
    private Integer opportunityStageSorder;

    @Field("opportunityConvertProject")
    private Boolean opportunityConvertProject;

    @Field("convertedFromLead")
    private Boolean convertedFromLead;

    @Field("amount")
    private Double amount;

    @Field("amountBaseCurrency")
    private Double amountBaseCurrency;

    @Field("expectedRevenue")
    private Double expectedRevenue;

    @Field("crmAccountCountryId")
    private Integer crmAccountCountryId;

    @Field("crmAccountCountryIdName")
    @Indexed(name = "crmAccountCountryIdName", type = "string", stored = false)
    private String crmAccountCountryIdName;

    @Field("crmAccountCountryName")
    private String crmAccountCountryName;

    @Field("campaignId")
    private Integer campaignId;

    @Field("campaignIdName")
    @Indexed(name = "campaignIdName", type = "string", stored = false)
    private String campaignIdName;

    @Field("campaignName")
    private String campaignName;

    @Field("currencyId")
    private Integer currencyId;

    @Field("currencyIdName")
    @Indexed(name = "currencyIdName", type = "string", stored = false)
    private String currencyIdName;

    @Field("currencyName")
    private String currencyName;

    @Field("typeId")
    private Integer typeId;

    @Field("typeIdName")
    private String typeIdName;

    @Field("typeName")
    private String typeName;

    @Field("leadSourceId")
    private Integer leadSourceId;

    @Field("leadSourceIdName")
    private String leadSourceIdName;

    @Field("leadSourceName")
    private String leadSourceName;

    @Field("nextStep")
    private String nextStep;

    @Field("probability")
    private Float probability;

    @Field("estimatorId")
    private Integer estimatorId;

    @Field("opportunityKanbanOrder")
    private Long opportunityKanbanOrder;

    @Field("relatedProjectId")
    private Integer relatedProjectId;

    @Field("relatedProjectName")
    private String relatedProjectName;

    @Field("relatedProjectCode")
    private String relatedProjectCode;

    @Field("relatedProjectIdName")
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

    @Field("hasAttachment")
    private Boolean hasAttachment;

    @Field("currentApproverId")
    @Indexed(name = "currentApproverId", type = "pint", stored = false)
    private Integer currentApproverId;

    @Field("currentApproverName")
    private String currentApproverName;

    @Field("currentApproverIdName")
    @Indexed(name = "currentApproverIdName", type = "string", stored = false)
    private String currentApproverIdName;

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

    public Integer getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(Integer opportunityId) {
        this.opportunityId = opportunityId;
    }

    public String getOpportunityIdName() {
        return opportunityIdName;
    }

    public void setOpportunityIdName(String opportunityIdName) {
        this.opportunityIdName = opportunityIdName;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public Integer getOpportunityIntNumber() {
        return opportunityIntNumber;
    }

    public void setOpportunityIntNumber(Integer opportunityIntNumber) {
        this.opportunityIntNumber = opportunityIntNumber;
    }

    public String getOpportunityNumber() {
        return opportunityNumber;
    }

    public void setOpportunityNumber(String opportunityNumber) {
        this.opportunityNumber = opportunityNumber;
    }

    public String getOpportunityStringNumber() {
        return opportunityStringNumber;
    }

    public void setOpportunityStringNumber(String opportunityStringNumber) {
        this.opportunityStringNumber = opportunityStringNumber;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerIdName() {
        return ownerIdName;
    }

    public void setOwnerIdName(String ownerIdName) {
        this.ownerIdName = ownerIdName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeIdName() {
        return assigneeIdName;
    }

    public void setAssigneeIdName(String assigneeIdName) {
        this.assigneeIdName = assigneeIdName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public Integer getBackupAssigneeId() {
        return backupAssigneeId;
    }

    public void setBackupAssigneeId(Integer backupAssigneeId) {
        this.backupAssigneeId = backupAssigneeId;
    }

    public String getBackupAssigneeIdName() {
        return backupAssigneeIdName;
    }

    public void setBackupAssigneeIdName(String backupAssigneeIdName) {
        this.backupAssigneeIdName = backupAssigneeIdName;
    }

    public String getBackupAssigneeName() {
        return backupAssigneeName;
    }

    public void setBackupAssigneeName(String backupAssigneeName) {
        this.backupAssigneeName = backupAssigneeName;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorIdName() {
        return creatorIdName;
    }

    public void setCreatorIdName(String creatorIdName) {
        this.creatorIdName = creatorIdName;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public void setCrmAccountId(Integer crmAccountId) {
        this.crmAccountId = crmAccountId;
    }

    public String getCrmAccountIdName() {
        return crmAccountIdName;
    }

    public void setCrmAccountIdName(String crmAccountIdName) {
        this.crmAccountIdName = crmAccountIdName;
    }

    public String getCrmAccountName() {
        return crmAccountName;
    }

    public void setCrmAccountName(String crmAccountName) {
        this.crmAccountName = crmAccountName;
    }

    public String getCrmAccountNumber() {
        return crmAccountNumber;
    }

    public void setCrmAccountNumber(String crmAccountNumber) {
        this.crmAccountNumber = crmAccountNumber;
    }

    public Integer getCrmContactId() {
        return crmContactId;
    }

    public void setCrmContactId(Integer crmContactId) {
        this.crmContactId = crmContactId;
    }

    public String getCrmContactIdName() {
        return crmContactIdName;
    }

    public void setCrmContactIdName(String crmContactIdName) {
        this.crmContactIdName = crmContactIdName;
    }

    public String getCrmContactName() {
        return crmContactName;
    }

    public void setCrmContactName(String crmContactName) {
        this.crmContactName = crmContactName;
    }

    public String getCrmContactPrimaryEmail() {
        return crmContactPrimaryEmail;
    }

    public void setCrmContactPrimaryEmail(String crmContactPrimaryEmail) {
        this.crmContactPrimaryEmail = crmContactPrimaryEmail;
    }

    public Boolean getCrmContactEmailAllowed() {
        return crmContactEmailAllowed != null && crmContactEmailAllowed;
    }

    public void setCrmContactEmailAllowed(Boolean crmContactEmailAllowed) {
        this.crmContactEmailAllowed = crmContactEmailAllowed;
    }

    public String getCrmContactPrimaryPhone() {
        return crmContactPrimaryPhone;
    }

    public void setCrmContactPrimaryPhone(String crmContactPrimaryPhone) {
        this.crmContactPrimaryPhone = crmContactPrimaryPhone;
    }

    public Integer getOpportunityStageId() {
        return opportunityStageId;
    }

    public void setOpportunityStageId(Integer opportunityStageId) {
        this.opportunityStageId = opportunityStageId;
    }

    public String getOpportunityStageName() {
        return opportunityStageName;
    }

    public void setOpportunityStageName(String opportunityStageName) {
        this.opportunityStageName = opportunityStageName;
    }

    public String getOpportunityStageCode() {
        return opportunityStageCode;
    }

    public void setOpportunityStageCode(String opportunityStageCode) {
        this.opportunityStageCode = opportunityStageCode;
    }

    public String getOpportunityStageIdCode() {
        return opportunityStageIdCode;
    }

    public void setOpportunityStageIdCode(String opportunityStageIdCode) {
        this.opportunityStageIdCode = opportunityStageIdCode;
    }

    public String getOpportunityStageIdCodeName() {
        return opportunityStageIdCodeName;
    }

    public void setOpportunityStageIdCodeName(String opportunityStageIdCodeName) {
        this.opportunityStageIdCodeName = opportunityStageIdCodeName;
    }

    public Integer getOpportunityStageSorder() {
        return opportunityStageSorder;
    }

    public void setOpportunityStageSorder(Integer opportunityStageSorder) {
        this.opportunityStageSorder = opportunityStageSorder;
    }

    public Boolean getOpportunityConvertProject() {
        return opportunityConvertProject != null && opportunityConvertProject;
    }

    public void setOpportunityConvertProject(Boolean opportunityConvertProject) {
        this.opportunityConvertProject = opportunityConvertProject;
    }

    public Boolean getConvertedFromLead() {
        return convertedFromLead != null && convertedFromLead;
    }

    public void setConvertedFromLead(Boolean convertedFromLead) {
        this.convertedFromLead = convertedFromLead;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAmountBaseCurrency() {
        return amountBaseCurrency;
    }

    public void setAmountBaseCurrency(Double amountBaseCurrency) {
        this.amountBaseCurrency = amountBaseCurrency;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public Integer getCrmAccountCountryId() {
        return crmAccountCountryId;
    }

    public void setCrmAccountCountryId(Integer crmAccountCountryId) {
        this.crmAccountCountryId = crmAccountCountryId;
    }

    public String getCrmAccountCountryIdName() {
        return crmAccountCountryIdName;
    }

    public void setCrmAccountCountryIdName(String crmAccountCountryIdName) {
        this.crmAccountCountryIdName = crmAccountCountryIdName;
    }

    public String getCrmAccountCountryName() {
        return crmAccountCountryName;
    }

    public void setCrmAccountCountryName(String crmAccountCountryName) {
        this.crmAccountCountryName = crmAccountCountryName;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignIdName() {
        return campaignIdName;
    }

    public void setCampaignIdName(String campaignIdName) {
        this.campaignIdName = campaignIdName;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyIdName() {
        return currencyIdName;
    }

    public void setCurrencyIdName(String currencyIdName) {
        this.currencyIdName = currencyIdName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getLeadSourceId() {
        return leadSourceId;
    }

    public void setLeadSourceId(Integer leadSourceId) {
        this.leadSourceId = leadSourceId;
    }

    public String getLeadSourceIdName() {
        return leadSourceIdName;
    }

    public void setLeadSourceIdName(String leadSourceIdName) {
        this.leadSourceIdName = leadSourceIdName;
    }

    public String getLeadSourceName() {
        return leadSourceName;
    }

    public void setLeadSourceName(String leadSourceName) {
        this.leadSourceName = leadSourceName;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public Integer getEstimatorId() {
        return estimatorId;
    }

    public void setEstimatorId(Integer estimatorId) {
        this.estimatorId = estimatorId;
    }

    public Long getOpportunityKanbanOrder() {
        return opportunityKanbanOrder;
    }

    public void setOpportunityKanbanOrder(Long opportunityKanbanOrder) {
        this.opportunityKanbanOrder = opportunityKanbanOrder;
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

    public String getRelatedProjectCode() {
        return relatedProjectCode;
    }

    public void setRelatedProjectCode(String relatedProjectCode) {
        this.relatedProjectCode = relatedProjectCode;
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

    public Boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(Boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public String getStageUzName() {
        return stageUzName;
    }

    public void setStageUzName(String stageUzName) {
        this.stageUzName = stageUzName;
    }

    public String getStageEnName() {
        return stageEnName;
    }

    public void setStageEnName(String stageEnName) {
        this.stageEnName = stageEnName;
    }

    public String getStageRuName() {
        return stageRuName;
    }

    public void setStageRuName(String stageRuName) {
        this.stageRuName = stageRuName;
    }

    public String getStageArName() {
        return stageArName;
    }

    public void setStageArName(String stageArName) {
        this.stageArName = stageArName;
    }

    public String getStageLocaleByCode(String code) {
        return switch (code) {
            case "uz" -> getStageUzName();
            case "en" -> getStageEnName();
            case "ru" -> getStageRuName();
            case "ar" -> getStageArName();
            default -> null;
        };
    }
}
