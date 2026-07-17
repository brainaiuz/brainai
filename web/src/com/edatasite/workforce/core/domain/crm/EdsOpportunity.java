package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsProductCategory;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.customform.EdsOpportunityCustomItemTable;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.accounting.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.HistoryLogManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitySolrItem;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 15:14:51
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "opportunity")
public class EdsOpportunity extends EdsApprovable {

    //OPPORTUNITY_STAGE
    public static final String _OPPORTUNITY_STAGE = "_OPPORTUNITY_STAGE";
    public static final String QUALIFICATION = "QUALIFICATION";
    public static final String NEEDS_ANALYSIS = "NEEDS_ANALYSIS";
    public static final String VALUE_PROPOSITION = "VALUE_PROPOSITION";
    public static final String ID_DECISION_MAKERS = "ID_DECISION_MAKERS";
    public static final String PROPOSAL_PRICE_QUOTE = "PROPOSAL/PRICE_QUOTE";
    public static final String NEGOTIATION_REVIEW = "NEGOTIATION/REVIEW";
    public static final String CLOSED_WON = "CLOSED_WON";
    public static final String CLOSED_LOST = "CLOSED_LOST";
    public static final String CLOSED_LOST_TO_COMPETITION = "CLOSED_LOST_TO_COMPETITION";
    //for company : id = ??
    public static final String NEW_STAGE = "NEW_STAGE";

    public static final String ON_TENDER = Constants.ON_TENDER;
    public static final String ESTIMATED = Constants.ESTIMATED;
    public static final String NEEDS_ESTIMATE = Constants.NEEDS_ESTIMATE;

    //OPPORTUNITY TYPE
    public static final String _OPPORTUNITY_TYPE = "_OPPORTUNITY_TYPE";
    public static final String NEW_BUSINESS = "NEW_BUSINESS";
    public static final String EXISTING_BUSINESS = "EXISTING_BUSINESS";

    public static final String APPROVED = "OPPORTUNITY_APPROVED";
    public static final String REJECTED = "OPPORTUNITY_REJECTED";
    public static final String DRAFT = "OPPORTUNITY_DRAFT";
    public static final String SUBMIT = "OPPORTUNITY_SUBMITTED";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leadOwner")
    private EdsUser owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private EdsEmployee assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backup_assignee")
    private EdsEmployee backupAssignee;

    @Column(name = "name")
    private String name;

    @Column(name = "amount")
    private Double amount = 0.0;

    @Column(name = "amount_base_currency")
    private Double amountBaseCurrency = 0.0;

    @Column(name = "closingDate")
    private Date closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccount")
    private EdsCrmAccount crmAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmContact")
    private EdsCrmContact crmContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stage")
    private EdsReference stage;

    //percent
    @Column(name = "probability")
    private Float probability = 0f;

    @Column(name = "nextStep")
    private String nextStep;

    @Column(name = "expectedRevenue")
    private Double expectedRevenue = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leadSource")
    private EdsReference leadSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign")
    private EdsCampaign campaign;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "convertedFromLead", columnDefinition = "boolean default false")
    private Boolean convertedFromLead = false;

    @Column(name = "entity_id")
    private Integer entityID;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsCrmCustomFields customFields;

    @OneToMany(mappedBy = "opportunity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsOpportunityCustomItemTable> itemTables = new HashSet<>();

    @Column(name = "is_converted_to_project")
    private Boolean isConvertedToProject = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "opportunity")
    private List<EdsOpportunityItem> opportunityItems = new ArrayList<>();

    @Column(name = "number")
    private String number;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "importFileID")
    private Integer importFileID;

    @Column(name = "kanban_order")
    private Long kanbanorder;

    /**
     * The subOpportunities in this opportunity. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicalParent", fetch = FetchType.LAZY)
    @OrderBy("objectID desc")
    private List<EdsOpportunity> subOpportunities = new ArrayList<>();

    /**
     * The parent Opportunity of this one.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsOpportunity historicalParent;

    @Column(name = "historical")
    private Boolean historical = false;

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    private transient boolean propertiesChanged = false;

    @Column(name = "comment", length = 5000)
    private String comment;

    @Transient
    private CompanyCustomFieldItem[] transientCustomFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejectReasonId")
    private EdsReference rejectReason;

    @Column(precision = 25, scale = 10)
    private BigDecimal exchangeRate;

    private Integer taxCalculationType;
    private BigDecimal subTotal;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private BigDecimal totalInBase;
    private BigDecimal quantityTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    @Type(type = "text")
    @Column(name = "sap_request_body")
    private String sapRequestBody;

    public void setAssignee(EdsEmployee assignee) {
        if (!ServerUtils.equalsEdsObject(this.assignee, assignee)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("assignee", "Assignee"), this.assignee != null ? this.assignee.getName() : "", assignee != null ? assignee.getName() : "");
        }
        this.assignee = assignee;
    }


    private Date approvedDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'OPPORTUNITY'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    public void setBackupAssignee(EdsEmployee backupAssignee) {
        if (!ServerUtils.equalsEdsObject(this.backupAssignee, backupAssignee)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("backupAssignee", "Backup Assignee"), this.backupAssignee != null ? this.backupAssignee.getName() : "", backupAssignee != null ? backupAssignee.getName() : "");
        }
        this.backupAssignee = backupAssignee;
    }

    public EdsProject getProject() {
        return this.project;
    }

    public void setProject(final EdsProject project) {
        if (!ServerUtils.equalsEdsObject(this.project, project)) {
            addChange(CustomFormConstants.PROJECT_FIELD);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("project", "Project"), this.project != null ? this.project.getName() : "", project != null ? project.getName() : "");
        }
        this.project = project;
    }

    public void setName(String name) {
        if (!ServerUtils.equalsString(this.name, name)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_NAME);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("name", "Name"), this.name != null ? this.name : "", name != null ? name : "");
        }
        this.name = name;
    }

    public void setAmountBaseCurrency(Double amountBaseCurrency) {
        this.amountBaseCurrency = amountBaseCurrency;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        if (!ServerUtils.equalsEdsObject(this.crmAccount, crmAccount)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("customer", "Customer"), this.crmAccount != null ? this.crmAccount.getName() : "", crmAccount != null ? crmAccount.getName() : "");
        }
        this.crmAccount = crmAccount;
    }

    public void setCrmContact(EdsCrmContact crmContact) {
        if (!ServerUtils.equalsEdsObject(this.crmContact, crmContact)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("contact", "Contact"), this.crmContact != null ? this.crmContact.getName() : "", crmContact != null ? crmContact.getName() : "");
        }
        this.crmContact = crmContact;
    }

    public void setType(EdsReference type) {
        if (!ServerUtils.equalsEdsObject(this.type, type)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_TYPE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("type", "Type"), this.type != null ? this.type.getName() : "", type != null ? type.getName() : "");
        }
        this.type = type;
    }

    public void setNextStep(String nextStep) {
        if (!ServerUtils.equalsString(this.nextStep, nextStep)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("nextStep", "Next Step"), this.nextStep != null ? this.nextStep : "", nextStep != null ? nextStep : "");
        }
        this.nextStep = nextStep;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        if (!ServerUtils.equalsDouble(this.expectedRevenue, expectedRevenue)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("expectedRevenue", "Expected Revenue"), this.expectedRevenue != null ? this.expectedRevenue : 0.0, expectedRevenue != null ? expectedRevenue : 0.0);
        }
        this.expectedRevenue = expectedRevenue;
    }

    public void setLeadSource(EdsReference leadSource) {
        if (!ServerUtils.equalsEdsObject(this.leadSource, leadSource)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("source", "Source"), this.leadSource != null ? this.leadSource.getName() : "", leadSource != null ? leadSource.getName() : "");
        }
        this.leadSource = leadSource;
    }

    public void setCampaign(EdsCampaign campaign) {
        if (!ServerUtils.equalsEdsObject(this.campaign, campaign)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("campaign", "Campaign"), this.campaign != null ? this.campaign.getName() : "", campaign != null ? campaign.getName() : "");
        }
        this.campaign = campaign;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setConvertedFromLead(Boolean convertedFromLead) {
        this.convertedFromLead = convertedFromLead;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public void setConvertedToProject(Boolean convertedToProject) {
        isConvertedToProject = convertedToProject;
    }

    public void setOpportunityItems(List<EdsOpportunityItem> opportunityItems) {
        this.opportunityItems = opportunityItems;
    }

    public void setNumber(String number) {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.CRM_OPPORTUNITY_NUMBER);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("number", "Number"), this.number != null ? this.number : "", number != null ? number : "");
        }
        this.number = number;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public void setKanbanorder(Long kanbanorder) {
        this.kanbanorder = kanbanorder;
    }

    public void setSubOpportunities(List<EdsOpportunity> subOpportunities) {
        this.subOpportunities = subOpportunities;
    }

    public void setHistoricalParent(EdsOpportunity historicalParent) {
        this.historicalParent = historicalParent;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public void setPropertiesChanged(boolean propertiesChanged) {
        this.propertiesChanged = propertiesChanged;
    }

    public void setNote(String note) {
        if (!ServerUtils.equalsString(this.comment, note)) {
            addChange("CRM_NOTE");
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("CRM_NOTE", "CRM_NOTE"), this.comment != null ? this.comment : "", note != null ? note : "");
        }
        this.comment = note;
    }

    public String getNote() {
        return comment;
    }

    public void setTransientCustomFields(CompanyCustomFieldItem[] transientCustomFields) {
        this.transientCustomFields = transientCustomFields;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public void setRejectReason(EdsReference rejectReason) {
        this.rejectReason = rejectReason;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setAmount(Double amount) {
        Double oldAmount = this.amount;
        this.amount = amount;
        if (!ServerUtils.equalsDouble(amount, oldAmount)) {
            propertiesChanged = true;
            addChange(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("amount", "Amount"), oldAmount != null ? oldAmount : 0.0, amount != null ? amount : 0.0);
        }
    }

    public void setClosingDate(Date closingDate) {
        Date oldDate = this.closingDate;
        this.closingDate = closingDate;
        if (!ServerUtils.equalsDate(closingDate, oldDate)) {
            propertiesChanged = true;
            addChange(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("closeDate", "Close Date"), oldDate, closingDate);
        }
    }

    public void setStage(EdsReference stage) {
        EdsReference oldStage = this.stage;
        this.stage = stage;
        if (stage != null && stage.getDescription() != null && stage.getDescription().matches(Constants.REGEX_INTEGER)) {
            try {
                setProbability(Float.valueOf(stage.getDescription()));
                if (getProbability() != null && getProbability() > 0) {
                    setExpectedRevenue((getAmount() == null ? 0d : getAmount()) * (getProbability() / 100f));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if (!ServerUtils.equalsReference(oldStage, stage)) {
            propertiesChanged = true;
            addChange(CustomFormConstants.CRM_OPPORTUNITY_STAGE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("stage", "Stage"), oldStage != null ? oldStage.getName() : "", stage != null ? stage.getName() : "");
        }
    }

    public void setProbability(Float probability) {
        Float oldProbability = this.probability;
        this.probability = probability;
        if (!ServerUtils.equalsFloat(oldProbability, probability)) {
            propertiesChanged = true;
            addChange(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("probability", "Probability"), oldProbability != null ? oldProbability : 0f, probability != null ? probability : 0f);
        }
    }

    public Set<EdsProject> getProjects() {
        Set<EdsProject> projects = new HashSet<>();

        if (getOpportunityItems() != null && !getOpportunityItems().isEmpty()) {
            for (EdsOpportunityItem item : getOpportunityItems()) {
                if (item.getProject() != null) {
                    projects.add(item.getProject());
                }
            }
        }

        return projects;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public Date getCreationDate() {
        return getAuditInfo().getCreationDate();
    }

    @Override
    public String getDomainType() {
        return RelationItem.TYPE_OPPORTUNITY;
    }

    public OpportunityListItem getRPC(OpportunityListItem item) {
        if (item == null) {
            item = new OpportunityListItem();
        }
        item.setObjectId(getObjectID());

        if (item.getNumberData() != null) {
            item.getNumberData().setIntNumber(intNumber);
            item.getNumberData().setNumberString(number);
            if (number != null && intNumber != null) {
                int prefixLenth = number.length() - (intNumber + "").length();
                try {
                    item.getNumberData().setFirstNumberString(number.substring(0, prefixLenth));
                } catch (NumberFormatException e) {
                    item.getNumberData().setIntNumber(intNumber);
                }
                try {
                    item.getNumberData().setIntNumber(Integer.valueOf(number.substring(prefixLenth)));
                } catch (NumberFormatException e) {
                    item.getNumberData().setIntNumber(intNumber);
                }
                try {
                    item.getNumberData().setNumberFormat(number.substring(0, prefixLenth) + "_" + intNumber);
                } catch (Exception ignored) {

                }
            }
        } else {
            NumberData nd = new NumberData(number, intNumber);
            if (number != null && intNumber != null) {
                nd.setFirstNumberString(number.replaceAll(intNumber + "", ""));
            }
            item.setNumberData(nd);
        }
        item.setOpportunityName(getName());
        item.setAmount(getAmount());
        if (getCurrency() != null) {
            item.setCurrency(getCurrency().getName());
            item.setCurrencyId(getCurrency().getObjectID());
        }
        item.setExchangeRate(getExchangeRate());
        item.setClosingDate(getClosingDate());
        item.setCreatedDate(getCreationDate());
        item.setNextStep(getNextStep());
        item.setProbability(getProbability());
        item.setExpectedRevenue(getExpectedRevenue());
        item.setConvertedToProject(isConvertedToProject != null ? isConvertedToProject : false);

//        initApproverData(item);

        if (getStage() != null) {
            item.setStage(getStage().getRPC());
            item.setStageCode(getStage().getCode());
        }

        if (getAssignee() != null) {
            item.setAssigneeId(getAssignee().getObjectID());
            item.setAssignee(getAssignee().getFullName());
        }
        if (getBackupAssignee() != null) {
            item.setBackupAssigneeID(getBackupAssignee().getObjectID());
            item.setBackupAssignee(getBackupAssignee().getFullName());
        }
        if (getProject() != null) {
            item.setProject(new SelectItem(getProject().getObjectID(), getProject().getNumber() != null ? getProject().getNumber() + " -> " + getProject().getName() : getProject().getName()));
        }
        if (getCrmAccount() != null && !getCrmAccount().isDeleted()) {
            item.getCrmAccountItem().setObjectId(getCrmAccount().getObjectID());
            item.getCrmAccountItem().setName(getCrmAccount().getName());
            item.getCrmAccountItem().setNumber(getCrmAccount().getNumber());
            item.getCrmAccountItem().setEmail(getCrmAccount().getEmail());
            item.getCrmAccountItem().setFax(getCrmAccount().getFax());
            item.getCrmAccountItem().setPhone(getCrmAccount().getPhone());
        }
        if (getCrmContact() != null) {
            item.setContactId(getCrmContact().getObjectID());
            item.setContact(getCrmContact().getName());
            item.setContactPrimaryEmail(getCrmContact().getPrimaryEmail());
            item.setContactEmailOptOut(getCrmContact().getEmailOptOut() != null ? getCrmContact().getEmailOptOut() : false);
            item.setContactPrimaryPhone(getCrmContact().getPrimaryPhone() != null ? getCrmContact().getPrimaryPhone() : getCrmAccount() != null ? getCrmAccount().getPhone() : null);
        }
        if (getType() != null) {
            item.setTypeId(getType().getObjectID());
            item.setType(getType().getName());
        }
        if (getLeadSource() != null) {
            item.setLeadSourceId(getLeadSource().getObjectID());
            item.setLeadSource(getLeadSource().getName());
        }
        if (getCampaign() != null) {
            item.setCampaignId(getCampaign().getObjectID());
            item.setCampaign(getCampaign().getName());
        }
        if (getAuditInfo() != null) {
            item.setAuditInfoResource(getAuditInfo().getDTO());
        }
        if (getConvertedFromLead() != null) {
            item.setConvertedLead(getConvertedFromLead());
        }
        item.setTaxCalculationType(getTaxCalculationType());
        item.setSubTotal(getSubTotal());
        item.setDiscountTotal(getDiscountTotal());
        item.setTaxTotal(getTaxTotal());
        item.setTotal(getTotal());
        item.setTotalInBase(getTotalInBase());
        item.setQuantityTotal(getQuantityTotal());

        item.setApprovers(getMiniApproversRPC());
        if (isOk(getCurrentApprover())) {
            item.setCurrentApprover(getCurrentApprover().getRPC());
        }
        if (isOk(getPrevApprover())) {
            item.setPrevApprover(getPrevApprover().getRPC());
        }
        if (isOk(getOverallStatus())) {
            item.setOverallStatus(getOverallStatus().getRPC());
        }
        if (getOverallStatus() != null) {
            item.setOverallStatus(getOverallStatus().getRPC());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            item.setApproverEmployee(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        return item;
    }

    public OpportunitySolrItem getOpportunitySolrRPC() {
        OpportunitySolrItem opportunitySolrItem = new OpportunitySolrItem();

        SelectItem opportunity = new SelectItem(getObjectID(), getName());
        opportunity.setNumber(getNumber());
        opportunitySolrItem.setOpportunity(opportunity);
        opportunitySolrItem.setOpportunityStringNumber(getNumber());
        opportunitySolrItem.setOpportunityIntNumber(getIntNumber());
        if (getOwner() != null) {
            opportunitySolrItem.setOwner(getOwner().getAsSelectItem());
        }
        if (getAssignee() != null) {
            opportunitySolrItem.setAssignee(getAssignee().getAsSelectItem());
        }
        if (getBackupAssignee() != null) {
            opportunitySolrItem.setBackupAssignee(getBackupAssignee().getAsSelectItem());
        }
        opportunitySolrItem.setClosingDate(getClosingDate());
        if (getAuditInfo() != null) {
            opportunitySolrItem.setCreationDate(getAuditInfo().getCreationDate());
            opportunitySolrItem.setModificationDate(getAuditInfo().getModificationDate());
        }
        if (getAuditInfo() != null && getAuditInfo().getCreatedBy() != null) {
            opportunitySolrItem.setCreator(getAuditInfo().getCreatedBy().getAsSelectItem());
        }
        if (getCrmAccount() != null) {
            opportunitySolrItem.setCrmAccount(getCrmAccount().getAsSelectItem());

            EdsAddress address = getCrmAccount().getBillingAddress();
            if (address != null && address.getCountry() != null) {
                opportunitySolrItem.setCrmAccountCountry(address.getCountry().getAsSelectItem());
            }
        }
        if (getCrmContact() != null) {
            opportunitySolrItem.setCrmContact(getCrmContact().getAsSelectItem());
            opportunitySolrItem.setCrmContactPrimaryEmail(getCrmContact().getPrimaryEmail());
            opportunitySolrItem.setCrmContactEmailAllowed(getCrmContact().getEmailOptOut());
            opportunitySolrItem.setCrmContactPrimaryPhone(getCrmContact().getPrimaryPhone());
        }
        if (getStage() != null) {
            opportunitySolrItem.setOpportunityStage(getStage().getAsSelectItem());
            if (getStage().getLocale() != null) {
                ReferenceLocale locale = getStage().getLocale().toRPC();
                opportunitySolrItem.setStageLocale(locale);
            }
        }
        opportunitySolrItem.setOpportunityConvertProject(getConvertedToProject());
        opportunitySolrItem.setConvertedFromLead(getConvertedFromLead());
        opportunitySolrItem.setAmount(getAmount());
        opportunitySolrItem.setAmountBaseCurrency(getAmountBaseCurrency());
        opportunitySolrItem.setExpectedRevenue(getExpectedRevenue());
        if (getCampaign() != null) {
            opportunitySolrItem.setCampaign(getCampaign().getAsSelectItem());
        }
        if (getCurrency() != null) {
            opportunitySolrItem.setCurrency(getCurrency().getAsSelectItem());
        }
        if (getType() != null) {
            opportunitySolrItem.setType(getType().getAsSelectItem());
        }
        if (getLeadSource() != null) {
            opportunitySolrItem.setLeadSource(getLeadSource().getAsSelectItem());
        }
        opportunitySolrItem.setNextStep(getNextStep());
        opportunitySolrItem.setProbability(getProbability());
        opportunitySolrItem.setOpportunityKanbanOrder(getKanbanorder());

        if (getProject() != null) {
            SelectItem project = getProject().getAsSelectItem();
            project.setNumber(project.getNumber());
            if (getProject().getStatus() != null) {
                project.setCode(getProject().getStatus().getCode());
            }
            opportunitySolrItem.setRelatedProject(project);
        }

        if (getProjects() != null && getProjects().size() > 0) {
            for (EdsProject project : getProjects()) {
                SelectItem relProject = new SelectItem(project.getObjectID(), project.getName());
                relProject.setNumber(project.getNumber());
                opportunitySolrItem.getMultiProject().add(relProject);
            }
        }

        return opportunitySolrItem;
    }

    public DiscountItem[] getProductDiscounts(List<EdsDiscount> discounts) {
        List<DiscountItem> discountItems = new ArrayList<>();
        if (discounts != null && discounts.size() > 0) {
            discountItems.add(new DiscountItem(Constants.ONE_OFF_DISCOUNT, Constants.ONE_OFF_DISCOUNT_STR));
            discountItems.add(new DiscountItem(Constants.ONE_OFF_FIXED_AMOUNT, Constants.ONE_OFF_FIXED_AMOUNT_STR));

            for (EdsDiscount discount : discounts) {
                if (!Constants.MULTI_RANGE_DISCOUNT.equals(discount.getType())) {
                    DiscountItem discountItem = new DiscountItem();
                    discountItem.setId(discount.getObjectID());
                    discountItem.setName(discount.getName());
                    discountItem.setCode(discount.getCode());
                    discountItem.setDescription(discount.getDescription());
                    discountItem.setActive(discount.isActive());
                    discountItem.setType(discount.getType());
                    discountItem.setPercentage(discount.getPercentage());
                    discountItem.setFixedAmount(discount.getFixedAmount());
                    discountItems.add(discountItem);
                }
            }
        }
        return discountItems.toArray(new DiscountItem[0]);
    }

    public SolrInputDocument getAsSolrDocument(Integer companyID, List<EdsRelation> relationList, String locale, List<Integer> opportunityAttachmentsIds) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrOpportunityRepresenter.FIELD_COMPOSITE_ID, companyID + "_" + getObjectID());
        doc.addField(SolrOpportunityRepresenter.FIELD_COMPANY_ID, Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID, getObjectID());
        doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME, getName());
        doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID_NAME, getObjectID() + SolrOpportunityRepresenter.SPLIT + getName());
        doc.addField(SolrOpportunityRepresenter.HAS_ATTACHMENT, opportunityAttachmentsIds.contains(getObjectID()));
        //Kanban view Order
        doc.addField(SolrOpportunityRepresenter.OPPORTUNITY_KANBAN_ORDER, getKanbanorder());

        if (getNumber() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NUMBER, getNumber());
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STRING_NUMBER, getNumber());
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_INT_NUMBER, getIntNumber());
        }
        if (getOwner() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_OWNER_ID, getOwner().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_OWNER_NAME, getOwner().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_OWNER_ID_NAME, getOwner().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getOwner().getName());
        }
        if (getAssignee() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID, getAssignee().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_ASSIGNEE_NAME, getAssignee().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID_NAME, getAssignee().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getAssignee().getName());
        }
        if (getBackupAssignee() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID, getBackupAssignee().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_NAME, getBackupAssignee().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID_NAME, getBackupAssignee().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getBackupAssignee().getName());
        }
        if (getClosingDate() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_CLOSING_DATE, getClosingDate());
        }
        if (getCrmAccount() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID, getCrmAccount().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_NAME, getCrmAccount().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_NUMBER, getCrmAccount().getNumber());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID_NAME, getCrmAccount().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getCrmAccount().getName());

            EdsAddress address = getCrmAccount().getBillingAddress();
            if (address != null && address.getCountry() != null) {
                doc.addField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_ID, address.getCountry().getObjectID());
                doc.addField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_NAME, address.getCountry().getName());
                doc.addField(SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_ID_NAME, address.getCountry().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + address.getCountry().getName());
            }
        }
        if (getCrmContact() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_ID, getCrmContact().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_NAME, getCrmContact().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_ID_NAME, getCrmContact().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getCrmContact().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_PRIMARY_EMAIL, getCrmContact().getPrimaryEmail());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_EMAIL_ALLOWED, getCrmContact().getEmailOptOut());
            doc.addField(SolrOpportunityRepresenter.FIELD_CRM_CONTACT_PRIMARY_PHONE, getCrmContact().getPrimaryPhone());
        }
        if (getCampaign() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID, getCampaign().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_CAMPAIGN_NAME, getCampaign().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID_NAME, getCampaign().getObjectID() + SolrOpportunityRepresenter.SPLIT + getCampaign().getName());
        }
        if (getStage() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID, getStage().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_NAME, getStage().getOriginalName());
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_CODE, getStage().getCode());
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID_CODE, getStage().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getStage().getCode());
            if (getStage().getLocale() != null) {
                EdsReferenceLocale localedName = getStage().getLocale();
                doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_UZ_NAME, localedName.getUzbek());
                doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_EN_NAME, localedName.getEnglish());
                doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_AR_NAME, localedName.getArabic());
                doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_RU_NAME, localedName.getRussian());
            }
        }
        if (getAmount() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_AMOUNT, getAmount());
        }
        if (getAmountBaseCurrency() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY, getAmountBaseCurrency());
        }
        if (getCurrency() != null) {
            doc.addField(SolrCrmAccountRepresenter.FIELD_CURRENCY_NAME, getCurrency().getName());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CURRENCY_ID, getCurrency().getObjectID());
            doc.addField(SolrCrmAccountRepresenter.FIELD_CURRENCY_ID_NAME, SolrUtils.getIdName(getCurrency()));
        }
        if (getConvertedToProject() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_CONVERT_PROJECT, getConvertedToProject());
        }
        if (getExpectedRevenue() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_EXPECTED_REVENUE, getExpectedRevenue());
        }

        if (getLeadSource() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_ID, getLeadSource().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_NAME, getLeadSource().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_ID_NAME, getLeadSource().getObjectID() + SolrOpportunityRepresenter.SPLIT + getLeadSource().getName());
        }

        if (getType() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_TYPE_ID, getType().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_TYPE_NAME, getType().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_TYPE_ID_NAME, getType().getObjectID() + SolrOpportunityRepresenter.SPLIT + getType().getName());
        }
        if (getNextStep() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_NEXT_STEP, getNextStep());
        }
        if (getProbability() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_PROBABILITY, getProbability());
        }
        if (getAuditInfo() != null && getAuditInfo().getCreatedBy() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_MODIFICATION_DATE, getAuditInfo().getModificationDate());
            doc.addField(SolrOpportunityRepresenter.FIELD_CREATION_DATE, getAuditInfo().getCreationDate());
            doc.addField(SolrOpportunityRepresenter.FIELD_CREATOR_ID, getAuditInfo().getCreatedBy().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_CREATOR_NAME, getAuditInfo().getCreatedBy().getFullName());
            doc.addField(SolrOpportunityRepresenter.FIELD_CREATOR_ID_NAME, getAuditInfo().getCreatedBy().getObjectID() + SolrOpportunityRepresenter.SPLIT + getAuditInfo().getCreatedBy().getFullName());
        }
        if (getConvertedFromLead() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_CONVERTED_FROM_LEAD, getConvertedFromLead());
        }
        if (getProject() != null) {
            doc.addField(SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_ID, getProject().getObjectID());
            doc.addField(SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_NAME, getProject().getName());
            doc.addField(SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_NUMBER, getProject().getNumber());
            if (getProject().getStatus() != null) {
                doc.addField(SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_CODE, getProject().getStatus().getCode());
            }
            doc.addField(SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_ID_NAME, getProject().getObjectID() + SolrOpportunityRepresenter.SPLIT + getProject().getName());
        }

        if (getProjects() != null && getProjects().size() > 0) {
            for (EdsProject project : getProjects()) {
                doc.addField(SolrOpportunityRepresenter.FIELD_MULTI_PROJECT_ID, project.getObjectID());
                doc.addField(SolrOpportunityRepresenter.FIELD_MULTI_PROJECT_NAME, project.getName());
                doc.addField(SolrOpportunityRepresenter.FIELD_MULTI_PROJECT_NUMBER, project.getNumber());
                doc.addField(SolrOpportunityRepresenter.FIELD_MULTI_PROJECT_ID_NAME, project.getObjectID() + SolrOpportunityRepresenter.SPLIT + project.getName());
                doc.addField(SolrOpportunityRepresenter.FIELD_MULTI_PROJECT_NUMBER_NAME, project.getNumber() + SolrOpportunityRepresenter.ARROW + project.getName());
            }
        }

        SolrRelationUtils.addToSolrRelations(doc, relationList, EdsRelation.TYPE_OPPORTUNITY);
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE)) {
                setAssignee((EdsEmployee) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE)) {
                setBackupAssignee((EdsEmployee) value);
            } else if (fieldID.equals(CustomFormConstants.PROJECT_FIELD)) {
                setProject((EdsProject) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_NUMBER)) {
                setNumber((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_NAME)) {
                setName((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME)) {
                setCrmAccount((EdsCrmAccount) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME)) {
                setCrmContact((EdsCrmContact) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_TYPE)) {
                setType((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP)) {
                setNextStep((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT)) {
                try {
                    setAmount(Double.valueOf((String) value));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE)) {
                if (value instanceof Date) {
                    setClosingDate((Date) value);
                } else {
                    final Calendar cal = Calendar.getInstance();
                    String condition = (String) value;
                    if ("Yesterday".equals(condition)) {
                        cal.add(Calendar.DATE, -1);
                        setClosingDate(cal.getTime());
                    } else {
                        setClosingDate(cal.getTime());
                    }
                }
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_STAGE)) {
                setStage((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY)) {
                setProbability((Float) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE)) {
                setExpectedRevenue((Double) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE)) {
                setCampaign((EdsCampaign) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE)) {
                setLeadSource((EdsReference) value);
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, fieldID);
            }
            addChange(fieldID);
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }


    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        String[] values = fieldID.split(",");
        fieldID = values.length >= 2 ? values[0] : fieldID;
        if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_ASSIGNEE)) {
            return getAssignee();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_BACKUP_ASSIGNEE)) {
            return getBackupAssignee();
        } else if (fieldID.equals(CustomFormConstants.PROJECT_FIELD)) {
            return getProject();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_NAME)) {
            return getName();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_ACCOUNT_NAME)) {
            return getCrmAccount();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_CONTACT_NAME)) {
            return getCrmContact();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_TYPE)) {
            return getType();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_NEXT_STEP)) {
            return getNextStep();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_AMOUNT)) {
            return getAmount();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_CLOSING_DATE)) {
            return getClosingDate();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_STAGE)) {
            return getStage();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_PROBABILITY)) {
            return getProbability();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_EXPECTED_REVENUE)) {
            return getExpectedRevenue();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_CAMPAIGN_SOURCE)) {
            return getCampaign();
        } else if (fieldID.equals(CustomFormConstants.CRM_OPPORTUNITY_LEAD_SOURCE)) {
            return getLeadSource();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getModificationDate() == null ? new Date() : getModificationDate();
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_ITEM)) {
            List<EdsItem> edsItems = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                edsItems.add(edsOpportunityItem.getItem());
            }
            return edsItems;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_BRAND)) {
            List<EdsBrand> edsBrands = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                edsBrands.add(edsOpportunityItem.getBrand());
            }
            return edsBrands;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_CATEGORY)) {
            List<EdsProductCategory> edsProductCategories = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                edsProductCategories.add(edsOpportunityItem.getCategory());
            }
            return edsProductCategories;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_DESCRIPTION)) {
            List<String> descriptions = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                descriptions.add(edsOpportunityItem.getDescription());
            }
            return descriptions;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_DISCOUNT)) {
            List<BigDecimal> discounts = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                discounts.add(edsOpportunityItem.getDiscount());
            }
            return discounts;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_MEASUREMENT)) {
            List<EdsUnitMeasurement> measurements = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                measurements.add(edsOpportunityItem.getUnitMeasurement());
            }
            return measurements;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_PRICE)) {
            List<BigDecimal> prices = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                prices.add(edsOpportunityItem.getPrice());
            }
            return prices;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_SUPPLIER)) {
            List<String> suppliers = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                suppliers.add(edsOpportunityItem.getSupplierName());
            }
            return suppliers;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_TOTAL_AMOUNT)) {
            List<BigDecimal> totals = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                totals.add(edsOpportunityItem.getSubTotal());
            }
            return totals;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_QTY)) {
            List<BigDecimal> quantities = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                quantities.add(edsOpportunityItem.getQty());
            }
            return quantities;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_NET_AMOUNT)) {
            List<BigDecimal> nets = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                nets.add(edsOpportunityItem.getNet());
            }
            return nets;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_TAX_RATE)) {
            List<BigDecimal> taxAmounts = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                taxAmounts.add(edsOpportunityItem.getTaxAmount());
            }
            return taxAmounts;
        } else if (fieldID.equalsIgnoreCase(CustomFormConstants.ITEM_TABLE_PROJECT)) {
            List<EdsProject> projects = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                projects.add(edsOpportunityItem.getProject());
            }
            return projects;
        } else if (fieldID.contains("string_value") && values.length >= 2 || fieldID.contains("double_value") && values.length >= 2 || fieldID.contains("date_value") && values.length >= 2) {
            List<Object> customFields = new ArrayList<>();
            for (EdsOpportunityItem edsOpportunityItem : getOpportunityItems()) {
                customFields.add(CustomFieldsUtils.getObjectValue(edsOpportunityItem.getCustomFields(), fieldID));
            }
            return customFields;
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    public Set<EdsOpportunityCustomItemTable> getItemTables() {
        return this.itemTables;
    }

    public void setItemTables(final Set<EdsOpportunityCustomItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public void addItemTable(EdsOpportunityCustomItemTable itemTable) {
        itemTables.add(itemTable);
    }

    public EdsCrmCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCrmCustomFields customFields) {
        this.customFields = customFields;
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue) {
        if (getObjectID() != null) {
            LogHistoryItem change = new LogHistoryItem();
            change.setField(field);
            change.setEntityID(getObjectID());
            change.setEntityType("OPPORTUNITY");
            if (oldValue instanceof String || oldValue instanceof Double) {
                oldValue = oldValue == null ? "" : oldValue;
                change.setFromStringValue(String.valueOf(oldValue));
            } else if (oldValue instanceof Integer) {
                change.setFromNumberValue(new BigDecimal((Integer) oldValue));
            } else if (oldValue instanceof Double) {
                change.setFromNumberValue(new BigDecimal((Double) oldValue));
            } else if (oldValue instanceof Float) {
                change.setFromNumberValue(new BigDecimal((Float) oldValue));
            } else if (oldValue instanceof Number) {
                change.setFromNumberValue((BigDecimal) oldValue);
            } else if (oldValue instanceof Date) {
                change.setFromDateValue((Date) oldValue);
            } else if (oldValue instanceof Boolean) {
                change.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
            }
            if (newValue instanceof String || newValue instanceof Double) {
                newValue = newValue == null ? "" : newValue;
                change.setToStringValue(String.valueOf(newValue));
            } else if (newValue instanceof Integer) {
                change.setToNumberValue(new BigDecimal((Integer) newValue));
            } else if (newValue instanceof Double) {
                change.setToNumberValue(new BigDecimal((Double) newValue));
            } else if (newValue instanceof Float) {
                change.setToNumberValue(new BigDecimal((Float) newValue));
            } else if (newValue instanceof Number) {
                change.setToNumberValue((BigDecimal) newValue);
            } else if (newValue instanceof Date) {
                change.setToDateValue((Date) newValue);
            } else if (newValue instanceof Boolean) {
                change.setToStringValue((Boolean) newValue ? "Yes" : "No");
            }
            change.setUpdatedDate(new Date());
            change.setCreator(((EdsUser) SecurityContext.getInstance().getUser()).getFullName());
            change.setCreatorID(((EdsUser) SecurityContext.getInstance().getUser()).getObjectID());

            HistoryLogManager historyLogManager = ApplicationContextProvider.applicationContext.getBean("historyLogManager", HistoryLogManager.class);
            historyLogManager.create(new EdsHistoryLog().convertToDb(change));
        }
    }

    public EdsUser getOwner() {
        return owner;
    }

    public void setOwner(EdsUser owner) {
        this.owner = owner;
    }

    public EdsEmployee getAssignee() {
        return assignee;
    }

    public EdsEmployee getBackupAssignee() {
        return backupAssignee;
    }

    @Override
    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getAmountBaseCurrency() {
        return amountBaseCurrency;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public EdsCrmContact getCrmContact() {
        return crmContact;
    }

    public EdsReference getType() {
        return type;
    }

    public EdsReference getStage() {
        return stage;
    }

    public Float getProbability() {
        return probability;
    }

    public String getNextStep() {
        return nextStep;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public EdsReference getLeadSource() {
        return leadSource;
    }

    public EdsCampaign getCampaign() {
        return campaign;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public Boolean getConvertedFromLead() {
        return convertedFromLead;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public Boolean getConvertedToProject() {
        return isConvertedToProject;
    }

    public List<EdsOpportunityItem> getOpportunityItems() {
        return opportunityItems;
    }

    public String getNumber() {
        return number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public Integer getImportFileID() {
        return importFileID;
    }

    public Long getKanbanorder() {
        return kanbanorder;
    }

    public List<EdsOpportunity> getSubOpportunities() {
        return subOpportunities;
    }

    public EdsOpportunity getHistoricalParent() {
        return historicalParent;
    }

    public Boolean getHistorical() {
        return historical;
    }

    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    public boolean isPropertiesChanged() {
        return propertiesChanged;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CompanyCustomFieldItem[] getTransientCustomFields() {
        return transientCustomFields;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public EdsReference getRejectReason() {
        return rejectReason;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalInBase() {
        return totalInBase;
    }

    public void setTotalInBase(BigDecimal totalInBase) {
        this.totalInBase = totalInBase;
    }

    public BigDecimal getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(BigDecimal quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public List<EdsHistoryLog> getLogHistories() {
        HistoryLogManager historyLogManager = ApplicationContextProvider.applicationContext.getBean("historyLogManager", HistoryLogManager.class);
        return historyLogManager.getEntityHistoryLog(getObjectID(), "OPPORTUNITY");
    }

    public String getSapRequestBody() {
        return sapRequestBody;
    }

    public void setSapRequestBody(String sapRequestBody) {
        this.sapRequestBody = sapRequestBody;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && REJECTED.equals(getCurrentApprover().getStatus().getCode());

    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.getByCode(REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.getByCode(APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.getByCode(REJECTED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.getByCode(REJECTED);
        }
        return null;
    }
}
