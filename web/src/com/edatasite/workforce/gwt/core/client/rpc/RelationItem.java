package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_ACCOUNT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_LEAD_CONTACT;

/**
 * User: Hayot
 * Date: 8/12/11
 * Time: 6:03 AM
 */
public class RelationItem implements Serializable {
    public static final String TYPE_PROJECT = "project";
    public static final String TYPE_CONTRACT = "contract";
    public static final String TYPE_TASK = "task";
    public static final String TYPE_PRODUCT_CATEGORY = "productCategory";
    public static final String TYPE_ISSUE = CrmConstants.ISSUE;
    public static final String PM_ISSUE = Constants.PM_ISSUE;
    public static final String TYPE_EVENT = CrmConstants.CRM_EVENT;
    public static final String TYPE_CONTACT = CrmConstants.CRM_CONTACT;
    public static final String TYPE_LEAD = CrmConstants.CRM_LEAD;
    public static final String TYPE_CRM_ACCOUNT = CrmConstants.CRM_ACCOUNT;
    public static final String TYPE_HRMS_EMPLOYEE = CrmConstants.HRMS_EMPLOYEE;
    public static final String TYPE_OPPORTUNITY = CrmConstants.CRM_OPPORTUNITY;
    public static final String TYPE_RFQ = CrmConstants.CRM_RFQ;
    public static final String TYPE_CASE = CrmConstants.CRM_CASE;
    public static final String TYPE_EMAIL_TRACKER = "EMAIL_TRACKER";
    public static final String TYPE_MEETING_MINUTES = "MEETING_MINUTES";
    public static final String TYPE_BOOKING = "BOOKING";
    public static final String TYPE_SALEQUOTE = CrmConstants.SALEQUOTE;
    public static final String TYPE_PURCHASEINVOICE = CrmConstants.PURCHASEINVOICE;
    public static final String TYPE_PURCHASEORDER = CrmConstants.PURCHASEORDER;
    public static final String TYPE_EXPENSECLAIM = CrmConstants.EXPENSECLAIM;
    public static final String TYPE_SALEORDER = CrmConstants.SALEORDER;
    public static final String TYPE_CUSTOMER_PREPAYMENTS = CrmConstants.TYPE_CUSTOMER_PREPAYMENTS;
    public static final String TYPE_SUPPLIER_PREPAYMENTS = CrmConstants.TYPE_SUPPLIER_PREPAYMENTS;
    public static final String REQUEST_FOR_PURCHASE = CrmConstants.REQUEST_FOR_PURCHASE;
    public static final String TYPE_STOCK_TRANSFER = CrmConstants.STOCK_TRANSFER;
    public static final String TYPE_STOCK_ADJUSTMENT = CrmConstants.STOCK_ADJUSTMENT;
    public static final String TYPE_REQUEST_FOR_QUOTE = Constants.REQUEST_FOR_QUOTE;
    public static final String TYPE_SALEINVOICE = CrmConstants.SALEINVOICE;
    public static final String TYPE_SALESINVOICE = CrmConstants.SALESINVOICE;
    public static final String TYPE_PRODUCT = CrmConstants.PRODUCT;
    public static final String TYPE_CANDIDATE = CrmConstants.CANDIDATE;
    public static final String TYPE_COURCE_SCHEDULE = CrmConstants.COURCE_SCHEDULE;
    public static final String TYPE_EMAIL_FILTER = CrmConstants.EMAIL_FILTER;
    public static final String TYPE_EMPLOYEE = CrmConstants.EMPLOYEE;
    public static final String TYPE_DEPARTMENT = CrmConstants.DEPARTMENT;
    public static final String TYPE_POSITION = CrmConstants.POSITION;
    public static final String TYPE_CLIENT = CrmConstants.CLIENT;
    public static final String TYPE_SUPPLIER = "supplier";
    public static final String TYPE_PURCHASE_ORDER = CrmConstants.TYPE_PURCHASE_ORDER;
    public static final String TYPE_GDN = CrmConstants.TYPE_GDN;
    public static final String TYPE_SHIPPING_DATA = CrmConstants.TYPE_SHIPPING_DATA;
    public static final String TYPE_PURCHASE_INVOICE = CrmConstants.TYPE_PURCHASE_INVOICE;
    public static final String TYPE_PROFIT = CrmConstants.PROFIT;
    public static final String TYPE_COST = CrmConstants.COST;
    public static final String TYPE_STUDENT = "STUDENT";
    public static final String TYPE_SICK_REQUEST = "LEAVE_REQUEST";
    public static final String TYPE_LEAVE_REQUEST = TYPE_SICK_REQUEST;
    public static final String TYPE_EXPENSE_CLAIM = "EXPENSE_CLAIM";
    public static final String TYPE_ADDITIONAL_PAYMENT = "ADDITIONAL_PAYMENT";
    public static final String TYPE_RENTAL_ORDER = "RENTAL_ORDER";
    public static final String TYPE_RENTAL_PRODUCT = "RENTAL_PRODUCT";
    public static final String TYPE_BUILD_ASSEMBLY = "BUILD_ASSEMBLY";

    public static final String TYPE_CAMPAIGN = CrmConstants.CAMPAIGN;
    public static final String TYPE_WORKFLOW = Constants.WORKFLOW;
    public static final String TYPE_WORKFLOW_ALERT = "WORKFLOW_ALERT";
    public static final String TYPE_WORKFLOW_SMS_ALERT = "WORKFLOW_SMS_ALERT";
    public static final String TYPE_WORKFLOW_TELEGRAM_ALERT = "TYPE_WORKFLOW_TELEGRAM_ALERT";
    public static final String TYPE_WORKFLOW_EMPLOYEE = "WORKFLOW_EMPLOYEE";
    public static final String TYPE_WORKFLOW_INVOICE = "WORKFLOW_INVOICE";
    public static final String TYPE_PAYRUN = "PAYRUN";
    public static final String TYPE_SINGLE_PAYRUN = "SINGLE_PAYRUN";
    public static final String TYPE_GROUP_PAYRUN = "GROUP_PAYRUN";
    public static final String TYPE_NEWS = "NEWS";
    public static final String TYPE_CASH_ADVANCE = "CASH_ADVANCE";
    public static final String TYPE_CERTIFICATE = "CERTIFICATE";
    public static final String TYPE_CERTIFICATE_OF_EMPLOYMENT = "CERTIFICATE_OF_EMPLOYMENT";
    public static final String TYPE_CHART_OF_ACCOUNT = "CHART_OF_ACCOUNT";
    public static final String TYPE_CS_STUDENT = "CS_STUDENT";
    public static final String TYPE_SMS = "SMS";
    public static final String TYPE_EMPLOYEE_STEP = "EMPLOYEE_STEP";
    public static final String TYPE_VACANCY = "VACANCY";
    public static final String TYPE_PLACEMENT = "PLACEMENT";
    public static final String TYPE_MANUAL_JOURNAL = Constants.MANUAL_JOURNAL;
    public static final String TYPE_BANK_TRANSFER = "BANK_TRANSFER";
    public static final String TYPE_BATCH_PAYMENT = "BATCH_PAYMENT";
    public static final String TYPE_PRE_PAYMENT = "PRE_PAYMENT";
    public static final String TYPE_CUSTOM_FORM_ITEM = "CUSTOM_FORM_ITEM";
    public static final String TYPE_BATCH_PAYMENT_RECEIVABLE = "BATCH_PAYMENT_RECEIVABLE";
    public static final String TYPE_BATCH_PAYMENT_PAYABLE = "BATCH_PAYMENT_PAYABLE";
    public static final String TYPE_INCIDENT = "TYPE_INCIDENT";

    public static final String TYPE_PERSONAL_GOAL = Constants.PERSONAL_GOAL;
    public static final String TYPE_BUSINESS_GOAL = Constants.BUSINESS_GOAL;
    public static final String TYPE_DEPARTMENT_GOAL = Constants.DEPARTMENT_GOAL;
    public static final String TYPE_PROJECT_GOAL = Constants.PROJECT_GOAL;
    public static final String TYPE_COMPANY_GOAL = Constants.COMPANY_GOAL;
    public static final String TYPE_GROUP_GOAL = Constants.GROUP_GOAL;
    public static final String TYPE_SHIFT = "SHIFT";
    public static final String TYPE_ROTATION = "ROTATION";
    public static final String TYPE_GROUP_PLACEMENT = "GROUP_PLACEMENT";
    public static final String TYPE_RECURRING_PAY_DEDUCTION = "RECURRING_PAY_DEDUCTION";
    public static final String TYPE_OVERTIME = "OVERTIME";
    public static final String TYPE_BACKUPS_EMPLOYEE = "BACKUPS_EMPLOYEE";
    public static final String TYPE_CREDIT_NOTE = "TYPE_CREDIT_NOTE";
    public static final String TYPE_DEBIT_NOTE = "TYPE_DEBIT_NOTE";
    public static final String TYPE_COMPANY_SETTINGS = "TYPE_COMPANY_SETTINGS";
    public static final String TYPE_EMPLOYEE_DOCUMENTS = "employeeDocument";
    
    public static Email emailItem;

    public static String[] relationTypes = new String[]{
            TYPE_CONTACT,
            TYPE_LEAD,
            TYPE_CAMPAIGN,
            TYPE_CRM_ACCOUNT,
            TYPE_CASE,
            TYPE_OPPORTUNITY,
            TYPE_EVENT,
            TYPE_CANDIDATE,
            TYPE_TASK,
            TYPE_PROJECT,
            TYPE_EMAIL_TRACKER,
            TYPE_EMAIL_FILTER,
            TYPE_ISSUE,
            TYPE_EMPLOYEE,
            TYPE_DEPARTMENT,
            TYPE_POSITION,
            TYPE_CLIENT,
            TYPE_SUPPLIER,
            TYPE_MEETING_MINUTES,
            TYPE_PROFIT,
            TYPE_COST,
            TYPE_VACANCY,
            TYPE_PLACEMENT,
            TYPE_PERSONAL_GOAL,
            TYPE_BUSINESS_GOAL,
            TYPE_DEPARTMENT_GOAL,
            TYPE_PROJECT_GOAL,
            TYPE_COMPANY_GOAL,
            TYPE_CUSTOMER_PREPAYMENTS,
            TYPE_SUPPLIER_PREPAYMENTS
    };

    private Integer objectID;

    private boolean remove = false;

    private Integer fromID;

    private String fromType;

    private String fromName;

    private Integer toID;

    private String toType;

    private String toName;

    private Date createdDate;

    private Date lastModifiedDate;

    public RelationItem() {
    }

    public RelationItem(Integer objectID, Integer toID, String toType, String toName, Integer fromID, String fromType, String fromName) {
        this();
        setObjectID(objectID);
        setToID(toID);
        setToType(toType);
        setToName(toName);
        setFromID(fromID);
        setFromType(fromType);
        setFromName(fromName);
        setRemove(false);
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isRemove() {
        return remove;
    }

    public void setRemove(boolean remove) {
        this.remove = remove;
    }

    public Integer getFromID() {
        return fromID;
    }

    public void setFromID(Integer fromID) {
        this.fromID = fromID;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public Integer getToID() {
        return toID;
    }

    public void setToID(Integer toID) {
        this.toID = toID;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelationItem that = (RelationItem) o;
        if (objectID != null && that.getObjectID() != null && objectID.equals(that.getObjectID())) {
            return true;
        }
        if (toID != null && toType != null && fromID != null && fromType != null) {
            if (toID.equals(that.getFromID()) && toType.equals(that.getFromType()) && fromID.equals(that.getToID()) && fromType.equals(that.getToType())) {
                return true;
            } else if (toID.equals(that.getToID()) && toType.equals(that.getToType()) && fromID.equals(that.getFromID()) && fromType.equals(that.getFromType())) {
                return true;
            }
        }
        return (Objects.equals(toID, that.toID))
                && (Objects.equals(toType, that.toType))
                && (fromID != null ? fromID.equals(that.fromID) : that.fromID != null)
                && (Objects.equals(fromType, that.fromType));
    }

    @Override
    public int hashCode() {
        int result = fromID != null ? fromID.hashCode() : 0;
        result = 31 * result + (fromType != null ? fromType.hashCode() : 0);
        result = 31 * result + (toID != null ? toID.hashCode() : 0);
        result = 31 * result + (toType != null ? toType.hashCode() : 0);
        return result;
    }

    public boolean isNew() {
        return getFromID() == null;
    }

    public boolean isTrueLinkage(String fromType, Integer fromID) {
        return isFromNewLinkage(fromType, fromID) || isToLinkage(fromType, fromID) || isFromLinkage(fromType, fromID);
    }

    private boolean isToLinkage(String fromType, Integer fromID) {
        return fromID != null && fromID.equals(getToID()) && fromType.equals(getToType());
    }

    public boolean isFromLinkage(String fromType, Integer fromID) {
        return fromID != null && fromID.equals(getFromID()) && fromType.equals(getFromType());
    }

    private boolean isFromNewLinkage(String fromType, Integer fromID) {
        return fromID == null && fromType.equals(getFromType());
    }

    public static void setFromName(Integer fromID, String fromType, String fromName, ArrayList<RelationItem> relations) {
        if (relations != null && relations.size() > 0) {
            for (RelationItem relation : relations) {
                if (fromID.equals(relation.getFromID()) && fromType.equals(relation.getFromType())) {
                    relation.setFromName(fromName);
                } else if (fromID.equals(relation.getToID()) && fromType.equals(relation.getToType())) {
                    relation.setToName(fromName);
                }
            }
        }
    }

    public static void setFromID(Integer fromID, ArrayList<RelationItem> relations) {
        if (relations != null && relations.size() > 0) {
            for (RelationItem relation : relations) {
                if (relation.isNew()) {
                    relation.setFromID(fromID);
                }
            }
        }
    }

    public static ArrayList<Integer> getRelatedIDs(ArrayList<RelationItem> relations, String type) {
        if (type != null && !"".equals(type.trim()) && relations != null && relations.size() > 0) {
            ArrayList<Integer> resultIDs = new ArrayList<>();
            for (RelationItem item : relations) {
                if (item.getToID() != null && type.equals(item.getToType())) {
                    resultIDs.add(item.getToID());
                } else if (item.getFromID() != null && type.equals(item.getFromType())) {
                    resultIDs.add(item.getFromID());
                }
            }
            return resultIDs;
        }
        return null;
    }

    public boolean isFrom(String type, Integer objectID) {
        return type.equals(getFromType()) && ((objectID == null && getFromID() == null) || (objectID != null && objectID.equals(getFromID())));
    }

    public Integer getIDByType(String relationType) {
        if (relationType == null) {
            return null;
        }
        if (relationType.equalsIgnoreCase(getToType())) {
            return getToID();
        }
        if (relationType.equalsIgnoreCase(getFromType())) {
            return getFromID();
        }
        return relationType != null && relationType.equals(getToType()) ? getToID() : getFromID();
    }

    public static RelationItem newEventRelation(String toType, Integer toID, String toName) {
        return new RelationItem(null, toID, toType, toName, null, TYPE_EVENT, null);
    }

    public static String getByContactType(Integer contactType) {
        if (contactType.equals(TYPE_LEAD_CONTACT)) {
            return RelationItem.TYPE_LEAD;
        } else if (contactType.equals(CrmConstants.TYPE_CANDIDATE)) {
            return RelationItem.TYPE_CANDIDATE;
        } else if (contactType.equals(TYPE_ACCOUNT)) {
            return RelationItem.TYPE_CRM_ACCOUNT;
        }else {
            return RelationItem.TYPE_CONTACT;
        }
    }
}
