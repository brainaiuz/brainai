package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsSickRequestCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.laborPeriod.MultiLeaveDTO;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.EmployeeSolr;
import com.edatasite.workforce.gwt.payroll.client.rpc.SickRequestSolr;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.server.app.ServerUtils.getTimeFormatted;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * User: Ula
 * Date: Aug 11, 2007
 * Time: 10:29:18 AM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "SickRequest",
        indexes = {
                @Index(columnList = "employeeId", name = "sickrequest_employeeId_idx"),
                @Index(columnList = "reason", name = "sickrequest_reason_idx")
        })
public class EdsSickRequest extends EdsApprovable {
    //---------------- leave request type ---------------//
    public static final String _LEAVE_REQUEST_TYPE = "_LEAVE_REQUEST_TYPE";
    public static final String LR_TYPE_ANNUAL_LEAVE = CustomFormConstants.LR_TYPE_ANNUAL_LEAVE;
    public static final String LR_TYPE_UNAUTHORIZED_LEAVE = CustomFormConstants.LR_TYPE_UNAUTHORIZED_LEAVE;

    //---------------- _sick type ---------------//
    public static final String _SICK_TYPE = "_SICK_TYPE";
    public static final String PAID = "ST_PAID";
    public static final String NON_PAID = "NON_PAID";
    //---------------- leave request status ---------------//
    public static final String _SICK_STATUS = "_SICK_STATUS";
    public static final String APPROVED = Constants.LR_STATUS_SS_APPROVED;
    public static final String DENIED = Constants.LR_STATUS_SS_DENIED;
    public static final String NOT_DEFINED = Constants.LR_STATUS_NOT_DEFINED;
    public static final String DRAFT = Constants.DRAFT;
    public static final String SUBMITTED = Constants.SUBMITTED;

    private static final boolean INCLUDE_DAYOFF_DEFAULT = EdsReference.INCLUDE_DAYOFF_DEFAULT;

    private String leaveRequestCode;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "recall_date")
    private Date recallDate;

    @Column(name = "intnumber")
    private Integer intNumber;

    @Column(name = "numberData")
    private String numberData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registeredBy")
    private EdsEmployee registeredBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_code", referencedColumnName = "code")
    private EdsLeaveReason leaveReason;

    @Deprecated //user leaveReason
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason")
    private EdsReference reason;

    @Deprecated
    @Column(name = "otherReason")
    private String otherReason;

    @Column(name = "toTakeFromAllowance")
    private Boolean toTakeFromAllowance = true;//to take/or not to take -> Taken from Annual Leave Allowance option; default to take allowance;

    @Column(name = "takeByMoney")
    private Boolean takeByMoney;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type")
    private EdsReference type;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sickRequest", fetch = FetchType.LAZY)
    @OrderBy(value = "creationDate DESC")
    private final List<EdsSickRequestComment> sickRequestComments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'LEAVE_REQUEST'")//LEAVE_REQUEST ni o'zgartirish kerak ko'chirganda.
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sickrequest_id")
    private List<EdsBackupEmployee> backupEmployees = new ArrayList<>();

    @Column(name = "startDate")
    private Date startDate;

    private Boolean includeDayOff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentdeductionid")
    private EdsPaymentDeduction paymentDeduction;

    @Column(name = "createdDate")
    private Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater")
    private EdsUser updater;

    @Column(name = "fromApi", columnDefinition = "boolean default false")
    private Boolean fromApi = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsSickRequestCustomFields customFields;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @OneToMany(mappedBy = "request", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<EdsSickRequestForPeriod> sickRequestForPeriodList = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsSickRequest parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<EdsSickRequest> childlist = new ArrayList<>();

    @Column(name = "labor_period")
    private String laborPeriod;

    @Column(name = "money_days")
    private Integer moneyDays;

    private Double usedExperienceDays;

    @Column(columnDefinition = "boolean default false")
    private Boolean recalculateLabourPeriod = Boolean.FALSE;

    /* start of APPROVE_ fields*/

    public Date getRecallDate() {
        return recallDate;
    }

    public void setRecallDate(Date recallDate) {
        this.recallDate = recallDate;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers == null ? new ArrayList<>() : approvers;
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
    public void updateStatus(EdsReference status) {
        if (getCurrentApprover() != null) {
            getCurrentApprover().setStatus(status);
        }
        //We must set OverallStatus=REJECTED only if thats set in action
        if (isCurrentApproverRejected() && getCurrentApprover().getOnRejectedAction().equals(ApproverItem.MARK_AS_REJECTED)) {
            setEntityStatus(getCurrentApprover().getStatus());
        }
        addChange(CustomFormConstants.STATUS);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && DENIED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(_SICK_STATUS, DENIED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(_SICK_STATUS, APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(_SICK_STATUS, DENIED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(_SICK_STATUS, DENIED);
        }
        return null;
    }
    /* end of APPROVE_ fields*/

    public List<EdsSickRequestForPeriod> getSickRequestForPeriodList() {
        return sickRequestForPeriodList;
    }

    public void setSickRequestForPeriodList(List<EdsSickRequestForPeriod> sickRequestForPeriodList) {
        this.sickRequestForPeriodList = sickRequestForPeriodList;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsEmployee getRegisteredBy() {
        return registeredBy;
    }

    public void setRegisteredBy(EdsEmployee registeredRy) {
        this.registeredBy = registeredRy;
    }

    public EdsLeaveReason getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(EdsLeaveReason reason) {
        this.leaveReason = reason;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public Boolean getToTakeFromAllowance() {
        if (toTakeFromAllowance == null) {
            toTakeFromAllowance = false;
        }
        return toTakeFromAllowance;
    }

    public void setToTakeFromAllowance(Boolean toTakeFromAllowance) {
        this.toTakeFromAllowance = toTakeFromAllowance;
    }

    public Boolean isTakeByMoney() {
        return takeByMoney != null ? takeByMoney : Boolean.FALSE;
    }

    public void setTakeByMoney(Boolean takeByMoney) {
        this.takeByMoney = takeByMoney;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public List<EdsSickRequestComment> getSickRequestComments() {
        return sickRequestComments;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Boolean getIncludeDayOff() {
        return includeDayOff != null ? includeDayOff : INCLUDE_DAYOFF_DEFAULT;
    }

    public void setIncludeDayOff(Boolean includeDayOff) {
        this.includeDayOff = includeDayOff;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public EdsPaymentDeduction getPaymentDeduction() {
        return paymentDeduction;
    }

    public void setPaymentDeduction(EdsPaymentDeduction paymentDeduction) {
        this.paymentDeduction = paymentDeduction;
    }

    public EdsSickRequestCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsSickRequestCustomFields customFields) {
        this.customFields = customFields;
    }

    public Boolean hasAccess(EdsUser user) {
        return user.getCompany().getObjectID().equals(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    public Boolean getFromApi() {
        return fromApi;
    }

    public void setFromApi(Boolean fromApi) {
        this.fromApi = fromApi;
    }

    public Integer getMoneyDays() {
        return moneyDays;
    }

    public void setMoneyDays(Integer moneyDays) {
        this.moneyDays = moneyDays;
    }

    public Boolean getRecalculateLabourPeriod() {
        return recalculateLabourPeriod;
    }

    public void setRecalculateLabourPeriod(Boolean recalculateLabourPeriod) {
        this.recalculateLabourPeriod = recalculateLabourPeriod;
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.REASON)) {
            return getLeaveReason();
        } else if (fieldID.equals(CustomFormConstants.CREATOR) || fieldID.equals(CustomFormConstants.REGISTERED_BY)) {
            return isOk(getRegisteredBy()) ? getRegisteredBy().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.CREATOR_EMAIL) || fieldID.equals(CustomFormConstants.REGISTERED_BY_EMAIL)) {
            return isOk(getRegisteredBy()) ? getRegisteredBy().getEmail() : "";
        } else if (fieldID.equals(CustomFormConstants.TYPE)) {
            return isOk(getType()) ? getType().getName() : "";
        } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
            return isOk(getStartDate()) ? ServerUtils.shortDateFormat(getStartDate(), getEmployee()) : null;
        } else if (fieldID.equals(CustomFormConstants.DUE_DATE) || fieldID.equals(CustomFormConstants.END_DATE)) {
            return isOk(getEndDate()) ? ServerUtils.shortDateFormat(getEndDate(), getEmployee()) : null;
        } else if (fieldID.equals(CustomFormConstants.START_TIME)) {
            return getTimeFormatted(getStartDate());
        } else if (fieldID.equals(CustomFormConstants.DUE_TIME) || fieldID.equals(CustomFormConstants.END_TIME)) {
            return getTimeFormatted(getEndDate());
        } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
            return getDescription() != null ? getDescription() : "";
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getCreatedDate();
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getOverallStatus();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    @Override
    public String getShortDateFormat() {
        return ServerUtils.getShortDateFormat(employee);
    }

    public SolrInputDocument indexToSolr(Integer companyID) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + getObjectID();
        doc.addField(SolrLeaveRequestConst.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrLeaveRequestConst.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrLeaveRequestConst.FIELD_OBJECT_ID, getObjectID());
        if (getEmployee() != null) {
            doc.addField(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID, getEmployee().getObjectID());
            doc.addField(SolrLeaveRequestConst.FIELD_EMPLOYEE_NAME, getEmployee().getName());
            doc.addField(SolrLeaveRequestConst.FIELD_EMPLOYEE_ID_NAME, getEmployee().getObjectID() + SolrLeaveRequestConst.SPLIT + getEmployee().getName());

            if (getEmployee().getLocation() != null) {
                doc.addField(SolrLeaveRequestConst.FIELD_LOCATION_ID, getEmployee().getLocation().getObjectID());
                doc.addField(SolrLeaveRequestConst.FIELD_LOCATION_NAME, getEmployee().getLocation().getName());
                doc.addField(SolrLeaveRequestConst.FIELD_LOCATION_ID_NAME, getEmployee().getLocation().getObjectID() + SolrLeaveRequestConst.SPLIT + getEmployee().getLocation().getName());
            }

            if (getEmployee().getTeam() != null) {
                doc.addField(SolrLeaveRequestConst.FIELD_DEPARTMENT_ID, getEmployee().getTeam().getObjectID());
                doc.addField(SolrLeaveRequestConst.FIELD_DEPARTMENT_NAME, getEmployee().getTeam().getName());
                doc.addField(SolrLeaveRequestConst.FIELD_DEPARTMENT_ID_NAME, getEmployee().getTeam().getObjectID() + SolrLeaveRequestConst.SPLIT + getEmployee().getTeam().getName());
            }

            if (getEmployee().getPosition() != null) {
                doc.addField(SolrLeaveRequestConst.FIELD_POSITION_ID, getEmployee().getPosition().getObjectID());
                doc.addField(SolrLeaveRequestConst.FIELD_POSITION_NAME, getEmployee().getPosition().getName());
                doc.addField(SolrLeaveRequestConst.FIELD_POSITION_ID_NAME, getEmployee().getPosition().getObjectID() + SolrLeaveRequestConst.SPLIT + getEmployee().getPosition().getName());
            }

            if (getEmployee().getProfile() != null) {
                if (getEmployee().getProfile().getReportsTo() != null) {
                    doc.addField(SolrLeaveRequestConst.FIELD_SUPERVISOR_ID, getEmployee().getProfile().getReportsTo().getObjectID());
                    doc.addField(SolrLeaveRequestConst.FIELD_SUPERVISOR_NAME, getEmployee().getProfile().getReportsTo().getFullName());
                    doc.addField(SolrLeaveRequestConst.FIELD_SUPERVISOR_ID_NAME, getEmployee().getProfile().getReportsTo().getObjectID() + SolrLeaveRequestConst.SPLIT + getEmployee().getProfile().getReportsTo().getFullName());
                }
            }
        }
        if (getNumberData() != null) {
            doc.addField(SolrLeaveRequestConst.FIELD_NUMBER_DATA, getNumberData());
        }

        if (getLeaveReason() != null) {
            doc.addField(SolrLeaveRequestConst.FIELD_REASON_ID, getLeaveReason().getObjectID());
            doc.addField(SolrLeaveRequestConst.FIELD_REASON_NAME, getLeaveReason().getName());
            doc.addField(SolrLeaveRequestConst.FIELD_REASON_CODE, getLeaveReason().getCode());
            doc.addField(SolrLeaveRequestConst.FIELD_REASON_ID_NAME, getLeaveReason().getObjectID() + SolrLeaveRequestConst.SPLIT + getLeaveReason().getCode());
        }
        if (getOverallStatus() != null) {
            doc.addField(SolrLeaveRequestConst.FIELD_STATUS_ID, getOverallStatus().getObjectID());
            doc.addField(SolrLeaveRequestConst.FIELD_STATUS_NAME, getOverallStatus().getName());
            doc.addField(SolrLeaveRequestConst.FIELD_STATUS_CODE, getOverallStatus().getCode());
            doc.addField(SolrLeaveRequestConst.FIELD_STATUS_ID_NAME, getOverallStatus().getObjectID() + SolrLeaveRequestConst.SPLIT + getOverallStatus().getName());
            doc.addField(SolrLeaveRequestConst.FIELD_STATUS_ID_CODE, getOverallStatus().getObjectID() + SolrLeaveRequestConst.SPLIT + getOverallStatus().getCode());
        }
        doc.addField(SolrLeaveRequestConst.FIELD_DESCRIPTION, getDescription());
        doc.addField(SolrLeaveRequestConst.FIELD_START_DATE, getStartDate());
        doc.addField(SolrLeaveRequestConst.FIELD_END_DATE, getEndDate());
        doc.addField(SolrLeaveRequestConst.FIELD_CREATED_DATE, getCreatedDate());
        doc.addField(SolrLeaveRequestConst.FIELD_RECALL_DATE, getRecallDate());
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrLeaveRequestConst.FIELD_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrLeaveRequestConst.FIELD_APPROVER_NAME, getCurrentApprover().getExactEmployee().getName());
            doc.addField(SolrLeaveRequestConst.FIELD_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrLeaveRequestConst.SPLIT + getCurrentApprover().getExactEmployee().getName());
        }
        if (getRegisteredBy() != null) {
            doc.addField(SolrLeaveRequestConst.FIELD_CREATOR_ID, getRegisteredBy().getObjectID());
            doc.addField(SolrLeaveRequestConst.FIELD_CREATOR_NAME, getRegisteredBy().getName());
            doc.addField(SolrLeaveRequestConst.FIELD_CREATOR_ID_NAME, getRegisteredBy().getObjectID() + SolrLeaveRequestConst.SPLIT + getRegisteredBy().getName());
        }
        if (getType() != null) {
            doc.addField(SolrLeaveRequestConst.FIELD_TYPE_ID, getType().getObjectID());
            doc.addField(SolrLeaveRequestConst.FIELD_TYPE_NAME, getType().getName());
        }
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getLeaveRequestCode() {
        return leaveRequestCode;
    }

    public void setLeaveRequestCode(String leaveRequestCode) {
        this.leaveRequestCode = leaveRequestCode;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumberData() {
        return numberData;
    }

    public void setNumberData(String numberData) {
        this.numberData = numberData;
    }

    public String getLaborPeriod() {
        return laborPeriod;
    }

    public void setLaborPeriod(String laborPeriod) {
        this.laborPeriod = laborPeriod;
    }

    public EdsSickRequest getParent() {
        return parent;
    }

    public void setParent(EdsSickRequest parent) {
        this.parent = parent;
    }

    public List<EdsSickRequest> getChildlist() {
        return childlist;
    }

    public void setChildlist(List<EdsSickRequest> childlist) {
        this.childlist = childlist;
    }

    public List<MultiLeaveDTO> getChildlistRPC() {
        List<MultiLeaveDTO> dtoList = new ArrayList<>();
        for (EdsSickRequest request : childlist) {
            MultiLeaveDTO dto = new MultiLeaveDTO();
            dto.setSickID(request.getObjectID());
            dto.setSickRequestType(request.isTakeByMoney() ? Constants.MONEY : Constants.DAY);
            dto.setSickRequestStartDate(request.getStartDate());
            dto.setSickRequestEndDate(request.getEndDate());
            if (request.getRecallDate() != null) {
                dto.setSickRequestRecallDate(request.getRecallDate());
            }
            dto.setMinLeaveDays(request.getLeaveReason().getMinLeaveDays() != null ? request.getLeaveReason().getMinLeaveDays() : 0);
            dto.setLaborPeriod(request.getLaborPeriod());
            dto.setSickRequestDuration(request.getMoneyDays());
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {

            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.REASON)) {
                setLeaveReason((EdsLeaveReason) value);
            } else if (fieldID.equals(CustomFormConstants.REGISTERED_BY)) {
                if (value instanceof EdsEmployee) {
                    setRegisteredBy((EdsEmployee) value);
                }
            } else if (fieldID.equals(CustomFormConstants.REGISTERED_BY_EMAIL)) {
                if (value instanceof String && getRegisteredBy() != null) {
                    EdsEmployee employee = getRegisteredBy();
                    employee.setEmail((String) value);
                }
            } else if (fieldID.equals(CustomFormConstants.TYPE)) {
                if (value instanceof EdsReference) {
                    setType((EdsReference) value);
                }
            } else if (fieldID.equals(CustomFormConstants.START_DATE)) {
                if (value instanceof Date) {
                    setStartDate((Date) value);
                }
            } else if (fieldID.equals(CustomFormConstants.DUE_DATE)) {
                if (value instanceof Date) {
                    setEndDate((Date) value);
                }
            } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
                if (value instanceof String) {
                    setDescription((String) value);
                }
            } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
                if (value instanceof Date) {
                    setCreatedDate((Date) value);
                }
            } else if (fieldID.equals(CustomFormConstants.STATUS)) {
                if (value instanceof EdsReference) {
                    setOverallStatus((EdsReference) value);
                }
            }
            if (field.isCustomField()) {
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


    public List<EdsBackupEmployee> getBackupEmployees() {
        return backupEmployees;
    }

    public void setBackupEmployees(List<EdsBackupEmployee> backupEmployees) {
        this.backupEmployees = backupEmployees;
    }

    public Double getUsedExperienceDays() {
        return usedExperienceDays;
    }

    public void setUsedExperienceDays(Double usedExperienceDays) {
        this.usedExperienceDays = usedExperienceDays;
    }

    public SickRequestSolr getRPC() {
        SickRequestSolr rpc = new SickRequestSolr();
        rpc.setId(getObjectID());
        rpc.setNumberData(getNumberData());
        rpc.setDescription(getDescription());
        Optional.ofNullable(getOverallStatus())
                .map(os -> new SelectItem(os.getObjectID(), os.getName(), os.getCode(), os.getDescription(), ""))
                .ifPresent(rpc::setOverallStatus);
        rpc.setStartDate(getStartDate());
        rpc.setEndDate(getEndDate());
        rpc.setCreatedDate(getCreatedDate());
        Optional.ofNullable(getType())
                .map(r -> new SelectItem(r.getObjectID(), r.getName()))
                .ifPresent(rpc::setType);
        Optional.ofNullable(getCurrentApprover())
                .map(EdsApprover::getExactEmployee)
                .map(a -> new SelectItem(a.getObjectID(), a.getName()))
                .ifPresent(rpc::setCurrentApprover);
        Optional.ofNullable(getRegisteredBy())
                .map(rb -> new SelectItem(rb.getObjectID(), rb.getName()))
                .ifPresent(rpc::setRegisteredBy);
        EdsEmployee employee = getEmployee();
        if (employee != null) {
            EmployeeSolr employeeSolr = new EmployeeSolr(employee.getObjectID(), employee.getName());
            Optional.ofNullable(employee.getTeam())
                    .map(t -> new SelectItem(t.getObjectID(), t.getName()))
                    .ifPresent(employeeSolr::setTeam);
            Optional.ofNullable(employee.getPosition())
                    .map(p -> new SelectItem(p.getObjectID(), p.getName()))
                    .ifPresent(employeeSolr::setPosition);
            Optional.ofNullable(employee.getLocation())
                    .map(l -> new SelectItem(l.getObjectID(), l.getName()))
                    .ifPresent(employeeSolr::setLocation);
            employee.getProfile().getReportsTo();
            Optional.ofNullable(employee.getProfile())
                    .map(EdsEmployeeProfile::getReportsTo)
                    .map(r -> new SelectItem(r.getObjectID(), r.getFullName()))
                    .ifPresent(employeeSolr::setSupervisor);
            rpc.setEmployee(employeeSolr);
        }
        Optional.ofNullable(getLeaveReason())
                .map(lr -> new SelectItem(lr.getObjectID(), lr.getName(), lr.getCode(), lr.getDescription(), ""))
                .ifPresent(rpc::setLeaveReason);
        return rpc;
    }
}
