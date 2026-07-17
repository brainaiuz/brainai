package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.crm.contact.WebFormInterface;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.EmailUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrRelationUtils;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseSolrItem;
import com.edatasite.workforce.gwt.webforms.client.forms.CaseField;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 21-Jul-2009
 * Time: 13:32:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmCase")
public class EdsCase extends EdsTraceable implements WebFormInterface {
    private static final ConcurrentHashMap<Integer, Map<Integer, String>> emailContents = new ConcurrentHashMap<>();

    public static final String _CASE_ORIGIN = "_CASE_ORIGIN";
    public static final String EMAIL = "EMAIL";
    public static final String PHONE = "PHONE";
    public static final String FAX = "FAX";
    public static final String WEB = "WEB";

    public static final String _CASE_TYPE = "_CASE_TYPE";
    public static final String PROBLEM = "PROBLEM";
    public static final String FEATURE_REQUEST = "FEATURE_REQUEST";
    public static final String QUESTION = "QUESTION";
    public static final String IN_TRASH = "IN_TRASH";

    public static final String _CASE_STATUS = "_CASE_STATUS";
    public static final String NEW = "NEW";
    public static final String CLOSED = CaseItem.CASE_STATUS_CLOSED;
    public static final String RESOLVED = "RESOLVED";
    public static final String REPLIED = CaseItem.CASE_STATUS_REPLIED;
    public static final String WAITING_FOR_REPLY = "WAITING_FOR_REPLY";
    public static final String _CASE_INTERNAL_STATUS = "_CASE_INTERNAL_STATUS";


    public static final String _CASE_PRIORITY = "_CASE_PRIORITY";
    public static final String CP_HIGH = "CP_HIGH";
    public static final String CP_MEDIUM = "CP_MEDIUM";
    public static final String CP_LOW = "CP_LOW";

    public static final String _CASE_REASON = "_CASE_REASON";
    public static final String OTHER_REASON = "OTHER_REASON";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private EdsUser assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolver")
    private EdsUser resolver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caseOrigion")
    private EdsReference caseOrigion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority")
    private EdsReference priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caseReason")
    private EdsReference caseReason;

    @Column(name = "otherReason")
    private String otherReason;

    @Column(name = "subject", length = 500)
    private String subject;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "inTrash")
    private Boolean inTrash = false;

    @Column(name = "internalComment")
    private String internalComment;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EdsEmailDetails crmCaseDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "releatedto")
    private EdsCrmContact crmContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccount")
    private EdsCrmAccount crmAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    private EdsCrmContact lead;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tracker_id")
    private EdsEmailTracker tracker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity")
    private EdsOpportunity opportunity;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsCrmCustomFields customFields;

    @Column(name = "closedDate")
    private Date closedDate;

    @Column(name = "recievedDate")
    private Date recievedDate;

    @Column(name = "caseNumber")
    private Integer caseNumber;

    @Column(name = "caseNumberString")
    private String caseNumberString;

    @Column(name = "webFormID")
    private Integer webFormID;

    @Column(name = "entity_id")
    private Integer entityID;

    @Column(name = "filter_id")
    private Integer filterID;

    @Column(name = "email_id")
    private String emailID;

    @Column(name = "kanban_order")
    private Long kanbanOrder;

    /**
     * The subCases in this case. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicalParent", fetch = FetchType.LAZY)
    @OrderBy("objectID DESC")
    private List<EdsCase> subCases = new ArrayList<>();

    /**
     * The parent case of this one.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsCase historicalParent;

    @Column(name = "historical")
    private Boolean historical = false;

    @Column(name = "isUserFeedBackAnonim")
    private Boolean isUserFeedBackAnonim = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "internalStatusId")
    private EdsReference internalStatus;

    @Column(name = "internalUpdatedDate")
    private Date internalUpdatedDate;

    @Column(name = "brandId")
    private Integer brandId;

    @Column(name = "productCategoryId")
    private Integer productCategoryId;

    @Column(name = "productId")
    private Integer productId;

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    private transient boolean propertiesChanged = false;

    @Column(name = "comment", length = 5000)
    private String comment;

    public String getName() {
        return getSubject();
    }

    public void setDepartment(EdsDepartment assignedDepartment) {
        this.department = assignedDepartment;
        EdsDepartment oldDepartment = this.department;
        this.department = assignedDepartment;
        if (!ServerUtils.equalsEdsObject(oldDepartment, assignedDepartment)) {
            propertiesChanged = true;
        }
    }

    public void setAssignee(EdsUser assignee) {
        if (!ServerUtils.equalsEdsObject(this.assignee, assignee)) {
            addChange(CustomFormConstants.ASSIGNEE);
            propertiesChanged = true;
            try {
                if (assignee != null && assignee.isEmployee()) {
                    setDepartment(assignee.getEmployee().getTeam());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.assignee = assignee;
    }

    public void setResolver(EdsUser resolver) {
        if (!ServerUtils.equalsEdsObject(this.resolver, resolver)) {
            addChange(CustomFormConstants.RESOLVER);
        }
        this.resolver = resolver;
    }

    public void setType(EdsReference type) {
        if (!ServerUtils.equalsReference(this.type, type)) {
            addChange(CustomFormConstants.TYPE);
        }
        this.type = type;
    }

    public void setCaseOrigion(EdsReference caseOrigion) {
        if (!ServerUtils.equalsReference(this.caseOrigion, caseOrigion)) {
            addChange(CustomFormConstants.CASE_ORIGIN);
        }
        this.caseOrigion = caseOrigion;
    }

    public void setTracker(EdsEmailTracker tracker) {
        this.tracker = tracker;
    }

    public void setCrmContact(EdsCrmContact crmContact) {
        EdsCrmContact oldCrmContact = this.crmContact;
        this.crmContact = crmContact;
        if (this.crmContact != null) {
            this.setEntityID(this.crmContact.getEntityID());
        }
        if (!ServerUtils.equalsEdsObject(oldCrmContact, crmContact)) {
            propertiesChanged = true;
        }
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        EdsCrmAccount oldCrmAccount = this.crmAccount;
        this.crmAccount = crmAccount;
        if (this.crmAccount != null) {
            this.setEntityID(this.crmAccount.getEntityID());
        }
        if (!ServerUtils.equalsEdsObject(oldCrmAccount, crmAccount)) {
            propertiesChanged = true;
        }
    }

    public void setStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(this.status, status)) {
            addChange(CustomFormConstants.STATUS);
            propertiesChanged = true;
        }
        this.status = status;
        if (status != null && status.getCode() != null) {
            if (CLOSED.equals(status.getCode())) {
                if (getClosedDate() == null) {
                    setClosedDate(new Date());
                }
            }
        }
    }

    public void setPriority(EdsReference priority) {
        if (!ServerUtils.equalsReference(this.priority, priority)) {
            addChange(CustomFormConstants.PRIORITY);
        }
        this.priority = priority;
    }

    public void setCaseReason(EdsReference caseReason) {
        if (!ServerUtils.equalsReference(this.caseReason, caseReason)) {
            addChange(CustomFormConstants.CASE_REASON);
        }
        this.caseReason = caseReason;
    }

    public String getSubject() {
        return addCaseNumberToSubject(subject);
    }

    private String addCaseNumberToSubject(String subject) {
        if (subject == null || "".equals(subject.trim())) {
            return getCaseNumberString();
        }
        if (getCaseNumberString() != null && !"".equals(getCaseNumberString())) {
            return subject.matches(".*\\[" + getCaseNumberString() + "\\].*") ? subject : subject + "[" + getCaseNumberString() + "]";
        }
        return subject;
    }

    public void setSubject(String subject) {
        if (subject != null && subject.length() > 500) {
            subject = subject.substring(0, 499);
        }
        if (!ServerUtils.equalsString(this.subject, subject)) {
            addChange(CustomFormConstants.SUBJECT);
        }
        this.subject = refactorNulls(subject);
    }

    public String getDescription() {
        if (this.crmCaseDetails != null) {
            return this.crmCaseDetails.getDescription();
        }
        return null;
    }

    public void setDescription(String description) {
        this.crmCaseDetails.setDescription(refactorNulls(description));
    }

    public void setLead(EdsCrmContact lead) {
        EdsCrmContact oldLead = this.lead;
        this.lead = lead;
        if (this.lead != null) {
            this.setEntityID(this.lead.getEntityID());
        }
        if (!ServerUtils.equalsEdsObject(oldLead, lead)) {
            propertiesChanged = true;
        }

    }

    public void setNote(String note) {
        this.comment = note;
    }

    public String getNote() {
        return comment;
    }

    public String getCcEmails() {
        if (crmCaseDetails == null) {
            return null;
        }
        return crmCaseDetails.getToCC();
    }

    public void setCcEmails(String ccEmails) {
        this.crmCaseDetails.settoCC(refactorNulls(ccEmails));
    }

    public String getReplyTo() {
        if (crmCaseDetails == null) {
            return null;
        }
        return crmCaseDetails.getReplyTo();
    }

    public void setReplyTo(String replyTo) {
        this.crmCaseDetails.setReplyTo(refactorNulls(replyTo));
    }

    @Override
    public String getDomainType() {
        return RelationItem.TYPE_CASE;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer prodcutCategoryId) {
        this.productCategoryId = prodcutCategoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getEmail() {
        if (getLead() != null) {
            return getLead().getPrimaryEmail();
        }
        if (getCrmAccount() != null) {
            return getCrmAccount().getEmail();
        }
        if (getCrmContact() != null) {
            return getCrmContact().getPrimaryEmail();
        }
        return "";
    }

    public String getPhone() {
        if (getLead() != null) {
            return getLead().getPrimaryPhone();
        }
        if (getCrmAccount() != null) {
            return getCrmAccount().getPhone();
        }
        if (getCrmContact() != null) {
            return getCrmContact().getPrimaryPhone();
        }
        return "";
    }

    public String getReportedBy() {
        if (getLead() != null) {
            return getLead().getName();
        }
        if (getCrmAccount() != null) {
            return getCrmAccount().getName();
        }
        if (getCrmContact() != null) {
            if (IsUserFeedBackAnonim()) {
                String l = ServerUtils.getUserLocale().getLanguage();
                if (l.equals("uz")) {
                    return "Anonim";
                } else if (l.equals("ru")) {
                    return "Анонимный";
                } else {
                    return "Anonymous";
                }
            } else {
                return getCrmContact().getName();
            }
        }
        return "";
    }

    public String getReportedByCompany() {
        if (getLead() != null && getLead().getCrmAccount() != null) {
            return getLead().getCrmAccount().getName();
        }
        if (getCrmContact() != null && getCrmContact().getCrmAccount() != null) {
            return getCrmContact().getCrmAccount().getName();
        }
        return "";
    }

    public String getReportEmail() {
        if (getLead() != null) {
            return getLead().getPrimaryEmail();
        }
        if (getCrmAccount() != null) {
            return getCrmAccount().getEmail();
        }
        if (getCrmContact() != null) {
            return getCrmContact().getPrimaryEmail();
        }
        return "";
    }

    public CaseItem getRPC(CaseItem item, boolean... briefly) {
        boolean isBrief = briefly != null && briefly.length > 0 && briefly[0];
        if (item == null) {
            item = new CaseItem();
        }
        item.setCcEmails(getCcEmails());
        item.setReplyTo(getReplyTo());
        item.setCaseNumber(getCaseNumberString());
        item.setObjectId(getObjectID());
        item.setSubject((getSubject() != null && !"".equals(getSubject())) ? getSubject() : "(no subject)");
        item.setReportedBy(getReportedBy());
        item.setReportEmail(getReportEmail());
        item.setTrackerID(getTracker() != null ? getTracker().getObjectID() : null);
        item.setEmailID(getEmailID());
        item.setInternalComment(getInternalComment());
        item.setDescription(isBrief ? null : EmailUtils.retrieveContent(getDescription(), null, new StringBuilder(), null).toString());
        item.setEmail(getEmail());
        item.setPhone(getPhone());
        item.setClosedDate(getClosedDate());
        item.setCreatedDate(getAuditInfo().getCreationDate());
        item.setLastUpdatedDate(getAuditInfo().getModificationDate());
        item.setFilterID(getFilterID());
        item.setAuditInfoResource(getAuditInfo().getDTO());
        item.setBrandId(getBrandId());
        item.setProductCategoryId(getProductCategoryId());
        item.setProductId(getProductId());
        if (getEntityID() != null) {
            item.setEntityID(getEntityID());
        }
        if (getCaseOrigion() != null) {
            item.setCaseOriginId(getCaseOrigion().getObjectID());
            item.setCaseOrigin(getCaseOrigion().getName());
            item.setCaseOriginCode(getCaseOrigion().getCode());
        }
        if (getPriority() != null) {
            item.setPriorityId(getPriority().getObjectID());
            item.setPriority(getPriority().getName());
            item.setPriorityCode(getPriority().getCode());
            item.setPriorityColor(getPriority().getReferenceColor() != null ? getPriority().getReferenceColor().getHex() : getPriority().getColor());
            item.setPrioritySorder(getPriority().getSorder());
        }
        if (getType() != null) {
            item.setType(getType().getName());
            item.setTypeId(getType().getObjectID());
            item.setTypeCode(getType().getCode());
        }
        if (getCrmContact() != null) {
            item.setCrmContact(getCrmContact().getName());
            item.setCrmContactID(getCrmContact().getObjectID());
            List<EdsCrmContactItemParams> fax = getCrmContact().getItemParams(EdsCrmContactItemParams.G_HOME_FAX);
            if (fax == null || fax.size() == 0) {
                fax = getCrmContact().getItemParams(EdsCrmContactItemParams.G_WORK_FAX);
            }
            if (fax != null && fax.size() > 0) {
                item.setFax(fax.get(0) != null ? fax.get(0).getValue() : "");
            }
            if ("".equals(item.getCompany()) && getCrmContact() != null && getCrmContact().getCrmAccount() != null) {
                item.setCompany(getCrmContact().getCrmAccount().getName());
            }

        }
        if (getCrmAccount() != null) {
            item.setAccountNumber(getCrmAccount().getNumber());
            item.setAccountName(getCrmAccount().getName());
            item.setAccountId(getCrmAccount().getObjectID());
            item.setCompany(getCrmAccount().getName());
            item.setFax(getCrmAccount().getFax());
        }
        if (getInTrash() != null) {
            item.setInTrash(getInTrash());
        }
        if (getLead() != null) {
            item.setLead(getLead().getName());
            item.setLeadId(getLead().getObjectID());
            item.setCompany(getLead().getCrmAccount() != null ? getLead().getCrmAccount().getName() : null);
            item.setFax(EdsCrmContactItemParams.getFirstItemParamValue(getLead().getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.HOME_FAX, EdsCrmContactItemParams.WORK_FAX));
        }
        if (getOpportunity() != null) {
            item.setPotentialName(getOpportunity().getName());
            item.setPotentialId(getOpportunity().getObjectID());
        }
        if (getResolver() != null) {
            item.setResolverName(getResolver().getFullName());
            item.setResolverId(getResolver().getObjectID());
        }
        if (getAssignee() != null) {
            item.setCaseAssigneeName(getAssignee().getName());
            item.setCaseAssigneeId(getAssignee().getObjectID());
        }
        if (getDepartment() != null) {
            item.setDepartment(getDepartment().getName());
            item.setDepartmentID(getDepartment().getObjectID());
        }
        if (getStatus() != null) {
            item.setStatus(getStatus().getRPC());
            item.setStatusCode(getStatus().getCode());
        }
        if (getCaseReason() != null) {
            item.setCaseReason(OTHER_REASON.equals(getCaseReason().getCode()) ? getOtherReason() : getCaseReason().getName());
            item.setCaseReasonId(getCaseReason().getObjectID());
            item.setCaseReasonCode(getCaseReason().getCode());
        }
        if (getInternalStatus() != null) {
            item.setInternalStatusId(getInternalStatus().getObjectID());
            item.setInternalStatusName(getInternalStatus().getName());
        }
        item.setInternalUpdatedDate(getInternalUpdatedDate());
        return item;
    }

    public CaseSolrItem getSolrRPC() {
        CaseSolrItem item = new CaseSolrItem();

        item.setObjectID(getObjectID());
        if (getTracker() != null) {
            item.setCaseTrackerId(getTracker().getObjectID());
        }
        item.setCaseEmailId(getEmailID());
        item.setCaseEmail(getEmail());
        item.setCasePhone(getPhone());
        item.setCaseSubject(StringUtils.isNotBlank(getSubject()) ? getSubject() : "(no subject)");
        item.setCaseNumber(getCaseNumberString());
        if (getAssignee() != null) {
            item.setCaseAssignee(getAssignee().getAsSelectItem());
        }
        if (getDepartment() != null) {
            item.setCaseDepartment(getDepartment().getAsSelectItem());
        }
        if (getCaseOrigion() != null) {
            item.setCaseOrigin(getCaseOrigion().getAsSelectItem());
        }
        if (getType() != null) {
            item.setCaseType(getType().getRPC());
        }
        if (getCaseReason() != null) {
            item.setCaseReason(getCaseReason().getAsSelectItem());
        }
        if (getPriority() != null) {
            item.setPriority(getPriority().getRPC());
        }
        if (getStatus() != null) {
            item.setStatus(getStatus().getRPC());
        }
        if (getResolver() != null) {
            item.setResolver(getResolver().getAsSelectItem());
        }
        if (getEntityID() != null) {
            item.setEntityId(getEntityID());
        }
        if (getLead() != null) {
            item.setLeadId(getLead().getObjectID());
        }
        if (getCrmAccount() != null) {
            item.setAccountId(getCrmAccount().getObjectID());
        }
        if (getCrmContact() != null) {
            item.setRelatedToId(getCrmContact().getObjectID());
        }
        item.setInTrash(getInTrash() != null ? getInTrash() : Boolean.FALSE);
        if (StringUtils.isNotBlank(getReportedBy())) {
            item.setReportedBy(getReportedBy());
        } else if (StringUtils.isNotBlank(getEmail())) {
            item.setReportedBy(getEmail());
        }
        if (getAuditInfo() != null) {
            item.setCreateDate(getAuditInfo().getCreationDate());
            item.setLastUpdatedDate(getAuditInfo().getModificationDate());
        }
        item.setInternalUpdatedDate(getInternalUpdatedDate());
        if (getInternalStatus() != null) {
            item.setInternalStatus(getInternalStatus().getRPC());
        }
        item.setKanbanOrder(item.getKanbanOrder());

        return item;
    }

    public SolrInputDocument wrapToSolrDocument(Integer companyID, List<Integer> attachmentedCasesTrackerIDs, List<EdsRelation> relationList) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrCaseRepresenter.COMPOSITE_ID, getObjectID() + "_" + companyID);
        doc.addField(SolrCaseRepresenter.COMPANY_ID, companyID);
        doc.addField(SolrCaseRepresenter.HAS_ATTACHMENT, attachmentedCasesTrackerIDs.contains(getObjectID()));
        doc.addField(SolrCaseRepresenter.CASE_ID, getObjectID());
        doc.addField(SolrCaseRepresenter.CASE_NUMBER, getCaseNumberString());
        doc.addField(SolrCaseRepresenter.CASE_SUBJECT, StringUtils.isNotBlank(getSubject()) ? getSubject() : "(no subject)");
        doc.addField(SolrCaseRepresenter.CASE_EMAIL, getEmail());
        doc.addField(SolrCaseRepresenter.CASE_PHONE, getPhone());
        doc.addField(SolrCaseRepresenter.CREATE_DATE, getAuditInfo().getCreationDate());
        doc.addField(SolrCaseRepresenter.LAST_UPDATE_DATE, getAuditInfo().getModificationDate());
        doc.addField(SolrCaseRepresenter.IN_TRASH, getInTrash() != null ? getInTrash() : Boolean.FALSE);
        doc.addField(SolrCaseRepresenter.KANBAN_ORDER, getKanbanOrder());
        if (getTracker() != null) {
            doc.addField(SolrCaseRepresenter.CASE_TRACKER_ID, getTracker().getObjectID());
        }
        if (getEmailID() != null) {
            doc.addField(SolrCaseRepresenter.CASE_EMAIL_ID, getEmailID());
        }
        // to solr registration reported by field
        if (StringUtils.isNotBlank(getReportedBy())) {
            doc.addField(SolrCaseRepresenter.REPORTED_BY, getReportedBy());
        } else if (StringUtils.isNotBlank(getEmail())) {
            doc.addField(SolrCaseRepresenter.REPORTED_BY, getEmail());
        }
        if (getEntityID() != null) {
            doc.addField(SolrCaseRepresenter.ENTITY_ID, getEntityID());
        }
        if (getLead() != null) {
            doc.addField(SolrCaseRepresenter.LEAD_ID, getLead().getObjectID());
        }
        if (getCrmAccount() != null) {
            doc.addField(SolrCaseRepresenter.ACCOUNT_ID, getCrmAccount().getObjectID());
        }
        if (getCrmContact() != null) {
            doc.addField(SolrCaseRepresenter.RELEATED_TO_ID, getCrmContact().getObjectID());
        }
        if (getAssignee() != null) {
            doc.addField(SolrCaseRepresenter.CASE_ASSIGNEE, getAssignee().getName());
            doc.addField(SolrCaseRepresenter.CASE_ASSIGNEE_ID, getAssignee().getObjectID());
            doc.addField(SolrCaseRepresenter.CASE_ASSIGNEE_ID_NAME, getAssignee().getObjectID() + SolrCaseRepresenter.SPLIT + getAssignee().getName());
        }
        if (getDepartment() != null) {
            doc.addField(SolrCaseRepresenter.CASE_DEPARTMENT, getDepartment().getName());
            doc.addField(SolrCaseRepresenter.CASE_DEPARTMENT_ID, getDepartment().getObjectID());
            doc.addField(SolrCaseRepresenter.CASE_DEPARTMENT_ID_NAME, getDepartment().getObjectID() + SolrCaseRepresenter.SPLIT + getDepartment().getName());
        }
        if (getStatus() != null) {
            doc.addField(SolrCaseRepresenter.STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrCaseRepresenter.STATUS_NAME, getStatus().getName());
            doc.addField(SolrCaseRepresenter.STATUS_ID_NAME, getStatus().getObjectID() + SolrCaseRepresenter.SPLIT + getStatus().getName());
            doc.addField(SolrCaseRepresenter.STATUS_ID_CODE_NAME, getStatus().getObjectID() + SolrCaseRepresenter.SPLIT + getStatus().getCode() + SolrCaseRepresenter.SPLIT + getStatus().getName());
            doc.addField(SolrCaseRepresenter.STATUS_CODE, getStatus().getCode());
            doc.addField(SolrCaseRepresenter.STATUS_SORDER, getStatus().getSorder());
        }
        if (getPriority() != null) {
            doc.addField(SolrCaseRepresenter.PRIORITY_ID, getPriority().getObjectID());
            doc.addField(SolrCaseRepresenter.PRIORITY_NAME, getPriority().getName());
            doc.addField(SolrCaseRepresenter.PRIORITY_ID_NAME, getPriority().getObjectID() + SolrCaseRepresenter.SPLIT + getPriority().getName());
            doc.addField(SolrCaseRepresenter.PRIORITY_ID_CODE_NAME, getPriority().getObjectID() + SolrCaseRepresenter.SPLIT + getPriority().getCode() + SolrCaseRepresenter.SPLIT + getPriority().getName());
            doc.addField(SolrCaseRepresenter.PRIORITY_CODE, getPriority().getCode());
            doc.addField(SolrCaseRepresenter.PRIORITY_COLOR, getPriority().getReferenceColor() != null ? getPriority().getReferenceColor().getHex() : getPriority().getColor());
            doc.addField(SolrCaseRepresenter.PRIORITY_SORDER, getPriority().getSorder());
        }
        if (getCaseOrigion() != null) {
            doc.addField(SolrCaseRepresenter.CASE_ORIGIN_ID, getCaseOrigion().getObjectID());
            doc.addField(SolrCaseRepresenter.CASE_ORIGIN_NAME, getCaseOrigion().getName());
            doc.addField(SolrCaseRepresenter.CASE_ORIGIN_ID_NAME, getCaseOrigion().getObjectID() + SolrCaseRepresenter.SPLIT + getCaseOrigion().getName());
            doc.addField(SolrCaseRepresenter.CASE_ORIGIN_ID_CODE_NAME, getCaseOrigion().getObjectID() + SolrCaseRepresenter.SPLIT + getCaseOrigion().getCode() + SolrCaseRepresenter.SPLIT + getCaseOrigion().getName());
            doc.addField(SolrCaseRepresenter.CASE_ORIGIN_CODE, getCaseOrigion().getCode());
        }
        if (getCaseReason() != null) {
            doc.addField(SolrCaseRepresenter.CASE_REASON_ID, getCaseReason().getObjectID());
            doc.addField(SolrCaseRepresenter.CASE_REASON_NAME, getCaseReason().getName());
            doc.addField(SolrCaseRepresenter.CASE_REASON_ID_NAME, SolrUtils.getIdName(getCaseReason()));
        }
        if (getResolver() != null) {
            doc.addField(SolrCaseRepresenter.RESOLVER_ID, getResolver().getObjectID());
            doc.addField(SolrCaseRepresenter.RESOLVER_NAME, getResolver().getName());
            doc.addField(SolrCaseRepresenter.RESOLVER_ID_NAME, getResolver().getObjectID() + SolrCaseRepresenter.SPLIT + getResolver().getName());
        }
        if (getType() != null) {
            doc.addField(SolrCaseRepresenter.CASE_TYPE_ID, getType().getObjectID());
            doc.addField(SolrCaseRepresenter.CASE_TYPE_NAME, getType().getName());
            doc.addField(SolrCaseRepresenter.CASE_TYPE_ID_NAME, getType().getObjectID() + SolrCaseRepresenter.SPLIT + getType().getName());
            doc.addField(SolrCaseRepresenter.CASE_TYPE_ID_CODE_NAME, getType().getObjectID() + SolrCaseRepresenter.SPLIT + getType().getCode() + SolrCaseRepresenter.SPLIT + getType().getName());
            doc.addField(SolrCaseRepresenter.CASE_TYPE_CODE, getType().getCode());
        }
        if (getInternalStatus() != null) {
            doc.addField(SolrCaseRepresenter.INTERNAL_STATUS_ID, getInternalStatus().getObjectID());
            doc.addField(SolrCaseRepresenter.INTERNAL_STATUS_NAME, getInternalStatus().getName());
            doc.addField(SolrCaseRepresenter.INTERNAL_STATUS_ID_NAME, SolrUtils.getIdName(getInternalStatus()));
        }
        doc.addField(SolrCaseRepresenter.INTERNAL_UPDATED_DATE, getInternalUpdatedDate());
        SolrRelationUtils.addToSolrRelations(doc, relationList, EdsRelation.TYPE_CASE);
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());

        return doc;
    }

    public String getFieldAsString(Integer savingField) {
        if (savingField.equals(CaseField.FIELD_ASSIGNEE)) {
            if (getAssignee() != null) {
                return getAssignee().getFullName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_DEPARTMENT)) {
            if (getDepartment() != null) {
                return getDepartment().getName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_SUBJECT)) {
            if (getSubject() != null) {
                return getSubject();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_DESCRIPTION)) {
            if (getDescription() != null) {
                return getDescription();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_CASE_ORIGIN)) {
            if (getCaseOrigion() != null) {
                return getCaseOrigion().getName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_STATUS)) {
            if (getStatus() != null) {
                return getStatus().getName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_TYPE)) {
            if (getType() != null) {
                return getType().getName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_REPORTED_BY_OTHER_FIRST_NAME)) {
            if (getCrmContact() != null) {
                return getCrmContact().getFirstName();
            } else if (getLead() != null) {
                return getLead().getFirstName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_REPORTED_BY_OTHER_LAST_NAME)) {
            if (getCrmContact() != null) {
                return getCrmContact().getLastName();
            } else if (getLead() != null) {
                return getLead().getLastName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_REPORTED_BY_OTHER_COMPANY)) {
            if (getCrmContact() != null && getCrmContact().getCrmAccount() != null) {
                return getCrmContact().getCrmAccount().getName();
            } else if (getLead() != null && getLead().getCrmAccount() != null) {
                return getLead().getCrmAccount().getName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_REPORTED_BY_OTHER_EMAIL)) {
            if (getCrmContact() != null) {
                return getCrmContact().getPrimaryEmail();
            } else if (getLead() != null) {
                return getLead().getPrimaryEmail();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_REPORTED_BY_OTHER_PHONE)) {
            if (getCrmContact() != null) {
                return getCrmContact().getPrimaryPhone();
            } else if (getLead() != null) {
                return getLead().getPrimaryPhone();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_REPORTED_BY_OTHER_FAX)) {
            if (getCrmContact() != null) {
                return EdsCrmContactItemParams.getFirstItemParamValue(getCrmContact().getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.HOME_FAX);
            } else if (getLead() != null) {
                return EdsCrmContactItemParams.getFirstItemParamValue(getLead().getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.HOME_FAX);
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_PRIORITY)) {
            if (getPriority() != null) {
                return getPriority().getName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_CASE_REASON)) {
            if (getCaseReason() != null) {
                return getCaseReason().getName();
            } else {
                return "N/A";
            }
        }
        if (savingField.equals(CaseField.FIELD_RESOLVER)) {
            if (getResolver() != null) {
                return getResolver().getFullName();
            } else {
                return "N/A";
            }
        }
        return "N/A";
    }

    private String getNotNull(String... values) {
        if (values != null && values.length > 0) {
            for (String value : values) {
                if (value != null && !"".equals(value)) {
                    return value;
                }
            }
        }
        return null;
    }

    public static String getEmailContent(Integer companyID, Integer crmCaseID) {
        if (companyID != null && crmCaseID != null && emailContents.get(companyID) != null) {
            return emailContents.get(companyID).get(crmCaseID);
        }
        return null;
    }

    public static void addTempContentReady(Integer companyID, Integer crmCaseID, String content) {
        if (content != null) {
            if (!emailContents.containsKey(companyID)) {
                emailContents.put(companyID, new HashMap<>());
            }
            emailContents.get(companyID).put(crmCaseID, content);
        }
    }

    public static void clearContents(Integer objectID) {
        emailContents.remove(objectID);
    }

    @Override
    public String getFieldLabelByCode(String fieldID) {
        if (CustomFormConstants.ASSIGNEE.equals(fieldID)) {
            return "Assignee";
        } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
            return "Attachments";
        } else if (CustomFormConstants.SUBJECT.equals(fieldID)) {
            return "Subject";
        } else if (CustomFormConstants.CRM_NOTE.equals(fieldID)) {
            return "Note";
        } else if (CustomFormConstants.CASE_DESCRIPTION.equals(fieldID)) {
            return "Description";
        } else if (CustomFormConstants.CASE_ORIGIN.equals(fieldID)) {
            return "Case Origin";
        } else if (CustomFormConstants.BILLABLE.equals(fieldID)) {
            return "Billable";
        } else if (CustomFormConstants.STATUS.equals(fieldID)) {
            return "Status";
        } else if (CustomFormConstants.TYPE.equals(fieldID)) {
            return "Type";
        } else if (CustomFormConstants.SLA.equals(fieldID)) {
            return "SLA";
        } else if (CustomFormConstants.REPORTED_BY.equals(fieldID)) {
            return "Reported By";
        } else if (CustomFormConstants.PRIORITY.equals(fieldID)) {
            return "Priority";
        } else if (CustomFormConstants.CASE_REASON.equals(fieldID)) {
            return "Case Reason";
        } else if (CustomFormConstants.RESOLVER.equals(fieldID)) {
            return "Resolver";
        }
        return null;
    }

    @Override
    public String getFieldValueByCode(String fieldID) {
        String na = "N/A";
        if (CustomFormConstants.ASSIGNEE.equals(fieldID)) {
            return getAssignee() != null ? getAssignee().getName() : na;
        } else if (CustomFormConstants.SUBJECT.equals(fieldID)) {
            return getSubject();
        } else if (CustomFormConstants.CRM_NOTE.equals(fieldID)) {
            return getNote();
        } else if (CustomFormConstants.CASE_DESCRIPTION.equals(fieldID)) {
            return getDescription();
        } else if (CustomFormConstants.CASE_ORIGIN.equals(fieldID)) {
            return getCaseOrigion() != null ? getCaseOrigion().getName() : na;
        } else if (CustomFormConstants.STATUS.equals(fieldID)) {
            return getStatus() != null ? getStatus().getName() : na;
        } else if (CustomFormConstants.TYPE.equals(fieldID)) {
            return getType() != null ? getType().getName() : na;
        } else if (CustomFormConstants.REPORTED_BY.equals(fieldID)) {
            return getReportedBy();
        } else if (CustomFormConstants.PRIORITY.equals(fieldID)) {
            return getPriority() != null ? getPriority().getName() : na;
        } else if (CustomFormConstants.CASE_REASON.equals(fieldID)) {
            return getCaseReason() != null ? getCaseReason().getName() : na;
        } else if (CustomFormConstants.RESOLVER.equals(fieldID)) {
            return getResolver() != null ? getResolver().getName() : na;
        }
        return na;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.SUBJECT)) {
                setSubject((String) value);
            } else if (fieldID.equals(CustomFormConstants.TYPE)) {
                setType((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.STATUS)) {
                setStatus((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.PRIORITY)) {
                setPriority((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CASE_ORIGIN)) {
                setCaseOrigion((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.CASE_REASON)) {
                setCaseReason((EdsReference) value);
            } else if (fieldID.equals(CustomFormConstants.ASSIGNEE)) {
                setAssignee((EdsEmployee) value);
            } else if (fieldID.equals(CustomFormConstants.RESOLVER)) {
                setResolver((EdsEmployee) value);
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
        } else if (fieldID.equals(CustomFormConstants.SUBJECT)) {
            return getSubject();
        } else if (fieldID.equals(CustomFormConstants.TYPE)) {
            return getType();
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.PRIORITY)) {
            return getPriority();
        } else if (fieldID.equals(CustomFormConstants.CASE_REASON)) {
            return getCaseReason();
        } else if (fieldID.equals(CustomFormConstants.CASE_ORIGIN)) {
            return getCaseOrigion();
        } else if (fieldID.equals(CustomFormConstants.RESOLVER)) {
            return getResolver();
        } else if (fieldID.equals(CustomFormConstants.ASSIGNEE)) {
            return getAssignee();
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getAuditInfo().getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getAuditInfo().getModificationDate();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        } else if (fieldID.equals(CustomFormConstants.INTERNAL_UPDATED_DATE)) {
            return getInternalUpdatedDate() != null ? getInternalUpdatedDate() : "";
        } else if (fieldID.equals(CustomFormConstants.INTERNAL_STATUS)) {
            return getInternalStatus() != null ? getInternalStatus().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
            return getDescription() != null ? getDescription() : "";
        }
        return super.getRealValue(fieldID);
    }

    public static String replaceAllUrl(String description) {
        if (description != null) {
            Pattern patternTag = Pattern.compile("<a([^>]+)>(.+?)</a>");
            Pattern patternLink = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");
            Matcher matcherTag = patternTag.matcher(description);
            Matcher matcherLink;

            while (matcherTag.find()) {
                String href = matcherTag.group(1); // href
                matcherLink = patternLink.matcher(href);

                while (matcherLink.find()) {
                    if (href != null) {
                        String hrefItem = "<a" + href;
                        String link = matcherLink.group(1).replace("\"", ""); // link
                        if (!hrefItem.equals("<a href=\"" + link + "\" target=\"_blank\"") && (link.contains("http:") || link.contains("https:"))) {
                            description = description.replace("<a" + href, "<a href=\"" + link + "\" target=\"_blank\"");
                        }
                    }
                }
            }
        }
        return description;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getAssignee() {
        return assignee;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public EdsUser getResolver() {
        return resolver;
    }

    public EdsReference getType() {
        return type;
    }

    public EdsReference getCaseOrigion() {
        return caseOrigion;
    }

    public EdsReference getStatus() {
        return status;
    }

    public EdsReference getPriority() {
        return priority;
    }

    public EdsReference getCaseReason() {
        return caseReason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getInTrash() {
        return inTrash;
    }

    public void setInTrash(Boolean inTrash) {
        this.inTrash = inTrash;
    }

    public String getInternalComment() {
        return internalComment;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
    }

    public EdsEmailDetails getCrmCaseDetails() {
        return crmCaseDetails;
    }

    public void setCrmCaseDetails(EdsEmailDetails crmCaseDetails) {
        this.crmCaseDetails = crmCaseDetails;
    }

    public EdsCrmContact getCrmContact() {
        return crmContact;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public EdsCrmContact getLead() {
        return lead;
    }

    public EdsEmailTracker getTracker() {
        return tracker;
    }

    public EdsOpportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(EdsOpportunity opportunity) {
        this.opportunity = opportunity;
    }

    @Override
    public EdsCrmCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCrmCustomFields customFields) {
        this.customFields = customFields;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Date getRecievedDate() {
        return recievedDate;
    }

    public void setRecievedDate(Date recievedDate) {
        this.recievedDate = recievedDate;
    }

    public Integer getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(Integer caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getCaseNumberString() {
        return caseNumberString;
    }

    public void setCaseNumberString(String caseNumberString) {
        this.caseNumberString = caseNumberString;
    }

    public Integer getWebFormID() {
        return webFormID;
    }

    public void setWebFormID(Integer webFormID) {
        this.webFormID = webFormID;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Integer getFilterID() {
        return filterID;
    }

    public void setFilterID(Integer filterID) {
        this.filterID = filterID;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public Long getKanbanOrder() {
        return kanbanOrder;
    }

    public void setKanbanOrder(Long kanbanOrder) {
        this.kanbanOrder = kanbanOrder;
    }

    public List<EdsCase> getSubCases() {
        return subCases;
    }

    public void setSubCases(List<EdsCase> subCases) {
        this.subCases = subCases;
    }

    public EdsCase getHistoricalParent() {
        return historicalParent;
    }

    public void setHistoricalParent(EdsCase historicalParent) {
        this.historicalParent = historicalParent;
    }

    public Boolean getHistorical() {
        return historical;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    public EdsReference getInternalStatus() {
        return internalStatus;
    }

    public void setInternalStatus(EdsReference internalStatus) {
        this.internalStatus = internalStatus;
    }

    public Date getInternalUpdatedDate() {
        return internalUpdatedDate;
    }

    public void setInternalUpdatedDate(Date internalUpdatedDate) {
        this.internalUpdatedDate = internalUpdatedDate;
    }

    public EdsAuditInfo getAuditInfo() {
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public boolean isPropertiesChanged() {
        return propertiesChanged;
    }

    public void setPropertiesChanged(boolean propertiesChanged) {
        this.propertiesChanged = propertiesChanged;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean IsUserFeedBackAnonim() {
        return isUserFeedBackAnonim != null && isUserFeedBackAnonim.booleanValue();
    }

    public void setUserFeedBackAnonim(Boolean userFeedBackAnonim) {
        isUserFeedBackAnonim = userFeedBackAnonim;
    }
}
